package fi.testbed2.android.ui.view;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the scale information of the map image
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MapScaleInfo {

    public static final float DEFAULT_SCALE_FACTOR = 1.0f;
    public static final float MIN_SCALE_FACTOR = 0.5f;
    public static final float MAX_SCALE_FACTOR = 3.0f;
    public static final float SCALE_STEP_WHEN_DOUBLE_TAPPING = 1.3f;
    public static final float MIN_SCALE_STEP_WHEN_PINCHING = 0.07f;

    private float scaleFactor = DEFAULT_SCALE_FACTOR;
    private float pivotX = 0.0f;
    private float pivotY = 0.0f;

}
