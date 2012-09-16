package fi.testbed2;

public class DownloadTaskException extends Exception {

	private static final long serialVersionUID = -1L;

	public DownloadTaskException() {
		super();
	}

	public DownloadTaskException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public DownloadTaskException(String detailMessage) {
		super(detailMessage);
	}

	public DownloadTaskException(Throwable throwable) {
		super(throwable);
	}

	
}
