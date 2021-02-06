package fi.testbed2.service;

import java.util.SortedMap;

import fi.testbed2.domain.Municipality;

public interface MunicipalityService {

    Municipality getMunicipality(String name);

    SortedMap<String, Municipality> getFinlandMunicipalitiesShownInTestbedMap();

    String[] getFinlandMunicipalityNamesShownInTestbedMap();

}
