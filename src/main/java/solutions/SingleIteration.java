package solutions;

import data.Key;
import data.Pair;
import data.SubResult;
import data.Value;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

public final class SingleIteration {
    private SingleIteration() {
    }

    public static Result solve(List<Pair> pairs, boolean parallel) {
        final Stream<Pair> stream = parallel ? pairs.parallelStream().unordered() : pairs.stream();
        return stream
                .collect(new Collector<Pair, SubResult, Result>() {
                    @Override
                    public Supplier<SubResult> supplier() {
                        return SubResult::new;
                    }

                    @Override
                    public BiConsumer<SubResult, Pair> accumulator() {
                        return (subResult, pair) -> {
                            final Map<String, Key> knownKeys = subResult.getKnownKeys();
                            final Map<Key, List<Value>> mappedValues = subResult.getSubResult();
                            final Map<String, List<Value>> valuesWithoutKeys = subResult.getValuesWithoutKeys();

                            Common.putPairToMaps(knownKeys, mappedValues, valuesWithoutKeys, pair);
                        };
                    }

                    @Override
                    public BinaryOperator<SubResult> combiner() {
                        return (sr1, sr2) -> {
                            final Map<String, Key> knownKeys = sr1.getKnownKeys();
                            Common.mergeMapsIntoFirstOneIfAbsent(knownKeys, sr2.getKnownKeys());

                            final Map<Key, List<Value>> mappedValues = sr1.getSubResult();
                            Common.mergeMapsIntoFirstOne(
                                    mappedValues,
                                    sr2.getSubResult(),
                                    Common::combineListsIntoFirstOne);

                            final Map<String, List<Value>> valuesWithoutKeys = sr1.getValuesWithoutKeys();
                            Common.mergeMapsIntoFirstOne(
                                    valuesWithoutKeys,
                                    sr2.getValuesWithoutKeys(),
                                    Common::combineListsIntoFirstOne);

                            final HashMap<String, List<Value>> subResultValuesWithoutKeys = new HashMap<>();

                            for (Map.Entry<String, List<Value>> e : valuesWithoutKeys.entrySet()) {
                                final Key key = knownKeys.get(e.getKey());

                                if (key == null) {
                                    subResultValuesWithoutKeys.put(e.getKey(), e.getValue());
                                } else {
                                    final List<Value> values = mappedValues.putIfAbsent(key, e.getValue());
                                    if (values != null) {
                                        values.addAll(e.getValue());
                                    }
                                }
                            }

                            sr1.setValuesWithoutKeys(subResultValuesWithoutKeys);

                            return sr1;
                        };
                    }

                    @Override
                    public Function<SubResult, Result> finisher() {
                        return sr -> {
                            final Map<String, Key> knownKeys = sr.getKnownKeys();
                            final Map<Key, List<Value>> mappedValues = sr.getSubResult();
                            final Map<String, List<Value>> valuesWithoutKeys = sr.getValuesWithoutKeys();

                            final List<Value> resultValuesWithoutKeys = new ArrayList<>();

                            Common.remapValuesWithoutKeys(
                                    knownKeys,
                                    valuesWithoutKeys,
                                    mappedValues,
                                    resultValuesWithoutKeys);

                            return new Result(mappedValues, resultValuesWithoutKeys);
                        };
                    }

                    @Override
                    public Set<Characteristics> characteristics() {
                        return Collections.unmodifiableSet(EnumSet.of(
                                Characteristics.UNORDERED
                        ));
                    }
                });
    }
}
