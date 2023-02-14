package mx.kenzie.clockwork.collection.primitive;

import java.util.Arrays;

class RawByteList {
    private byte[] data;
    private int capacity;
    private int size;

    RawByteList(final int capacity) {
        this.capacity = capacity;
        this.data = new byte[capacity];
    }

    Byte set(final int index, final byte value) {
        final Byte old = data[index];
        data[index] = value;
        return old;
    }

    void add(final int index, final byte value) {
        if (index == size) {
            add(value);
            return;
        }

        final byte[] newData = new byte[capacity + 1];

        System.arraycopy(data, 0, newData, 0, index);
        System.arraycopy(data, index, newData, index + 1, capacity - index);
        newData[index] = value;

        data = newData;
        capacity++;
        size++;
    }

    void add(final byte value) {
        if (capacity == size) {
            final byte[] newData = new byte[capacity * 2];
            System.arraycopy(data, 0, newData, 0, capacity);
            data = newData;
            capacity *= 2;
        }

        data[size++] = value;
    }

    Byte get(final int index) {
        return data[index];
    }

    Byte remove(final int index) {
        final Byte old = data[index];
        final byte[] newData = new byte[capacity - 1];

        System.arraycopy(data, 0, newData, 0, index);
        System.arraycopy(data, index + 1, newData, index, capacity - index - 1);
        data = newData;
        capacity--;
        size--;

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
