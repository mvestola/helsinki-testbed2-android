package fi.testbed2;

public class DownloadTaskProgress {

	public int m_progress;
	public int s_progress;
	public boolean intermediate;
	public String message;
	
	public DownloadTaskProgress(int mProgress, int sProgress, String message) {
		super();
		this.m_progress = mProgress;
		this.s_progress = sProgress;
		this.intermediate = false;
		this.message = message;
	}

	public DownloadTaskProgress(int mProgress, int sProgress, boolean intermediate, String message) {
		super();
		this.m_progress = mProgress;
		this.s_progress = sProgress;
		this.intermediate = intermediate;
		this.message = message;
	}
	
}
