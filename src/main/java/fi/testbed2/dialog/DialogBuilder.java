package fi.testbed2.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public interface DialogBuilder {

    public AlertDialog getAboutAlertDialog();
    public AlertDialog getWhatsNewAlertDialog();
    public AlertDialog getErrorDialog(String errorMessage);

}
