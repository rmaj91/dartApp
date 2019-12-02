package com.rmaj91.utility;

import com.rmaj91.domain.ThrowValues;

public class Utility {

    public static final Filters filters = new Filters();

    private Utility() {}

    // statyczne metody
    // todo przerobic na cricket i master wersje
//    public static ThrowValues readValues(String string){
//
//        String value = string.replaceAll("[^0-9]","");
//
//        String multiplierString = string.replaceAll("[^a-zA-Z]","");
//
//        int intValue=25;
//        if(!value.equals("")){
//            intValue = Integer.parseInt(value);
//        }
//
//        int multiplier;
//        if(multiplierString.equalsIgnoreCase("single"))
//            multiplier=1;
//        else if(multiplierString.equalsIgnoreCase("double"))
//            multiplier=2;
//        else if(multiplierString.equalsIgnoreCase("triple"))
//            multiplier=3;
//        else
//            multiplier=0;
//
//        return new ThrowValues(intValue,multiplier);
//    }

    //////////////////
    public static int getRadiusIndex(double x,double y){
        for (Filters.RadiusScope radiusScope : filters.getRadiusList()) {
            if(radiusScope.isInRange(getRadius(x,y)))
                return filters.getRadiusList().indexOf(radiusScope);
        }
        return 7;
    }


    public static int getRadius(double x, double y){
        return (int)Math.sqrt(x*x+y*y);
    }
    ///////////////////

    public static double getAngle(double x, double y){
        double angle=Math.atan2(y,x);
        angle = Math.toDegrees(angle);
        if(angle<0)
            angle+=360;
        return angle;
    }
    ///////////////////

    public static int getAngleIndex(double x,double y){

        for (Filters.AngleScope angleScope : filters.getAngleList()) {
            if(angleScope.isInRange(getAngle(x,y)))
                return filters.getAngleList().indexOf(angleScope);
        }
        return 20;
    }

}
