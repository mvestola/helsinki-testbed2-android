package fi.testbed2.app;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import fi.testbed2.dialog.DefaultDialogBuilder;
import fi.testbed2.dialog.DialogBuilder;
import fi.testbed2.service.*;
import fi.testbed2.service.impl.DefaultPreferenceService;
import fi.testbed2.service.impl.NetworkLocationService;
import fi.testbed2.service.impl.InlineMunicipalityService;
import fi.testbed2.service.impl.MercatorCoordinateService;

/**
 * Module used by the RoboGuice IoC framework.
 */
public class MainModule extends AbstractModule {

    public MainModule() {

    }

    @Override
    protected void configure() {
        bind(DialogBuilder.class).to(DefaultDialogBuilder.class);
        bind(MunicipalityService.class).to(InlineMunicipalityService.class);
        bind(LocationService.class).to(NetworkLocationService.class);
        bind(CoordinateService.class).to(MercatorCoordinateService.class);
        bind(PreferenceService.class).to(DefaultPreferenceService.class);
    }

}
