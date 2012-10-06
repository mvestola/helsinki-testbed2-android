package fi.testbed2.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import com.jhlabs.map.Point2D;
import fi.testbed2.R;
import fi.testbed2.app.MainApplication;
import fi.testbed2.data.Municipality;
import fi.testbed2.data.TestbedMapImage;
import fi.testbed2.util.CoordinateUtil;
import fi.testbed2.util.SeekBarUtil;

import java.util.ArrayList;
import java.util.List;

public class AnimationView extends View {

    /**
     * Original map image dimensions.
     */
    public static final double MAP_IMAGE_ORIG_WIDTH = 600d;
    public static final double MAP_IMAGE_ORIG_HEIGHT = 508d;

    // The gesture threshold expressed in dip
	private static final float GESTURE_THRESHOLD_DIP = 16.0f;

	private int frameDelay;
	private int currentFrame;
	private int frames;
	private int frameWidth;
	private int frameHeight;
	private Rect bounds;
	private float mStart_y;
	private float mDistance_y;
	private float mStart_x;
	private float mDistance_x;
	private boolean moveMap;
	private TextView timestampView;
	private boolean play;
    private String downloadProgressText;
    private boolean allImagesDownloaded;
    private SeekBar seekBar;

    private Bitmap markerImage;

	public AnimationView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public AnimationView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public AnimationView(Context context) {
		super(context);
		init(context);
	}
	
	public void start(TextView timestampView, SeekBar seekBar) {
		this.play = true;
		this.timestampView = timestampView;
        this.seekBar = seekBar;
        initializeBounds();
		next();
	}
	
	public void start(TextView timestampView, SeekBar seekBar, Rect bounds) {
		this.play = true;
		this.timestampView = timestampView;
        this.seekBar = seekBar;
        this.bounds = bounds;
		next();
	}
	
	public void previous() {
		this.play = false;
		
		currentFrame--;
		
		if(currentFrame < 0)
			currentFrame = Math.abs((frames - 1) - currentFrame);
		
		invalidate();
		
	}

	public void playpause() {
		if(play) {
			play = false;
		} else {
			play = true;
			next();
		}
		invalidate();
	}

    public void play() {
        play = true;
        invalidate();
    }

    public void pause() {
        play = false;
        invalidate();
    }

    public void forward() {
		this.play = false;
		currentFrame++;
		
		if(currentFrame > frames)
			currentFrame = 0;
		
		invalidate();
	}

    public void goToFrame(int frameNumber) {
        this.play = false;
        currentFrame=frameNumber;

        if(currentFrame > frames)
            currentFrame = 0;

        invalidate();
    }
	
	public void stop() {
		this.play = false;
	}

    public void setDownloadProgressText(String text) {
        downloadProgressText = text;
    }
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

        TestbedMapImage currentMap = getMapImagesToBeDrawn().get(currentFrame);
        String timestamp = currentMap.getTimestamp();
		String text = String.format("%1$02d/%2$02d @ ", currentFrame + 1 , frames + 1) + timestamp;

        if (downloadProgressText!=null) {
            text="@ "+timestamp+"  "+downloadProgressText;
        }

        seekBar.setProgress(SeekBarUtil.getSeekBarValueFromFrameNumber(currentFrame,
                MainApplication.getTestbedParsedPage().getAllTestbedImages().size()));

		timestampView.setText(text);
		timestampView.invalidate();

        BitmapDrawable frame = new BitmapDrawable(currentMap.getDownloadedBitmapImage());
        frame.setBounds(bounds);

        frame.draw(canvas);

        //drawTestPoint(canvas);
        drawUserLocation(canvas);
        drawMunicipalities(canvas);

    }

    /**
     * This method is used just for testing. It draws a red dot to the known
     * point directly at the road intersection near Humppila. This can be used
     * to check that the x,y coordinates are calculated correctly.
     */
    private void drawTestPoint(Canvas canvas) {
        Point2D.Double point = CoordinateUtil.getKnownPositionForTesting();
        drawPoint(point, Color.RED, canvas, false);
    }

    private void drawUserLocation(Canvas canvas) {
        if (MainApplication.showUserLocation()) {
            Point2D.Double userLocation = MainApplication.getUserLocationInMapPixels();
            //Point2D.Double userLocation = Municipality.getMunicipality("Helsinki").getPositionInMapPx();
            if (userLocation!=null) {
                drawPoint(userLocation, Color.BLACK, canvas, true);
            }
        }
    }

    private void drawMunicipalities(Canvas canvas) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String municipalitiesString = sharedPreferences.getString(MainApplication.PREF_LOCATION_SHOW_MUNICIPALITIES, "");

        String[] municipalities = municipalitiesString.split(
                MainApplication.PREF_LOCATION_SHOW_MUNICIPALITIES_SPLIT);

        if (municipalities.length<1 || municipalities[0].length()==0) {
            return;
        }

        for (String municipalityName : municipalities) {
            Municipality municipality = Municipality.getMunicipality(municipalityName);
            if (municipality!=null) {
                drawPoint(municipality.getPositionInMapPx(), Color.BLACK, canvas, false);
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
            canvas.drawCircle(xScaled, yScaled, 5, paint);
        }

    }

    private Bitmap getMarkerImage (int size) {
        if (markerImage ==null) {
            Bitmap bmp = BitmapFactory.decodeResource( getResources(), R.drawable.marker);
            Bitmap img = Bitmap.createScaledBitmap( bmp, size, size, true );
            bmp.recycle();
            markerImage = img;
        }
        return markerImage;
    }

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		int action = event.getAction();
		Rect viewBounds = new Rect(0, 0, getWidth(), getHeight());
		
		switch(action)
		{
		case MotionEvent.ACTION_CANCEL:
			break;
		case MotionEvent.ACTION_UP:
			moveMap = false;
			break;
		case MotionEvent.ACTION_DOWN:
			mStart_y = event.getY();
			mDistance_y = 0.0f;
			mStart_x = event.getX();
			mDistance_x = 0.0f;
			break;
		case MotionEvent.ACTION_MOVE:
			mDistance_y = -(mStart_y - event.getY());
			mDistance_x = -(mStart_x - event.getX());
			
			Rect savedBounds = new Rect(bounds);
			
			// Convert the dips to pixels
			final float scale = getContext().getResources().getDisplayMetrics().density;
			int mGestureThreshold = (int) (GESTURE_THRESHOLD_DIP * scale + 0.5f);


			if(!moveMap && (Math.abs(mDistance_y) > mGestureThreshold || Math.abs(mDistance_x) > mGestureThreshold)) {
				moveMap = true;
			}

			
			if(moveMap) {
				
				float mDistance_y_dip = mDistance_y * scale + 0.5f;
				float mDistance_x_dip = mDistance_x * scale + 0.5f;
				
				bounds.offset((int)mDistance_x_dip, (int)mDistance_y_dip);
				
				if(!bounds.contains(viewBounds)) {
					// overflow was on top or bottom y-axis, invalidate y
					if(bounds.top > viewBounds.top || bounds.bottom < viewBounds.bottom)
						mDistance_y = 0.0f;
					// if overflow was on left or right x-axis, invalidate x
					if(bounds.left > viewBounds.left || bounds.right < viewBounds.right)
						mDistance_x = 0.0f;
					savedBounds.offset((int)mDistance_x, (int)mDistance_y);
					bounds = savedBounds;
				}
				
				// restart measuring distances
				mStart_y = event.getY();
				mDistance_y = 0.0f;
				mStart_x = event.getX();
				mDistance_x = 0.0f;
				
				invalidate();
			}

			
			break;
		default:
			return super.onTouchEvent(event);
		}

		return true;
	}

    public void refresh(Context context) {
        this.init(context);
    }

	private void init(Context context) {
        
		// get default frame delay
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		frameDelay = Integer.parseInt(
                sharedPreferences.getString(MainApplication.PREF_ANIM_FRAME_DELAY, "1000"));
		
        BitmapDrawable firstMap = new BitmapDrawable(getMapImagesToBeDrawn().get(0).getDownloadedBitmapImage());

    	// Assume all frames have same dimensions
        frameWidth = firstMap.getMinimumWidth();
        frameHeight = firstMap.getMinimumHeight();

		currentFrame = 0;
		frames = getMapImagesToBeDrawn().size() - 1;
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
	
	private void next() {
		postDelayed(new Runnable() {
			
			@Override
			public void run() {
				
				if(play) {
					currentFrame++;
					
					if(currentFrame > frames)
						currentFrame = 0;
					
					invalidate();
					next();
				}

			}
		}, frameDelay);
	}

    public void setFrameDelay(int frameDelay) {
        this.frameDelay = frameDelay;
    }

	public Rect getBounds() {
		return bounds;
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

    private List<TestbedMapImage> getMapImagesToBeDrawn() {

        if (allImagesDownloaded) {
            return MainApplication.getTestbedParsedPage().getAllTestbedImages();
        } else {
            List<TestbedMapImage> list = new ArrayList<TestbedMapImage>();
            list.add(MainApplication.getTestbedParsedPage().getLatestTestbedImage());
            return list;
        }

    }

}
