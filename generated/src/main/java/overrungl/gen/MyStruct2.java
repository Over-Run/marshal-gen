// This file is auto-generated. DO NOT EDIT!
package overrungl.gen;
import java.lang.foreign.MemorySegment;
import java.lang.invoke.MethodHandles;
import overrun.marshal.LayoutBuilder;
import overrun.marshal.gen.CType;
import overrun.marshal.struct.Struct;
import overrun.marshal.struct.StructAllocator;
import overrungl.gen.MyStruct;

/// Javadoc
/// 
/// ## Members
/// 
/// ### MyStruct
/// 
/// [Getter](#MyStruct()) - [Setter](#MyStruct(java.lang.foreign.MemorySegment))
/// 
/// ## Layout
/// 
/// ```
/// typedef struct {
///     mystruct MyStruct;
/// } MyStruct2;
/// ```
/// 
public interface MyStruct2 extends Struct<MyStruct2> {
    /// The struct allocator.
    StructAllocator<MyStruct2> OF = new StructAllocator<>(MethodHandles.lookup(), LayoutBuilder.struct()
        .add(MyStruct.OF.layout(), "MyStruct")
        .build());
    
    @Override
    MyStruct2 slice(long index, long count);

    @Override
    MyStruct2 slice(long index);

    /// {@return `MyStruct`}
    @CType("mystruct")
    MemorySegment MyStruct();

    /// Sets `MyStruct` with the given value.
    /// @param MyStruct the value
    /// @return `this`
    MyStruct2 MyStruct(@CType("mystruct") MemorySegment MyStruct);

}
