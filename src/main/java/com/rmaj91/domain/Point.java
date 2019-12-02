package com.rmaj91.domain;

/**
 * The Point class Specifies two coordinate of point in cartesian coordinate system
 */
public class Point {

    /*Variables*/
    private int xCoordinate;
    private int yCoordinate;

    /*Constructor*/
    public Point(int xCoordinate, int yCoordinate) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
    }

    /*Getters*/
    public int getxCoordinate() {
        return xCoordinate;
    }
    public int getyCoordinate() {
        return yCoordinate;
    }
}
