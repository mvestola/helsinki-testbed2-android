package fi.testbed2.util;


import android.location.Location;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CoordinateUtil {

    public final static BigDecimal PI = BigDecimal.valueOf(Math.PI);
    public final static double OneEightyDeg = 180.0d;
    public final static double ImageWidth = 600d;
    public final static double ImageHeight = 508d;


    public static double getCurrentPixelY(Location upperLeft, Location lowerRight, Location current) {
        double hypotenuse = upperLeft.distanceTo(current);
        double bearing = upperLeft.bearingTo(current);
        double currentDistanceY = Math.cos(bearing * Math.PI / OneEightyDeg) * hypotenuse;
        //                           "percentage to mark the position"
        double totalHypotenuse = upperLeft.distanceTo(lowerRight);
        double totalDistanceY = totalHypotenuse * Math.cos(upperLeft.bearingTo(lowerRight) * Math.PI / OneEightyDeg);
        double currentPixelY = currentDistanceY / totalDistanceY * ImageHeight;

        return currentPixelY;
    }
    public static double getCurrentPixelX(Location upperLeft, Location lowerRight, Location current) {
        double hypotenuse = upperLeft.distanceTo(current);
        double bearing = upperLeft.bearingTo(current);
        double currentDistanceX = Math.sin(bearing * Math.PI / OneEightyDeg) * hypotenuse;
        //                           "percentage to mark the position"
        double totalHypotenuse = upperLeft.distanceTo(lowerRight);
        double totalDistanceX = totalHypotenuse * Math.sin(upperLeft.bearingTo(lowerRight) * Math.PI / OneEightyDeg);
        double currentPixelX = currentDistanceX / totalDistanceX * ImageWidth;

        return currentPixelX;
    }


}
