package data;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Pair {
    private final Key key;
    private final Value value;

    public Pair(Key key, Value value) {
        this.key = key;
        this.value = value;
    }

    public Key getKey() {
        return key;
    }

    public Value getValue() {
        return value;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("key", key)
                .append("value", value)
                .toString();
    }
}
