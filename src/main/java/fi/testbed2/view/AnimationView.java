package fi.testbed2.view;

import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import com.jhlabs.map.Point2D;
import fi.testbed2.R;
import fi.testbed2.data.Municipality;
import fi.testbed2.data.TestbedMapImage;
import fi.testbed2.service.BitmapService;
import fi.testbed2.service.LocationService;
import fi.testbed2.service.PageService;
import fi.testbed2.service.PreferenceService;
import fi.testbed2.util.SeekBarUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * View which shows the map animation.
 */
public class AnimationView extends View {

    /**
     * Original map image dimensions.
     */
    public static final double MAP_IMAGE_ORIG_WIDTH = 600d;
    public static final double MAP_IMAGE_ORIG_HEIGHT = 508d;

    /**
     * Used for bounds calculation
     */
    private static final float GESTURE_THRESHOLD_DIP = 16.0f;

    // Scaling related
    private ScaleGestureDetector mScaleDetector;
    private GestureDetector mGestureDetector;
    private float scaleFactor = 1.0f;
    private float minScaleFactor = 0.5f;
    private float maxScaleFactor = 3.0f;
    private float scaleStepWhenDoubleTapping = 1.3f;
    private boolean mapWasScaled;

    // Animation frame related
    private int frameWidth;
    private int frameHeight;

    // Bounds calculation related
    private Rect bounds;
    private float boundsStartY;
    private float boundsStartX;
    private float boundsDistanceY;
	private float boundsDistanceX;
	private boolean boundsMoveMap;

    // Views and texts
	private TextView timestampView;
    private SeekBar seekBar;
    private String downloadProgressText;

    // Marker image caching
    private Bitmap markerImage;
    private int markerImageSize;

    // Data
    public List<Municipality> municipalities;

    // Services
    public LocationService userLocationService;
    public BitmapService bitmapService;
    public PageService pageService;
    public PreferenceService preferenceService;

    private boolean allImagesDownloaded;

    private AnimationViewPlayer player;

    /**
     * All three constructors are needed!!
     */

	public AnimationView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public AnimationView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AnimationView(Context context) {
		super(context);
	}

    public void initView(Context context) {

        player = new AnimationViewPlayer(this);

        mGestureDetector = new GestureDetector(context, new GestureListener());
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());

        BitmapDrawable firstMap = new BitmapDrawable(bitmapService.getBitmap(getMapImagesToBeDrawn().get(0)));

        frameWidth = firstMap.getMinimumWidth();
        frameHeight = firstMap.getMinimumHeight();

        player.setFrameDelay(preferenceService.getSavedFrameDelay());
        player.setCurrentFrame(0);
        player.setFrames(getMapImagesToBeDrawn().size() - 1);

    }

    public void startAnimation(TextView timestampView, SeekBar seekBar, Rect bounds, float scale) {
		player.play();
        this.scaleFactor = scale;
        this.timestampView = timestampView;
        this.seekBar = seekBar;
        if (bounds==null) {
            initializeBounds();
        } else {
            this.bounds = bounds;
        }
		next();
	}

    public void next() {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                if(player.isPlaying()) {
                    player.goToNextFrame();
                }
            }
        }, player.getFrameDelay());
    }

    private void initializeBounds() {

        // scale bounds
        int measuredHeight = getMeasuredHeight();
        int measuredWidth = getMeasuredWidth();

        if(measuredHeight > measuredWidth) {
            int scaleBy = measuredHeight-frameHeight;
            if(scaleBy > 0)
                bounds = new Rect(0, 0, frameWidth + scaleBy, frameHeight + scaleBy);
            else
                bounds = new Rect(0, 0, frameWidth, frameHeight);
        } else {
            int scaleBy = measuredWidth-frameWidth;
            if(scaleBy > 0)
                bounds = new Rect(0, 0, frameWidth + scaleBy, frameHeight + scaleBy);
            else
                bounds = new Rect(0, 0, frameWidth, frameHeight);
        }
    }

    private List<TestbedMapImage> getMapImagesToBeDrawn() {

        if (allImagesDownloaded) {
            return pageService.getTestbedParsedPage().getAllTestbedImages();
        } else {
            List<TestbedMapImage> list = new ArrayList<TestbedMapImage>();
            list.add(pageService.getTestbedParsedPage().getLatestTestbedImage());
            return list;
        }

    }

    @Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

        canvas.save();
        canvas.scale(scaleFactor, scaleFactor);

        TestbedMapImage currentMap = getMapImagesToBeDrawn().get(player.getCurrentFrame());

        updateTimestampView(currentMap);
        updateSeekBar();

        BitmapDrawable frame = new BitmapDrawable(bitmapService.getBitmap(currentMap));
        frame.setBounds(bounds);
        frame.draw(canvas);

        drawUserLocation(canvas);
        drawMunicipalities(canvas);

        canvas.restore();

    }

    private void updateTimestampView(TestbedMapImage currentMap) {

        String timestamp = currentMap.getTimestamp();
        String text = String.format("%1$02d/%2$02d @ ",
                player.getCurrentFrame() + 1 , player.getFrames() + 1) + timestamp;

        if (downloadProgressText!=null) {
            text="@ "+timestamp+"  "+downloadProgressText;
        }

        timestampView.setText(text);
        timestampView.invalidate();

    }

    private void updateSeekBar() {
        seekBar.setProgress(SeekBarUtil.getSeekBarValueFromFrameNumber(player.getCurrentFrame(),
                pageService.getTestbedParsedPage().getAllTestbedImages().size()));
    }

    private void drawUserLocation(Canvas canvas) {
        Point2D.Double userLocation = userLocationService.getUserLocationXY();
        if (userLocation!=null) {
            drawPoint(userLocation, Color.BLACK, canvas, true);
        }
    }

    private void drawMunicipalities(Canvas canvas) {

        for (Municipality municipality : municipalities) {
            if (municipality!=null) {
                drawPoint(municipality.getXyPos(), Color.BLACK, canvas, false);
            }
        }

    }

    private void drawPoint(Point2D.Double point, int color, Canvas canvas, boolean useMarker) {

        Paint paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);
        paint.setAlpha(200); // 0...255, 255 = no transparency

        double imgScaledWidth = bounds.width();
        double imgScaledHeight = bounds.height();

        double widthRatio = imgScaledWidth / MAP_IMAGE_ORIG_WIDTH;
        double heightRatio = imgScaledHeight / MAP_IMAGE_ORIG_HEIGHT;

        float xScaled = Double.valueOf(bounds.left + point.x*widthRatio).floatValue();
        float yScaled = Double.valueOf(bounds.top + point.y*heightRatio).floatValue();

        if (useMarker) {
            int markerImgSize = 32;
            Bitmap bitmap = getMarkerImage(markerImgSize);
            /*
             * x, y coordinates are image's top left corner,
             * so position the marker to the bottom center
             */
            canvas.drawBitmap(bitmap, xScaled-markerImgSize/2, yScaled-markerImgSize, paint);
        } else {
            int radius = 5;
            canvas.drawCircle(xScaled, yScaled, radius, paint);
        }

    }

    private Bitmap getMarkerImage (int size) {
        if (markerImage==null || markerImageSize!=size) {
            Bitmap bmp = BitmapFactory.decodeResource( getResources(), R.drawable.marker);
            Bitmap img = Bitmap.createScaledBitmap( bmp, size, size, true );
            bmp.recycle();
            markerImageSize = size;
            markerImage = img;
        }
        return markerImage;
    }

	@Override
	public boolean onTouchEvent(MotionEvent event) {

        mGestureDetector.onTouchEvent(event);
        mScaleDetector.onTouchEvent(event);
        calculateNewBounds(event);

		return true;
	}


    /**
     * This method is quite a mess. It is hard to understand how the bounds
     * are calculated. Do not touch this if you don't know what you are doing.
     *
     * @param event
     * @return
     */
    private boolean calculateNewBounds(MotionEvent event) {

        int action = event.getAction();
        int scaledWidth = Float.valueOf(getWidth()/ scaleFactor).intValue();
        int scaledHeight = Float.valueOf(getHeight()/ scaleFactor).intValue();
        Rect viewBounds = new Rect(0, 0, scaledWidth, scaledHeight);

        if (mapWasScaled && action==MotionEvent.ACTION_DOWN) {
            mapWasScaled = false;
        }

        if (mapWasScaled) {

            if (scaleFactor==1.0) {
                bounds.offset(0, 0);
            }

            return true;
        }

        switch(action) {
            case MotionEvent.ACTION_CANCEL:
                break;
            case MotionEvent.ACTION_UP:
                boundsMoveMap = false;
                break;
            case MotionEvent.ACTION_DOWN:
                boundsStartY = event.getY();
                boundsDistanceY = 0.0f;
                boundsStartX = event.getX();
                boundsDistanceX = 0.0f;
                break;
            case MotionEvent.ACTION_MOVE:
                boundsDistanceY = -(boundsStartY - event.getY());
                boundsDistanceX = -(boundsStartX - event.getX());

                Rect savedBounds = new Rect(bounds);

                // Convert the dips to pixels
                final float scale = getContext().getResources().getDisplayMetrics().density;
                int mGestureThreshold = (int) (GESTURE_THRESHOLD_DIP * scale / scaleFactor + 0.5f);

                if(!boundsMoveMap && (Math.abs(boundsDistanceY) > mGestureThreshold || Math.abs(boundsDistanceX) > mGestureThreshold)) {
                    boundsMoveMap = true;
                }

                if(boundsMoveMap) {

                    float mDistance_y_dip = boundsDistanceY * scale / scaleFactor + 0.5f;
                    float mDistance_x_dip = boundsDistanceX * scale / scaleFactor + 0.5f;

                    bounds.offset((int)mDistance_x_dip, (int)mDistance_y_dip);

                    if(!bounds.contains(viewBounds)) {
                        // overflow was on top or bottom y-axis, invalidate y
                        if(bounds.top > viewBounds.top || bounds.bottom < viewBounds.bottom)
                            boundsDistanceY = 0.0f;
                        // if overflow was on left or right x-axis, invalidate x
                        if(bounds.left > viewBounds.left || bounds.right < viewBounds.right)
                            boundsDistanceX = 0.0f;
                        savedBounds.offset((int) boundsDistanceX, (int) boundsDistanceY);
                        bounds = savedBounds;
                    }

                    // restart measuring distances
                    boundsStartY = event.getY();
                    boundsDistanceY = 0.0f;
                    boundsStartX = event.getX();
                    boundsDistanceX = 0.0f;

                    invalidate();
                }


                break;
            default:
                return super.onTouchEvent(event);

        }

        return true;
    }

    /*
    * ============
    * Listeners for pinch zooming
    * ============
    */

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleFactor *= detector.getScaleFactor();

            // Don't let the object get too small or too large.
            scaleFactor = Math.max(minScaleFactor, Math.min(scaleFactor, maxScaleFactor));

            mapWasScaled = true;
            invalidate();
            return true;
        }
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {

            scaleFactor *= scaleStepWhenDoubleTapping;

            if (scaleFactor >=maxScaleFactor) {
                scaleFactor = 1.0f;
            }

            mapWasScaled = true;
            invalidate();
            return true;
        }
    }

    /*
    * ============
    * Setters and getters
    * ============
    */

	public Rect getBounds() {
		return bounds;
	}

    public float getScaleFactor() {
        return scaleFactor;
    }

    public void setScaleFactor(float scaleFactor) {
        this.scaleFactor = scaleFactor;
    }

    public void updateBounds(Rect bounds) {
        if (bounds==null) {
            initializeBounds();
        } else {
            this.bounds = bounds;
        }
    }

    public void setAllImagesDownloaded(boolean allImagesDownloaded) {
        this.allImagesDownloaded = allImagesDownloaded;
    }

    public void setDownloadProgressText(String text) {
        downloadProgressText = text;
    }

    public AnimationViewPlayer getPlayer() {
        return player;
    }

}
