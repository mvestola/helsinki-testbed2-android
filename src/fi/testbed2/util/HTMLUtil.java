package fi.testbed2.util;

import fi.testbed2.exception.DownloadTaskException;
import fi.testbed2.data.ParsedHTML;
import fi.testbed2.data.TestbedMapImage;
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
    public static ParsedHTML parseHTML(final String url) throws DownloadTaskException {

        ParsedHTML parsed = new ParsedHTML();
        HttpEntity entity = HTTPUtil.getHttpEntityForUrl(url);

        try {

            InputStream in = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String[] timestamps = null;
            String[] imageUrls = null;

            while(true)
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

            // validate timestamps and imageUrls
            if(timestamps == null) {
                throw new DownloadTaskException("Could not parse timestamps (null)");
            }
            if(imageUrls == null) {
                throw new DownloadTaskException("Could not parse image url's (null)");
            }
            if(timestamps.length != imageUrls.length) {
                throw new DownloadTaskException("Timestamps and image counts differ. timestamps.length=" + timestamps.length + ", imageUrls.length=" + imageUrls.length);
            }

            int i = 0;
            for(String imageUrl : imageUrls) {
                TestbedMapImage image = new TestbedMapImage(imageUrl, timestamps[i]);
                i++;
                parsed.addTestbedMapImage(image);
            }

        } catch (IllegalStateException e) {
            throw new DownloadTaskException("IllegalStateException: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new DownloadTaskException("IOException: " + e.getMessage(), e);
        } catch(IndexOutOfBoundsException e) {
            String message = "Parse error. This is programmers fault or webpage has changed its syntax. please report. IndexOutOfBoundsException: " + e.getMessage();
            throw new DownloadTaskException(message, e);
        }

        return parsed;
    }
}
