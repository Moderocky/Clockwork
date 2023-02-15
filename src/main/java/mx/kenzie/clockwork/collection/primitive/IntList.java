package mx.kenzie.clockwork.collection.primitive;

import java.util.AbstractList;

public class IntList extends AbstractList<Integer> {
    private final RawIntList list;

    public IntList() {
        this(10);
    }

    public IntList(final int capacity) {
        this.list = new RawIntList(capacity);
    }

    @Override
    public Integer get(final int index) {
        this.list.checkBounds(index);
        return list.get(index);
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public Integer set(final int index, final Integer element) {
        this.list.checkBounds(index);
        return list.set(index, element);
    }

    @Override
    public boolean add(Integer integer) {
        this.list.add(integer);
        return true;
    }

    public boolean add(int value) {
        this.list.add(value);
        return true;
    }

    @Override
    public void add(final int index, final Integer element) {
        if (index > 0) list.checkBounds(index - 1);
        this.list.add(index, element);
    }

    @Override
    public Integer remove(final int index) {
        this.list.checkBounds(index);
        return list.remove(index);
    }

    public int[] array() {
        return list.array();
    }
}
