package fi.testbed2.result;

import fi.testbed2.data.MapImage;

import java.util.List;

public abstract class AbstractTaskResult {

    public static final String MSG_CODE = "downloadTaskMsg";

	private TaskResultType type;
	private String message;

	public AbstractTaskResult(TaskResultType type, String message) {
		super();
		this.type = type;
		this.message = message;
	}

    public boolean isError() {
        return this.type==TaskResultType.ERROR;
    }

    public String getMessage() {
        return message;
    }

}
