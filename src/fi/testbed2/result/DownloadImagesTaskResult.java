package fi.testbed2.result;

import fi.testbed2.data.MapImage;

import java.util.List;

/**
 * Results from downloading all map images.
 */
public class DownloadImagesTaskResult extends AbstractTaskResult {

    private List<MapImage> mapImages;

    public DownloadImagesTaskResult(TaskResultType type, String message) {
        super(type, message);
    }

    public List<MapImage> getMapImages() {
        return mapImages;
    }

    public void setMapImages(List<MapImage> mapImages) {
        this.mapImages = mapImages;
    }
}
