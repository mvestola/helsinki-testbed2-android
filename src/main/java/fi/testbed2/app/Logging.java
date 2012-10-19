package fi.testbed2.app;

import fi.testbed2.Environment;
import roboguice.util.Ln;

/**
 * Logger class used in testbed app.
 */
public final class Logging {

    public static void debug(String s) {
        if (Environment.DEBUG) {
            Ln.d(s);
        }
    }

}
