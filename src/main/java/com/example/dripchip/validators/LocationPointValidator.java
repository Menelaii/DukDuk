package com.example.dripchip.validators;

import com.example.dripchip.entities.LocationPoint;

public class LocationPointValidator {
    public static boolean isInvalid(LocationPoint locationPoint){
        return locationPoint.getLatitude() == null
                || locationPoint.getLatitude() < -90
                || locationPoint.getLatitude() > 90
                || locationPoint.getLongitude() == null
                || locationPoint.getLongitude() < -180
                || locationPoint.getLongitude() > 180;
    }
}
