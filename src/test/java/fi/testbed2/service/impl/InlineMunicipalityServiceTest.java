package fi.testbed2.service.impl;

import fi.testbed2.AbstractTestCase;
import fi.testbed2.InjectedTestRunner;
import fi.testbed2.data.Municipality;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.SortedMap;

import static junit.framework.Assert.*;

@RunWith(InjectedTestRunner.class)
public class InlineMunicipalityServiceTest extends AbstractTestCase {

    private InlineMunicipalityService inlineMunicipalityService;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        inlineMunicipalityService = new InlineMunicipalityService();
        inlineMunicipalityService.coordinateService = mockCoordinateService;
    }

    @Test
    public void testGetFinlandMunicipalitiesShownInTestbedMap() throws Exception {

        SortedMap<String, Municipality> municipalities = inlineMunicipalityService.getFinlandMunicipalitiesShownInTestbedMap();

        // Manually calculated that 79 Finnish municipalities should be shown in the map
        assertEquals(79, municipalities.size());

        // Test only some of the major municipalities exist
        assertNotNull(inlineMunicipalityService.getMunicipality("Helsinki"));
        assertNotNull(inlineMunicipalityService.getMunicipality("Espoo"));
        assertNotNull(inlineMunicipalityService.getMunicipality("Vantaa"));
        assertNotNull(inlineMunicipalityService.getMunicipality("Kirkkonummi"));
        assertNotNull(inlineMunicipalityService.getMunicipality("Lohja"));
        assertNotNull(inlineMunicipalityService.getMunicipality("Humppila"));
        assertNotNull(inlineMunicipalityService.getMunicipality("Hanko"));
        assertNotNull(inlineMunicipalityService.getMunicipality("Kouvola"));
        assertNotNull(inlineMunicipalityService.getMunicipality("Hämeenlinna"));
        assertNotNull(inlineMunicipalityService.getMunicipality("Västanfjärd"));

        // Test only some of the major municipalities NOT exist
        assertNull(inlineMunicipalityService.getMunicipality("Turku"));
        assertNull(inlineMunicipalityService.getMunicipality("Tampere"));
        assertNull(inlineMunicipalityService.getMunicipality("Rovaniemi"));
        assertNull(inlineMunicipalityService.getMunicipality("Utsjoki"));

    }

    @Test
    public void testGetFinlandMunicipalityNamesShownInTestbedMapRightOrder() throws Exception {

        String[] municipalities = inlineMunicipalityService.getFinlandMunicipalityNamesShownInTestbedMap();

        assertEquals(79, municipalities.length);
        assertEquals("Alastaro", municipalities[0]);
        assertEquals("Kouvola", municipalities[30]);
        assertEquals("Ypäjä", municipalities[78]);

    }

}
