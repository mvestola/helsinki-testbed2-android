package fi.testbed2.service;

import com.google.inject.Inject;
import com.jhlabs.map.Point2D;

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
            return municipalityService.getMunicipality("Helsinki").getPositionInMapPx();
        } else {
            return userLocationInMapPx;
        }

    }

    @Override
    public void setUserLocationInMapPixels(Point2D.Double pos) {
        userLocationInMapPx = pos;
    }


}
