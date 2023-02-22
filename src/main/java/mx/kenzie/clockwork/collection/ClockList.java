package mx.kenzie.clockwork.collection;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

public class ClockList<Type> implements List<Type>, RandomAccess, Cloneable, java.io.Serializable {

    private static final Class<?> sync;

    static {
        try {
            sync = Class.forName("java.util.Collections$SynchronizedCollection");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Missing core class.", e);
        }
    }

    protected final Class<Type> type;
    protected final List<Type> list;
    private transient final Type[] array;

    @SuppressWarnings("unchecked")
    public ClockList(Class<Type> type, List<Type> list) {
        this.type = type;
        this.list = list;
        this.array = (Type[]) Array.newInstance(type, 0);
    }

    @SafeVarargs
    @SuppressWarnings("unchecked")
    public ClockList(Type... entries) {
        this((Class<Type>) entries.getClass().getComponentType(), new ArrayList<>(Arrays.asList(entries)));
    }

    public ClockList(Class<Type> type, int size) {
        this(type, new ArrayList<>(size));
    }

    public ClockList(Class<Type> type) {
        this(type, new ArrayList<>());
    }

    protected static boolean isSynchronized(Collection<?> collection) {
        return sync.isInstance(collection);
    }

    public Class<Type> getType() {
        return type;
    }

    public boolean canAccept(Object object) {
        return object == null || type.isInstance(object);
    }

    public Type getRandom() {
        return this.getRandom(ThreadLocalRandom.current());
    }

    public Type getRandom(Random random) {
        if (list.isEmpty()) throw new IllegalStateException("Cannot pull random element from empty list.");
        return list.get(random.nextInt(list.size()));
    }

    public Type getFirst() {
        return this.get(0);
    }

    @Override
    @Contract(pure = true)
    public int size() {
        return list.size();
    }

    @Override
    @Contract(pure = true)
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    @Contract(pure = true)
    public boolean contains(Object o) {
        return list.contains(o);
    }

    @Override
    public Iterator<Type> iterator() {
        return list.iterator();
    }

    public Iterator<Type> arrayIterator() {
        return new ArrayIterator<>(this.toArray());
    }

    @Override
    @Contract(pure = true)
    public Type[] toArray() {
        return list.toArray(array);
    }

    @Override
    @SafeVarargs
    public final <T> T @NotNull [] toArray(T @NotNull ... array) {
        return list.toArray(array);
    }

    @Override
    public boolean add(Type type) {
        return list.add(type);
    }

    @Override
    public boolean remove(Object o) {
        return list.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return list.contains(c);
    }

    @Override
    public boolean addAll(Collection<? extends Type> c) {
        return list.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends Type> c) {
        return list.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return list.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return list.retainAll(c);
    }

    @Override
    public void clear() {
        this.list.clear();
    }

    @Override
    public Type get(int index) {
        return list.get(index);
    }

    @Override
    public Type set(int index, Type element) {
        return list.set(index, element);
    }

    @Override
    public void add(int index, Type element) {
        this.list.add(index, element);
    }

    @Override
    public Type remove(int index) {
        return list.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return list.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return list.lastIndexOf(o);
    }

    @Override
    public ListIterator<Type> listIterator() {
        return list.listIterator();
    }

    @Override
    public ListIterator<Type> listIterator(int index) {
        return list.listIterator(index);
    }

    @Override
    public ClockList<Type> subList(int fromIndex, int toIndex) {
        return new ClockList<>(type, list.subList(fromIndex, toIndex));
    }

    @Override
    public ClockList<Type> clone() {
        return new ClockList<>(type, new ArrayList<>(list));
    }

    public ClockList<Type> clone(Function<List<Type>, List<Type>> backer) {
        return new ClockList<>(type, backer.apply(list));
    }

    @Contract(pure = true)
    public ClockList<Type> synchronize() {
        if (this.isSynchronized()) return this;
        return new ClockList<>(type, Collections.synchronizedList(list));
    }

    public boolean isSynchronized() {
        return isSynchronized(list);
    }

    public boolean isRandomAccess() {
        return list instanceof RandomAccess;
    }

    public boolean isSerializable() {
        return list instanceof Serializable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClockList<?> clockList)) return false;
        return Objects.equals(type, clockList.type) && Objects.equals(list, clockList.list);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, list);
    }

}
