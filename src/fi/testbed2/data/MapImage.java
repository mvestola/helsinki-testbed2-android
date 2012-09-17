package fi.testbed2.data;

import android.graphics.drawable.BitmapDrawable;

/**
 * MapImage used in drawing the UI
 */
public class MapImage {

	public String timestamp;
	public BitmapDrawable bitmapDrawable;

	public MapImage() {
		
	}
	
	public MapImage(String timestamp, BitmapDrawable bitmapDrawable) {
		this.timestamp = timestamp;
		this.bitmapDrawable = bitmapDrawable;
	}

}
