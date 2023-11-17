package mx.kenzie.clockwork.collection.primitive;

import java.io.Serializable;
import java.util.Arrays;

class RawIntList implements Serializable {
    
    private int[] data;
    private int capacity;
    private int size;
    
    RawIntList(final int capacity) {
        this.capacity = capacity;
        this.data = new int[capacity];
    }
    
    int set(final int index, final int value) {
        final int old = data[index];
        this.data[index] = value;
        return old;
    }
    
    void add(final int index, final int value) {
        if (index == size) {
            this.add(value);
            return;
        }
        final int[] data = new int[capacity + 1];
        System.arraycopy(this.data, 0, data, 0, index);
        System.arraycopy(this.data, index, data, index + 1, capacity - index);
        data[index] = value;
        this.data = data;
        this.capacity++;
        this.size++;
    }
    
    void add(final int value) {
        if (capacity == size) {
            final int[] data = new int[capacity * 2];
            System.arraycopy(this.data, 0, data, 0, capacity);
            this.data = data;
            this.capacity *= 2;
        }
        this.data[size++] = value;
    }
    
    int get(final int index) {
        return data[index];
    }
    
    int remove(final int index) {
        final int old = data[index];
        final int[] data = new int[capacity - 1];
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
    
    int[] array() {
        return Arrays.copyOfRange(data, 0, size);
    }
    
    void checkBounds(final int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException(
            "Index " + index + " out of bounds for size " + size);
    }
    
}
