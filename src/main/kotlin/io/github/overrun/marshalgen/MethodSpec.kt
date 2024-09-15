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

class MethodSpec(
    private val returnType: ClassRefSupplier,
    private val name: String,
    private val parameters: List<ParameterSpec>,
    private val javadocSpec: JavadocSpec?
) : AnnotatedSpec, Spec {
    private val annotations = mutableListOf<AnnotationSpec>()
    private var defaultCode: String? = null
    private var static: Boolean = false

    private fun internalAppendString(indent: Int, builder: StringBuilder, factory: ClassRefFactory) {
        builder.apply {
            javadocSpec?.also {
                appendLine(it.build(indent))
            }

            returnType.get(factory).also {
                if (it.cType != null) {
                    at(CType, "value" to { _ -> listOf(""""${it.cType}"""") })
                }
                if (it.canonicalType != null) {
                    at(CanonicalType, "value" to { _ -> listOf(""""${it.canonicalType}"""") })
                }
            }

            // annotations
            annotations.sortedBy { it.type.get(factory).simpleName }
                .forEach {
                    it.appendString(indent, this, factory)
                    appendLine()
                }

            // signature
            append("    ")
            if (defaultCode != null) {
                if (static) {
                    append("static ")
                } else {
                    append("default ")
                }
            }
            append("${returnType.get(factory)} ${name}(")
            append(parameters.joinToString(", ") { param ->
                buildString {
                    param.type.get(factory).also {
                        if (it.cType != null) {
                            param.at(CType, "value" to { _ -> listOf(""""${it.cType}"""") })
                        }
                        if (it.canonicalType != null) {
                            param.at(CanonicalType, "value" to { _ -> listOf(""""${it.canonicalType}"""") })
                        }

                        param.annotations.sortedBy { annotation -> annotation.type.get(factory).simpleName }
                            .forEach { annotation ->
                                annotation.appendString(0, this, factory)
                                append(" ")
                            }

                        append("${it.get(factory)} ${param.name}")
                    }
                }
            })
            append(")")
            // body
            if (defaultCode != null) {
                appendLine(" {")
                appendLine(defaultCode?.prependIndent("        "))
                appendLine("    }")
            } else {
                appendLine(";")
            }
            appendLine()
        }
    }

    override fun appendString(indent: Int, builder: StringBuilder, factory: ClassRefFactory) {
        internalAppendString(indent, builder, factory)

        if (!static && !annotations.any { it.type.get(factory) == Skip }) {
            // generate carrier overload
            if (returnType.get(factory).let { it.carrier != null && it.carrier != it } ||
                parameters.any { it.type.get(factory).let { ref -> ref.carrier != null && ref.carrier != ref } }) {
                MethodSpec(
                    returnType.get(factory).let { it.carrier ?: it },
                    if (parameters.isEmpty()) "${name}_" else name,
                    parameters.map { ParameterSpec(it.type.get(factory).let { ref -> ref.carrier ?: ref }, it.name) },
                    javadocSpec
                ).internalAppendString(indent, builder, factory)
            }

            // generate default parameter overload
            if (parameters.any { it.defaultValue != null }) {
                MethodSpec(
                    returnType,
                    name,
                    parameters.filter { it.defaultValue == null },
                    javadocSpec?.withParams {
                        it.filter { p ->
                            parameters.filter { s -> s.defaultValue == null }.any { s -> s.name == p.first }
                        }
                    }
                ).also {
                    it.skip(
                        """
                        ${if (returnType.get(factory) == void) "" else "return "}this.$name(${parameters.joinToString(", ") { param -> param.defaultValue ?: param.name }});
                    """.trimIndent()
                    )
                }.internalAppendString(indent, builder, factory)
            }
        }
    }

    override fun at(classRef: ClassRefSupplier, vararg values: AnnotationKV) {
        annotations.add(AnnotationSpec(classRef, *values))
    }

    fun critical(allowHeapAccess: Boolean) {
        at(Critical, "allowHeapAccess" to { listOf(allowHeapAccess.toString()) })
    }

    fun entrypoint(name: String) {
        at(Entrypoint, "value" to { listOf(""""$name"""") })
    }

    fun skip() {
        at(Skip)
    }

    fun default(string: String) {
        defaultCode = string
    }

    fun static(string: String) {
        defaultCode = string
        static = true
    }

    fun skip(string: String) {
        skip()
        default(string)
    }
}

@MarshalGen
class ParameterSpec(val type: ClassRefSupplier, val name: String) : AnnotatedSpec {
    internal val annotations = mutableListOf<AnnotationSpec>()
    internal var defaultValue: String? = null

    override fun at(classRef: ClassRefSupplier, vararg values: AnnotationKV) {
        annotations.add(AnnotationSpec(classRef, *values))
    }

    fun default(value: String) {
        defaultValue = value
    }

    fun ref() {
        at(Ref)
    }

    operator fun invoke(action: ParameterSpec.() -> Unit): ParameterSpec =
        apply(action)
}
