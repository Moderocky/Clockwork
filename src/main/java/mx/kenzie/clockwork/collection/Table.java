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
        if (elements.length == 0) return;
        for (int i = 0; i < elements.length; i++) this.set(i, elements[i]);
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
        for (int column = 0; column < columns; column++) {
            for (int row = 0; row < rows; row++) {
                array[index++] = matrix[row][column];
            }
        }
        return array;
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
        return new ArrayIterator<>(this.toArray());
    }

    @Override
    public int size() {
        return columns * rows;
    }

    @Override
    protected Table<Type> clone() {
        return new Table<>(this);
    }

}
