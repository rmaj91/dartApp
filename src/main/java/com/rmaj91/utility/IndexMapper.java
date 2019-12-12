package com.rmaj91.utility;

/**
 * IndexMapper class mapps dart boards fields, it's mapps fields using radius and angle scope. The value
 * contained is String name of a field eg. "DOUBLE 25".
 */
public class IndexMapper {

    //==================================================================================================
    // Properties
    //==================================================================================================
    private int indexRadius;
    private int indexAngle;
    private String fieldName;


    //==================================================================================================
    // Getters/Setters
    //==================================================================================================
    public String getFieldName() {
        return fieldName;
    }


    //==================================================================================================
    // Constructors
    //==================================================================================================
    public IndexMapper(int indexRadius, int indexAngle, String fieldName) {
        this.indexRadius = indexRadius;
        this.indexAngle = indexAngle;

        this.fieldName = fieldName;
    }

    /**
     * This methods returns the field name using angle and radius scope index
     *
     * @param indexRadius index of radius scope
     * @param indexAngle  index of angle scope
     * @return returns String fieldName eg. "DOUBLE 25"
     */
    public boolean hasFieldName(int indexRadius, int indexAngle) {
        if (indexRadius == 0 || indexRadius == 1 || indexRadius == 6)
            indexAngle = 20;
        if (this.indexAngle == indexAngle && this.indexRadius == indexRadius)
            return true;
        else
            return false;
    }


}
