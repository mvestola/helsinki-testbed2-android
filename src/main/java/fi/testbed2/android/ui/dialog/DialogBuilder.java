package fi.testbed2.android.ui.dialog;

import android.app.Dialog;

public interface DialogBuilder {

    public Dialog getAboutDialog();
    public Dialog getWhatsNewDialog();
    public Dialog getLocationPermissionDialog(final String permission, final AlertDialogBuilder.LocationPermissionDialogCloseHandler resultHandler);
    public Dialog getHardwareAccelerationDialog();
    public Dialog getErrorDialog(String errorMessage);

}
