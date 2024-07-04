package tir;

import sun.misc.Unsafe;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class CopyUtils {

    private static final Unsafe UNSAFE;

    static {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            UNSAFE = (Unsafe) f.get(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T deepCopy(T original) {
        Map<Object, Object> visited = new IdentityHashMap<>();
        try {
            return (T) deepCopyRecursive(original, visited);
        } catch (Exception e) {
            throw new RuntimeException("Deep copy failed", e);
        }
    }

    private static Object deepCopyRecursive(Object original, Map<Object, Object> visited) throws Exception {
        if (original == null) {
            return null;
        }

        Object existing = visited.get(original);
        if (existing != null) {
            return existing;
        }

        Class<?> clazz = original.getClass();

        if (clazz.isPrimitive()
                || clazz == String.class
                || clazz == Boolean.class
                || clazz == Character.class
                || Number.class.isAssignableFrom(clazz)) {
            return original;
        }

        if (clazz.isArray()) {
            if (clazz.getComponentType().isPrimitive()) {
                return deepCopyPrimitiveArray(original, visited);
            } else {
                return deepCopyObjectArray((Object[]) original, visited, clazz.getComponentType());
            }
        }

        Object collectionCopy = switch (original) {
            case LinkedList<?> list -> deepCopyCollection(list, visited, new LinkedList<>());
            case ArrayList<?> list -> deepCopyCollection(list, visited, new ArrayList<>(list.size()));
            case List<?> list -> deepCopyCollection(list, visited, new ArrayList<>(list.size()));
            case LinkedHashSet<?> list -> deepCopyCollection(list, visited, new LinkedHashSet<>());
            case HashSet<?> list -> deepCopyCollection(list, visited, new HashSet<>());
            case TreeSet<?> list -> deepCopyCollection(list, visited, new TreeSet<>());
            case Set<?> list -> deepCopyCollection(list, visited, new HashSet<>());
            case LinkedHashMap<?, ?> map -> deepCopyMap(map, visited, new LinkedHashMap<>());
            case HashMap<?, ?> map -> deepCopyMap(map, visited, new HashMap<>());
            case TreeMap<?, ?> map -> deepCopyMap(map, visited, new TreeMap<>());
            case Map<?, ?> map -> deepCopyMap(map, visited, new HashMap<>());

            default -> null;
        };

        if (collectionCopy != null) {
            return collectionCopy;
        }

        Object copy = UNSAFE.allocateInstance(clazz);
        visited.put(original, copy);

        for (Field field : clazz.getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers())) {
                field.setAccessible(true);
                Object fieldValue = field.get(original);
                field.set(copy, deepCopyRecursive(fieldValue, visited));
            }
        }

        return copy;
    }

    private static Object deepCopyPrimitiveArray(Object original, Map<Object, Object> visited) {
        int length = Array.getLength(original);
        Object copy = Array.newInstance(original.getClass().getComponentType(), length);
        visited.put(original, copy);

        for (int i = 0; i < length; i++) {
            Array.set(copy, i, Array.get(original, i));
        }
        return copy;
    }

    private static Object deepCopyObjectArray(Object[] original, Map<Object, Object> visited, Class<?> componentType)
            throws Exception {
        int length = original.length;
        Object[] copy = (Object[]) Array.newInstance(componentType, length);
        visited.put(original, copy);

        for (int i = 0; i < length; i++) {
            copy[i] = deepCopyRecursive(original[i], visited);
        }
        return copy;
    }

    private static Collection<?> deepCopyCollection(Collection<?> original,
                                                    Map<Object, Object> visited,
                                                    Collection<Object> copy) throws Exception {
        visited.put(original, copy);
        for (Object item : original) {
            copy.add(deepCopyRecursive(item, visited));
        }
        return copy;
    }

    private static Map<?, ?> deepCopyMap(Map<?, ?> original,
                                         Map<Object, Object> visited,
                                         Map<Object, Object> copy) throws Exception {
        visited.put(original, copy);
        for (Map.Entry<?, ?> entry : original.entrySet()) {
            copy.put(deepCopyRecursive(entry.getKey(), visited), deepCopyRecursive(entry.getValue(), visited));
        }
        return copy;
    }
}
