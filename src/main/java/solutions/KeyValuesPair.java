package solutions;

import data.Key;
import data.Value;

import java.util.List;

public class KeyValuesPair {
    private final Key key;
    private final List<Value> values;

    public KeyValuesPair(Key key, List<Value> values) {
        this.key = key;
        this.values = values;
    }

    public Key getKey() {
        return key;
    }

    public List<Value> getValues() {
        return values;
    }
}
