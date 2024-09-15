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

package io.github.overrun.marshalgen.test

import io.github.overrun.marshalgen.*

fun main() {
    downcall("overrungl.gen.MyDowncall", javadoc = javadoc {
        +"Paragraph 1"
        +"Paragraph 2"
        +"My downcall `code`"
    }) {
        extends(DirectAccess)

        int("INT" to "1", javadoc = javadoc {
            +"An integer."
            +"Paragraph 2"
        })
        double("DOUBLE" to "2")

        int(javadoc = javadoc {
            +"The javadoc."
        }) {
            "int1"("1")
            "int2"("2")
        }

        +literal(
            """

            // This is a comment.

        """.trimIndent()
        )

        instanceField(""""example.dll"""")
        instanceGetter(""""example.dll"""")

        void("StaticMethod") {
            static(
                """
                    System.out.println("Static method");
                """.trimIndent()
            )
        }

        void("SkippedFunction", javadoc = javadoc {
            +"Skipped"
        }) {
            skip()
        }
        address("ReturnAddress")
        void("SetEntrypoint") {
            entrypoint("_entrypoint")
        }
        int("WithParameter", int * "Parameter1", double * "Parameter2", javadoc = javadoc {
            +"A method with parameters."
            "Parameter1" param "The first parameter"
            "Parameter2" param "The second parameter"
            returns("The returned value")
        })
        string("StringFunction", string * "Parameter1")
        void("DefaultFunction1") {
            skip(
                """
                    System.out.println("Hello world");
                """.trimIndent()
            )
        }
        void("DefaultFunction2") {
            default(
                """
                    System.out.println("default operation");
                """.trimIndent()
            )
        }
        const_char_pointer("NativeType", const_char_pointer * "Parameter1")
        handle("ReturnMethodHandle")
        string("StringCharset", (string * "Parameter1") { charset("UTF-16") }) {
            charset("UTF-16")
        }
        void("CriticalFunction") {
            critical(true)
        }
        void_pointer("SizedFunction", (void_pointer * "Parameter1") { sized(1L) }) {
            sized(1L)
        }
        void(
            "DefaultParamFunction",
            int * "Parameter1",
            (int * "Parameter2") { default("42") },
            int * "Parameter3",
            javadoc = javadoc {
                +"Default parameters"
                "Parameter1" param "The first parameter"
                "Parameter2" param "The second parameter"
                "Parameter3" param "The third parameter"
            })
        string("TestDefaultOverload", (int * "Parameter1") { default("42") }, string * "Parameter2")
        string("TestReturnOverload")
        int_array("TestIntArray", int_array * "Parameter1", (int_array * "Parameter2") { ref() })
        address_array("TestAddressArray", address_array * "Parameter1")
        boolean("TestConvert", (boolean * "Parameter1") { convert(BoolConvert.INT) }) { convert(BoolConvert.INT) }
        void("TestAllocator1", MemoryStack * "Parameter1")
        void("TestAllocator2", allocator * "Parameter1")
        void("TestAllocator3", arena * "Parameter1")
        void(
            "CanonicalLayouts", c_bool * "p0",
            c_char * "p1",
            c_short * "p2",
            c_int * "p3",
            c_float * "p4",
            c_long * "p5",
            c_long_long * "p6",
            c_double * "p7",
            size_t * "p8",
            wchar_t * "p9"
        )
    }
}
