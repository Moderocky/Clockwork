package mx.kenzie.clockwork.collection;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.*;

public class Table<Type> extends AbstractCollection<Type> implements Collection<Type>, Serializable, RandomAccess, Cloneable {
    protected final Class<Type> type;
    protected final Type[][] matrix;
    protected final int columns, rows;

    @SuppressWarnings("unchecked")
    public Table(Class<Type> type, int columns, int rows) {
        this.type = type;
        this.columns = columns;
        this.rows = rows;
        this.matrix = (Type[][]) Array.newInstance(type, rows, columns);
    }

    protected Table(Type[][] matrix) {
        this.matrix = matrix;
        this.rows = matrix.length;
        if (rows > 0) columns = matrix[0].length;
        else columns = 0;
        this.type = infer(matrix);
    }

    @SafeVarargs
    public Table(int columns, int rows, Type... elements) {
        this(infer(elements), columns, rows);
        if (elements.length == 0 || this.size() == 0) return;
        final int length = Math.min(this.size(), elements.length);
        for (int i = 0; i < length; i++) this.set(i, elements[i]);
    }

    public Table(Table<Type> table) {
        this(Arrays.copyOf(table.matrix, table.matrix.length));
    }

    public static <Type> Table<Type> of(Type[][] matrix) {
        return new Table<>(matrix);
    }

    @SuppressWarnings("unchecked")
    private static <Type> Class<Type> infer(Type... array) {
        return (Class<Type>) array.getClass().getComponentType();
    }

    @SuppressWarnings("unchecked")
    private static <Type> Class<Type> infer(Type[][] matrix) {
        return (Class<Type>) matrix.getClass().getComponentType().getComponentType();
    }

    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    public Type @NotNull [] toArray() {
        final Type[] array = (Type[]) Array.newInstance(type, this.size());
        int index = 0;
        for (int column = 0; column < columns; column++)
            for (int row = 0; row < rows; row++) array[index++] = matrix[row][column];
        return array;
    }

    public Set row(int index) {
        return new Set(true, index);
    }

    public Set column(int index) {
        return new Set(false, index);
    }

    public Type get(int col, int row) {
        return matrix[row][col];
    }

    public void set(int col, int row, Type value) {
        this.matrix[row][col] = value;
    }

    public Type get(int index) {
        final int col = index / rows;
        final int row = index % rows;
        return matrix[row][col];
    }

    public Type set(int index, Type value) {
        final int col = index / rows;
        final int row = index % rows;
        final Type old = this.matrix[row][col];
        this.matrix[row][col] = value;
        return old;
    }

    @Override
    public Iterator<Type> iterator() {
        return new ArrayIterator<>(this.toArray()) {
            @Override
            public void remove() {
                super.remove();
                Table.this.set(pointer - 1, null);
            }
        };
    }

    @Override
    public int size() {
        return columns * rows;
    }

    @Override
    protected Table<Type> clone() {
        return new Table<>(this);
    }

    @Override
    public String toString() {
        return Arrays.deepToString(matrix);
    }

    public class Set extends AbstractList<Type> {

        private final boolean row;
        private final int index;

        private Set(boolean row, int index) {
            this.index = index;
            this.row = row;
        }

        public boolean isRow() {
            return row;
        }

        public boolean isColumn() {
            return !row;
        }

        public int getIndex() {
            return index;
        }

        @NotNull
        @Override
        @SuppressWarnings("unchecked")
        public Type @NotNull [] toArray() {
            if (row) return Arrays.copyOf(matrix[index], columns);
            final Type[] column = (Type[]) Array.newInstance(type, rows);
            for (int i = 0; i < rows; i++) column[i] = matrix[i][index];
            return column;
        }

        @Override
        public Type get(int index) {
            if (row) return matrix[this.index][index];
            else return matrix[index][this.index];
        }

        @Override
        public Type set(int index, Type value) {
            final int x, y;
            if (row) {
                x = this.index;
                y = index;
            } else {
                y = this.index;
                x = index;
            }
            final Type old = matrix[x][y];
            matrix[x][y] = value;
            return old;
        }

        @Override
        public Iterator<Type> iterator() {
            if (row) return new ArrayIterator<>(matrix[index]);
            return new ColumnIterator();
        }

        @Override
        public int size() {
            if (row) return columns;
            else return rows;
        }

        private class ColumnIterator implements Iterator<Type> {
            private int pointer;

            @Override
            public boolean hasNext() {
                return pointer < rows;
            }

            @Override
            public Type next() {
                return matrix[pointer++][index];
            }

            @Override
            public void remove() {
                matrix[pointer - 1][index] = null;
            }
        }

    }

}
