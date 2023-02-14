package mx.kenzie.clockwork.collection.primitive;

import org.junit.Test;

import java.util.Arrays;

import static java.lang.Byte.MIN_VALUE;
import static java.lang.Math.random;

public class SortedByteListTest {
    public static final int RANGE = Byte.MAX_VALUE - MIN_VALUE;

    private static byte randomByte() {
        return (byte) (random() * RANGE + MIN_VALUE);
    }

    @Test
    public void testSort() {
        final SortedByteList list = new SortedByteList();
        for (int i = 0; i < 10 * RANGE; ++i) {
            list.add(randomByte());
        }

        for (int i = 0, len = list.size() - 1; i < len; ++i) {
            assert list.get(i) <= list.get(i + 1);
        }
    }

    @Test
    public void testGet() {
        final SortedByteList list = new SortedByteList(2, 0, 1, 2, 0, 2, 0, 1, 1);

        assert list.get(0) == 0;
        assert list.get(6) == 2;
    }

    @Test
    public void testAdd() {
        final SortedByteList list = new SortedByteList(2, 0, 1, 2, 0, 2, 0, 1, 1);

        list.add(-1);
        list.add(1);
        list.add(3);

        assert Arrays.equals(list.array(), new byte[]{-1, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 3});
    }

    @Test
    public void testRemove() {
        final SortedByteList list = new SortedByteList(2, 0, 1, 2, 0, 2, 0, 1, 1);

        list.remove(0);
        list.remove(1);
        list.remove(4);

        assert Arrays.equals(list.array(), new byte[]{0, 1, 1, 1, 2, 2});
    }

    @Test
    public void testCount() {
        final SortedByteList list = new SortedByteList(2, 0, 1, 0, 2, 0, 1, 1);

        assert list.count(0) == 3;
        assert list.count(1) == 3;
        assert list.count(2) == 2;
        assert list.count(3) == 0;
    }
}
