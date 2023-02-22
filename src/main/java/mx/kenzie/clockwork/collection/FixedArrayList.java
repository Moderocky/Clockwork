package mx.kenzie.clockwork.collection;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.RandomAccess;

public class FixedArrayList<Type> extends AbstractList<Type> implements RandomAccess, Serializable, Cloneable {

    protected final Type[] array;
    protected int pointer;

    public FixedArrayList(Type[] array) {
        this.array = array;
        this.check();
    }

    @SafeVarargs
    public FixedArrayList(int length, Type... elements) {
        this(Arrays.copyOf(elements, length));
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
    public Type get(int index) {
        return array[index];
    }

    @Override
    public int size() {
        return array.length;
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
