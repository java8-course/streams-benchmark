package solutions;

import data.Key;
import data.MapPair;
import data.Pair;
import data.Value;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toConcurrentMap;
import static java.util.stream.Collectors.toList;

public final class MapPairSolution {
    private MapPairSolution() {
    }

    public static Result solve(List<Pair> pairs, boolean parallel) {
        final Stream<Pair> stream = parallel ? pairs.parallelStream().unordered() : pairs.stream();
        return stream
                .collect(new Collector<Pair, MapPair, Result>() {
                    @Override
                    public Supplier<MapPair> supplier() {
                        return MapPair::new;
                    }

                    @Override
                    public BiConsumer<MapPair, Pair> accumulator() {
                        return (mp, p) -> {
                            final Key key = p.getKey();
                            mp.getKeyById().putIfAbsent(key.getId(), key);

                            final Value value = p.getValue();
                            final List<Value> values =
                                    mp.getValueById()
                                            .computeIfAbsent(value.getKeyId(), (id) -> new ArrayList<>());
                            values.add(value);
                        };
                    }

                    @Override
                    public BinaryOperator<MapPair> combiner() {
                        return (p1, p2) -> {
                            final Map<String, Key> keyById1 = p1.getKeyById();
                            final Map<String, Key> keyById2 = p2.getKeyById();
                            Common.mergeMapsIntoFirstOneIfAbsent(keyById1, keyById2);

                            Common.mergeMapsIntoFirstOne(
                                    p1.getValueById(),
                                    p2.getValueById(),
                                    Common::combineListsIntoFirstOne);
                            return p1;
                        };
                    }

                    @Override
                    public Function<MapPair, Result> finisher() {
                        return (mapPair) -> {
                            final Map<String, Key> knownKeys = mapPair.getKeyById();

                            final Set<Map.Entry<String, List<Value>>> entries = mapPair.getValueById().entrySet();
                            final Stream<Map.Entry<String, List<Value>>> unknownStream =
                                    parallel ? entries.parallelStream().unordered() : entries.stream();
                            final List<Value> unknownValues =
                                    unknownStream
                                            .filter(e -> !knownKeys.containsKey(e.getKey()))
                                            .map(Map.Entry::getValue)
                                            .flatMap(Collection::parallelStream)
                                            .collect(toList());

                            final Stream<Map.Entry<String, List<Value>>> mappedStream =
                                    parallel ? entries.parallelStream().unordered() : entries.stream();
                            final Map<Key, List<Value>> mappedValues =
                                    mappedStream
                                            .map(e -> new KeyValuesPair(knownKeys.get(e.getKey()), e.getValue()))
                                            .filter(p -> p.getKey() != null)
                                            .collect(toConcurrentMap(KeyValuesPair::getKey, KeyValuesPair::getValues));

                            return new Result(mappedValues, unknownValues);
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
