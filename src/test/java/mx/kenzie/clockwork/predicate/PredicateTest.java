package mx.kenzie.clockwork.predicate;

import junit.framework.TestCase;

import java.util.Objects;

import static mx.kenzie.clockwork.predicate.Plural.*;

/**
 * Six test cases are provided for each quantifier.
 * {} = A // universal, plausible, possible
 * {} = B // universal, plausible, possible
 * {A, A, B} = B // existential, unique, plausible, possible
 * {A, A, B} = A // existential, plausible
 * {A, B} = A // existential, unique, plausible, possible
 * {A, A} = A // universal, existential, strict, plausible
 */
public class PredicateTest extends TestCase {

    public void testOf() {
        assert Predicate.of(UNIVERSAL).areNull().result();
        assert !Predicate.of(EXISTENTIAL).areNotNull().result();
        assert Predicate.of(UNIQUE, "hello", "there", null).areNull().result();
        assert !Predicate.of(STRICT, "hello", "there", null).areNotNull().result();
        assert Predicate.of(PLAUSIBLE, "hello", "there", null).where(Objects::nonNull).are(string -> string.equals("hello")).result();
        assert !Predicate.of(POSSIBLE, "hello", "there", null).where(Objects::nonNull).are(string -> string.length() == 5).result();
    }

    public void testStrict() {
        assert !Predicate.of(STRICT).areNull().result();
        assert !Predicate.of(STRICT).areNotNull().result();
        assert !Predicate.of(STRICT, "hello", "there", null).areNull().result();
        assert !Predicate.of(STRICT, "hello", "there", null).areNotNull().result();
        assert !Predicate.of(STRICT, "hello", "there", null).where(Objects::nonNull).are(string -> string.equals("hello")).result();
        assert Predicate.of(STRICT, "hello", "there", null).where(Objects::nonNull).are(string -> string.length() == 5).result();
    }

    public void testPlausible() {
        assert Predicate.of(PLAUSIBLE).areNull().result();
        assert Predicate.of(PLAUSIBLE).areNotNull().result();
        assert Predicate.of(PLAUSIBLE, "hello", "there", null).areNull().result();
        assert Predicate.of(PLAUSIBLE, "hello", "there", null).areNotNull().result();
        assert Predicate.of(PLAUSIBLE, "hello", "there", null).where(Objects::nonNull).are(string -> string.equals("hello")).result();
        assert Predicate.of(PLAUSIBLE, "hello", "there", null).where(Objects::nonNull).are(string -> string.length() == 5).result();
    }

    public void testPossible() {
        assert Predicate.of(POSSIBLE).areNull().result();
        assert Predicate.of(POSSIBLE).areNotNull().result();
        assert Predicate.of(POSSIBLE, "hello", "there", null).areNull().result();
        assert !Predicate.of(POSSIBLE, "hello", "there", null).areNotNull().result();
        assert Predicate.of(POSSIBLE, "hello", "there", null).where(Objects::nonNull).are(string -> string.equals("hello")).result();
        assert !Predicate.of(POSSIBLE, "hello", "there", null).where(Objects::nonNull).are(string -> string.length() == 5).result();
    }

    public void testUniversal() {
        assert Predicate.allOf().areNull().result();
        assert Predicate.allOf().areNotNull().result();
        assert !Predicate.allOf("hello", "there", null).areNull().result();
        assert !Predicate.allOf("hello", "there", null).areNotNull().result();
        assert !Predicate.allOf("hello", "there", null).where(Objects::nonNull).are(string -> string.equals("hello")).result();
        assert Predicate.allOf("hello", "there", null).where(Objects::nonNull).are(string -> string.length() == 5).result();
    }

    public void testExistential() {
        assert !Predicate.anyOf().areNull().result();
        assert !Predicate.anyOf().areNotNull().result();
        assert Predicate.anyOf("hello", "there", null).areNull().result();
        assert Predicate.anyOf("hello", "there", null).areNotNull().result();
        assert Predicate.anyOf("hello", "there", null).where(Objects::nonNull).are(string -> string.equals("hello")).result();
        assert Predicate.anyOf("hello", "there", null).where(Objects::nonNull).are(string -> string.length() == 5).result();
    }

    public void testUnique() {
        assert !Predicate.oneOf().areNull().result();
        assert !Predicate.oneOf().areNotNull().result();
        assert Predicate.oneOf("hello", "there", null).areNull().result();
        assert !Predicate.oneOf("hello", "there", null).areNotNull().result();
        assert Predicate.oneOf("hello", "there", null).where(Objects::nonNull).are(string -> string.equals("hello")).result();
        assert !Predicate.oneOf("hello", "there", null).where(Objects::nonNull).are(string -> string.length() == 5).result();
    }

    public void testAnd() {
        final Predicate predicate = new Predicate();
        assert !predicate.result();
        predicate.test(true);
        assert predicate.result();
        predicate.and();
        assert predicate.result();
        predicate.test(true);
        assert predicate.result();
        predicate.test(false);
        assert !predicate.result();
    }

    public void testOr() {
        final Predicate predicate = new Predicate();
        assert !predicate.result();
        predicate.test(true);
        assert predicate.result();
        predicate.or();
        assert predicate.result();
        predicate.test(true);
        assert predicate.result();
        predicate.test(false);
        assert predicate.result();
    }

    public void testXor() {
        final Predicate predicate = new Predicate();
        assert !predicate.result();
        predicate.test(true);
        assert predicate.result();
        predicate.xor();
        assert predicate.result();
        predicate.test(true);
        assert !predicate.result();
        predicate.test(false);
        assert !predicate.result();
        predicate.test(true);
        assert predicate.result();
    }

}