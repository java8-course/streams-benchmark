package solutions;

import data.Key;
import data.Pair;
import data.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

public final class Trice {
    private Trice() {
    }

    public static Result solve(List<Pair> pairs, boolean parallel) {
        final Stream<Pair> stream1 = parallel ? pairs.parallelStream().unordered() : pairs.stream();
        final Map<String, Key> knownKeys =
                stream1
                        .map(Pair::getKey)
                        .collect(toConcurrentMap(Key::getId, Function.identity(), (o, o2) -> o));

        final Stream<Pair> stream2 = parallel ? pairs.parallelStream().unordered() : pairs.stream();
        final Map<Boolean, List<Value>> partitions =
                stream2
                        .map(Pair::getValue)
                        .collect(Collectors.partitioningBy(v -> knownKeys.containsKey(v.getKeyId())));

        final List<Value> unknownValues = partitions.getOrDefault(false, new ArrayList<>());

        final List<Value> known = partitions.getOrDefault(true, new ArrayList<>());
        final Stream<Value> stream3 = parallel ? known.parallelStream().unordered() : known.stream();
        final Map<Key, List<Value>> mappedValues = stream3
                .collect(groupingByConcurrent(v -> knownKeys.get(v.getKeyId()), toList()));

        return new Result(mappedValues, unknownValues);
    }
}
