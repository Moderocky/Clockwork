package mx.kenzie.clockwork.collection;

import org.junit.Test;

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

}
