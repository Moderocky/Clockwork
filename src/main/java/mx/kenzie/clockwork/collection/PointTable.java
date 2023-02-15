package mx.kenzie.clockwork.collection;

public class PointTable<Type> extends Table<Type> {
    protected volatile int pointer;

    public PointTable(Class<Type> type, int columns, int rows) {
        super(type, columns, rows);
    }

    protected PointTable(Type[][] matrix) {
        super(matrix);
    }

    @SafeVarargs
    public PointTable(int columns, int rows, Type... elements) {
        super(columns, rows, elements);
    }

    public PointTable(Table<Type> table) {
        super(table);
    }

    @Override
    public boolean add(Type type) {
        synchronized (matrix) {
            while (pointer < this.size() && this.get(pointer) != null) pointer++;
            if (pointer >= this.size()) return false;
            this.set(pointer, type);
        }
        return true;
    }

    public void resetPointer() {
        if (pointer == 0) return;
        synchronized (matrix) {
            int counter = 0;
            check:
            for (Type[] types : matrix)
                for (Type type : types)
                    if (type == null) break check;
                    else counter++;
            this.pointer = counter;
        }
    }

}
