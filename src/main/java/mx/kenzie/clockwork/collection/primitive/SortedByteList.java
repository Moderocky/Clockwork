package mx.kenzie.clockwork.collection.primitive;

import java.util.AbstractList;

import static java.lang.Byte.MIN_VALUE;

public class SortedByteList extends AbstractList<Byte> {
    private final int[] counts = new int[Byte.MAX_VALUE - MIN_VALUE + 1];

    public SortedByteList() {

    }

    public SortedByteList(int... values) {
        for (int value : values) {
            counts[((byte) value) - MIN_VALUE]++;
        }
    }

    public int count(final int value) {
        return count((byte) value);
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
        counts[element - MIN_VALUE]++;
        return true;
    }

    public boolean add(final int element) {
        counts[(byte) element - MIN_VALUE]++;
        return true;
    }

    @Override
    public Byte remove(final int index) {
        int needle = 0;
        for (int j = 0; j < counts.length; j++) {
            needle += counts[j];
            if (needle > index) {
                counts[j]--;
                return (byte) (j + MIN_VALUE);
            }
        }

        return null;
    }

    @Override
    public int size() {
        int i = 0;
        for (int j : counts) i += j;
        return i;
    }

    public byte[] array() {
        final byte[] array = new byte[size()];
        int index = 0;
        for (byte value : this) array[index++] = value;
        return array;
    }
}
