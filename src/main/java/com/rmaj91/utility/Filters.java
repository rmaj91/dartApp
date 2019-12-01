package com.rmaj91.utility;

import java.util.ArrayList;
import java.util.List;

public class Filters {

    List<AngleScope> angleList = new ArrayList<>();
    List<RadiusScope> radiusList = new ArrayList<>();
    List<IndexMapper> indexMapperList = new ArrayList<>();




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

        //todo test

        public int getSmallRadius() {
            return smallRadius;
        }

        public int getBigRadius() {
            return bigRadius;
        }
    }

    public class IndexMapper{
        private int indexRadius;
        private int indexAngle;
        private String key;

        public IndexMapper(int indexRadius, int indexAngle, String key) {
            this.indexRadius = indexRadius;
            this.indexAngle = indexAngle;

            this.key = key;
        }

        public boolean hasKey(int indexRadius,int indexAngle){
            if(indexRadius == 0 ||indexRadius == 1 ||indexRadius == 6 )
                indexAngle=20;
            if(this.indexAngle == indexAngle && this.indexRadius == indexRadius)
                return true;
            else
                return false;
        }

        public String getKey() {
            return key;
        }
    }

    public Filters() {
        // Angle
        for(int i=0;i<19;i++){
            angleList.add(new AngleScope(9+18*i,27+18*i));
        }
        angleList.add(new AngleScope(351,9));
        angleList.add(new AngleScope(0,0));

        // Radius mapping //
        radiusList.add(new RadiusScope(0,8));
        radiusList.add(new RadiusScope(9,19));
        radiusList.add(new RadiusScope(20,117));
        radiusList.add(new RadiusScope(118,127));
        radiusList.add(new RadiusScope(128,193));
        radiusList.add(new RadiusScope(194,205));
        radiusList.add(new RadiusScope(206,249));
        radiusList.add(new RadiusScope(249,0));

        // Fields Mapping //
        ///////////
        indexMapperList.add(new IndexMapper(0,20,"DOUBLE 25"));
        indexMapperList.add(new IndexMapper(1,20,"SINGLE 25"));
        indexMapperList.add(new IndexMapper(6,20,"BARREL"));
        // SINGLE FIELDS mapping
        for(int i=2;i<=4;i+=2){
            indexMapperList.add(new IndexMapper(i,0,"SINGLE 13"));
            indexMapperList.add(new IndexMapper(i,1,"SINGLE 4"));
            indexMapperList.add(new IndexMapper(i,2,"SINGLE 18"));
            indexMapperList.add(new IndexMapper(i,3,"SINGLE 1"));
            indexMapperList.add(new IndexMapper(i,4,"SINGLE 20"));
            indexMapperList.add(new IndexMapper(i,5,"SINGLE 5"));
            indexMapperList.add(new IndexMapper(i,6,"SINGLE 12"));
            indexMapperList.add(new IndexMapper(i,7,"SINGLE 9"));
            indexMapperList.add(new IndexMapper(i,8,"SINGLE 14"));
            indexMapperList.add(new IndexMapper(i,9,"SINGLE 11"));
            indexMapperList.add(new IndexMapper(i,10,"SINGLE 8"));
            indexMapperList.add(new IndexMapper(i,11,"SINGLE 16"));
            indexMapperList.add(new IndexMapper(i,12,"SINGLE 7"));
            indexMapperList.add(new IndexMapper(i,13,"SINGLE 19"));
            indexMapperList.add(new IndexMapper(i,14,"SINGLE 3"));
            indexMapperList.add(new IndexMapper(i,15,"SINGLE 17"));
            indexMapperList.add(new IndexMapper(i,17,"SINGLE 15"));
            indexMapperList.add(new IndexMapper(i,18,"SINGLE 10"));
            indexMapperList.add(new IndexMapper(i,19,"SINGLE 6"));
        }

        // DOUBLE FIELDS mapping
        indexMapperList.add(new IndexMapper(5,0,"DOUBLE 13"));
        indexMapperList.add(new IndexMapper(5,1,"DOUBLE 4"));
        indexMapperList.add(new IndexMapper(5,2,"DOUBLE 18"));
        indexMapperList.add(new IndexMapper(5,3,"DOUBLE 1"));
        indexMapperList.add(new IndexMapper(5,4,"DOUBLE 20"));
        indexMapperList.add(new IndexMapper(5,5,"DOUBLE 5"));
        indexMapperList.add(new IndexMapper(5,6,"DOUBLE 12"));
        indexMapperList.add(new IndexMapper(5,7,"DOUBLE 9"));
        indexMapperList.add(new IndexMapper(5,8,"DOUBLE 14"));
        indexMapperList.add(new IndexMapper(5,9,"DOUBLE 11"));
        indexMapperList.add(new IndexMapper(5,10,"DOUBLE 8"));
        indexMapperList.add(new IndexMapper(5,11,"DOUBLE 16"));
        indexMapperList.add(new IndexMapper(5,12,"DOUBLE 7"));
        indexMapperList.add(new IndexMapper(5,13,"DOUBLE 19"));
        indexMapperList.add(new IndexMapper(5,14,"DOUBLE 3"));
        indexMapperList.add(new IndexMapper(5,15,"DOUBLE 17"));
        indexMapperList.add(new IndexMapper(5,16,"DOUBLE 2"));
        indexMapperList.add(new IndexMapper(5,17,"DOUBLE 15"));
        indexMapperList.add(new IndexMapper(5,18,"DOUBLE 10"));
        indexMapperList.add(new IndexMapper(5,19,"DOUBLE 6"));

        // TRIPLE FIELDS mapping
        indexMapperList.add(new IndexMapper(3,0,"TRIPLE 13"));
        indexMapperList.add(new IndexMapper(3,1,"TRIPLE 4"));
        indexMapperList.add(new IndexMapper(3,2,"TRIPLE 18"));
        indexMapperList.add(new IndexMapper(3,3,"TRIPLE 1"));
        indexMapperList.add(new IndexMapper(3,4,"TRIPLE 20"));
        indexMapperList.add(new IndexMapper(3,5,"TRIPLE 5"));
        indexMapperList.add(new IndexMapper(3,6,"TRIPLE 12"));
        indexMapperList.add(new IndexMapper(3,7,"TRIPLE 9"));
        indexMapperList.add(new IndexMapper(3,8,"TRIPLE 14"));
        indexMapperList.add(new IndexMapper(3,9,"TRIPLE 11"));
        indexMapperList.add(new IndexMapper(3,10,"TRIPLE 8"));
        indexMapperList.add(new IndexMapper(3,11,"TRIPLE 16"));
        indexMapperList.add(new IndexMapper(3,12,"TRIPLE 7"));
        indexMapperList.add(new IndexMapper(3,13,"TRIPLE 19"));
        indexMapperList.add(new IndexMapper(3,14,"TRIPLE 3"));
        indexMapperList.add(new IndexMapper(3,15,"TRIPLE 17"));
        indexMapperList.add(new IndexMapper(3,16,"TRIPLE 2"));
        indexMapperList.add(new IndexMapper(3,17,"TRIPLE 15"));
        indexMapperList.add(new IndexMapper(3,18,"TRIPLE 10"));
        indexMapperList.add(new IndexMapper(3,19,"TRIPLE 6"));
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

    public List<IndexMapper> getIndexMapperList() {
        return indexMapperList;
    }

}