package mx.kenzie.clockwork.collection.primitive;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.RandomAccess;

import static java.lang.Byte.MAX_VALUE;
import static java.lang.Byte.MIN_VALUE;

public class SortedByteList extends AbstractList<Byte> implements Serializable, RandomAccess {
    
    private final int[] counts = new int[MAX_VALUE - MIN_VALUE + 1];
    private int size;
    
    public SortedByteList() {
    
    }
    
    public SortedByteList(int... values) {
        for (int value : values) counts[((byte) value) - MIN_VALUE]++;
        this.size = values.length;
    }
    
    public int count(final int value) {
        return this.count((byte) value);
    }
    
    public int count(final byte value) {
        return counts[value - MIN_VALUE];
    }
    
    @Override
    public Byte get(final int index) {
        int needle = 0;
        for (int j = 0; j < counts.length; j++) {
            needle += counts[j];
            if (needle > index) return (byte) (j + MIN_VALUE);
        }
        return null;
    }
    
    @Override
    public boolean add(final Byte element) {
        this.counts[element - MIN_VALUE]++;
        this.size++;
        return true;
    }
    
    public boolean add(final int element) {
        this.counts[(byte) element - MIN_VALUE]++;
        this.size++;
        return true;
    }
    
    @Override
    public Byte remove(final int index) {
        int needle = 0;
        for (int j = 0; j < counts.length; j++) {
            needle += counts[j];
            if (needle > index) {
                this.counts[j]--;
                this.size--;
                return (byte) (j + MIN_VALUE);
            }
        }
        return null;
    }
    
    @Override
    public void clear() {
        Arrays.fill(counts, 0);
        this.size = 0;
    }
    
    @Override
    public int size() {
        return size;
    }
    
    public byte[] array() {
        final byte[] array = new byte[size];
        int index = 0;
        for (byte value : this) array[index++] = value;
        return array;
    }
    
}
