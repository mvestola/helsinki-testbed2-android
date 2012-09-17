package fi.testbed2.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import fi.testbed2.app.MainApplication;
import fi.testbed2.result.AbstractTaskResult;
import fi.testbed2.R;

public class MainActivity extends Activity implements OnClickListener {

    public static final int PARSING_SUB_ACTIVITY = 1;
    public static final int ANIMATION_SUB_ACTIVITY = 2;

    private static final int ABOUT_DIALOG = 0;
	
	private ImageButton refreshButton;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        refreshButton = (ImageButton) findViewById(R.id.button_refresh);
        refreshButton.setOnClickListener(this);
        
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.main_menu_preferences:
			Intent intent = new Intent(this, MyPreferenceActivity.class);
			startActivity(intent);
			return true;
		case R.id.main_menu_about:
			showDialog(ABOUT_DIALOG);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.button_refresh) {
            MainApplication.setParsedHTML(null);
            MainApplication.setMapImageList(null);
            Intent intent = new Intent(this, ParsingActivity.class);
            startActivityForResult(intent, PARSING_SUB_ACTIVITY);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case PARSING_SUB_ACTIVITY:
                handleParsingResult(resultCode, data);
                break;
            case ANIMATION_SUB_ACTIVITY:
                handleAnimationResult(resultCode, data);
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }

	}

    private void handleParsingResult(int resultCode, Intent data) {

        switch(resultCode) {
            case MainApplication.RESULT_OK:
                startActivityForResult(new Intent(this, AnimationActivity.class), ANIMATION_SUB_ACTIVITY);
                break;
            case Activity.RESULT_CANCELED:
                Toast toast = Toast.makeText(getApplicationContext(),
                        this.getString(R.string.notice_cancelled),
                        Toast.LENGTH_SHORT);
                toast.show();
                break;
            case MainApplication.RESULT_ERROR:
                String errorMsg = this.getString(R.string.error_message_detailed,
                        data.getStringExtra(AbstractTaskResult.MSG_CODE));
                this.showErrorDialog(errorMsg);
                break;
        }

    }

    private void handleAnimationResult(int resultCode, Intent data) {

        switch(resultCode) {
            case Activity.RESULT_CANCELED:
                Toast toast = Toast.makeText(getApplicationContext(),
                        this.getString(R.string.notice_cancelled),
                        Toast.LENGTH_SHORT);
                toast.show();
                break;
            case MainApplication.RESULT_ERROR:
                String errorMsg = this.getString(R.string.error_message_detailed,
                        data.getStringExtra(AbstractTaskResult.MSG_CODE));
                this.showErrorDialog(errorMsg);
                break;
        }

    }

    private void showErrorDialog(String errorMessage) {

        AlertDialog ad = new AlertDialog.Builder(this).create();
        ad.setCancelable(false); // This blocks the 'BACK' button
        ad.setMessage(errorMessage);
        ad.setButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        ad.show();

    }

	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog;
		switch(id) {
		case ABOUT_DIALOG:
			dialog = new Dialog(this);
			dialog.setContentView(R.layout.about);
			dialog.setTitle(R.string.about_title);
			TextView text = (TextView) dialog.findViewById(R.id.about_text);
			text.setText(R.string.about_text);
			break;
		default:
			dialog = null;
		}
		
		return dialog;
	}
}