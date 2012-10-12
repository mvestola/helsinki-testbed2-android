package fi.testbed2.dialog;

import android.app.AlertDialog;
import android.content.Context;

public interface DialogBuilder {

    public AlertDialog getAboutAlertDialog(Context context);
    public AlertDialog getWhatsNewAlertDialog(Context context);

}
