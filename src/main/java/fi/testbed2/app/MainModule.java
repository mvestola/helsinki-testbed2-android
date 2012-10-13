package fi.testbed2.app;

import com.google.inject.AbstractModule;
import fi.testbed2.dialog.DefaultDialogBuilder;
import fi.testbed2.dialog.DialogBuilder;

/**
 * Module used by the RoboGuice IoC framework.
 */
public class MainModule extends AbstractModule {

    public MainModule() {

    }

    @Override
    protected void configure() {
        bind(DialogBuilder.class).to(DefaultDialogBuilder.class);
    }

}
