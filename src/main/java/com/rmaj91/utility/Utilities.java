package com.rmaj91.utility;

/**
 * This class provides the utilities for calculating, cartesian cooridanes, angles, radius etc.
 * This class have private constructor. it's impossible to create object of this class.
 */
public class Utilities {

    /*Variables*/
    public static final Filters filters = new Filters();

    /*Private Constructor*/
    private Utilities() {
    }

    /*Public Static Methods*/

    /**
     * This method calculates radius. method use pythagorean theorem
     *
     * @param xCoordiante Cartesian x coordinate
     * @param yCoordinate Cartesian y coordinate
     * @return radius
     */
    public static int getRadius(double xCoordiante, double yCoordinate) {
        return (int) Math.sqrt(xCoordiante * xCoordiante + yCoordinate * yCoordinate);
    }

    /**
     * This method calculates radius, method use arcus tangens function
     *
     * @param xCoordinate Cartesian x coordinate
     * @param yCoordinate Cartesian y coordinate
     * @return angle, starting in 1st quarter of  cartesian coordinate system
     */
    public static double getAngle(double xCoordinate, double yCoordinate) {
        double angle = Math.atan2(yCoordinate, xCoordinate);
        angle = Math.toDegrees(angle);
        if (angle < 0)
            angle += 360;
        return angle;
    }

    /** This method uses cartesian x and y coordinates and returns index of angle scope(mapped in Filters)
     * @param xCoordinate Cartesian x coordinate
     * @param yCoordinate Cartesian y coordinate
     * @return index of angle scope
     */
    public static int getAngleIndex(double xCoordinate, double yCoordinate) {
        for (AngleScope angleScope : filters.getAngleMapperList()) {
            if (angleScope.isInRange(getAngle(xCoordinate, yCoordinate)))
                return filters.getAngleMapperList().indexOf(angleScope);
        }
        return 20;
    }

    /** This method uses cartesian x and y coordinate and returns index of radius scope(mapped in Filters)
     * @param x Cartesian x coordinate
     * @param y Cartesian y coordinate
     * @return index of radius scope
     */
    public static int getRadiusIndex(double x, double y) {
        for (RadiusScope radiusScope : filters.getRadiusMapperList()) {
            if (radiusScope.isInRange(getRadius(x, y)))
                return filters.getRadiusMapperList().indexOf(radiusScope);
        }
        return 7;
    }

    /**
     * This method checks in which scope is passed angle
     *
     * @param angle in degrees
     * @return index number of angle scope of passed angle
     */
    public static int getAngleScope(double angle) {
        for (AngleScope angleScope : filters.getAngleMapperList()) {
            if (angleScope.isInRange(angle))
                return filters.getAngleMapperList().indexOf(angleScope);
        }
        return 20;
    }

    /**
     * This method checks in which scope is passed radius
     *
     * @param radius in pixels
     * @return index number of radius scope of passed radius
     */
    public static int getRadiusScope(int radius) {
        for (RadiusScope radiusScope : filters.getRadiusMapperList()) {
            if (radiusScope.isInRange(radius))
                return filters.getRadiusMapperList().indexOf(radiusScope);
        }
        return 7;
    }

}
