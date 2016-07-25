package solutions;

import data.Key;
import data.Pair;
import data.Value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Classic {
    private Classic(){}

    public static Result solve(List<Pair> pairs) {
        final Map<String, Key> knownKeys = new HashMap<>();
        final Map<Key, List<Value>> mappedValues = new HashMap<>();
        final Map<String, List<Value>> unknownValues = new HashMap<>();

        for (Pair pair : pairs) {
            Common.putPairToMaps(knownKeys, mappedValues, unknownValues, pair);
        }

        final List<Value> resultUnknownValues = new ArrayList<>();

        Common.remapValuesWithoutKeys(knownKeys, unknownValues, mappedValues, resultUnknownValues);

        return new Result(mappedValues, resultUnknownValues);
    }
}
