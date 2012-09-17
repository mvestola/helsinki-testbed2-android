package fi.testbed2.result;

import fi.testbed2.data.MapImage;
import fi.testbed2.data.ParsedHTML;

/**
 * Results from parsing the HTML page and downloading the latest map image.
 */
public class ParseAndInitTaskResult extends AbstractTaskResult {

    private ParsedHTML parsedHTML;
    private MapImage latestImage;

    public ParseAndInitTaskResult(TaskResultType type, String message) {
        super(type, message);
    }

    public ParsedHTML getParsedHTML() {
        return parsedHTML;
    }

    public void setParsedHTML(ParsedHTML parsedHTML) {
        this.parsedHTML = parsedHTML;
    }

    public MapImage getLatestImage() {
        return latestImage;
    }

    public void setLatestImage(MapImage latestImage) {
        this.latestImage = latestImage;
    }

}
