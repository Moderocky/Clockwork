package mx.kenzie.clockwork.collection;

import java.util.Iterator;

public class ArrayIterator<Type> implements Iterator<Type> {

    final Type[] array;
    protected int pointer;

    public ArrayIterator(Type[] array) {
        this.array = array;
    }

    @Override
    public boolean hasNext() {
        return pointer < array.length;
    }

    @Override
    public Type next() {
        return array[pointer++];
    }

    @Override
    public void remove() {
        this.array[pointer - 1] = null;
    }

}
