package mx.kenzie.clockwork.collection;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class Node<Type> extends AtomicReference<Type> {
    
    public Node(Type value) {
        super(value);
    }
    
    @SuppressWarnings("unchecked")
    public static <Base, Type extends Base> Node<Type> of(Base o) {
        if (o instanceof Node<?> node) return (Node<Type>) node;
        return (Node<Type>) new Node<>(o);
    }
    
    public boolean is(Type value) {
        return Objects.equals(value, this.get());
    }
    
    public boolean isNull() {
        return null == this.get();
    }
    
    @Override
    public int hashCode() {
        final Object value = this.get();
        if (value == null) return 0;
        return value.hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof Node<?> node) return Objects.equals(this.get(), node.get());
        else return Objects.equals(this.get(), obj);
    }
    
    @Override
    public String toString() {
        return Objects.toString(this.get());
    }
    
}
