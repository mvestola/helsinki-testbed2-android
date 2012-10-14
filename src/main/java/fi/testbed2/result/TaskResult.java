package fi.testbed2.result;

public class TaskResult {

    public static final String MSG_CODE = "downloadTaskMsg";

	private TaskResultType type;
	private String message;

	public TaskResult(TaskResultType type, String message) {
		super();
		this.type = type;
		this.message = message;
	}

    public boolean isCancelled() {
        return this.type==TaskResultType.CANCELLED;
    }

    public String getMessage() {
        return message;
    }

}
