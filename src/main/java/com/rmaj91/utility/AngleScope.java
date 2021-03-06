package com.rmaj91.utility;

/**
 * AngleScope class provides container for angles scope of dart board
 */
public class AngleScope {


    //==================================================================================================
    // Properties
    //==================================================================================================
    private double firstAngle;
    private double secondAngle;


    //==================================================================================================
    // Constructors
    //==================================================================================================
    public AngleScope(double firstAngle, double secondAngle) {
        this.firstAngle = firstAngle;
        this.secondAngle = secondAngle;
    }


    //==================================================================================================
    // Getters/Setters
    //==================================================================================================
    public double getFirstAngle() {
        return firstAngle;
    }

    public double getSecondAngle() {
        return secondAngle;
    }


    //==================================================================================================
    // Public Methods
    //==================================================================================================

    /**
     * This methods checks if angle passed through parameter are in a object specified angle scope
     *
     * @param angle in degrees
     * @return Returns true if angle is in scope and false if not.
     */
    public boolean isInRange(double angle) {
        if ((angle > firstAngle && angle < secondAngle) || (firstAngle > secondAngle && (angle > firstAngle || angle < secondAngle)))
            return true;
        else
            return false;

    }
}