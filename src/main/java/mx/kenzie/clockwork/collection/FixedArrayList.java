package mx.kenzie.clockwork.collection;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.RandomAccess;

public class FixedArrayList<Type> extends AbstractList<Type> implements RandomAccess, Serializable, Cloneable {

    protected final Type[] array;
    protected int pointer;

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
        this.pointer--;
        return thing;
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

    @Override
    public FixedArrayList<Type> clone() {
        final Type[] copy = Arrays.copyOf(array, array.length);
        return new FixedArrayList<>(copy);
    }

    public FixedArrayList<Type> clone(int length) {
        final Type[] copy = Arrays.copyOf(array, length);
        return new FixedArrayList<>(copy);
    }

}
