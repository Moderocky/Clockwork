package mx.kenzie.clockwork.predicate;

import org.intellij.lang.annotations.MagicConstant;

import java.util.Arrays;

import static mx.kenzie.clockwork.predicate.Plural.*;

/**
 * A predicate is something that can be tested.
 * This is designed to be extended by something else that will apply some framework of logic.
 */
public class Predicate {

    public static final int OR = 0, AND = 1, XOR = 2;
    protected boolean state;
    protected int mode;

    public static <Element> Plural<Element> of(@MagicConstant(flagsFromClass = Plural.class) int quantifier, Iterable<Element> elements) {
        return new Plural<>(quantifier, elements);
    }

    @SafeVarargs
    public static <Element> Plural<Element> of(@MagicConstant(flagsFromClass = Plural.class) int quantifier, Element... elements) {
        return of(quantifier, Arrays.asList(elements));
    }

    public static <Element> Plural<Element> allOf(Iterable<Element> elements) {
        return new Plural<>(UNIVERSAL, elements);
    }

    @SafeVarargs
    public static <Element> Plural<Element> allOf(Element... elements) {
        return allOf(Arrays.asList(elements));
    }

    public static <Element> Plural<Element> anyOf(Iterable<Element> elements) {
        return new Plural<>(EXISTENTIAL, elements);
    }

    @SafeVarargs
    public static <Element> Plural<Element> anyOf(Element... elements) {
        return anyOf(Arrays.asList(elements));
    }

    public static <Element> Plural<Element> oneOf(Iterable<Element> elements) {
        return new Plural<>(UNIQUE, elements);
    }

    @SafeVarargs
    public static <Element> Plural<Element> oneOf(Element... elements) {
        return oneOf(Arrays.asList(elements));
    }

    public Predicate and() {
        this.mode = AND;
        return this;
    }

    public Predicate or() {
        this.mode = OR;
        return this;
    }

    public Predicate xor() {
        this.mode = XOR;
        return this;
    }

    protected boolean test(boolean stage) {
        return switch (mode) {
            case 1 -> state &= stage;
            case 2 -> state ^= stage;
            default -> state |= stage;
        };
    }

    public boolean result() {
        return state;
    }

}
