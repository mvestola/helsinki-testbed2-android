package fi.testbed2.service;

import fi.testbed2.AbstractRoboGuiceTestCase;
import fi.testbed2.AbstractTestCase;
import fi.testbed2.data.Municipality;
import org.junit.Test;
import org.junit.runner.RunWith;
import roboguice.test.RobolectricRoboTestRunner;

import java.util.SortedMap;

import static junit.framework.Assert.*;

public class InlineMunicipalityServiceTest extends AbstractRoboGuiceTestCase {

    private static MunicipalityService municipalityService =
            new InlineMunicipalityService();

    @Test
    public void testGetFinlandMunicipalitiesShownInTestbedMap() throws Exception {

        SortedMap<String, Municipality> municipalities = municipalityService.getFinlandMunicipalitiesShownInTestbedMap();

        // Manually calculated that 79 Finnish municipalities should be shown in the map
        assertEquals(79, municipalities.size());

        // Test only some of the major municipalities exist
        assertNotNull(municipalityService.getMunicipality("Helsinki"));
        assertNotNull(municipalityService.getMunicipality("Espoo"));
        assertNotNull(municipalityService.getMunicipality("Vantaa"));
        assertNotNull(municipalityService.getMunicipality("Kirkkonummi"));
        assertNotNull(municipalityService.getMunicipality("Lohja"));
        assertNotNull(municipalityService.getMunicipality("Humppila"));
        assertNotNull(municipalityService.getMunicipality("Hanko"));
        assertNotNull(municipalityService.getMunicipality("Kouvola"));
        assertNotNull(municipalityService.getMunicipality("Hämeenlinna"));
        assertNotNull(municipalityService.getMunicipality("Västanfjärd"));

        // Test only some of the major municipalities NOT exist
        assertNull(municipalityService.getMunicipality("Turku"));
        assertNull(municipalityService.getMunicipality("Tampere"));
        assertNull(municipalityService.getMunicipality("Rovaniemi"));
        assertNull(municipalityService.getMunicipality("Utsjoki"));

    }

    @Test
    public void testGetFinlandMunicipalityNamesShownInTestbedMapRightOrder() throws Exception {

        String[] municipalities = municipalityService.getFinlandMunicipalityNamesShownInTestbedMap();

        assertEquals(79, municipalities.length);
        assertEquals("Alastaro", municipalities[0]);
        assertEquals("Kouvola", municipalities[30]);
        assertEquals("Ypäjä", municipalities[78]);

    }



}
