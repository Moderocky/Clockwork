package mx.kenzie.clockwork.predicate;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * A plural predicate over a set of elements.
 * The three traditional quantifiers are provided (universal, existential, unique), as detailed below.
 * <p>
 * Universal (∀x) -- 'For all x...' whatever is appended is true for every element in the set,
 * and is trivially true for an empty set.
 * In other words, it passes IFF there is no element in the set that does not meet the condition.
 * <p>
 * Existential (∃x) -- 'There exists an x...' whatever is appended is true for at least one element in the set,
 * and is trivially false for an empty set.
 * In other words, it passes IFF there is at least one element in the set that meets the condition.
 * <p>
 * Unique (∃!x) -- 'There exists one x...' whatever is appended is true for exactly one element in the set,
 * and is trivially false for an empty set.
 * In other words, it passes IFF there is exactly one element in the set that meets the condition.
 * It is false if no elements match, and false if more than one element matches.
 * <p>
 * On top of this, three non-traditional quantifiers of my own divination are provided, as detailed below.
 * <p>
 * Strict (∀!) -- 'There exist all x...' whatever is appended is true for every element in the set,
 * and is FALSE for an empty set (∀ & ∃).
 * In other words, it passes IFF the set is not empty and every element in the set meets the condition.
 * <p>
 * Plausible (ɪx) -- 'For any x...' whatever is appended is true for at least one element in the set,
 * and is trivially true for an empty set (∀ | ∃).
 * In other words, it passes IFF the set is empty or any element in it meets the condition.
 * <p>
 * Possible (ɪ!x) -- 'For any x...' whatever is appended is true for exactly one element in the set,
 * and is trivially true for an empty set (∃! | (∀ & ¬∃)).
 * In other words, it passes IFF the set is empty or only one element in it meets the condition.
 */
@SuppressWarnings("unchecked")
public class Plural<Element> extends Predicate implements Iterable<Element> {

    public static final int UNIVERSAL = 0, EXISTENTIAL = 1, UNIQUE = 2, STRICT = 3, PLAUSIBLE = 4, POSSIBLE = 5;

    private final Iterable<Element> iterable;
    private final int mode;

    public Plural(int mode, Iterable<Element> iterable) {
        this.mode = mode;
        this.iterable = iterable;
    }

    public Plural<Element> where(java.util.function.Predicate<Element> test) {
        final List<Element> list = new LinkedList<>();
        for (Element element : iterable)
            if (test.test(element)) list.add(element);
        return new Plural<>(mode, list);
    }

    public Plural<Element> sample() {
        final List<Element> list = new LinkedList<>();
        for (Element element : iterable) list.add(element);
        return new Plural<>(mode, list);
    }

    public Plural<Element> areNull() {
        return this.are(Objects::isNull);
    }

    public Plural<Element> areNotNull() {
        return this.are(Objects::nonNull);
    }

    public Plural<Element> are(java.util.function.Predicate<Element> test) {
        boolean ok = true;
        switch (mode) {
            case EXISTENTIAL -> {
                ok = false;
                for (Element element : iterable) {
                    if (test.test(element)) {
                        ok = true;
                        break;
                    }
                }
            }
            case UNIQUE -> {
                ok = false;
                for (Element element : iterable) {
                    if (test.test(element)) {
                        if (ok) {
                            ok = false;
                            break;
                        }
                        ok = true;
                    }
                }
            }
            case STRICT -> {
                ok = false;
                for (Element element : iterable) {
                    if (test.test(element)) {
                        ok = true;
                    } else {
                        ok = false;
                        break;
                    }
                }
            }
            case PLAUSIBLE -> {
                ok = false;
                boolean empty = true;
                for (Element element : iterable) {
                    empty = false;
                    if (test.test(element)) {
                        ok = true;
                        break;
                    }
                }
                ok |= empty;
            }
            case POSSIBLE -> {
                ok = false;
                boolean empty = true;
                for (Element element : iterable) {
                    empty = false;
                    if (test.test(element)) {
                        if (ok) {
                            ok = false;
                            break;
                        }
                        ok = true;
                    }
                }
                ok |= empty;
            }
            default -> {
                for (Element element : iterable) {
                    if (test.test(element)) continue;
                    ok = false;
                    break;
                }
            }
        }
        this.test(ok);
        return this;
    }

    public Plural<Element> areNot(java.util.function.Predicate<Element> test) {
        return this.are(test.negate());
    }

    @NotNull
    @Override
    public Iterator<Element> iterator() {
        return iterable.iterator();
    }

    @Override
    public Plural<Element> and() {
        return (Plural<Element>) super.and();
    }

    @Override
    public Plural<Element> or() {
        return (Plural<Element>) super.or();
    }

    @Override
    public Plural<Element> xor() {
        return (Plural<Element>) super.xor();
    }

}
