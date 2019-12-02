package com.rmaj91.domain;

/**
 * The ThrowValues class contain two variables specifying throw of dart.
 */
public class ThrowValues {

    //value of throwns dart field
    private int value;
    //multiplier of thrown dart field
    private int mulitplier;

    /*Constructor*/
    public ThrowValues(int value, int mulitplier) {
        this.value = value;
        this.mulitplier = mulitplier;
    }

    /*Getters*/
    public int getValue() {
        return value;
    }
    public int getMulitplier() {
        return mulitplier;
    }

    /*Public Methods*/
    @Override
    public String toString() {
        return "ThrowValues{" +
                "value=" + value +
                ", mulitplier=" + mulitplier +
                '}';
    }
}
