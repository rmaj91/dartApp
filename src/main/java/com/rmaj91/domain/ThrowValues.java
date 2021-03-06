package com.rmaj91.domain;

/**
 * The ThrowValues class contain two variables specifying throw of dart.
 */
public class ThrowValues {

    private int value;
    private int mulitplier;


    public ThrowValues(int value, int mulitplier) {
        this.value = value;
        this.mulitplier = mulitplier;
    }


    public int getValue() {
        return value;
    }

    public int getMulitplier() {
        return mulitplier;
    }

    @Override
    public String toString() {
        return "ThrowValues{" +
                "value=" + value +
                ", mulitplier=" + mulitplier +
                '}';
    }
}
