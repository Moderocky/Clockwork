package mx.kenzie.clockwork.collection.primitive;

import org.junit.Test;

public class ByteListTest {

    @Test
    public void basic() {
        final ByteList list = new ByteList();
        assert list.isEmpty();
        assert list.size() == 0;
        list.add((byte) 0);
        assert list.size() == 1;
        assert list.toArray().length == 1;
        assert list.array().length == 1;
        assert list.array()[0] == 0;
        assert list.get(0) == 0;
        list.add((byte) 1);
        assert list.get(1) == 1;
        assert list.size() == 2;
        assert list.size() == 2;
        assert list.toArray().length == 2;
        assert list.array().length == 2;
        assert list.array()[1] == 1;
        list.add(0, (byte) 6);
        assert list.size() == 3;
        assert list.get(0) == 6 : list;
        assert list.get(1) == 0;
        assert list.get(2) == 1;
    }

}
