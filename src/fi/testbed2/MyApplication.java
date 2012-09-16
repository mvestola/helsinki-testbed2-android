package fi.testbed2;

import java.util.List;

import android.app.Activity;
import android.app.Application;

public class MyApplication extends Application {
	public static final String TAG = "testbed2";
	
	public static final String PREF_MAP_TYPE = "PREF_MAP_TYPE";
	public static final String PREF_MAP_TIME_STEP = "PREF_MAP_TIME_STEP";
	public static final String PREF_MAP_NUMBER_OF_IMAGES = "PREF_MAP_NUMBER_OF_IMAGES";
	
	public static final int RESULT_ERROR = Activity.RESULT_FIRST_USER;
	
	private static List<MapImage> mapImageList;

	public static List<MapImage> getMapImageList() {
		return mapImageList;
	}

	public static void setMapImageList(List<MapImage> mapImageList) {
		MyApplication.mapImageList = mapImageList;
	}

}
