package fi.testbed2.android.ui.dialog;

public enum DialogType {

    ABOUT, WHATS_NEW, ERROR;

    public static DialogType getById(int id) {
        DialogType dialogType = null;

        for (DialogType dialogTypeTmp : DialogType.values()) {
            if(id == dialogTypeTmp.ordinal()) {
                dialogType = dialogTypeTmp;
                break;
            }
        }

        return dialogType;
    }

}
