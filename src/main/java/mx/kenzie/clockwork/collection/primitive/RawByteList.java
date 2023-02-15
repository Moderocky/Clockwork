package mx.kenzie.clockwork.collection.primitive;

import java.io.Serializable;
import java.util.Arrays;

class RawByteList implements Serializable {
    private byte[] data;
    private int capacity;
    private int size;

    RawByteList(final int capacity) {
        this.capacity = capacity;
        this.data = new byte[capacity];
    }

    Byte set(final int index, final byte value) {
        final Byte old = data[index];
        this.data[index] = value;
        return old;
    }

    void add(final int index, final byte value) {
        if (index == size) {
            this.add(value);
            return;
        }
        final byte[] data = new byte[capacity + 1];
        System.arraycopy(this.data, 0, data, 0, index);
        System.arraycopy(this.data, index, data, index + 1, capacity - index);
        data[index] = value;
        this.data = data;
        this.capacity++;
        this.size++;
    }

    void add(final byte value) {
        if (capacity == size) {
            final byte[] data = new byte[capacity * 2];
            System.arraycopy(this.data, 0, data, 0, capacity);
            this.data = data;
            this.capacity *= 2;
        }
        this.data[size++] = value;
    }

    Byte get(final int index) {
        return data[index];
    }

    Byte remove(final int index) {
        final Byte old = data[index];
        final byte[] data = new byte[capacity - 1];
        System.arraycopy(this.data, 0, data, 0, index);
        System.arraycopy(this.data, index + 1, data, index, capacity - index - 1);
        this.data = data;
        this.capacity--;
        this.size--;
        return old;
    }

    int size() {
        return size;
    }

    byte[] array() {
        return Arrays.copyOfRange(data, 0, size);
    }

    void checkBounds(final int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException(
            "Index " + index + " out of bounds for size " + size);
    }
}
