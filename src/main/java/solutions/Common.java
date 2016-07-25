package solutions;

import data.Key;
import data.Pair;
import data.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public final class Common {
    private Common(){}

    public static void remapValuesWithoutKeys(Map<String, Key> knownKeys, Map<String, List<Value>> valuesWithoutKeys, Map<Key, List<Value>> targetMappedValues, List<Value> targetValuesWithoutKeys) {
        for (Map.Entry<String, List<Value>> e : valuesWithoutKeys.entrySet()) {
            final Key valueKey = knownKeys.get(e.getKey());

            if (valueKey == null) {
                targetValuesWithoutKeys.addAll(e.getValue());
            } else {
                final List<Value> values = targetMappedValues.putIfAbsent(valueKey, e.getValue());
                if (values != null) {
                    values.addAll(e.getValue());
                }
            }
        }
    }

    public static void putPairToMaps(Map<String, Key> knownKeys,
                                     Map<Key, List<Value>> mappedValues,
                                     Map<String, List<Value>> unknownValues,
                                     Pair pair) {
        final Key key = pair.getKey();
        final Value value = pair.getValue();
        knownKeys.putIfAbsent(key.getId(), key);

        final Key valueKey = knownKeys.get(value.getKeyId());

        if (valueKey == null) {
            final List<Value> values = unknownValues.computeIfAbsent(value.getKeyId(), (id) -> new ArrayList<>());
            values.add(value);
        } else {
            final List<Value> values = mappedValues.computeIfAbsent(valueKey, (k) -> new ArrayList<>());
            values.add(value);
        }
    }

    public static <K, V> void mergeMapsIntoFirstOneIfAbsent(Map<K, V> targetMap, Map<K, V> additionalMap) {
        for (Map.Entry<K, V> e : additionalMap.entrySet()) {
            targetMap.putIfAbsent(e.getKey(), e.getValue());
        }
    }

    public static <V> List<V> combineListsIntoFirstOne(List<V> l1, List<V> l2) {
        l1.addAll(l2);
        return l1;
    }

    public static <K, V> void mergeMapsIntoFirstOne(Map<K, V> targetMap,
                                                    Map<K, V> additionalMap,
                                                    BiFunction<V, V, V> remappingFunction) {
        for (Map.Entry<K, V> e : additionalMap.entrySet()) {
            targetMap.merge(e.getKey(), e.getValue(), remappingFunction);
        }
    }

}
