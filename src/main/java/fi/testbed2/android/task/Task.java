package fi.testbed2.android.task;

/**
 * Interface for tasks which can be executed and which can be aborted.
 */
public interface Task {

    public static final String ERROR_MSG_CODE = "errorMsg";

    public void execute();

    public void cancel();
    public boolean isCancelled();

}
