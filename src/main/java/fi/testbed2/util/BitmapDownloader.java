package fi.testbed2.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Log;
import fi.testbed2.R;
import fi.testbed2.app.MainApplication;
import fi.testbed2.exception.DownloadTaskException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class BitmapDownloader {

    public static Bitmap downloadTestbedBitmap(String imageURL) throws DownloadTaskException {

        InputStream stream = null;

        try {

            if (MainApplication.isDebug()) {
                Log.e(MainApplication.LOG_IDENTIFIER, "downloading image: " + imageURL);
            }

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inTempStorage =  new byte[16 * 1024];
            options.inPurgeable = true;
            options.inInputShareable = true;
            options.inDither=false;

            stream = new URL(imageURL).openStream();
            Bitmap downloadedBitmapImage = BitmapFactory.decodeStream(stream, new Rect(-1,-1,-1,-1), options);

            if (downloadedBitmapImage==null) {
                throw new DownloadTaskException(R.string.error_msg_map_image_could_not_download);
            }

            return downloadedBitmapImage;

        } catch (IOException e) {
            throw new DownloadTaskException(R.string.error_msg_io_exception);
        } finally {
            if (stream!=null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
