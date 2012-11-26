package fi.testbed2.service;

import fi.testbed2.domain.Municipality;

import java.util.SortedMap;

public interface MunicipalityService {

    Municipality getMunicipality(String name);
    SortedMap<String, Municipality> getFinlandMunicipalitiesShownInTestbedMap();
    String[] getFinlandMunicipalityNamesShownInTestbedMap();

}
