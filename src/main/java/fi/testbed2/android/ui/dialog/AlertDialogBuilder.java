package fi.testbed2.android.ui.dialog;

import android.support.v7.app.AlertDialog;
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

        AlertDialog.Builder builder = getAlertDialogBuilder();
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

        AlertDialog.Builder builder = getAlertDialogBuilder();
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
        return alertDialog;
    }

    @Override
    public Dialog getLocationPermissionDialog(final String permission, final LocationPermissionDialogCloseHandler locationPermissionDialogCloseHandler) {

        AlertDialog.Builder builder = getAlertDialogBuilder();
        builder.setView(getLocationPermissionDialogContents(context))
                .setPositiveButton(context.getText(R.string.close_button), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        locationPermissionDialogCloseHandler.onCloseLocationPermissionDialog(permission);
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.setTitle(context.getString(R.string.location_permission_title));
        return alertDialog;
    }

    @Override
    public Dialog getHardwareAccelerationDialog() {

        AlertDialog.Builder builder = getAlertDialogBuilder();
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
        return alertDialog;
    }

    @Override
    public Dialog getErrorDialog(String errorMessage) {
        AlertDialog.Builder builder = getAlertDialogBuilder();
        builder.setMessage(errorMessage)
            .setPositiveButton(context.getText(R.string.close_button), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false); // This blocks the 'BACK' button
        return alertDialog;
    }

    private TextView getAboutDialogContents(final Context context) {

        TextView messageBoxText = new TextView(context);
        int padding = getDefaultPaddingInPx();
        messageBoxText.setPadding(padding, padding, padding, padding);
        final SpannableString s1 = new SpannableString(context.getText(R.string.about_text));
        final SpannableString s2 = new SpannableString(context.getText(R.string.extra_license_text));
        Linkify.addLinks(s1, Linkify.WEB_URLS);
        messageBoxText.append(s1);
        messageBoxText.append(s2);
        messageBoxText.setMovementMethod(LinkMovementMethod.getInstance());
        messageBoxText.setAutoLinkMask(1);

        return messageBoxText;

    }

    private TextView getHardwareAccelerationDialogContents(final Context context) {

        TextView messageBoxText = new TextView(context);
        int padding = getDefaultPaddingInPx();
        messageBoxText.setPadding(padding, padding, padding, padding);
        final SpannableString s = new SpannableString(context.getText(R.string.hardware_accel_dialog_text));
        messageBoxText.setText(s);
        messageBoxText.setMovementMethod(LinkMovementMethod.getInstance());

        return messageBoxText;

    }

    private TextView getLocationPermissionDialogContents(final Context context) {

        TextView messageBoxText = new TextView(context);
        int padding = getDefaultPaddingInPx();
        messageBoxText.setPadding(padding, padding, padding, padding);
        final SpannableString s = new SpannableString(context.getText(R.string.location_permission_text));
        messageBoxText.setText(s);
        messageBoxText.setMovementMethod(LinkMovementMethod.getInstance());

        return messageBoxText;

    }

    private TextView getWhatsNewDialogContents(final Context context) {

        TextView messageBoxText = new TextView(context);
        int padding = getDefaultPaddingInPx();
        messageBoxText.setPadding(padding, padding, padding, padding);
        final SpannableString s = new SpannableString(context.getText(R.string.whats_new_text));
        messageBoxText.setText(s);
        messageBoxText.setMovementMethod(LinkMovementMethod.getInstance());

        return messageBoxText;

    }

    private AlertDialog.Builder getAlertDialogBuilder() {
        return new AlertDialog.Builder(context, R.style.AlertDialogStyle);
    }

    private int getDefaultPaddingInPx() {
        return dpToPixels(25);
    }

    private int dpToPixels(int dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public interface LocationPermissionDialogCloseHandler {
        void onCloseLocationPermissionDialog(String permission);
    }

}
