package com.rmaj91.utility;

/**
 * RadiusScope class provides container for radius scope of dart board
 */
public class RadiusScope{

    /*Variables*/
    private int smallRadius;
    private int bigRadius;

    /*Constructor*/
    public RadiusScope(int smallRadius, int bigRadius) {
        this.smallRadius = smallRadius;
        this.bigRadius = bigRadius;
    }

    /*Getters*/
    public int getSmallRadius() {
        return smallRadius;
    }

    public int getBigRadius() {
        return bigRadius;
    }

    /*Public Methods*/

    /**
     * This methods checks if radius passed through parameter are in a object specified radius scope
     * @param radius Radius (pixels)
     * @return Returns true if radius is in scope and false if not.
     */
    public boolean isInRange(int radius) {
        if(radius >= smallRadius && radius < bigRadius)
            return true;
        else
            return false;
    }
}
