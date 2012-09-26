package fi.testbed2.util;

import fi.testbed2.R;
import fi.testbed2.data.TestbedMapImage;
import fi.testbed2.data.TestbedParsedPage;
import fi.testbed2.exception.DownloadTaskException;
import fi.testbed2.task.Task;
import org.apache.http.HttpEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Utility class for parsing the HTML page.
 */
public class HTMLUtil {

    private static final String HTML_TIMESTAMP_PREFIX = "var anim_timestamps = new Array(\"";
    private static final String HTML_TIMESTAMP_SUFFIX = "\");";
    private static final String HTML_IMAGE_PREFIX = "var anim_images_anim_anim = new Array(\"";
    private static final String HTML_IMAGE_SUFFIX = "\");";

    /**
     *
     * @param url from to parse timestamps and images.
     * @return ParseHTMLResult container for parsed data
     * @throws fi.testbed2.exception.DownloadTaskException on any error
     */
    public static TestbedParsedPage parseTestbedPage(final String url, Task task) throws DownloadTaskException {

        TestbedParsedPage testbedParsedPage = new TestbedParsedPage();
        HttpEntity entity = HTTPUtil.getHttpEntityForUrl(url);

        try {

            InputStream in = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String[] timestamps = null;
            String[] imageUrls = null;

            while(!task.isAbort())
            {
                String line = reader.readLine();

                if (line == null)
                    break;

                if(line.startsWith(HTML_TIMESTAMP_PREFIX) && line.endsWith(HTML_TIMESTAMP_SUFFIX)) {
                    String tmp = line.substring(HTML_TIMESTAMP_PREFIX.length(), line.length() - HTML_TIMESTAMP_SUFFIX.length());
                    tmp = tmp.replaceAll("\"", "");
                    timestamps = tmp.split(",");
                }

                else if(line.startsWith(HTML_IMAGE_PREFIX) && line.endsWith(HTML_IMAGE_SUFFIX)) {
                    String tmp = line.substring(HTML_IMAGE_PREFIX.length(), line.length() - HTML_IMAGE_SUFFIX.length());
                    tmp = tmp.replaceAll("\"", "");
                    imageUrls = tmp.split(",");
                    break;
                }
            }

            in.close();

            if (task.isAbort()) {
                return null;
            }

            // validate timestamps and imageUrls
            if(timestamps == null) {
                throw new DownloadTaskException(R.string.error_msg_parsing_timestamp);
            }
            if(imageUrls == null) {
                throw new DownloadTaskException(R.string.error_msg_parsing_urls);
            }
            if(timestamps.length != imageUrls.length) {
                throw new DownloadTaskException(R.string.error_msg_parsing_length);
            }

            int i = 0;
            for(String imageUrl : imageUrls) {
                TestbedMapImage image = new TestbedMapImage(imageUrl, timestamps[i], i);
                i++;
                testbedParsedPage.addTestbedMapImage(image);
            }

        } catch (IOException e) {
            throw new DownloadTaskException(R.string.error_msg_parsing_page);
        } catch(IndexOutOfBoundsException e) {
            throw new DownloadTaskException(R.string.error_msg_parsing_page_changed);
        }

        return testbedParsedPage;
    }
}
