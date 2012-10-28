package fi.testbed2.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.widget.TextView;
import com.google.inject.Inject;
import fi.testbed2.R;
import fi.testbed2.app.Logging;
import fi.testbed2.app.MainApplication;
import fi.testbed2.service.PreferenceService;

public class DefaultDialogBuilder implements DialogBuilder {

    @Inject
    protected Context context;

    @Inject
    PreferenceService preferenceService;

    public DefaultDialogBuilder() {
        Logging.debug("DefaultDialogBuilder instantiated");
    }

    @Override
    public AlertDialog getAboutAlertDialog() {

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

    @Override
    public AlertDialog getWhatsNewAlertDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(getWhatsNewDialogContents(context))
                .setPositiveButton(context.getText(R.string.close_button), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        preferenceService.saveWhatsNewDialogShownForCurrentVersion();
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.setTitle(context.getString(R.string.whats_new_title,
                MainApplication.getVersionName()));
        return  alertDialog;
    }

    @Override
    public AlertDialog getErrorDialog(String errorMessage) {
        AlertDialog ad = new AlertDialog.Builder(context).create();
        ad.setCancelable(false); // This blocks the 'BACK' button
        ad.setMessage(errorMessage);
        ad.setButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return ad;
    }

    private TextView getAboutDialogContents(final Context context) {

        TextView messageBoxText = new TextView(context);
        messageBoxText.setTextSize(16);
        messageBoxText.setTextColor(0xffffffff);
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

    private TextView getWhatsNewDialogContents(final Context context) {

        TextView messageBoxText = new TextView(context);
        messageBoxText.setTextSize(18);
        messageBoxText.setTextColor(0xffffffff);
        messageBoxText.setPadding(10,5,5,5);
        final SpannableString s = new SpannableString(" "+context.getText(R.string.whats_new_text));
        messageBoxText.setText(s);
        messageBoxText.setMovementMethod(LinkMovementMethod.getInstance());

        return messageBoxText;

    }

}
