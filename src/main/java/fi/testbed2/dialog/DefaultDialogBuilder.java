package fi.testbed2.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.widget.TextView;
import fi.testbed2.R;
import fi.testbed2.app.MainApplication;
import fi.testbed2.app.Preference;

public class DefaultDialogBuilder implements DialogBuilder {

    @Override
    public AlertDialog getAboutAlertDialog(final Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(getAboutDialogContents(context))
                .setPositiveButton(context.getText(R.string.about_visit_website), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Open app homepage
                        Uri url = Uri.parse(context.getString(R.string.app_homepage));
                        Intent intent = new Intent(Intent.ACTION_VIEW, url);
                        context.startActivity(intent);
                    }
                })
                .setNegativeButton(context.getText(R.string.close_button), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.setTitle(context.getString(R.string.about_title,
                MainApplication.getVersionName()));
        return alertDialog;
    }

    private TextView getAboutDialogContents(final Context context) {

        TextView messageBoxText = new TextView(context);
        messageBoxText.setTextSize(16);
        messageBoxText.setPadding(10,5,5,5);
        final SpannableString s1 = new SpannableString(context.getText(R.string.about_text));
        final SpannableString s2 = new SpannableString(context.getText(R.string.extra_license_text));
        Linkify.addLinks(s1, Linkify.WEB_URLS);
        Linkify.addLinks(s2, Linkify.WEB_URLS);
        messageBoxText.append(s1);
        messageBoxText.append(s2);
        messageBoxText.setMovementMethod(LinkMovementMethod.getInstance());

        return messageBoxText;

    }

    @Override
    public AlertDialog getWhatsNewAlertDialog(final Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(getWhatsNewDialogContents(context))
                .setPositiveButton(context.getText(R.string.close_button), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(Preference.PREF_WHATS_NEW_DIALOG_SHOWN_FOR_VERSION,
                                MainApplication.getVersionName());
                        editor.commit();
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.setTitle(context.getString(R.string.whats_new_title,
                MainApplication.getVersionName()));
        return  alertDialog;
    }

    private TextView getWhatsNewDialogContents(final Context context) {

        TextView messageBoxText = new TextView(context);
        messageBoxText.setTextSize(18);
        messageBoxText.setPadding(10,5,5,5);
        final SpannableString s = new SpannableString(" "+context.getText(R.string.whats_new_text));
        messageBoxText.setText(s);
        messageBoxText.setMovementMethod(LinkMovementMethod.getInstance());

        return messageBoxText;

    }

}
