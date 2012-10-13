package fi.testbed2.app;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import fi.testbed2.dialog.DefaultDialogBuilder;
import fi.testbed2.dialog.DialogBuilder;
import fi.testbed2.service.*;
import fi.testbed2.service.impl.DefaultUserLocationService;
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
        bind(MunicipalityService.class).to(InlineMunicipalityService.class).in(Singleton.class);
        bind(UserLocationService.class).to(DefaultUserLocationService.class).in(Singleton.class);
        bind(CoordinateService.class).to(MercatorCoordinateService.class).in(Singleton.class);
    }

}
