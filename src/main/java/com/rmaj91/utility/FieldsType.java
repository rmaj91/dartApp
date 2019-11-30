package com.rmaj91.utility;

public enum FieldsType {
    SINGLE(1),
    DOUBLE(2),
    TRIPPLE(3);

    private int value;

    public int getValue() {
        return value;
    }

    FieldsType(int value) {
        this.value = value;
    }
}
