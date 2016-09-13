package fi.testbed2.android.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
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
import fi.testbed2.android.app.Logger;
import fi.testbed2.android.app.MainApplication;
import fi.testbed2.service.SettingsService;

public class AlertDialogBuilder implements DialogBuilder {

    @Inject
    protected Context context;

    @Inject
    SettingsService settingsService;

    public AlertDialogBuilder() {
        Logger.debug("AlertDialogBuilder instantiated");
    }

    @Override
    public Dialog getAboutDialog() {

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
    public Dialog getWhatsNewDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(getWhatsNewDialogContents(context))
                .setPositiveButton(context.getText(R.string.close_button), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        settingsService.saveWhatsNewDialogShownForCurrentVersion();
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.setTitle(context.getString(R.string.whats_new_title,
                MainApplication.getVersionName()));
        return  alertDialog;
    }

    @Override
    public Dialog getHardwareAccelerationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(getHardwareAccelerationDialogContents(context))
                .setNegativeButton(context.getText(R.string.do_not_show_again), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        settingsService.saveHardwareAccelerationDialogShown();
                        dialog.cancel();
                    }
                })
                .setPositiveButton(context.getText(R.string.close_button), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.setTitle(context.getString(R.string.hardware_accel_dialog_title));
        return  alertDialog;
    }

    @Override
    public Dialog getErrorDialog(String errorMessage) {
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

    private TextView getHardwareAccelerationDialogContents(final Context context) {

        TextView messageBoxText = new TextView(context);
        messageBoxText.setTextSize(18);
        messageBoxText.setPadding(10,5,5,5);
        final SpannableString s = new SpannableString(" "+context.getText(R.string.hardware_accel_dialog_text));
        messageBoxText.setText(s);
        messageBoxText.setMovementMethod(LinkMovementMethod.getInstance());

        return messageBoxText;

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
