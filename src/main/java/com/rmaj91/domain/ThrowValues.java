package com.rmaj91.domain;

/**
 * The ThrowValues class contain two variables specifying throw of dart.
 */
public class ThrowValues {

    //==================================================================================================
    // Properties
    //==================================================================================================
    private int value;
    private int mulitplier;


    //==================================================================================================
    // Constructors
    //==================================================================================================
    public ThrowValues(int value, int mulitplier) {
        this.value = value;
        this.mulitplier = mulitplier;
    }


    //==================================================================================================
    // Assesors
    //==================================================================================================
    public int getValue() {
        return value;
    }

    public int getMulitplier() {
        return mulitplier;
    }


    //==================================================================================================
    // Public methods
    //==================================================================================================
    @Override
    public String toString() {
        return "ThrowValues{" +
                "value=" + value +
                ", mulitplier=" + mulitplier +
                '}';
    }
}
