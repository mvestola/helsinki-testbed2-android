package fi.testbed2.app;

import java.util.List;

import android.app.Activity;
import android.app.Application;
import fi.testbed2.data.MapImage;
import fi.testbed2.data.ParsedHTML;

public class MainApplication extends Application {

	public static final String LOG_IDENTIFIER = "testbed2";

	public static final int RESULT_ERROR = Activity.RESULT_FIRST_USER;
    public static final int RESULT_OK = Activity.RESULT_OK;

    private static List<MapImage> mapImageList;
    private static ParsedHTML parsedHTML;

    public static List<MapImage> getMapImageList() {
		return mapImageList;
	}

	public static void setMapImageList(List<MapImage> mapImageList) {
		MainApplication.mapImageList = mapImageList;
	}

    public static ParsedHTML getParsedHTML() {
        return parsedHTML;
    }

    public static void setParsedHTML(ParsedHTML parsedHTML) {
        MainApplication.parsedHTML = parsedHTML;
    }

}
