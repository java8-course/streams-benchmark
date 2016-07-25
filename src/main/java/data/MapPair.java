package data;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapPair {
    private final Map<String, Key> keyById;
    private final Map<String, List<Value>> valueById;

    public MapPair() {
        this(new HashMap<>(), new HashMap<>());
    }

    public MapPair(Map<String, Key> keyById, Map<String, List<Value>> valueById) {
        this.keyById = keyById;
        this.valueById = valueById;
    }

    public Map<String, Key> getKeyById() {
        return keyById;
    }

    public Map<String, List<Value>> getValueById() {
        return valueById;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("keyById", keyById)
                .append("valueById", valueById)
                .toString();
    }
}
