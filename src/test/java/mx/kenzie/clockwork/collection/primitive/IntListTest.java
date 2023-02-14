package mx.kenzie.clockwork.collection.primitive;

import org.junit.Test;

public class IntListTest {

    @Test
    public void basic() {
        final IntList list = new IntList();
        assert list.isEmpty();
        assert list.size() == 0;
        list.add(0);
        assert list.size() == 1;
        assert list.toArray().length == 1;
        assert list.array().length == 1;
        assert list.array()[0] == 0;
        assert list.get(0) == 0;
        list.add(1);
        assert list.get(1) == 1;
        assert list.size() == 2;
        assert list.size() == 2;
        assert list.toArray().length == 2;
        assert list.array().length == 2;
        assert list.array()[1] == 1;
        list.add(0, 6);
        assert list.size() == 3;
        assert list.get(0) == 6 : list;
        assert list.get(1) == 0;
        assert list.get(2) == 1;
    }

}
