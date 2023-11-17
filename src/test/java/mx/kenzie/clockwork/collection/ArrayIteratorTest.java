package mx.kenzie.clockwork.collection;

import org.junit.Test;

import java.util.Iterator;

public class ArrayIteratorTest {
    
    @Test
    public void dynamic() {
        final String[] strings = {"hello", "there"};
        final Iterator<String> iterator = new ArrayIterator<>(strings);
        assert iterator.hasNext();
        assert iterator.next().equals("hello");
        strings[1] = "kenobi";
        assert iterator.next().equals("kenobi");
        assert !iterator.hasNext();
    }
    
    @Test
    public void next() {
        final String[] strings = {"hello", "there"};
        final Iterator<String> iterator = new ArrayIterator<>(strings);
        assert iterator.hasNext();
        assert iterator.next().equals("hello");
        assert iterator.next().equals("there");
        assert !iterator.hasNext();
    }
    
    @Test
    public void remove() {
        final String[] strings = {"hello", "there"};
        final Iterator<String> iterator = new ArrayIterator<>(strings);
        assert iterator.next().equals("hello");
        iterator.remove();
        assert iterator.next().equals("there");
        iterator.remove();
        assert !iterator.hasNext();
        assert strings[0] == null;
        assert strings[1] == null;
    }
    
    @Test
    public void hasNext() {
        final String[] strings = {"hello", "there"};
        final Iterator<String> iterator = new ArrayIterator<>(strings);
        assert iterator.hasNext();
        assert iterator.next().equals("hello");
        assert iterator.hasNext();
        assert iterator.next().equals("there");
        assert !iterator.hasNext();
    }
    
}
