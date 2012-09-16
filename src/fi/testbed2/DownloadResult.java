package fi.testbed2;

import java.util.List;

public class DownloadResult {
	
	public static final int SUCCEED = 0;
	public static final int ABORTED = 1;
	public static final int ERROR   = 2;
	
	public int code;
	public String message;
	List<MapImage> mapImageList;
	
	public DownloadResult(final int code, final String message) {
		this.code = code;
		this.message = message;
	}

	public List<MapImage> getMapImageList() {
		return mapImageList;
	}

	public void setMapImageList(List<MapImage> mapImageList) {
		this.mapImageList = mapImageList;
	}
	
	
}
