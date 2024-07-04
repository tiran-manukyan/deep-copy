package tir;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class CopyUtilsTest {

    @Test
    public void testPrimitive() {
        int original = 42;
        int copy = CopyUtils.deepCopy(original);
        assertEquals(original, copy);
    }

    @Test
    public void testString() {
        String original = "Hello, World!";
        String copy = CopyUtils.deepCopy(original);
        assertEquals(original, copy);
    }

    @Test
    public void testArray() {
        String[] original = {"one", "two", "three"};
        String[] copy = CopyUtils.deepCopy(original);
        assertArrayEquals(original, copy);
    }

    @Test
    public void testPrimitiveArray() {
        int[] original = {1, 2, 3};
        int[] copy = CopyUtils.deepCopy(original);
        assertArrayEquals(original, copy);
    }

    @Test
    public void testArrayWithNull() {
        String[] original = {"one", null, "three"};
        String[] copy = CopyUtils.deepCopy(original);
        assertArrayEquals(original, copy);
    }

    @Test
    public void testList() {
        List<String> original = new ArrayList<>(List.of("one", "two", "three"));
        List<String> copy = CopyUtils.deepCopy(original);
        assertEquals(original, copy);
    }

    @Test
    public void testLinkedList() {
        LinkedList<String> original = new LinkedList<>(List.of("one", "two", "three"));
        LinkedList<String> copy = CopyUtils.deepCopy(original);
        assertEquals(original, copy);
    }

    @Test
    public void testSet() {
        Set<String> original = new HashSet<>(Set.of("one", "two", "three"));
        Set<String> copy = CopyUtils.deepCopy(original);
        assertEquals(original, copy);
    }

    @Test
    public void testLinkedHashSet() {
        LinkedHashSet<String> original = new LinkedHashSet<>(Set.of("one", "two", "three"));
        LinkedHashSet<String> copy = CopyUtils.deepCopy(original);
        assertEquals(original, copy);
    }

    @Test
    public void testTreeSet() {
        TreeSet<String> original = new TreeSet<>(Set.of("one", "two", "three"));
        TreeSet<String> copy = CopyUtils.deepCopy(original);
        assertEquals(original, copy);
    }

    @Test
    public void testMap() {
        Map<String, Integer> original = new HashMap<>();
        original.put("one", 1);
        original.put("two", 2);
        original.put("three", 3);
        Map<String, Integer> copy = CopyUtils.deepCopy(original);
        assertEquals(original, copy);
    }

    @Test
    public void testLinkedHashMap() {
        LinkedHashMap<String, Integer> original = new LinkedHashMap<>();
        original.put("one", 1);
        original.put("two", 2);
        original.put("three", 3);
        LinkedHashMap<String, Integer> copy = CopyUtils.deepCopy(original);
        assertEquals(original, copy);
    }

    @Test
    public void testTreeMap() {
        TreeMap<String, Integer> original = new TreeMap<>();
        original.put("one", 1);
        original.put("two", 2);
        original.put("three", 3);
        TreeMap<String, Integer> copy = CopyUtils.deepCopy(original);
        assertEquals(original, copy);
    }

    @Test
    public void testNestedCollections() {
        List<Map<String, Set<Integer>>> original = new ArrayList<>();
        Map<String, Set<Integer>> map = new HashMap<>();
        map.put("one", new HashSet<>(Set.of(1, 2, 3)));
        original.add(map);

        List<Map<String, Set<Integer>>> copy = CopyUtils.deepCopy(original);
        assertEquals(original, copy);
    }

    @Test
    public void testCustomObject() {
        CustomObject original = new CustomObject("John", 30);
        CustomObject copy = CopyUtils.deepCopy(original);
        assertEquals(original, copy);
        assertNotSame(original, copy);
    }

    @Test
    public void testCustomObjectWithNullFields() {
        CustomObject original = new CustomObject(null, 30);
        CustomObject copy = CopyUtils.deepCopy(original);
        assertEquals(original, copy);
        assertNotSame(original, copy);
    }

    @Test
    public void testCircularReference() {
        CircularReferenceObject original = new CircularReferenceObject();
        original.setReference(original);
        CircularReferenceObject copy = CopyUtils.deepCopy(original);
        assertNotSame(original, copy);
        assertSame(copy, copy.getReference());
    }

    @Test
    public void testDeeplyNestedCustomObject() {
        NestedCustomObject original = new NestedCustomObject(new CustomObject("John", 30));
        NestedCustomObject copy = CopyUtils.deepCopy(original);
        assertEquals(original, copy);
        assertNotSame(original, copy);
        assertNotSame(original.getCustomObject(), copy.getCustomObject());
    }

    @Getter
    @EqualsAndHashCode
    @RequiredArgsConstructor
    static class CustomObject {
        private final String name;
        private final int age;
    }

    @Getter
    @Setter
    private static class CircularReferenceObject {
        private CircularReferenceObject reference;
    }

    @Getter
    @EqualsAndHashCode
    @RequiredArgsConstructor
    private static class NestedCustomObject {
        private final CustomObject customObject;
    }
}
