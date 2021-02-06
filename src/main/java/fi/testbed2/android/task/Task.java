package fi.testbed2.android.task;

/**
 * Interface for tasks which can be executed and which can be aborted.
 */
public interface Task {

    String ERROR_MSG_CODE = "errorMsg";

    void execute();

    void cancel();

    boolean isCancelled();

}
