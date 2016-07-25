package solutions;

import data.Generator;
import data.Key;
import data.Pair;
import data.Value;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;
import static junit.framework.TestCase.assertEquals;

public class Compare {

    private void compareResults(int idCount, int length) {
        final List<Pair> pairs = Generator.generatePairs(idCount, length);

        final Result classicResult = Classic.solve(pairs);
        final Result triceResult = Trice.solve(pairs, true);
        final Result mapPairResult = MapPairSolution.solve(pairs, true);
        final Result singleIterationResult = SingleIteration.solve(pairs, true);

        assertSameResult(classicResult, triceResult);
        assertSameResult(classicResult, mapPairResult);
        assertSameResult(classicResult, singleIterationResult);
    }

    @Test
    public void compare_1_1() {
        compareResults(1, 1);
    }

    @Test
    public void compare_10_1() {
        compareResults(10, 1);
    }

    @Test
    public void compare_1_10() {
        compareResults(1, 10);
    }

    @Test
    public void compare_10_10() {
        compareResults(10, 10);
    }

    private void assertSameResult(Result result1, Result result2) {
        assertSameMappedValues(result1.getMappedValues(), result2.getMappedValues());

        assertSameValuesWithoutKeys(result1.getUnknownValues(), result2.getUnknownValues());
    }

    private void assertSameValuesWithoutKeys(List<Value> values1, List<Value> values2) {
        final Set<Value> valueSet1 = values1.stream().collect(toSet());
        final Set<Value> valueSet2 = values2.stream().collect(toSet());

        assertEquals(valueSet1, valueSet2);
        assertEquals(values1.size(), values2.size());
    }

    private void assertSameMappedValues(Map<Key, List<Value>> mappedValues1, Map<Key, List<Value>> mappedValues2) {
        final Map<Key, Set<Value>> comparableMappedValues1 =
                mappedValues1.entrySet().stream()
                        .collect(toMap(Map.Entry::getKey, e -> e.getValue().stream().collect(toSet())));

        final Map<Key, Set<Value>> comparableMappedValues2 =
                mappedValues2.entrySet().stream()
                        .collect(toMap(Map.Entry::getKey, e -> e.getValue().stream().collect(toSet())));

        assertEquals(comparableMappedValues1, comparableMappedValues2);

        final Map<Key, Long> mappedValuesCounts1 =
                mappedValues1.entrySet().stream()
                        .collect(toMap(Map.Entry::getKey, e -> e.getValue().stream().count()));

        final Map<Key, Long> mappedValuesCounts2 =
                mappedValues2.entrySet().stream()
                        .collect(toMap(Map.Entry::getKey, e -> e.getValue().stream().count()));

        assertEquals(mappedValuesCounts1, mappedValuesCounts2);
    }



}
