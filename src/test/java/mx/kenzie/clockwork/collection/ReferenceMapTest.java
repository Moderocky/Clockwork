package mx.kenzie.clockwork.collection;

import org.junit.Test;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

public class ReferenceMapTest {

    @Test
    public void constructor() {
        final Map<String, String> map = new ReferenceMap<>(new HashMap<>(), SoftReference::new);
        assert map.isEmpty();
        assert map.size() == 0;
        map.put("hello", "there");
        assert !map.isEmpty();
        assert map.size() == 1;
        assert map.get("hello").equals("there");
        assert map.getOrDefault("general", "kenobi").equals("kenobi");
    }

}
