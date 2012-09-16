package fi.testbed2;

import android.graphics.drawable.BitmapDrawable;

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
