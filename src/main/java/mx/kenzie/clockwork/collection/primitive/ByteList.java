package mx.kenzie.clockwork.collection.primitive;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.RandomAccess;

public class ByteList extends AbstractList<Byte> implements RandomAccess, Serializable {
    private final RawByteList list;

    public ByteList() {
        this(10);
    }

    public ByteList(final int capacity) {
        this.list = new RawByteList(capacity);
    }

    @Override
    public Byte get(final int index) {
        this.list.checkBounds(index);
        return list.get(index);
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public Byte set(final int index, final Byte element) {
        this.list.checkBounds(index);
        return list.set(index, element);
    }


    @Override
    public void add(final int index, final Byte element) {
        if (index > 0) list.checkBounds(index - 1);
        this.list.add(index, element);
    }

    @Override
    public boolean add(Byte element) {
        this.list.add(element);
        return true;
    }

    public boolean add(byte value) {
        this.list.add(value);
        return true;
    }

    @Override
    public Byte remove(final int index) {
        this.list.checkBounds(index);
        return list.remove(index);
    }

    public byte[] array() {
        return list.array();
    }

}
