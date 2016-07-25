package data;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Value {
    private final String keyId;

    public Value(String keyId) {
        this.keyId = keyId;
    }

    public String getKeyId() {
        return keyId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("keyId", keyId)
                .toString();
    }
}
