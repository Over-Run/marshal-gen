// This file is auto-generated. DO NOT EDIT!
package overrungl.gen;
import io.github.overrun.memstack.MemoryStack;
import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import overrun.marshal.DirectAccess;
import overrun.marshal.Downcall;
import overrun.marshal.gen.CType;
import overrun.marshal.gen.CanonicalType;
import overrun.marshal.gen.Convert;
import overrun.marshal.gen.Critical;
import overrun.marshal.gen.Entrypoint;
import overrun.marshal.gen.Ref;
import overrun.marshal.gen.Sized;
import overrun.marshal.gen.Skip;
import overrun.marshal.gen.StrCharset;
import overrun.marshal.gen.processor.ProcessorType.BoolConvert;

/// Paragraph 1
/// 
/// Paragraph 2
/// 
/// My downcall `code`
public interface MyDowncall extends DirectAccess {
    /// An integer.
    /// 
    /// Paragraph 2
    int INT = 1;
    double DOUBLE = 2;
    /// The javadoc.
    int int1 = 1,
        int2 = 2;
    
    // This is a comment.
    
    MyDowncall INSTANCE = Downcall.load(MethodHandles.lookup(), "example.dll");
    static MyDowncall getInstance() {
        final class Holder {
            static final MyDowncall INSTANCE = Downcall.load(MethodHandles.lookup(), "example.dll");
        }
        return Holder.INSTANCE;
    }

    static void StaticMethod() {
        System.out.println("Static method");
    }

    /// Skipped
    @Skip
    void SkippedFunction();

    MemorySegment ReturnAddress();

    @Entrypoint("_entrypoint")
    void SetEntrypoint();

    /// A method with parameters.
    /// @param Parameter1 The first parameter
    /// @param Parameter2 The second parameter
    /// @return The returned value
    /// @see #SkippedFunction()
    /// @see #ReturnAddress()
    int WithParameter(int Parameter1, double Parameter2);

    String StringFunction(String Parameter1);

    MemorySegment StringFunction(MemorySegment Parameter1);

    @Skip
    default void DefaultFunction1() {
        System.out.println("Hello world");
    }

    default void DefaultFunction2() {
        System.out.println("default operation");
    }

    default void DefaultFunction3(String Parameter1) {
        System.out.println("default operation 3");
    }

    void DefaultFunction3(MemorySegment Parameter1);

    @CType("const char*")
    String NativeType(@CType("const char*") String Parameter1);

    @CType("const char*")
    MemorySegment NativeType(@CType("const char*") MemorySegment Parameter1);

    MethodHandle ReturnMethodHandle();

    @StrCharset("UTF-16")
    String StringCharset(@StrCharset("UTF-16") String Parameter1);

    @StrCharset("UTF-16")
    MemorySegment StringCharset(@StrCharset("UTF-16") MemorySegment Parameter1);

    @Critical(allowHeapAccess = true)
    void CriticalFunction();

    @CType("void*")
    @CanonicalType("void*")
    @Sized(1L)
    MemorySegment SizedFunction(@CType("void*") @CanonicalType("void*") @Sized(1L) MemorySegment Parameter1);

    /// Default parameters
    /// @param Parameter1 The first parameter
    /// @param Parameter2 The second parameter
    /// @param Parameter3 The third parameter
    /// @param Parameter4 The fourth parameter
    void DefaultParamFunction(int Parameter1, int Parameter2, int Parameter3, @CType("void*") @CanonicalType("void*") MemorySegment Parameter4);

    /// Default parameters
    /// @param Parameter1 The first parameter
    /// @param Parameter3 The third parameter
    /// @param Parameter4 The fourth parameter
    @Skip
    default void DefaultParamFunction(int Parameter1, int Parameter3, @CType("void*") @CanonicalType("void*") MemorySegment Parameter4) {
        this.DefaultParamFunction(Parameter1, 42, Parameter3, Parameter4);
    }

    String TestDefaultOverload(int Parameter1, String Parameter2);

    MemorySegment TestDefaultOverload(int Parameter1, MemorySegment Parameter2);

    @Skip
    default String TestDefaultOverload(String Parameter2) {
        return this.TestDefaultOverload(42, Parameter2);
    }

    String TestReturnOverload();

    MemorySegment TestReturnOverload_();

    int[] TestIntArray(int[] Parameter1, @Ref int[] Parameter2);

    MemorySegment TestIntArray(MemorySegment Parameter1, @Ref MemorySegment Parameter2);

    MemorySegment[] TestAddressArray(MemorySegment[] Parameter1);

    MemorySegment TestAddressArray(MemorySegment Parameter1);

    @Convert(BoolConvert.INT)
    boolean TestConvert(@Convert(BoolConvert.INT) boolean Parameter1);

    void TestAllocator1(MemoryStack Parameter1);

    void TestAllocator2(SegmentAllocator Parameter1);

    void TestAllocator3(Arena Parameter1);

    void CanonicalLayouts(@CType("bool") @CanonicalType("bool") boolean p0, @CType("char") @CanonicalType("char") byte p1, @CType("short") @CanonicalType("short") short p2, @CType("int") @CanonicalType("int") int p3, @CType("float") @CanonicalType("float") float p4, @CType("long") @CanonicalType("long") long p5, @CType("long long") @CanonicalType("long long") long p6, @CType("double") @CanonicalType("double") double p7, @CType("size_t") @CanonicalType("size_t") long p8, @CType("wchar_t") @CanonicalType("wchar_t") int p9);

}
