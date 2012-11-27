package fi.testbed2.android.ui.util;

import fi.testbed2.android.ui.view.MapScaleInfo;

public class CanvasUtil {

    public static float convertRawXCoordinateToScaledCanvasCoordinate(float rawX, MapScaleInfo scaleInfo) {
        return (rawX-scaleInfo.getPivotX())/scaleInfo.getScaleFactor()+scaleInfo.getPivotX();
    }

    public static float convertRawYCoordinateToScaledCanvasCoordinate(float rawY, MapScaleInfo scaleInfo) {
        return (rawY-scaleInfo.getPivotY())/scaleInfo.getScaleFactor()+scaleInfo.getPivotY();
    }


}
