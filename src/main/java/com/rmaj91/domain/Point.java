package com.rmaj91.domain;

/**
 * The Point class Specifies two coordinate of point in cartesian coordinate system
 */
public class Point {

    //==================================================================================================
    // Properties
    //==================================================================================================
    private int xCoordinate;
    private int yCoordinate;


    //==================================================================================================
    // Constructors
    //==================================================================================================
    public Point(int xCoordinate, int yCoordinate) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
    }


    //==================================================================================================
    // Assesors
    //==================================================================================================
    public int getxCoordinate() {
        return xCoordinate;
    }

    public int getyCoordinate() {
        return yCoordinate;
    }
}
