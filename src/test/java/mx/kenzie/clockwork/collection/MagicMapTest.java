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

}
