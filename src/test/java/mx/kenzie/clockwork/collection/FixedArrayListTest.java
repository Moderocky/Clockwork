package mx.kenzie.clockwork.collection;

import org.junit.Test;

import java.util.Objects;

public class FixedArrayListTest {

    @Test
    public void add() {
        final FixedArrayList<String> list = new FixedArrayList<>(2);
        assert !list.isFull();
        assert list.isEmpty();
        list.add("hello");
        list.add("there");
        assert !list.isEmpty();
        assert list.isFull();
        assert list.size() == 2;
        assert Objects.equals(list.get(1), "there");
    }

    @Test
    public void toArray() {
        final FixedArrayList<String> list = new FixedArrayList<>(2);
        assert list.toArray().length == 0;
        list.add("hello");
        assert list.toArray().length == 1;
        list.add("there");
        final String[] strings = list.toArray();
        assert strings.length == 2;
        assert list.isFull();
        assert list.size() == strings.length;
        assert Objects.equals(strings[0], list.get(0));
        assert Objects.equals(strings[1], list.get(1));
        assert Objects.equals(strings[0], "hello");
        assert Objects.equals(strings[1], "there");
    }

    @Test
    public void remove() {
        final FixedArrayList<String> list = new FixedArrayList<>(2);
        assert !list.isFull();
        assert list.isEmpty();
        list.add("hello");
        list.add("there");
        assert !list.isEmpty();
        assert list.isFull();
        assert list.size() == 2;
        assert Objects.equals(list.get(1), "there");
        assert Objects.equals(list.remove(1), "there");
        assert !list.isEmpty();
        assert !list.isFull();
        assert list.size() == 1;
        assert list.get(1) == null;
        assert list.get(0).equals("hello");
        assert !list.remove(null);
        assert list.remove("hello");
        assert list.size() == 0;
        assert list.isEmpty();
        assert list.toArray().length == 0;
        list.add("hello");
        list.add("there");
        assert Objects.equals(list.get(0), "hello");
        assert Objects.equals(list.get(1), "there");
        list.remove("hello");
        assert Objects.equals(list.get(0), "there");
        list.add("hello");
        assert Objects.equals(list.get(1), "hello");
        assert list.remove(0).equals("there");
        assert Objects.equals(list.get(0), "hello");
        assert Objects.equals(list.get(1), null);
        list.add("there");
        assert Objects.equals(list.get(1), "there");
    }

    @Test
    public void get() {
        final FixedArrayList<String> list = new FixedArrayList<>(4, "hello", "there", "general", "kenobi");
        assert list.isFull();
        assert !list.isEmpty();
        assert list.size() == 4;
        assert Objects.equals(list.get(0), "hello");
        assert Objects.equals(list.get(1), "there");
        assert Objects.equals(list.get(2), "general");
        assert Objects.equals(list.get(3), "kenobi");
    }

    @Test
    public void size() {
        final FixedArrayList<String> list = new FixedArrayList<>(2);
        assert !list.isFull();
        assert list.isEmpty();
        assert list.size() == 0;
        list.add("hello");
        assert list.size() == 1;
        list.add("there");
        assert list.size() == 2;
        assert !list.isEmpty();
        assert list.isFull();
        list.remove("there");
        assert list.size() == 1;
        assert !list.isFull();
        assert !list.isEmpty();
        list.remove(0);
        assert list.size() == 0;
        assert list.isEmpty();
    }

    @Test
    public void isEmpty() {
        final FixedArrayList<String> list = new FixedArrayList<>(2);
        assert !list.isFull();
        assert list.isEmpty();
        assert list.size() == 0;
        list.add("hello");
        assert list.size() == 1;
        list.add("there");
        assert list.size() == 2;
        assert !list.isEmpty();
        assert list.isFull();
        list.remove("there");
        assert list.size() == 1;
        assert !list.isFull();
        assert !list.isEmpty();
        list.remove(0);
        assert list.size() == 0;
        assert list.isEmpty();
    }

    @Test
    public void testClone() {
        final FixedArrayList<String> list = new FixedArrayList<>(2, "hello", "there");
        final FixedArrayList<String> duplicate = list.clone();
        assert list.isFull();
        assert duplicate.isFull();
        assert list != duplicate;
        assert list.equals(duplicate);
        assert duplicate.equals(list);
    }

    @Test
    public void testClone1() {
        final FixedArrayList<String> list = new FixedArrayList<>(2, "hello", "there");
        final FixedArrayList<String> duplicate = list.clone(3);
        assert list.isFull();
        assert !duplicate.isFull();
        assert list != duplicate;
        assert list.equals(duplicate);
        assert duplicate.equals(list);
        assert duplicate.capacity() != list.capacity();
    }

}
