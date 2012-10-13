package fi.testbed2.service.impl;

import com.google.inject.Inject;
import com.jhlabs.map.Point2D;
import fi.testbed2.service.MunicipalityService;
import fi.testbed2.service.UserLocationService;

public class DefaultUserLocationService implements UserLocationService {

    @Inject
    MunicipalityService municipalityService;

    private boolean debug = true;

    private Point2D.Double userLocationInMapPx;

    public DefaultUserLocationService() {


    }

    @Override
    public Point2D.Double getUserLocationInMapPixels() {

        if (debug) {
            return municipalityService.getMunicipality("Helsinki").getXyPos();
        } else {
            return userLocationInMapPx;
        }

    }

    @Override
    public void setUserLocationInMapPixels(Point2D.Double pos) {
        userLocationInMapPx = pos;
    }


}
