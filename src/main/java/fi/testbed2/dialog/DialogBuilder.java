package fi.testbed2.dialog;

import android.app.AlertDialog;

public interface DialogBuilder {

    public AlertDialog getAboutAlertDialog();
    public AlertDialog getWhatsNewAlertDialog();
    public AlertDialog getErrorDialog(String errorMessage);

}
