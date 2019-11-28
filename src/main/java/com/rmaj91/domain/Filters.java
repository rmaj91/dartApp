package com.rmaj91.domain;

import java.util.ArrayList;
import java.util.List;

public class Filters {

    List<AngleScope> angleList = new ArrayList<>();
    List<RadiusScope> radiusList = new ArrayList<>();

    public class AngleScope{
        private double firstAngle;
        private double secondAngle;

        public AngleScope(double firstAngle, double secondAngle) {
            this.firstAngle = firstAngle;
            this.secondAngle = secondAngle;
        }

        public boolean isInRange(double angle) {
            if((angle > firstAngle && angle < secondAngle)||(firstAngle > secondAngle && (angle > firstAngle || angle < secondAngle)))
                return true;
            else
                return false;

        }
    }
    public class RadiusScope{
        private int smallRadius;
        private int bigRadius;

        public RadiusScope(int smallRadius, int bigRadius) {
            this.smallRadius = smallRadius;
            this.bigRadius = bigRadius;
        }

        public boolean isInRange(int radius) {
            if(radius >= smallRadius && radius < bigRadius)
                return true;
            else
                return false;
        }
    }

    public Filters() {
        // Angle
        angleList.add(new AngleScope(7.2, 25.2));
        angleList.add(new AngleScope(25.2, 43.2));
        angleList.add(new AngleScope(43.2, 61.2));
        angleList.add(new AngleScope(61.2, 79.2));
        angleList.add(new AngleScope(79.2, 97.2));
        angleList.add(new AngleScope(97.2, 115.2));
        angleList.add(new AngleScope(115.2, 133.2));
        angleList.add(new AngleScope(133.2, 151.2));
        angleList.add(new AngleScope(151.2, 169.2));
        angleList.add(new AngleScope(169.2, 187.2));
        angleList.add(new AngleScope(187.2, 205.2));
        angleList.add(new AngleScope(205.2, 223.2));
        angleList.add(new AngleScope(223.2, 241.2));
        angleList.add(new AngleScope(241.2, 259.2));
        angleList.add(new AngleScope(259.2, 277.2));
        angleList.add(new AngleScope(277.2, 295.2));
        angleList.add(new AngleScope(295.2, 313.2));
        angleList.add(new AngleScope(313.2, 331.2));
        angleList.add(new AngleScope(331.2, 349.2));
        angleList.add(new AngleScope(349.2, 7.2));
        angleList.add(new AngleScope(0, 0));
        // Radius
        radiusList.add(new RadiusScope(0,8));
        radiusList.add(new RadiusScope(9,19));
        radiusList.add(new RadiusScope(20,117));
        radiusList.add(new RadiusScope(118,127));
        radiusList.add(new RadiusScope(128,193));
        radiusList.add(new RadiusScope(194,205));
        radiusList.add(new RadiusScope(206,249));
        radiusList.add(new RadiusScope(249,0));
    }

    public int getAngleScope(double angle){
        for (AngleScope angleScope : angleList) {
            if(angleScope.isInRange(angle))
                return angleList.indexOf(angleScope);
        }
        return 20;
    }

    public int getRadiusScope(int radius) {
        for (RadiusScope radiusScope : radiusList) {
            if(radiusScope.isInRange(radius))
                return radiusList.indexOf(radiusScope);
        }
        return 7;
    }

    public List<AngleScope> getAngleList() {
        return angleList;
    }

    public List<RadiusScope> getRadiusList() {
        return radiusList;
    }
}