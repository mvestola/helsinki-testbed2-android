package fi.testbed2;

import java.util.List;

public class DownloadTaskResult {

	public static final int DL_CODE_OK = 0;
	public static final int DL_CODE_ERROR = 1;

	public int code;
	public String message;
	public List<MapImage> mapImageList;
	
	public DownloadTaskResult(int code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

	
}
