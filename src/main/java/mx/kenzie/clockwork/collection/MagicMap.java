package mx.kenzie.clockwork.collection;

import mx.kenzie.foundation.*;
import mx.kenzie.foundation.instruction.AccessField;
import mx.kenzie.foundation.instruction.CallMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static mx.kenzie.foundation.Modifier.PUBLIC;
import static mx.kenzie.foundation.Type.OBJECT;
import static mx.kenzie.foundation.instruction.Instruction.*;

public abstract class MagicMap implements Map<String, Object> {

    private static final String location = MagicMap.class.getPackageName();

    protected final Set<String> keySet = new HashSet<>();

    protected MagicMap() {
        super();
    }

    protected static <Map> Map construct(Class<Map> type) {
        try {
            if (!MagicMap.class.isAssignableFrom(type)) throw new ClassCastException();
            return type.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static <Map extends MagicMap & Accessor> Map create(java.util.Map<?, ?> map) {
        final Object[] objects = map.keySet().toArray();
        final String[] keys = new String[objects.length];
        for (int i = 0; i < keys.length; i++) keys[i] = Objects.toString(objects[i]);
        final Object[] values = map.values().toArray();
        final Class<Map> type = prepare(MagicMap.class, Accessor.class, Loader.DEFAULT, keys);
        final Map ours = construct(type);
        for (int i = 0; i < keys.length; i++) ours.put(keys[i], values[i]);
        return ours;
    }

    public static <Map extends MagicMap & Accessor> Map create(String... keys) {
        final Class<Map> type = prepare(MagicMap.class, Accessor.class, Loader.DEFAULT, keys);
        return construct(type);
    }

    @SuppressWarnings("unchecked")
    public static <Helper extends Accessor, Map extends Helper> Map create(Class<Helper> helper, String... keys) {
        final Class<Map> type = (Class<Map>) (Object) prepare(MagicMap.class, helper, Loader.DEFAULT, keys);
        return construct(type);
    }

    @SuppressWarnings("unchecked")
    public static <Super extends MagicMap, Helper extends Accessor, Map extends Helper> Map createComplex(Class<Super> superType, Class<Helper> helper, String... keys) {
        final Class<Map> type = (Class<Map>) (Object) prepare(superType, helper, Loader.DEFAULT, keys);
        return construct(type);
    }

    @SuppressWarnings("unchecked")
    public static <Super extends MagicMap, Map extends Super> Map createComplex(Class<Super> superType, String... keys) {
        final Class<Map> type = (Class<Map>) (Object) prepare(superType, Accessor.class, Loader.DEFAULT, keys);
        return construct(type);
    }

    private static void addConstructor(PreClass builder, Type parent, String... names) {
        final PreMethod method = builder.add(PreMethod.constructor(PUBLIC));
        method.line(SUPER.of(parent).call(LOAD_VAR.object(0)));
        final AccessField.Stub field = FIELD.of(MagicMap.class, "keySet", Set.class);
        final CallMethod.Stub add = METHOD.of(Set.class, boolean.class, "add", Object.class);
        method.line(STORE_VAR.object(1, field.get(LOAD_VAR.object(0))));
        for (final String name : names) method.line(add.call(LOAD_VAR.object(1), CONSTANT.of(name)));
        method.line(RETURN.none());
    }

    protected static String identifier(String... names) {
        long result = 1;
        for (String element : names) result = 31 * result + element.hashCode();
        return "MagicMap_" + Long.toHexString(result);
    }

    public static CallMethod.Stub getVariable(String name) {
        return METHOD.of(true, compileInterface(name), Object.class, name);
    }

    public static CallMethod.Stub setVariable(String name) {
        return METHOD.of(true, compileInterface(name), Object.class, name, Object.class);
    }

    private static Type compileInterface(String name) {
        final PreClass builder = new PreClass(location + ".accessor", name);
        try {
            Class.forName(builder.type().getTypeName(), true, Loader.getDefault());
            return builder.type();
        } catch (ClassNotFoundException | NoClassDefFoundError ex) {
            builder.setInterface(true);
            builder.add(new PreMethod(PUBLIC, Modifier.ABSTRACT, Object.class, name, Object.class));
            builder.add(new PreMethod(PUBLIC, Modifier.ABSTRACT, Object.class, name));
            return Type.of(builder.compile().load(Loader.DEFAULT));
        }
    }

    protected static Type[] makeInterfaces(String... names) {
        final Type[] types = new Type[names.length];
        for (int i = 0; i < names.length; i++) types[i] = compileInterface(names[i]);
        return types;
    }

    private static void addAccessor(PreClass builder, Class<? extends Accessor> helper, String... names) {
        if (helper == Accessor.class) return;
        final Set<String> set = Set.of(names);
        for (final Method method : helper.getMethods()) {
            if (java.lang.reflect.Modifier.isStatic(method.getModifiers())) continue;
            if (!java.lang.reflect.Modifier.isPublic(method.getModifiers())) continue;
            if (!java.lang.reflect.Modifier.isAbstract(method.getModifiers())) continue;
            final Class<?> result = method.getReturnType();
            if (result == void.class || result.isPrimitive()) continue;
            if (!set.contains(method.getName())) continue;
            final AccessField.Stub stub = FIELD.of(builder, method.getName(), Object.class);
            if (method.getParameterCount() == 1) {
                final PreMethod setter = builder.add(
                    new PreMethod(PUBLIC, result, method.getName(), method.getParameterTypes()[0]));
                setter.line(visitor -> {
                    stub.get(THIS).write(visitor);
                    stub.set(THIS, LOAD_VAR.object(1)).write(visitor);
                    if (result != Object.class) visitor.visitTypeInsn(Opcodes.CHECKCAST, Type.internalName(result));
                    visitor.visitInsn(Opcodes.ARETURN);
                });
            } else if (method.getParameterCount() == 0) {
                final PreMethod getter = builder.add(new PreMethod(PUBLIC, result, method.getName()));
                if (result != Object.class) getter.line(RETURN.object(CAST.object(stub.get(THIS), result)));
                else getter.line(RETURN.object(stub.get(THIS)));
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected static <Map extends MagicMap & Accessor> Class<Map> prepare(Class<? extends MagicMap> superType, Class<? extends Accessor> helper, Loader loader, String... names) {
        try {
            return (Class<Map>) Class.forName(location + "." + identifier(names), true, (ClassLoader) loader);
        } catch (ClassNotFoundException | NoClassDefFoundError ex) {
            return compile(superType, helper, loader, names);
        }
    }

    protected static <Map extends MagicMap & Accessor> Class<Map> compile(Class<Map> superType, Loader loader, String... names) {
        return compile(superType, Accessor.class, loader, names);
    }

    @SuppressWarnings("unchecked")
    protected static <Map extends MagicMap & Accessor> Class<Map> compile(Class<? extends MagicMap> superType, Class<? extends Accessor> helper, Loader loader, String... names) {
        final String name = identifier(names);
        final PreClass builder = new PreClass(location, name);
        builder.setParent(Type.of(superType));
        builder.addInterfaces(makeInterfaces(names));
        addConstructor(builder, Type.of(superType), names);
        Arrays.sort(names, Comparator.comparingInt(String::hashCode));
        for (final String s : names) builder.add(new PreField(PUBLIC, Object.class, s));
        for (final String var : names) {
            final PreMethod set = builder.add(new PreMethod(PUBLIC, Object.class, var, Object.class));
            final AccessField.Stub stub = FIELD.of(builder, var, Object.class);
            set.line(visitor -> {
                stub.get(THIS).write(visitor);
                stub.set(THIS, LOAD_VAR.object(1)).write(visitor);
                visitor.visitInsn(Opcodes.ARETURN);
            });
            final PreMethod get = builder.add(new PreMethod(PUBLIC, Object.class, var));
            get.line(RETURN.object(stub.get(THIS)));
        }
        addAccessor(builder, helper, names);
        final PreMethod put = builder.add(new PreMethod(PUBLIC, OBJECT, "put", String.class, Object.class));
        final PreMethod get = builder.add(new PreMethod(PUBLIC, OBJECT, "get", String.class));
        final int[] keys = new int[names.length];
        for (int i = 0; i < names.length; i++) keys[i] = names[i].hashCode();
        put.line(visitor -> {
            final Label[] labels = new Label[keys.length];
            final Label end = new Label();
            for (int i = 0; i < labels.length; i++) labels[i] = new Label();
            METHOD.of(String.class, int.class, "hashCode").get(LOAD_VAR.object(1)).write(visitor);
            visitor.visitLookupSwitchInsn(end, keys, labels);
            for (int i = 0; i < names.length; i++) {
                final AccessField.Stub stub = FIELD.of(builder, names[i], Object.class);
                visitor.visitLabel(labels[i]);
                stub.set(THIS, LOAD_VAR.object(2)).write(visitor);
                visitor.visitVarInsn(Opcodes.ALOAD, 2);
                visitor.visitInsn(Opcodes.ARETURN);
            }
            visitor.visitLabel(end);
            SUPER.of(superType, OBJECT, "put", String.class, Object.class)
                .get(THIS, LOAD_VAR.object(1), LOAD_VAR.object(2)).write(visitor);
            visitor.visitInsn(Opcodes.ARETURN);
        });
        get.line(visitor -> {
            final Label[] labels = new Label[names.length];
            final Label end = new Label();
            for (int i = 0; i < labels.length; i++) labels[i] = new Label();
            METHOD.of(String.class, int.class, "hashCode").get(LOAD_VAR.object(1)).write(visitor);
            visitor.visitLookupSwitchInsn(end, keys, labels);
            for (int i = 0; i < names.length; i++) {
                final AccessField.Stub stub = FIELD.of(builder, names[i], Object.class);
                visitor.visitLabel(labels[i]);
                stub.get(THIS).write(visitor);
                visitor.visitInsn(Opcodes.ARETURN);
            }
            visitor.visitLabel(end);
            SUPER.of(superType, OBJECT, "get", String.class).get(THIS, LOAD_VAR.object(1)).write(visitor);
            visitor.visitInsn(Opcodes.ARETURN);
        });
        return (Class<Map>) builder.compile().load(loader);
    }

    @Override
    public @Nullable Object put(String key, Object value) {
        throw new IllegalArgumentException("Key '" + key + "' was not baked.");
    }

    public Object get(String key) {
        throw new IllegalArgumentException("Key '" + key + "' was not baked.");
    }

    @Override
    public Object get(Object key) {
        if (key == null) throw new NullPointerException();
        return this.get(key.toString());
    }

    @Override
    public Object remove(Object key) {
        if (key != null) return this.put(Objects.toString(key), null);
        return null;
    }

    @NotNull
    @Override
    public Set<String> keySet() {
        return new HashSet<>(keySet);
    }

    @NotNull
    @Override
    public Collection<Object> values() {
        final List<Object> values = new ArrayList<>(keySet.size());
        for (final String string : keySet) values.add(this.get(string));
        return values;
    }

    @NotNull
    @Override
    public Set<Entry<String, Object>> entrySet() {
        class Entry implements Map.Entry<String, Object> {
            //<editor-fold desc="Entry Class" defaultstate="collapsed">

            private final String getKey;

            Entry(String getKey) {this.getKey = getKey;}

            @Override
            public Object getValue() {
                return MagicMap.this.get(getKey);
            }

            @Override
            public Object setValue(Object value) {
                return MagicMap.this.put(getKey, value);
            }

            @Override
            public String getKey() {return getKey;}
            //</editor-fold>
        }
        final Set<Map.Entry<String, Object>> entrySet = new HashSet<>();
        for (final String string : keySet) entrySet.add(new Entry(string));
        return entrySet;
    }

    @Override
    public int size() {
        return keySet.size();
    }

    @Override
    public boolean isEmpty() {
        return keySet.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return keySet.contains(key);
    }

    @Override
    public boolean containsValue(Object value) {
        for (final String string : keySet) if (Objects.equals(this.get(string), value)) return true;
        return false;
    }

    @Override
    public void putAll(@NotNull Map<? extends String, ?> m) {
        for (final String string : m.keySet()) this.put(string, m.get(string));
    }

    @Override
    public void clear() {
        for (final String string : keySet) this.put(string, null);
    }

    public interface Accessor extends Map<String, Object> {}

}
