// This file is auto-generated. DO NOT EDIT!
package overrungl.gen;
import java.lang.foreign.Linker;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandles;
import overrun.marshal.LayoutBuilder;
import overrun.marshal.Unmarshal;
import overrun.marshal.gen.CType;
import overrun.marshal.gen.CanonicalType;
import overrun.marshal.struct.Struct;
import overrun.marshal.struct.StructAllocator;

/// ## Members
/// 
/// ### Int
/// 
/// [Getter](#Int()) - [Setter](#Int(int))
/// 
/// ### Address
/// 
/// [Getter](#Address()) - [Setter](#Address(java.lang.foreign.MemorySegment))
/// 
/// ### VoidPointer
/// 
/// [Getter](#VoidPointer()) - [Setter](#VoidPointer(java.lang.foreign.MemorySegment))
/// 
/// A `void*` member.
/// 
/// ### CInt
/// 
/// [Getter](#CInt()) - [Setter](#CInt(int))
/// 
/// ### String
/// 
/// [Getter](#String()) - [Setter](#String(java.lang.foreign.MemorySegment))
/// 
/// ### ConstCharPointer
/// 
/// [Getter](#ConstCharPointer()) - [Setter](#ConstCharPointer(java.lang.foreign.MemorySegment))
/// 
/// A `const char*` member.
/// 
/// ### SizeT
/// 
/// [Getter](#SizeT()) - [Setter](#SizeT(long))
/// 
/// ## Layout
/// 
/// ```
/// typedef struct mystruct {
///     int Int;
///     MemorySegment Address;
///     void* VoidPointer;
///     int CInt;
///     String String;
///     const char* ConstCharPointer;
///     size_t SizeT;
/// } MyStruct;
/// ```
/// 
public interface MyStruct extends Struct<MyStruct> {
    /// The struct allocator.
    StructAllocator<MyStruct> OF = new StructAllocator<>(MethodHandles.lookup(), LayoutBuilder.struct()
        .add(ValueLayout.JAVA_INT, "Int")
        .add(ValueLayout.ADDRESS, "Address")
        .add(ValueLayout.ADDRESS, "VoidPointer")
        .add(ValueLayout.JAVA_INT, "CInt")
        .add(Unmarshal.STR_LAYOUT, "String")
        .add(Unmarshal.STR_LAYOUT, "ConstCharPointer")
        .add(Linker.nativeLinker().canonicalLayouts().get("size_t"), "SizeT")
        .build());
    
    @Override
    MyStruct slice(long index, long count);

    @Override
    MyStruct slice(long index);

    /// {@return `Int`}
    int Int();

    /// Sets `Int` with the given value.
    /// @param Int the value
    /// @return `this`
    MyStruct Int(int Int);

    /// {@return `Address`}
    MemorySegment Address();

    /// Sets `Address` with the given value.
    /// @param Address the value
    /// @return `this`
    MyStruct Address(MemorySegment Address);

    /// {@return `VoidPointer`}
    @CType("void*")
    MemorySegment VoidPointer();

    /// Sets `VoidPointer` with the given value.
    /// @param VoidPointer the value
    /// @return `this`
    MyStruct VoidPointer(@CType("void*") MemorySegment VoidPointer);

    /// {@return `CInt`}
    @CType("int")
    @CanonicalType("int")
    int CInt();

    /// Sets `CInt` with the given value.
    /// @param CInt the value
    /// @return `this`
    MyStruct CInt(@CType("int") @CanonicalType("int") int CInt);

    /// {@return `String`}
    MemorySegment String();

    /// Sets `String` with the given value.
    /// @param String the value
    /// @return `this`
    MyStruct String(MemorySegment String);

    /// {@return `ConstCharPointer`}
    @CType("const char*")
    MemorySegment ConstCharPointer();

    /// Sets `ConstCharPointer` with the given value.
    /// @param ConstCharPointer the value
    /// @return `this`
    MyStruct ConstCharPointer(@CType("const char*") MemorySegment ConstCharPointer);

    /// {@return `SizeT`}
    @CType("size_t")
    @CanonicalType("size_t")
    long SizeT();

    /// Sets `SizeT` with the given value.
    /// @param SizeT the value
    /// @return `this`
    MyStruct SizeT(@CType("size_t") @CanonicalType("size_t") long SizeT);

}
