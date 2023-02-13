package mx.kenzie.clockwork.collection.primitive;

import org.jetbrains.annotations.NotNull;

import java.util.AbstractList;
import java.util.Arrays;

public class ByteList extends AbstractList<Byte> {
    protected static final int DEFAULT_SIZE = 16, GROW_SIZE = 16;

    protected byte[] array;
    protected int size;

    public ByteList(byte... array) {
        this.array = array;
        this.resize(size = array.length);
    }

    public ByteList() {
        this.array = new byte[DEFAULT_SIZE];
        this.size = 0;
    }

    @Override
    public Byte set(int index, Byte element) {
        return this.setRaw(index, element != null ? element : 0);
    }

    public byte setRaw(int index, int element) {
        if (index >= array.length) this.resize(index);
        final byte current = array[index];
        this.array[index] = (byte) element;
        this.size = Math.max(size, index + 1);
        return current;
    }

    @Override
    public boolean add(Byte b) {
        this.addRaw(size, b != null ? b : 0);
        return true;
    }

    public void addRaw(int b) {
        if (size >= array.length) this.resize(size);
        this.array[size] = (byte) b;
        this.size++;
    }

    @Override
    public void add(int index, Byte element) {
        this.addRaw(index, element == null ? 0 : element);
    }

    public void addRaw(int index, int element) {
        final byte[] old = this.array;
        if (index >= array.length) this.resize(index);
        if (index < old.length) System.arraycopy(old, index, array, index + 1, old.length - index - 1);
        this.array[index] = (byte) element;
        this.size++;
    }

    protected void resize(int to) {
        final int size = ((Math.max(to, this.size) / GROW_SIZE) + 1) * GROW_SIZE;
        this.array = Arrays.copyOf(array, size);
    }

    @Override
    public Byte get(int index) {
        return this.getRaw(index);
    }

    public byte getRaw(int index) {
        return array[index];
    }

    @Override
    public int size() {
        return size;
    }

    @NotNull
    @Override
    public Byte @NotNull [] toArray() {
        final byte[] ints = this.array();
        final Byte[] bytes = new Byte[ints.length];
        for (int i = 0; i < bytes.length; i++) bytes[i] = ints[i];
        return bytes;
    }

    public byte[] array() {
        return Arrays.copyOf(array, size);
    }

    @Override
    public String toString() {
        return Arrays.toString(this.array());
    }

}
