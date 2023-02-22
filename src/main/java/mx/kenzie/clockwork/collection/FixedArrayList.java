package mx.kenzie.clockwork.collection;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.*;

public class FixedArrayList<Type> extends AbstractList<Type> implements RandomAccess, Serializable, Cloneable {

    protected final Type[] array;
    protected int pointer;

    protected FixedArrayList(Void marker, Type[] array) {
        this.array = array;
    }

    public FixedArrayList(Type[] array) {
        this.array = initialShuffle(array, array.length);
        this.check();
    }

    @SafeVarargs
    public FixedArrayList(int length, Type... elements) {
        this.array = initialShuffle(elements, length);
        this.check();
    }

    @SuppressWarnings("unchecked")
    private static <Type> Type[] initialShuffle(Type[] dirty, int length) {
        final Type[] array = (Type[]) Array.newInstance(dirty.getClass().getComponentType(), length);
        int index = 0;
        for (Type type : dirty) if (type != null) array[index++] = type;
        return array;
    }

    public boolean isFull() {
        return pointer >= array.length;
    }

    protected void shuffle() {
        final Type[] copy = Arrays.copyOf(array, array.length);
        int index = 0;
        Arrays.fill(array, null);
        for (Type type : copy) if (type != null) array[index++] = type;
    }

    protected void check() {
        for (pointer = 0; pointer < array.length; pointer++) if (array[pointer] == null) break;
    }

    @Override
    public boolean add(Type type) {
        this.array[pointer++] = type;
        return true;
    }

    @Override
    public Type @NotNull [] toArray() {
        return Arrays.copyOf(array, pointer);
    }

    @Override
    public Type remove(int index) {
        if (index >= pointer) return null;
        final Type thing = array[index];
        this.array[index] = null;
        System.arraycopy(array, index + 1, array, index, array.length - (index + 1));
        this.array[--pointer] = null;
        return thing;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) return false;
        int index;
        for (index = 0; index < array.length; index++) {
            if (Objects.equals(o, array[index])) break;
        }
        if (index == array.length) return false;
        this.remove(index);
        return true;
    }

    @Override
    public Type get(int index) {
        return array[index];
    }

    @Override
    public int size() {
        return pointer;
    }

    public boolean isEmpty() {
        return pointer == 0;
    }

    public int capacity() {
        return array.length;
    }

    @Override
    public FixedArrayList<Type> clone() {
        final FixedArrayList<Type> list = new FixedArrayList<>(null, Arrays.copyOf(array, array.length));
        list.pointer = this.pointer;
        return list;
    }

    public FixedArrayList<Type> clone(int length) {
        final FixedArrayList<Type> list = new FixedArrayList<>(null, Arrays.copyOf(array, length));
        list.pointer = Math.min(pointer, length);
        return list;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof List<?> list)) return false;
        if (o instanceof FixedArrayList<?> that) {
            if (that.capacity() == this.capacity())
                return Arrays.equals(array, that.array);
            else
                return Arrays.equals(that.toArray(), this.toArray());
        } else return super.equals(list);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(array);
    }

}
