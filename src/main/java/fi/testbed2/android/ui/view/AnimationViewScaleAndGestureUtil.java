package fi.testbed2.android.ui.view;

import android.content.Context;
import android.view.*;
import android.widget.TextView;
import android.widget.Toast;
import com.google.inject.Inject;
import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.googlecode.androidannotations.api.Scope;
import com.jhlabs.map.Point2D;
import fi.testbed2.R;
import fi.testbed2.android.app.Logger;
import fi.testbed2.android.app.MainApplication;
import fi.testbed2.domain.Municipality;
import fi.testbed2.service.SettingsService;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Utility class for scaling and gesture related functions
 * in AnimationView.
 */
@EBean(scope = Scope.Singleton)
public class AnimationViewScaleAndGestureUtil {

    /**
     * Defines how many pixels the user is allowed to click
     * off the municipality point. If the touch is withing this
     * threshold, the municipality info is shown.
     */
    public static final float MUNICIPALITY_SEARCH_THRESHOLD = 15.0f;

    @Inject
    SettingsService settingsService;

    @Setter
    AnimationView view;

    @Getter
    private ScaleListener scaleListener;

    @Getter
    private GestureListener gestureListener;

    private Toast municipalityToast;

    public AnimationViewScaleAndGestureUtil() {
        scaleListener = new ScaleListener();
        gestureListener = new GestureListener();
    }

    /*
    * ============
    * Listeners for pinch zooming
    * ============
    */

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {

            if (detector.getScaleFactor()>1.0+MapScaleInfo.MIN_SCALE_STEP_WHEN_PINCHING ||
                    detector.getScaleFactor()<1.0-MapScaleInfo.MIN_SCALE_STEP_WHEN_PINCHING) {

                float newScaleFactor = view.getScaleInfo().getScaleFactor() * detector.getScaleFactor();

                // Don't let the map to get too small or too large.
                newScaleFactor = Math.max(MapScaleInfo.MIN_SCALE_FACTOR,
                        Math.min(newScaleFactor, MapScaleInfo.MAX_SCALE_FACTOR));
                view.getScaleInfo().setScaleFactor(newScaleFactor);
                view.getScaleInfo().setPivotX(view.getMeasuredWidth() / 2);
                view.getScaleInfo().setPivotY(view.getMeasuredHeight() / 2);

                hideMunicipalityToast();
                view.invalidate();
                return true;

            }

            return false;
        }

    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {

            view.getScaleInfo().setPivotX(e.getX());
            view.getScaleInfo().setPivotY(e.getY());
            float newScaleFactor = view.getScaleInfo().getScaleFactor() *
                    MapScaleInfo.SCALE_STEP_WHEN_DOUBLE_TAPPING;

            if (newScaleFactor >= MapScaleInfo.MAX_SCALE_FACTOR) {
                newScaleFactor = MapScaleInfo.DEFAULT_SCALE_FACTOR;
            }

            view.getScaleInfo().setScaleFactor(newScaleFactor);

            hideMunicipalityToast();
            view.invalidate();
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {

            float xCanvas = view.getCanvasUtil().convertRawXCoordinateToScaledCanvasCoordinate(e.getX(), view.getScaleInfo());
            float yCanvas = view.getCanvasUtil().convertRawYCoordinateToScaledCanvasCoordinate(e.getY(), view.getScaleInfo());

            Logger.debug("Long pressed x: " + e.getX());
            Logger.debug("Long pressed y: " + e.getY());
            Logger.debug("Long pressed x (canvas): " + xCanvas);
            Logger.debug("Long pressed y (canvas): " + yCanvas);
            Logger.debug("Scale factor: " + view.getScaleInfo().getScaleFactor());
            Logger.debug("Scale pivotX: " + view.getScaleInfo().getPivotX());
            Logger.debug("Scale pivotY: " + view.getScaleInfo().getPivotY());

            hideMunicipalityToast(); // always hide previous toast
            showInfoForMunicipality(xCanvas, yCanvas, e.getX(), e.getY());
        }

    }

    /**
     * Shows info toast about selected municipality if there is
     * a municipality close to the given x,y coordinates.
     *
     * @param canvasX X coordinate in canvas coordinate
     * @param canvasY Y coordinate in canvas coordinate
     * @param rawX Raw X coordinate from the touch event
     * @param rawY Raw Y coordinate from the touch event
     * @return True if the toast was shown, false if not shown.
     */
    public boolean showInfoForMunicipality(float canvasX, float canvasY, float rawX, float rawY) {

        for (Municipality municipality : view.getMunicipalities()) {

            if (municipality!=null) {

                Point2D.Double pos = view.getCanvasUtil().getMunicipalitiesOnScreen().get(municipality);

                if (pos!=null) {

                    Logger.debug("Pos: " + pos + ", for: " + municipality.getName());

                    final float scale = view.getContext().getResources().getDisplayMetrics().density;
                    int threshold = (int) ((settingsService.getMapPointSize()/2)*scale +
                            (MUNICIPALITY_SEARCH_THRESHOLD/view.getScaleInfo().getScaleFactor())*scale);

                    if (Math.abs(pos.x-canvasX)<=threshold && Math.abs(pos.y-canvasY)<=threshold) {

                        Logger.debug("Show info for municipality: " + municipality.getName());

                        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                        View layout = inflater.inflate(R.layout.toast_municipality,
                                (ViewGroup) view.findViewById(R.id.toast_layout_root));

                        TextView text = (TextView) layout.findViewById(R.id.text);
                        text.setText(municipality.getName());

                        municipalityToast = new Toast(view.getContext());
                        municipalityToast.setGravity(Gravity.TOP| Gravity.LEFT,
                                Double.valueOf(rawX+20*scale).intValue(),
                                Double.valueOf(rawY-40*scale).intValue());
                        municipalityToast.setDuration(Toast.LENGTH_LONG);
                        municipalityToast.setView(layout);
                        municipalityToast.show();
                        return true;

                    }

                }

            }

        }

        return false;

    }

    public void hideMunicipalityToast() {
        if (municipalityToast!=null) {
            municipalityToast.cancel();
        }
    }

    @AfterInject
    void injectRoboGuiceDependencies() {
        MainApplication.getApplication().getInjector().injectMembers(this);
    }


}
