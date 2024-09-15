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

    @CType("const char*")
    String NativeType(@CType("const char*") String Parameter1);

    MemorySegment NativeType(MemorySegment Parameter1);

    MethodHandle ReturnMethodHandle();

    @StrCharset("UTF-16")
    String StringCharset(@StrCharset("UTF-16") String Parameter1);

    MemorySegment StringCharset(MemorySegment Parameter1);

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
    void DefaultParamFunction(int Parameter1, int Parameter2, int Parameter3);

    /// Default parameters
    /// @param Parameter1 The first parameter
    /// @param Parameter3 The third parameter
    @Skip
    default void DefaultParamFunction(int Parameter1, int Parameter3) {
        this.DefaultParamFunction(Parameter1, 42, Parameter3);
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

    MemorySegment TestIntArray(MemorySegment Parameter1, MemorySegment Parameter2);

    MemorySegment[] TestAddressArray(MemorySegment[] Parameter1);

    MemorySegment TestAddressArray(MemorySegment Parameter1);

    @Convert(BoolConvert.INT)
    boolean TestConvert(@Convert(BoolConvert.INT) boolean Parameter1);

    void TestAllocator1(MemoryStack Parameter1);

    void TestAllocator2(SegmentAllocator Parameter1);

    void TestAllocator3(Arena Parameter1);

    void CanonicalLayouts(@CanonicalType("bool") boolean p0, @CanonicalType("char") byte p1, @CanonicalType("short") short p2, @CanonicalType("int") int p3, @CanonicalType("float") float p4, @CanonicalType("long") long p5, @CanonicalType("long long") long p6, @CanonicalType("double") double p7, @CanonicalType("size_t") long p8, @CanonicalType("wchar_t") int p9);

}
