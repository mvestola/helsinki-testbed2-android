package fi.testbed2.data;

import fi.testbed2.AbstractTestCase;
import org.junit.Test;

import java.util.SortedMap;

import static junit.framework.Assert.*;

public class MunicipalityTest extends AbstractTestCase {

    @Test
    public void testGetFinlandMunicipalitiesShownInTestbedMap() throws Exception {

        SortedMap<String, Municipality> municipalities = Municipality.getFinlandMunicipalitiesShownInTestbedMap();

        // Manually calculated that 79 Finnish municipalities should be shown in the map
        assertEquals(79, municipalities.size());

        // Test only some of the major municipalities exist
        assertNotNull(Municipality.getMunicipality("Helsinki"));
        assertNotNull(Municipality.getMunicipality("Espoo"));
        assertNotNull(Municipality.getMunicipality("Vantaa"));
        assertNotNull(Municipality.getMunicipality("Kirkkonummi"));
        assertNotNull(Municipality.getMunicipality("Lohja"));
        assertNotNull(Municipality.getMunicipality("Humppila"));
        assertNotNull(Municipality.getMunicipality("Hanko"));
        assertNotNull(Municipality.getMunicipality("Kouvola"));
        assertNotNull(Municipality.getMunicipality("Hämeenlinna"));
        assertNotNull(Municipality.getMunicipality("Västanfjärd"));

        // Test only some of the major municipalities NOT exist
        assertNull(Municipality.getMunicipality("Turku"));
        assertNull(Municipality.getMunicipality("Tampere"));
        assertNull(Municipality.getMunicipality("Rovaniemi"));
        assertNull(Municipality.getMunicipality("Utsjoki"));

    }

    @Test
    public void testGetFinlandMunicipalityNamesShownInTestbedMapRightOrder() throws Exception {

        String[] municipalities = Municipality.getFinlandMunicipalityNamesShownInTestbedMap();

        assertEquals(79, municipalities.length);
        assertEquals("Alastaro", municipalities[0]);
        assertEquals("Kouvola", municipalities[30]);
        assertEquals("Ypäjä", municipalities[78]);

    }


}
