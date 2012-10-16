package fi.testbed2;

import fi.testbed2.app.MainModule;
import fi.testbed2.dialog.DefaultDialogBuilder;
import fi.testbed2.dialog.DialogBuilder;
import fi.testbed2.service.*;
import fi.testbed2.service.impl.*;

import static org.mockito.Mockito.mock;

public class TestModule extends MainModule {

    public HTTPService mockHttpService = mock(HTTPService.class);

    @Override
    protected void configure() {

        // Same as in MainModule
        bind(DialogBuilder.class).to(DefaultDialogBuilder.class);
        bind(MunicipalityService.class).to(InlineMunicipalityService.class);
        bind(LocationService.class).to(NetworkLocationService.class);
        bind(CoordinateService.class).to(MercatorCoordinateService.class);
        bind(PreferenceService.class).to(DefaultPreferenceService.class);
        bind(BitmapService.class).to(LruCacheBitmapService.class);
        bind(PageService.class).to(LruCachePageService.class);

        // Mocked services
        bind(HTTPService.class).toInstance(mockHttpService);
    }
}
