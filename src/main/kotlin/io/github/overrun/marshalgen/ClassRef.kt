/*
 * MIT License
 *
 * Copyright (c) 2024 Overrun Organization
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 */

package io.github.overrun.marshalgen

import java.lang.invoke.MethodHandle
import java.lang.invoke.MethodHandles

internal class ClassRefs {
    val importedClasses = mutableListOf<String>()
}

interface ClassRef : ClassRefSupplier {
    val qualifiedName: String
    val simpleName: String
    val cType: String?
    val canonicalType: String?
    val carrier: ClassRef?

    override fun get(factory: ClassRefFactory): ClassRef = this
}

fun interface ClassRefSupplier {
    fun get(factory: ClassRefFactory): ClassRef

    infix fun c(cType: String): ClassRefSupplier =
        ClassRefSupplier {
            get(it).let { ref ->
                it.classRef(
                    ref.qualifiedName,
                    ref.carrier,
                    cType = cType,
                    canonicalType = ref.canonicalType
                )
            }
        }

    infix fun canonical(canonicalType: String): ClassRefSupplier =
        ClassRefSupplier {
            get(it).let { ref ->
                it.classRef(ref.qualifiedName, ref.carrier, cType = ref.cType, canonicalType = canonicalType)
            }
        }

    fun array(nativeType: String, carrier: ClassRef?): ClassRefSupplier =
        ClassRefSupplier { ArrayClassRef(get(it), nativeType, carrier) }

    fun array(): ClassRefSupplier =
        ClassRefSupplier { ArrayClassRef(get(it), null, address.get(it)) }
}

internal open class ObjectClassRef(
    classRefs: ClassRefs,
    override val qualifiedName: String,
    override val cType: String?,
    override val canonicalType: String?,
    override val carrier: ClassRef?
) : ClassRef {
    override val simpleName by lazy {
        if (classRefs.importedClasses.contains(qualifiedName)) {
            qualifiedName.substringAfterLast('.')
        } else if (classRefs.importedClasses.any { it.substringAfterLast('.') == qualifiedName.substringAfterLast('.') }) {
            qualifiedName
        } else {
            classRefs.importedClasses.add(qualifiedName)
            qualifiedName.substringAfterLast('.')
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ObjectClassRef) return false

        if (qualifiedName != other.qualifiedName) return false
        if (carrier != other.carrier) return false

        return true
    }

    override fun hashCode(): Int {
        var result = qualifiedName.hashCode()
        result = 31 * result + carrier.hashCode()
        return result
    }

    override fun toString(): String = simpleName
}

internal class PrimitiveClassRef(
    name: String,
    override val cType: String?,
    override val canonicalType: String?
) : ClassRef {
    override val qualifiedName: String = name
    override val simpleName: String = name
    override val carrier: ClassRef = this

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PrimitiveClassRef) return false

        if (qualifiedName != other.qualifiedName) return false
        if (carrier != other.carrier) return false

        return true
    }

    override fun hashCode(): Int {
        var result = qualifiedName.hashCode()
        result = 31 * result + carrier.hashCode()
        return result
    }

    override fun toString(): String = simpleName
}

internal class ArrayClassRef(
    componentType: ClassRef,
    override val cType: String?,
    override val carrier: ClassRef?
) : ClassRef {
    override val canonicalType: String? = null
    override val qualifiedName: String = "${componentType.qualifiedName}[]"
    override val simpleName: String = "${componentType}[]"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ArrayClassRef) return false

        if (carrier != other.carrier) return false
        if (qualifiedName != other.qualifiedName) return false

        return true
    }

    override fun hashCode(): Int {
        var result = carrier?.hashCode() ?: 0
        result = 31 * result + qualifiedName.hashCode()
        return result
    }

    override fun toString(): String = simpleName
}

interface ClassRefFactory {
    fun classRef(name: String, carrier: ClassRef?, cType: String? = null, canonicalType: String? = null): ClassRef
}

fun classRefSupplier(name: String, carrier: ClassRefSupplier? = null): ClassRefSupplier =
    ClassRefSupplier { it.classRef(name, carrier?.get(it)) }

inline fun <reified T> classRefSupplier(carrier: ClassRefSupplier? = null): ClassRefSupplier =
    ClassRefSupplier { it.classRef(T::class.java.name, carrier?.get(it)) }

// Java types
val void: ClassRef = PrimitiveClassRef("void", null, null)
val boolean: ClassRef = PrimitiveClassRef("boolean", null, null)
val char: ClassRef = PrimitiveClassRef("char", null, null)
val byte: ClassRef = PrimitiveClassRef("byte", null, null)
val short: ClassRef = PrimitiveClassRef("short", null, null)
val int: ClassRef = PrimitiveClassRef("int", null, null)
val long: ClassRef = PrimitiveClassRef("long", null, null)
val float: ClassRef = PrimitiveClassRef("float", null, null)
val double: ClassRef = PrimitiveClassRef("double", null, null)

val address = ClassRefSupplier {
    object : ObjectClassRef(
        when (it) {
            is DowncallSpec -> it.classRefs
            else -> error("Cannot access ClassRefs")
        },
        "java.lang.foreign.MemorySegment",
        null,
        null,
        null
    ) {
        override val carrier: ClassRef = this
    }
}
val string = classRefSupplier<String>(address)

// TODO: JDK 22
val arena = classRefSupplier("java.lang.foreign.Arena")
val allocator = classRefSupplier("java.lang.foreign.SegmentAllocator")
val handle = classRefSupplier<MethodHandle>()
val handles = classRefSupplier<MethodHandles>()

val boolean_array = boolean.array()
val char_array = char.array()
val byte_array = byte.array()
val short_array = short.array()
val int_array = int.array()
val long_array = long.array()
val float_array = float.array()
val double_array = double.array()
val address_array = address.array()
val string_array = string.array()

// C types
val c_bool = boolean canonical "bool"
val c_char = byte canonical "char"
val c_short = short canonical "short"
val c_int = int canonical "int"
val c_float = float canonical "float"
val c_long = long canonical "long"
val c_long_long = long canonical "long long"
val c_double = double canonical "double"
val size_t = long canonical "size_t"
val wchar_t = int canonical "wchar_t"

val void_pointer = address c "void*" canonical "void*"
val const_char_pointer = string c "const char*"

// objects

val MemoryStack = classRefSupplier("io.github.overrun.memstack.MemoryStack")

val Convert = classRefSupplier("overrun.marshal.gen.Convert")
val Critical = classRefSupplier("overrun.marshal.gen.Critical")
val CType = classRefSupplier("overrun.marshal.gen.CType")
val CanonicalType = classRefSupplier("overrun.marshal.gen.CanonicalType")
val Entrypoint = classRefSupplier("overrun.marshal.gen.Entrypoint")
val Ref = classRefSupplier("overrun.marshal.gen.Ref")
val Sized = classRefSupplier("overrun.marshal.gen.Sized")
val Skip = classRefSupplier("overrun.marshal.gen.Skip")
val StrCharset = classRefSupplier("overrun.marshal.gen.StrCharset")

val ProcessorType_BoolConvert = classRefSupplier("overrun.marshal.gen.processor.ProcessorType.BoolConvert")

val DirectAccess = classRefSupplier("overrun.marshal.DirectAccess")
val Downcall = classRefSupplier("overrun.marshal.Downcall")

enum class BoolConvert {
    CHAR,
    BYTE,
    SHORT,
    INT,
    LONG,
    FLOAT,
    DOUBLE,
}
