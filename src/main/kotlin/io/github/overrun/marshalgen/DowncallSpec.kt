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

import java.nio.file.Files
import kotlin.io.path.Path
import kotlin.io.path.createDirectories

@DslMarker
annotation class MarshalGen

@MarshalGen
class DowncallSpec(private val qualifiedName: String, private var javadoc: JavadocSpec?) : ClassRefFactory {
    private val packageName = qualifiedName.substringBeforeLast('.')
    private val simpleName = qualifiedName.substringAfterLast('.')
    internal val classRefs = ClassRefs()
    private val superclasses = mutableListOf<ClassRefSupplier>()
    private val specs = mutableListOf<Spec>()

    init {
        classRefs.importedClasses.add(qualifiedName)
    }

    private fun findPrimitiveRef(name: String, cType: String?, canonicalType: String?): ClassRef? {
        return when (name) {
            "void" -> void
            "boolean" -> PrimitiveClassRef("boolean", cType, canonicalType)
            "char" -> PrimitiveClassRef("char", cType, canonicalType)
            "byte" -> PrimitiveClassRef("byte", cType, canonicalType)
            "short" -> PrimitiveClassRef("short", cType, canonicalType)
            "int" -> PrimitiveClassRef("int", cType, canonicalType)
            "long" -> PrimitiveClassRef("long", cType, canonicalType)
            "float" -> PrimitiveClassRef("float", cType, canonicalType)
            "double" -> PrimitiveClassRef("double", cType, canonicalType)
            else -> null
        }
    }

    override fun classRef(name: String, carrier: ClassRef?, cType: String?, canonicalType: String?): ClassRef =
        findPrimitiveRef(name, cType, canonicalType) ?: ObjectClassRef(classRefs, name, cType, canonicalType, carrier)

    fun extends(vararg superclasses: ClassRefSupplier) {
        superclasses.forEach { this.superclasses.add(it) }
    }

    operator fun ClassRefSupplier.invoke(declaration: Pair<String, String>, javadoc: JavadocSpec? = null) {
        specs.add(FieldSpec(this, declaration.first, declaration.second, javadoc))
    }

    operator fun ClassRefSupplier.invoke(javadoc: JavadocSpec? = null, action: FieldListSpec.() -> Unit) {
        specs.add(FieldListSpec(this, mutableListOf(), javadoc).also(action))
    }

    operator fun ClassRefSupplier.invoke(
        methodName: String,
        vararg parameters: ParameterSpec,
        javadoc: JavadocSpec? = null,
        action: (MethodSpec.() -> Unit)? = null
    ) {
        specs.add(MethodSpec(this, methodName, parameters.toList(), javadoc).also { action?.invoke(it) })
    }

    operator fun ClassRefSupplier.times(name: String): ParameterSpec =
        ParameterSpec(this, name)

    fun ClassRefSupplier.get(): ClassRef = get(this@DowncallSpec)

    operator fun Spec.unaryPlus() {
        this@DowncallSpec.specs.add(this)
    }

    private fun downcallLoadMethod(symbolLookup: String, downcallOptions: String?): String =
        "${Downcall.get()}.load(${handles.get()}.lookup(), $symbolLookup${if (downcallOptions != null) ", $downcallOptions" else ""})"

    fun instanceField(symbolLookup: String, downcallOptions: String? = null) {
        classRef(qualifiedName, null)("INSTANCE" to downcallLoadMethod(symbolLookup, downcallOptions))
    }

    fun instanceGetter(symbolLookup: String, downcallOptions: String? = null) {
        classRef(qualifiedName, null).also {
            it("getInstance") {
                static(
                    """
                    final class Holder {
                        static final $it INSTANCE = ${this@DowncallSpec.downcallLoadMethod(symbolLookup, downcallOptions)};
                    }
                    return Holder.INSTANCE;
                """.trimIndent()
                )
            }
        }
    }

    fun generate() {
        Files.writeString(
            Path(packageName.replace('.', '/'))
                .createDirectories()
                .resolve("$simpleName.java"),
            buildString {
                appendLine(fileHeader)
                appendLine("package $packageName;")
                val s = buildString {
                    javadoc?.also {
                        appendLine(it.build(0))
                    }
                    append("public interface $simpleName")
                    if (superclasses.isNotEmpty()) {
                        append(" extends ")
                        append(superclasses.joinToString(separator = ", ") { it.get().simpleName })
                    }
                    appendLine(" {")
                    specs.forEach { it.appendString(4, this, this@DowncallSpec) }
                    appendLine("}")
                }

                // imports
                classRefs.importedClasses
                    .filterNot {
                        (it.startsWith("java.lang.") && it.lastIndexOf('.') == 9) ||
                            it == qualifiedName
                    }
                    .sorted()
                    .forEach {
                        appendLine("import $it;")
                    }
                appendLine()
                append(s)
            }
        )
    }
}

fun downcall(name: String, javadoc: JavadocSpec? = null, action: DowncallSpec.() -> Unit) {
    DowncallSpec(name, javadoc).also {
        it.action()
        it.generate()
    }
}
