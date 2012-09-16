package fi.testbed2;

public class DownloadProgress {
	
	public static final int UNKNOWN = 0;
	public static final int INIT = 1;
	public static final int PARSING = 2;
	public static final int DOWNLOADING = 3;
	
	public int code;
	public long progress;
	public long sProgress;
	public String message;

	public DownloadProgress(int code, long progress, long sProgress, String message) {
		super();
		this.code = code;
		this.progress = progress;
		this.sProgress = sProgress;
		this.message = message;
	}
	
	public DownloadProgress() {
		super();
		this.code = UNKNOWN;
		this.progress = 0;
		this.sProgress = 0;
		this.message = "UNKNOWN";
	}

}
