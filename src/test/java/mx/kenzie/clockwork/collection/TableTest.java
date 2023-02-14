package mx.kenzie.clockwork.collection;

import org.junit.Test;

public class TableTest {

    @Test
    public void constructor() {
        final Table<String> table = new Table<>(String.class, 5, 3);
        assert table.matrix.length == 3;
        assert table.matrix[0].length == 5;
    }

    @Test
    public void toArray() {
        final Table<String> table = new Table<>(String.class, 3, 2);
        table.set(0, "hello");
        table.set(1, "there");
        final String[] array = table.toArray();
        assert array.length == 6;
        assert array[0].equals("hello");
    }

    @Test
    public void get() {
        final Table<String> table = new Table<>(String.class, 3, 2);
        table.set(0, "hello");
        table.set(1, "there");
        assert table.get(0).equals("hello");
        assert table.get(1).equals("there");
    }

    @Test
    public void iterator() {
        final Table<String> table = new Table<>(String.class, 1, 2);
        table.set(0, "hello");
        table.set(1, "there");
        assert table.size() > 0;
        int count = 0;
        for (String s : table) {
            assert s != null;
            assert s.length() > 0;
            count++;
        }
        assert count == table.size();
        assert count == 2;
    }

    @Test
    public void size() {
        final Table<String> table = new Table<>(String.class, 2, 2);
        assert table.size() == 4;
        table.set(0, "hello");
        table.set(1, "there");
        assert table.size() == 4;
    }

    @Test
    public void set() {
        final Table<String> table = new Table<>(String.class, 3, 2);
        table.set(0, "hello");
        table.set(1, "there");
        final String[] array = table.toArray();
        assert array[0].equals("hello");
        assert array[1].equals("there");
    }

}
