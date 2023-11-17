package mx.kenzie.clockwork.collection;

import org.junit.Test;

import java.util.Map;
import java.util.Objects;

public class MagicMapTest {
    
    @Test
    public void simple() {
        final MagicMap map = MagicMap.create("hello");
        assert Objects.equals(map.get("hello"), null);
        map.put("hello", "there");
        assert Objects.equals(map.get("hello"), "there");
    }
    
    @Test
    public void reCreate() {
        final MagicMap map = MagicMap.create("hello");
        assert Objects.equals(map.get("hello"), null);
        map.put("hello", "there");
        assert Objects.equals(map.get("hello"), "there");
        final MagicMap other = MagicMap.create("hello");
        assert Objects.equals(other.get("hello"), null);
        other.put("hello", "there");
        assert Objects.equals(other.get("hello"), "there");
    }
    
    @Test
    public void forMap() {
        final Map<String, Object> original = Map.of("hello", "there");
        final MagicMap map = MagicMap.create(original);
        assert Objects.equals(map.get("hello"), "there");
        assert original != map;
        map.put("hello", "world");
        assert Objects.equals(map.get("hello"), "world");
        assert Objects.equals(original.get("hello"), "there");
    }
    
    @Test
    public void keySet() {
        final Map<String, Object> original = Map.of("hello", "there");
        final MagicMap map = MagicMap.create(original);
        assert !map.keySet().isEmpty();
        assert map.keySet().size() == 1;
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void missing() {
        final MagicMap map = MagicMap.create("hello");
        map.put("there", "hello");
    }
    
    @Test
    public void parent() {
        final MagicMap parent = MagicMap.create("hello");
        final MagicMap map = MagicMap.createComplex(parent.getClass(), "there");
        assert parent.keySet.size() == 1;
        assert map.keySet.size() == 2;
        map.put("hello", "there");
        assert Objects.equals(map.get("hello"), "there");
        assert parent.get("hello") == null;
        map.put("there", "bean");
        assert Objects.equals(map.get("there"), "bean");
    }
    
    @Test
    public void otherParent() {
        final MagicMap map = MagicMap.createComplex(MyMap.class, "hello");
        assert map.keySet.size() == 1;
        map.put("hello", "there");
        assert Objects.equals(map.get("hello"), "there");
        assert Objects.equals(map.get("foo"), "bar");
    }
    
    @Test
    public void accessor() {
        final Thing thing = MagicMap.create(Thing.class, "hello", "foo");
        assert thing.hello() == null;
        assert thing.hello("there") == null;
        assert Objects.equals(thing.hello(), "there");
        thing.foo(10);
        assert thing.get("foo").equals(10);
    }
    
    public interface Thing extends MagicMap.Accessor {
        
        String hello();
        
        String hello(String value);
        
        void foo(Object value);
        
    }
    
    public static abstract class MyMap extends MagicMap {
        
        @Override
        public Object get(String key) {
            return "bar";
        }
        
    }
    
}
