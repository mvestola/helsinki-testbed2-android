package fi.testbed2.exception;

public class DownloadTaskException extends Exception {

	private static final long serialVersionUID = -1L;

	public DownloadTaskException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public DownloadTaskException(String detailMessage) {
		super(detailMessage);
	}

}