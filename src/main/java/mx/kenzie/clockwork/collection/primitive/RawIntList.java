package mx.kenzie.clockwork.collection.primitive;

import java.util.Arrays;

class RawIntList {
    private int[] data;
    private int capacity;
    private int size;

    RawIntList(final int capacity) {
        this.capacity = capacity;
        this.data = new int[capacity];
    }

    int set(final int index, final int value) {
        final int old = data[index];
        data[index] = value;
        return old;
    }

    void add(final int index, final int value) {
        if (index == size) {
            add(value);
            return;
        }
        final int[] newData = new int[capacity + 1];
        System.arraycopy(data, 0, newData, 0, index);
        System.arraycopy(data, index, newData, index + 1, capacity - index);
        newData[index] = value;
        data = newData;
        capacity++;
        size++;
    }

    void add(final int value) {
        if (capacity == size) {
            final int[] newData = new int[capacity * 2];
            System.arraycopy(data, 0, newData, 0, capacity);
            data = newData;
            capacity *= 2;
        }

        data[size++] = value;
    }

    int get(final int index) {
        return data[index];
    }

    int remove(final int index) {
        final int old = data[index];
        final int[] newData = new int[capacity - 1];
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

    int[] array() {
        return Arrays.copyOfRange(data, 0, size);
    }

    void checkBounds(final int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException(
            "Index " + index + " out of bounds for size " + size);
    }
}
