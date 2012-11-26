package fi.testbed2.android.task.exception;

import fi.testbed2.android.app.MainApplication;

public class DownloadTaskException extends Exception {

	private static final long serialVersionUID = -1L;

	public DownloadTaskException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public DownloadTaskException(String detailMessage) {
		super(detailMessage);
	}

    public DownloadTaskException(int detailMessageId) {
        super(MainApplication.getContext().getString(detailMessageId));
    }

    public DownloadTaskException(int detailMessageId, String extraContent) {
        super(MainApplication.getContext().getString(detailMessageId, extraContent));
    }

}
