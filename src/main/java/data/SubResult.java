package data;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubResult {
    private final Map<Key, List<Value>> subResult;
    private final Map<String, Key> knownKeys;
    private Map<String, List<Value>> valuesWithoutKeys;

    public SubResult() {
        this(new HashMap<>(), new HashMap<>(), new HashMap<>());
    }

    public SubResult(Map<Key, List<Value>> subResult, Map<String, Key> knownKeys, Map<String, List<Value>> valuesWithoutKeys) {
        this.subResult = subResult;
        this.knownKeys = knownKeys;
        this.valuesWithoutKeys = valuesWithoutKeys;
    }

    public Map<Key, List<Value>> getSubResult() {
        return subResult;
    }

    public Map<String, List<Value>> getValuesWithoutKeys() {
        return valuesWithoutKeys;
    }

    public void setValuesWithoutKeys(Map<String, List<Value>> valuesWithoutKeys) {
        this.valuesWithoutKeys = valuesWithoutKeys;
    }

    public Map<String, Key> getKnownKeys() {
        return knownKeys;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("subResult", subResult)
                .append("knownKeys", knownKeys)
                .append("valuesWithoutKeys", valuesWithoutKeys)
                .toString();
    }
}
