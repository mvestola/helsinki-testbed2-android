package fi.testbed2.android.ui.view.util;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Rect;

import com.google.inject.Inject;
import com.jhlabs.map.Point2D;
import com.larvalabs.svgandroid.SVGParser;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;

import java.util.List;
import java.util.Map;

import fi.testbed2.android.app.MainApplication;
import fi.testbed2.android.ui.svg.LocationMarkerSVG;
import fi.testbed2.android.ui.svg.MunicipalityMarkerSVG;
import fi.testbed2.android.ui.view.MapScaleInfo;
import fi.testbed2.domain.MapLocationXY;
import fi.testbed2.domain.Municipality;
import fi.testbed2.service.LocationService;
import fi.testbed2.service.SettingsService;
import lombok.Getter;
import lombok.Setter;
import roboguice.RoboGuice;

/**
 * Utility class for canvas drawing operations used in AnimationView.
 */
@EBean(scope = EBean.Scope.Singleton)
public class AnimationViewCanvasUtil {

    /**
     * Original map image dimensions.
     */
    public static final double MAP_IMAGE_ORIG_WIDTH = 600d;
    public static final double MAP_IMAGE_ORIG_HEIGHT = 508d;

    @Inject
    SettingsService settingsService;

    @Inject
    LocationService userLocationService;

    // For caching the images
    private Picture locationMarkerImage;
    private Picture municipalityMarkerImage;

    @Getter
    @Setter
    private Map<Municipality, Point2D.Double> municipalitiesOnScreen;


    public float convertRawXCoordinateToScaledCanvasCoordinate(float rawX, MapScaleInfo scaleInfo) {
        return (rawX - scaleInfo.getPivotX()) / scaleInfo.getScaleFactor() + scaleInfo.getPivotX();
    }

    public float convertRawYCoordinateToScaledCanvasCoordinate(float rawY, MapScaleInfo scaleInfo) {
        return (rawY - scaleInfo.getPivotY()) / scaleInfo.getScaleFactor() + scaleInfo.getPivotY();
    }

    public void drawUserLocation(Canvas canvas, Rect bounds) {
        MapLocationXY userLocation = userLocationService.getUserLocationXY();
        if (userLocation != null) {
            drawPoint(userLocation, canvas, bounds, null);
        }
    }

    public void drawMunicipalities(Canvas canvas, Rect bounds, List<Municipality> municipalities) {

        for (Municipality municipality : municipalities) {
            if (municipality != null) {
                drawPoint(municipality.getXyPos(), canvas, bounds, municipality);
            }
        }

    }

    public void resetMarkerAndPointImageCache() {
        locationMarkerImage = null;
        municipalityMarkerImage = null;
    }

    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    private void drawPoint(MapLocationXY point, Canvas canvas, Rect bounds, Municipality municipality) {

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        paint.setAlpha(200); // 0...255, 255 = no transparency, does not affect SVG transparency!

        double imgScaledWidth = bounds.width();
        double imgScaledHeight = bounds.height();

        double widthRatio = imgScaledWidth / MAP_IMAGE_ORIG_WIDTH;
        double heightRatio = imgScaledHeight / MAP_IMAGE_ORIG_HEIGHT;

        float xScaled = Double.valueOf(bounds.left + point.getX() * widthRatio).floatValue();
        float yScaled = Double.valueOf(bounds.top + point.getY() * heightRatio).floatValue();

        int xInt = Float.valueOf(xScaled).intValue();
        int yInt = Float.valueOf(yScaled).intValue();

        if (municipality == null) {

            Picture pic = getLocationMarkerImage();

            int markerImageHeight = settingsService.getMapMarkerSizePx();

            // Scale width a bit larger than original width (otherwise looks a bit too thin marker)
            float ratio = pic.getWidth() / pic.getHeight();
            int width = Float.valueOf(markerImageHeight * ratio + markerImageHeight / 5).intValue();

            /*
             * x, y coordinates are image's top left corner,
             * so position the marker to the bottom center
             */
            int left = xInt - width / 2;
            int top = yInt - markerImageHeight;
            int right = xInt + width / 2;

            drawPicture(pic, canvas, new Rect(left, top, right, yInt));

        } else {

            // Save canvas coordinates for info toast
            this.municipalitiesOnScreen.put(municipality,
                    new Point2D.Double(xInt, yInt));

            Picture pic = getMunicipalityMarkerImage();

            int circleDiameter = settingsService.getMapPointSizePx();

            /*
             * x, y coordinates are image's top left corner,
             * so position the marker to the center
             */

            int left = xInt - circleDiameter / 2;
            int top = yInt - circleDiameter / 2;
            int right = xInt + circleDiameter / 2;
            int bottom = yInt + circleDiameter / 2;

            drawPicture(pic, canvas, new Rect(left, top, right, bottom));
        }

    }

    private void drawPicture(Picture pic, Canvas canvas, Rect rect) {

        try {
            canvas.drawPicture(pic, rect);
        } catch (Throwable e) {
            /*
             * Some user's seem to use Force GPU setting which does not support
             * the drawPicture method (throws java.lang.UnsupportedOperationException).
             * Do not show anything to those users but an alert dialog
             * that they should disabled Force GPU settings.
             */
        }
    }

    private Picture getLocationMarkerImage() {

        if (locationMarkerImage == null) {
            String color = settingsService.getMapMarkerColor();
            locationMarkerImage = SVGParser.getSVGFromString(new LocationMarkerSVG(color).getXmlContent()).getPicture();
        }
        return locationMarkerImage;

    }

    private Picture getMunicipalityMarkerImage() {

        if (municipalityMarkerImage == null) {
            String color = settingsService.getMapPointColor();
            municipalityMarkerImage = SVGParser.getSVGFromString(new MunicipalityMarkerSVG(color).getXmlContent()).getPicture();
        }
        return municipalityMarkerImage;

    }

    @AfterInject
    void injectRoboGuiceDependencies() {
        RoboGuice.getInjector(MainApplication.getContext()).injectMembers(this);
    }

}
