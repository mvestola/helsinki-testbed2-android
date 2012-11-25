package fi.testbed2;

import fi.testbed2.app.MainModule;
import fi.testbed2.dialog.DefaultDialogBuilder;
import fi.testbed2.dialog.DialogBuilder;
import fi.testbed2.service.*;
import fi.testbed2.service.impl.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.mock;

public class TestModule extends MainModule {

    @Mock
    public MunicipalityService mockMunicipalityService;

    @Mock
    public LocationService mockLocationService;

    @Mock
    public CoordinateService mockCoordinateService;

    @Mock
    public PreferenceService mockPreferenceService;

    @Mock
    public BitmapService mockBitmapService;

    @Mock
    public PageService mockPageService;

    @Mock
    public HTTPService mockHttpService;

    @Override
    protected void configure() {

        MockitoAnnotations.initMocks(this);

        // Same as in MainModule
        bind(DialogBuilder.class).to(DefaultDialogBuilder.class);

        // Mocked
        bind(MunicipalityService.class).toInstance(mockMunicipalityService);
        bind(LocationService.class).toInstance(mockLocationService);
        bind(CoordinateService.class).toInstance(mockCoordinateService);
        bind(PreferenceService.class).toInstance(mockPreferenceService);
        bind(BitmapService.class).toInstance(mockBitmapService);
        bind(PageService.class).toInstance(mockPageService);
        bind(HTTPService.class).toInstance(mockHttpService);
    }
}
