package mx.kenzie.clockwork.collection;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;

public class NodeList<Type> implements List<Type> {

    protected final List<Node<Type>> list;

    public NodeList(Collection<Type> collection) {
        this(wrap(collection));
    }

    protected NodeList(List<Node<Type>> list) {
        this.list = list;
    }

    public NodeList() {
        this(new ArrayList<Node<Type>>());
    }

    protected static <Type> List<Type> unwrap(Collection<Node<Type>> list) {
        final List<Type> copy = new ArrayList<>(list.size());
        for (final Node<Type> node : list) copy.add(node.get());
        return copy;
    }

    protected static <Type> List<Node<Type>> wrap(Collection<Type> list) {
        final List<Node<Type>> copy = new ArrayList<>(list.size());
        for (final Type value : list) copy.add(Node.of(value));
        return copy;
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return list.contains(o instanceof Node<?> node ? node : Node.of(o));
    }

    @NotNull
    @Override
    public Iterator iterator() {
        return new Iterator(0);
    }

    @NotNull
    @Override
    public Object[] toArray() {
        return unwrap(list).toArray();
    }

    @NotNull
    @Override
    public <T> T[] toArray(@NotNull T... array) {
        return unwrap(list).toArray(array);
    }

    @Override
    public Type get(int index) {
        return list.get(index).get();
    }

    @Override
    public boolean add(Type type) {
        return list.add(Node.of(type));
    }

    @Override
    public boolean remove(Object o) {
        return list.remove(Node.of(o));
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        for (final Object object : c) if (!this.contains(object)) return false;
        return true;
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends Type> c) {
        for (final Type type : c) list.add(Node.of(type));
        return true;
    }

    @Override
    public boolean addAll(int index, @NotNull Collection<? extends Type> c) {
        final Collection<Node<Type>> collection = new ArrayList<>(c.size());
        for (final Type type : c) collection.add(Node.of(type));
        return this.list.addAll(collection);
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        return this.list.removeIf(node -> c.contains(node.get()));
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return this.list.removeIf(node -> !c.contains(node.get()));
    }

    @Override
    public void clear() {
        this.list.clear();
    }

    @Override
    public Type set(int index, Type element) {
        return this.list.get(index).getAndSet(element);
    }

    @Override
    public void add(int index, Type element) {
        this.list.add(index, Node.of(element));
    }

    @Override
    public Type remove(int index) {
        return list.remove(index).get();
    }

    @Override
    public int indexOf(Object o) {
        return list.indexOf(Node.of(o));
    }

    @Override
    public int lastIndexOf(Object o) {
        return list.lastIndexOf(Node.of(o));
    }

    @NotNull
    @Override
    public Iterator listIterator() {
        return new Iterator(0);
    }

    @NotNull
    @Override
    public Iterator listIterator(int index) {
        return new Iterator(index);
    }

    @NotNull
    @Override
    public List<Type> subList(int fromIndex, int toIndex) {
        return new NodeList<>(list.subList(fromIndex, toIndex));
    }

    public class Iterator implements ListIterator<Type> {

        protected int cursor, checked = -1;

        protected Iterator(int index) {
            this.cursor = index;
        }

        @Override
        public boolean hasPrevious() {
            return this.cursor != 0;
        }

        @Override
        public int nextIndex() {
            return this.cursor;
        }

        @Override
        public int previousIndex() {
            return this.cursor - 1;
        }

        @Override
        public Type previous() {
            final int index = cursor - 1;
            if (index < 0) throw new NoSuchElementException();
            else return NodeList.this.get(index);
        }

        @Override
        public void set(Type value) {
            if (checked < 0) throw new IllegalStateException("No previous element seen.");
            else NodeList.this.set(checked, value);
        }

        @Override
        public void add(Type value) {
            NodeList.this.add(this.cursor, value);
            this.checked = -1;
        }

        @Override
        public boolean hasNext() {
            return this.cursor < NodeList.this.size();
        }

        @Override
        public Type next() {
            final int index = this.cursor;
            if (index >= NodeList.this.size()) throw new NoSuchElementException();
            else return NodeList.this.get(checked = ++cursor);
        }

        @Override
        public void remove() {
            if (checked < 0) throw new IllegalStateException();
            NodeList.this.remove(cursor = checked);
            this.checked = -1;
        }

        @Override
        public void forEachRemaining(Consumer<? super Type> action) {
            final int size = NodeList.this.size();
            for (; cursor < size; cursor++) action.accept(NodeList.this.get(cursor));
            this.checked = cursor - 1;
        }

    }

}
