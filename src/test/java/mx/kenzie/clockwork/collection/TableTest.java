package mx.kenzie.clockwork.collection;

import org.junit.Test;

import java.util.Arrays;

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
        table.set(1, 0, "general");
        table.set(1, 1, "kenobi");
        assert table.get(0).equals("hello");
        assert table.get(1).equals("there");
        assert table.get(1, 0).equals("general");
        assert table.get(1, 1).equals("kenobi");
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
        final Table<String> table = new Table<>(String.class, 2, 2);
        table.set(0, "hello");
        table.set(1, "there");
        table.set(1, 0, "general");
        table.set(1, 1, "kenobi");
        final String[] array = table.toArray();
        assert array[0].equals("hello");
        assert array[1].equals("there");
        assert array[2].equals("general");
        assert array[3].equals("kenobi");
    }
    
    @Test
    public void row() {
        final Table<String> table = new Table<>(3, 2, "hello", "there", "general", "kenobi");
        assert table.get(0).equals("hello");
        assert table.get(3).equals("kenobi");
        assert table.get(5) == null;
        final Table<String>.Set row = table.row(0);
        assert row.isRow();
        assert !row.isColumn();
        assert row.size() == 3;
        assert row.get(0).equals("hello");
        assert row.get(1).equals("general");
        assert row.get(2) == null;
        assert Arrays.toString(row.toArray()).equals("[hello, general, null]");
        assert String.join(" ", table.row(1)).equals("there kenobi null");
    }
    
    @Test
    public void column() {
        final Table<String> table = new Table<>(3, 2, "hello", "there", "general", "kenobi");
        assert table.get(1).equals("there");
        assert table.get(2).equals("general");
        assert table.get(4) == null;
        final Table<String>.Set column = table.column(0);
        assert !column.isRow();
        assert column.isColumn();
        assert column.size() == 2;
        assert column.get(0).equals("hello");
        assert column.get(1).equals("there");
        assert Arrays.toString(column.toArray()).equals("[hello, there]");
        assert String.join(" ", table.column(0)).equals("hello there");
        assert String.join(" ", table.column(1)).equals("general kenobi");
        assert String.join(" ", table.column(2)).equals("null null");
    }
    
}
