package com.mypersonalupdates.filters;

public class FilterValue {
    private String value;
    private boolean partialValue;

    public FilterValue(String value, boolean partialValue) {
        this.value = value;
        this.partialValue = partialValue;
    }

    public String getValue() {
        return this.value;
    }

    public boolean isPartialValue() {
        return this.partialValue;
    }

    public boolean equals(FilterValue other) {
        return this.value.equals(other.value) && this.partialValue == other.partialValue;
    }
}
