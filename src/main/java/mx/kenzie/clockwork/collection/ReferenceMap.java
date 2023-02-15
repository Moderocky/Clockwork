package mx.kenzie.clockwork.collection;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.Reference;
import java.util.*;
import java.util.function.Function;

public class ReferenceMap<Key, Value> extends AbstractMap<Key, Value> {

    protected final Map<Key, Reference<Value>> map;
    protected final Function<Value, Reference<Value>> wrapper;

    protected ReferenceMap(Map<Key, Reference<Value>> map, Function<Value, Reference<Value>> wrapper) {
        this.map = map;
        this.wrapper = wrapper;
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @NotNull
    @Override
    public Set<Key> keySet() {
        return map.keySet();
    }

    @NotNull
    @Override
    public Collection<Value> values() {
        return super.values();
    }

    @Override
    public Value getOrDefault(Object key, Value defaultValue) {
        final Reference<Value> reference = map.get(key);
        if (reference == null) return defaultValue;
        final Value value = reference.get();
        if (value == null) return defaultValue;
        return value;
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        for (Reference<Value> reference : map.values()) {
            if (reference == value) return true;
            if (reference == null) continue;
            if (reference.get() == value) return true;
        }
        return false;
    }

    @Override
    public Value get(Object key) {
        final Reference<Value> reference = map.get(key);
        if (reference == null) return null;
        return reference.get();
    }

    @Override
    public Value put(Key key, Value value) {
        final Reference<Value> reference = map.put(key, wrapper.apply(value));
        if (reference == null) return null;
        return reference.get();
    }

    @NotNull
    @Override
    public Set<Entry<Key, Value>> entrySet() {
        final LinkedHashSet<Entry<Key, Value>> set = new LinkedHashSet<>();
        for (Entry<Key, Reference<Value>> entry : map.entrySet()) set.add(new ReferenceEntry(entry));
        return set;
    }

    protected class ReferenceEntry implements Entry<Key, Value> {

        private final Key key;
        private final Reference<Value> reference;

        protected ReferenceEntry(Key key, Reference<Value> reference) {
            this.key = key;
            this.reference = reference;
        }

        protected ReferenceEntry(Entry<Key, Reference<Value>> entry) {
            this.key = entry.getKey();
            this.reference = entry.getValue();
        }

        @Override
        public Key getKey() {
            return key;
        }

        @Override
        public Value getValue() {
            return reference.get();
        }

        @Override
        public Value setValue(Value value) {
            return ReferenceMap.this.put(key, value);
        }
    }

}
