package fi.testbed2.android.app;

import fi.testbed2.BuildConfig;
import roboguice.util.Ln;

/**
 * Logger class used in testbed app.
 */
public final class Logger {

    public static void debug(String s) {
        if (BuildConfig.DEBUG_OUTPUT) {
            Ln.e(s);
        }
    }

}
