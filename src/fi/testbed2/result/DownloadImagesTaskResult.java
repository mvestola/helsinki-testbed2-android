package fi.testbed2.result;

import java.util.List;
import fi.testbed2.data.TestbedMapImage;

/**
 * Results from downloading all map images.
 */
public class DownloadImagesTaskResult extends AbstractTaskResult {

    public DownloadImagesTaskResult(TaskResultType type, String message) {
        super(type, message);
    }

}
