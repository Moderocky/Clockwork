package mx.kenzie.clockwork.collection;

import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;

public class
ClockListTest {

    @Test
    public void basic() {
        final ClockList<String> list = new ClockList<>();
        list.add("hello");
        assert list.contains("hello");
        assert list.getType() == String.class;
        assert list.getRandom().equals("hello");
    }

    @Test
    public void testClone() {
        final ClockList<String> list = new ClockList<>();
        list.add("hello");
        assert list.contains("hello");
        final ClockList<String> clone = list.clone(), copy;
        assert list != clone;
        assert list.list != clone.list;
        assert list.equals(clone);
        assert list.list.equals(clone.list);
        copy = list.clone(LinkedList::new);
        assert list != copy;
        assert copy != clone;
        assert list.list != copy.list;
        assert list.equals(copy);
        assert list.list.equals(copy.list);
        assert copy.list.equals(clone.list);
    }

    @Test
    public void synchronize() {
        final ClockList<String> list = new ClockList<>("hello", "there");
        assert !list.isSynchronized();
        final ClockList<String> duplicate = list.synchronize();
        assert duplicate != list;
        assert duplicate.equals(list);
        assert duplicate.isSynchronized();
        assert !duplicate.isEmpty();
        final ClockList<String> triplicate = duplicate.synchronize();
        assert duplicate == triplicate;
        assert duplicate.equals(triplicate);
        assert triplicate.equals(list);
        assert triplicate.isSynchronized();
        assert !triplicate.isEmpty();
    }

    @Test
    public void isRandomAccess() {
        final ClockList<String> list = new ClockList<>(String.class, new ArrayList<>());
        assert list.isRandomAccess();
        final ClockList<String> other = new ClockList<>(String.class, new LinkedList<>());
        assert !other.isRandomAccess();
    }

    @Test
    public void isSerializable() {
        final ClockList<String> list = new ClockList<>(String.class, new ArrayList<>());
        assert list.isSerializable();
        final ClockList<String> other = new ClockList<>(String.class, new LinkedList<>());
        assert other.isSerializable();
    }

}
