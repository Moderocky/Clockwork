package mx.kenzie.clockwork.collection;

import org.junit.Test;

public class ClockListTest {

    @Test
    public void basic() {
        final ClockList<String> list = new ClockList<>();
        list.add("hello");
        assert list.contains("hello");
        assert list.getType() == String.class;
        assert list.getRandom().equals("hello");
    }

}
