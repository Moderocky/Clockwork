package mx.kenzie.clockwork.collection.primitive;

import org.jetbrains.annotations.NotNull;

import java.util.AbstractList;
import java.util.Arrays;

public class IntList extends AbstractList<Integer> {
    protected static final int DEFAULT_SIZE = 16, GROW_SIZE = 16;

    protected int[] array;
    protected int size;

    public IntList(int... array) {
        this.array = array;
        this.resize(size = array.length);
    }

    public IntList() {
        this.array = new int[DEFAULT_SIZE];
        this.size = 0;
    }

    @Override
    public Integer set(int index, Integer element) {
        return this.setRaw(index, element != null ? element : 0);
    }

    public int setRaw(int index, int element) {
        if (index >= array.length) this.resize(index);
        final int current = array[index];
        this.array[index] = element;
        this.size = Math.max(size, index + 1);
        return current;
    }

    @Override
    public boolean add(Integer integer) {
        this.addRaw(size, integer != null ? integer : 0);
        return true;
    }

    public void addRaw(int integer) {
        if (size >= array.length) this.resize(size);
        this.array[size] = integer;
        this.size++;
    }

    @Override
    public void add(int index, Integer element) {
        this.addRaw(index, element == null ? 0 : element);
    }

    public void addRaw(int index, int element) {
        final int[] old = this.array;
        if (index >= array.length) this.resize(index);
        if (index < old.length) System.arraycopy(old, index, array, index + 1, old.length - index - 1);
        this.array[index] = element;
        this.size++;
    }

    protected void resize(int to) {
        final int size = ((Math.max(to, this.size) / GROW_SIZE) + 1) * GROW_SIZE;
        this.array = Arrays.copyOf(array, size);
    }

    @Override
    public Integer get(int index) {
        return this.getRaw(index);
    }

    public int getRaw(int index) {
        return array[index];
    }

    @Override
    public int size() {
        return size;
    }

    @NotNull
    @Override
    public Integer @NotNull [] toArray() {
        final int[] ints = this.array();
        final Integer[] integers = new Integer[ints.length];
        for (int i = 0; i < integers.length; i++) integers[i] = ints[i];
        return integers;
    }

    public int[] array() {
        return Arrays.copyOf(array, size);
    }

    @Override
    public String toString() {
        return Arrays.toString(this.array());
    }

}
