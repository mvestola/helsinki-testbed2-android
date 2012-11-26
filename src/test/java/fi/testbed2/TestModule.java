package fi.testbed2;

import fi.testbed2.android.ui.dialog.AlertDialogBuilder;
import fi.testbed2.android.ui.dialog.DialogBuilder;
import fi.testbed2.service.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TestModule extends MainModule {

    @Mock
    public MunicipalityService mockMunicipalityService;

    @Mock
    public LocationService mockLocationService;

    @Mock
    public CoordinateService mockCoordinateService;

    @Mock
    public SettingsService mockSettingsService;

    @Mock
    public BitmapService mockBitmapService;

    @Mock
    public PageService mockPageService;

    @Mock
    public HttpUrlService mockHttpUrlService;

    @Override
    protected void configure() {

        MockitoAnnotations.initMocks(this);

        // Same as in MainModule
        bind(DialogBuilder.class).to(AlertDialogBuilder.class);

        // Mocked
        bind(MunicipalityService.class).toInstance(mockMunicipalityService);
        bind(LocationService.class).toInstance(mockLocationService);
        bind(CoordinateService.class).toInstance(mockCoordinateService);
        bind(SettingsService.class).toInstance(mockSettingsService);
        bind(BitmapService.class).toInstance(mockBitmapService);
        bind(PageService.class).toInstance(mockPageService);
        bind(HttpUrlService.class).toInstance(mockHttpUrlService);
    }
}
