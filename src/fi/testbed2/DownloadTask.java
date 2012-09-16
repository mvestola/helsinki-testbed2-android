package fi.testbed2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DownloadTask extends AsyncTask<Void, DownloadTaskProgress, DownloadTaskResult> {

	private static final int HTTP_VALID_STATUS_CODE = 200;
	private static final String HTML_TIMESTAMP_PREFIX = "var anim_timestamps = new Array(\"";
	private static final String HTML_TIMESTAMP_SUFFIX = "\");";
	private static final String HTML_IMAGE_PREFIX = "var anim_images_anim_anim = new Array(\"";
	private static final String HTML_IMAGE_SUFFIX = "\");";

	private boolean abort;
	private String url;
	private ProgressBar progressBar;
	private TextView progressTextView;
	private Activity activity;

	public DownloadTask(final Context context, final Activity activity) {
		
		abort = false;
		
		// construct URL where to download content
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		String mapType = sharedPreferences.getString("PREF_MAP_TYPE", "radar");
		String mapTimeStep = sharedPreferences.getString("PREF_MAP_TIME_STEP", "5");
		String mapNumberOfImages = sharedPreferences.getString("PREF_MAP_NUMBER_OF_IMAGES", "10");
		
		url = activity.getString(R.string.testbed_base_url, mapType, mapTimeStep, mapNumberOfImages);
		
		// find progress bar etc
		progressBar = (ProgressBar) activity.findViewById(R.id.progressbar);
		progressTextView = (TextView) activity.findViewById(R.id.progresstext);
		this.activity = activity;
	}
	
	public void abort() {
		this.abort = true;
	}
	
	@Override
	protected void onCancelled() {
		super.onCancelled();
//		activity.setResult(Activity.RESULT_CANCELED);
//		activity.finish();
	}

	@Override
	protected void onPostExecute(DownloadTaskResult result) {
		super.onPostExecute(result);
		
		if(result.code == DownloadTaskResult.DL_CODE_OK)
		{
			Log.i(MyApplication.TAG, result.message);
			MyApplication.setMapImageList(result.mapImageList);
			activity.setResult(Activity.RESULT_OK);
			activity.finish();
		}
		else if(result.code == DownloadTaskResult.DL_CODE_ERROR)
		{
			Log.e(MyApplication.TAG, result.message);
			activity.setResult(MyApplication.RESULT_ERROR);
			activity.finish();
		}
		
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected void onProgressUpdate(DownloadTaskProgress... values) {
		super.onProgressUpdate(values);
		DownloadTaskProgress progress = values[0];
			
		progressTextView.setText(progress.message);
		
		if(progress.intermediate != progressBar.isIndeterminate()) {
			progressBar.setIndeterminate(progress.intermediate);
		}

		progressBar.setProgress(progress.m_progress);
		progressBar.setSecondaryProgress(progress.s_progress);
	}

	@Override
	protected DownloadTaskResult doInBackground(Void... params) {
		
		try
		{
			long downloadStart = System.currentTimeMillis();

			publishProgress(new DownloadTaskProgress(0, 0, true, activity.getString(R.string.progress_parsing)));
			
			parseHTMLResult parsed = parseHTML(url);
			
			publishProgress(new DownloadTaskProgress(100, 0, true, activity.getString(R.string.progress_done)));
						
			List<Bitmap> bitmapList = downloadImages(parsed.imageUrls);
			
			// check we have as many timestamps as bitmaps
			if(parsed.len != bitmapList.size()) {
				throw new DownloadTaskException("parsed.timestampList.size() != bitmapList.size()");
			}
			
			// construct result
			List<MapImage> mapImageList = new ArrayList<MapImage>();
			for(int n = 0; n < parsed.len; n++)
			{
				String timestamp = parsed.timestamps[n];
				Bitmap bitmap = bitmapList.get(n);
				
				String localTimestamp = timeStampToLocal(timestamp);
				
				MapImage mapImage = new MapImage(localTimestamp, new BitmapDrawable(bitmap));
				
				mapImageList.add(mapImage);
			}
			
			long downloadEnd = System.currentTimeMillis();
			long ms = downloadEnd - downloadStart;
			float sec = ms / 1000.0f; 
			String message = String.format("%1$d images downloaded in %2$.1f seconds", mapImageList.size(), sec);
			
			// return valid result
			DownloadTaskResult result = new DownloadTaskResult(DownloadTaskResult.DL_CODE_OK, message);
			result.mapImageList = mapImageList;
			return result;
			
		}
		catch(DownloadTaskException e)
		{
			return new DownloadTaskResult(DownloadTaskResult.DL_CODE_ERROR, e.getMessage());
		}
		
	}
	
	private HttpEntity getHttpEntityForUrl(final String url) throws DownloadTaskException {

			try
			{
				DefaultHttpClient client = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet(url);
				HttpResponse response = client.execute(httpGet);
				StatusLine statusLine = response.getStatusLine();
				
				if(statusLine == null) {
					String message = "Invalid HTTP status line (init)";
					throw new DownloadTaskException(message);
				}
				
				int statusCode = statusLine.getStatusCode();
				
				if(statusCode != HTTP_VALID_STATUS_CODE) {
					String message = "Invalid HTTP status code (init): " + statusCode;
					throw new DownloadTaskException(message);
				}
				
				HttpEntity entity = response.getEntity();
				
				if(entity == null) {
					throw new DownloadTaskException("Unable to retrieve HttpEntity for url: " + url);
				}
				
				return entity;
			}
			catch(IllegalArgumentException e)
			{
				throw new DownloadTaskException("IllegalArgumentException. Invalid url? url=" + url, e);
			} catch (ClientProtocolException e) {
				throw new DownloadTaskException("ClientProtocolException: " + e.getMessage(), e);
			} catch (IOException e) {
				throw new DownloadTaskException("IOException: " + e.getMessage(), e);
			}
		
	}
	
	private String timeStampToLocal(String gmtstamp) {
        int year = Integer.parseInt(gmtstamp.substring(0, 4));
        int month = Integer.parseInt(gmtstamp.substring(4, 6));
        int day = Integer.parseInt(gmtstamp.substring(6, 8));
        int hour = Integer.parseInt(gmtstamp.substring(8, 10));
        int minute = Integer.parseInt(gmtstamp.substring(10, 12));
        int second = 0;
                        
        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("Helsinki"));
        cal.set(year + 1900, month, day, hour, minute, second);
        cal.setTimeInMillis(cal.getTimeInMillis()); // XXX + Calendar.getInstance().get(Calendar.DST_OFFSET));

        String localStamp = String.format("%1$tH:%2$tM", cal.getTime(), cal.getTime());
        
        return localStamp;
	}

	private class parseHTMLResult {
		public String[] timestamps;
		public String[] imageUrls;
		public int len;
	}
	
	/**
	 * 
	 * @param url from to parse timestamps and images.
	 * @return parseHTMLResult container for parsed data
	 * @throws DownloadTaskException on any error
	 */
	private parseHTMLResult parseHTML(final String url) throws DownloadTaskException {
		HttpEntity entity = getHttpEntityForUrl(url);
		try {

			InputStream in = entity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			
			String[] timestamps = null;
			String[] imageUrls = null;
			
			while(!abort)
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
			
			if(abort) {
				String message = "task aborted";
				throw new DownloadTaskException(message);
			}
			
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
			
			parseHTMLResult result = new parseHTMLResult();
			result.len = timestamps.length;
			result.imageUrls = imageUrls; 
			result.timestamps = timestamps;

			return result;
			
		} catch (IllegalStateException e) {
			throw new DownloadTaskException("IllegalStateException: " + e.getMessage(), e);
		} catch (IOException e) {
			throw new DownloadTaskException("IOException: " + e.getMessage(), e);
		} catch(IndexOutOfBoundsException e) {
			String message = "Parse error. This is programmers fault or webpage has changed its syntax. please report. IndexOutOfBoundsException: " + e.getMessage();
			throw new DownloadTaskException(message, e);
		}
	}
	
	/**
	 * 
	 * @param imageUrls
	 * @return
	 * @throws DownloadTaskException 
	 */
	private List<Bitmap> downloadImages(final String[] imageUrls) throws DownloadTaskException {
		
		int downloaded = 0;
		int m_progress = 0;
		int s_progress = 0;

		List<Bitmap> resultList = new ArrayList<Bitmap>();
		
		String progressText;
		
		for(String url : imageUrls)
		{

			progressText = String.format(activity.getString(R.string.progress_downloading), downloaded + 1, imageUrls.length);
			publishProgress(new DownloadTaskProgress(m_progress, s_progress, progressText));

            try {

                if(abort) {
					String message = "task aborted";
					throw new DownloadTaskException(message);
				}
				
				Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(url).getContent());

                if(bitmap == null) {
					String message = "image cound not be decoded (null)";
					Log.e(MyApplication.TAG, message);
					throw new DownloadTaskException(message);
				}
				
				resultList.add(bitmap);
				
			} catch (IllegalStateException e) {
				throw new DownloadTaskException("IllegalStateException: " + e.getMessage(), e);
			} catch (IOException e) {
				throw new DownloadTaskException("IOException: " + e.getMessage(), e);
			}
			
			downloaded++;
			
			m_progress = (int) (((double)downloaded / imageUrls.length) * 100);

			publishProgress(new DownloadTaskProgress(m_progress, s_progress, progressText));
		}
		
		return resultList;
	}
}
