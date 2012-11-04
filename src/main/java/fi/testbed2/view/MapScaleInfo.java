package fi.testbed2.view;

/**
 * Represents the scale information of the map image
 */
public class MapScaleInfo {

    public static final float DEFAULT_SCALE_FACTOR = 1.0f;
    public static final float MIN_SCALE_FACTOR = 0.5f;
    public static final float MAX_SCALE_FACTOR = 3.0f;
    public static final float SCALE_STEP_WHEN_DOUBLE_TAPPING = 1.3f;

    private float scaleFactor = DEFAULT_SCALE_FACTOR;
    private float pivotX = 0.0f;
    private float pivotY = 0.0f;

    public MapScaleInfo() {
    }

    public MapScaleInfo(float scaleFactor, float pivotX, float pivotY) {
        this.pivotX = pivotX;
        this.pivotY = pivotY;
        this.scaleFactor = scaleFactor;
    }

    public float getPivotX() {
        return pivotX;
    }

    public void setPivotX(float pivotX) {
        this.pivotX = pivotX;
    }

    public float getPivotY() {
        return pivotY;
    }

    public void setPivotY(float pivotY) {
        this.pivotY = pivotY;
    }

    public float getScaleFactor() {
        return scaleFactor;
    }

    public void setScaleFactor(float scaleFactor) {
        this.scaleFactor = scaleFactor;
    }
}
