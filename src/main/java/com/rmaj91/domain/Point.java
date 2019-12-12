package com.rmaj91.domain;

/**
 * The Point class Specifies two coordinate of point in cartesian coordinate system, this class is used for
 * drawing dart board and displaying board fields highlight
 */
public class Point {

    private int xCoordinate;
    private int yCoordinate;

    public Point(int xCoordinate, int yCoordinate) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
    }

    public int getxCoordinate() {
        return xCoordinate;
    }

    public int getyCoordinate() {
        return yCoordinate;
    }
}
