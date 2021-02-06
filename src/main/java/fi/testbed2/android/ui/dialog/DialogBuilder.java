package fi.testbed2.android.ui.dialog;

import android.app.Dialog;

public interface DialogBuilder {

    Dialog getAboutDialog();

    Dialog getWhatsNewDialog();

    Dialog getLocationPermissionDialog(final String permission, final AlertDialogBuilder.LocationPermissionDialogCloseHandler resultHandler);

    Dialog getHardwareAccelerationDialog();

    Dialog getErrorDialog(String errorMessage);

}
