package solutions;

import data.Key;
import data.Value;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Result {
    final Map<Key, List<Value>> mappedValues;
    final List<Value> unknownValues;

    public Result(Map<Key, List<Value>> mappedValues, List<Value> unknownValues) {
        this.mappedValues = Collections.unmodifiableMap(mappedValues);
        this.unknownValues = Collections.unmodifiableList(unknownValues);
    }

    public Map<Key, List<Value>> getMappedValues() {
        return mappedValues;
    }

    public List<Value> getUnknownValues() {
        return unknownValues;
    }
}
