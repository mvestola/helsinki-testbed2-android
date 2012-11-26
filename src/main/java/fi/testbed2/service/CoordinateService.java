package fi.testbed2.service;

import fi.testbed2.domain.MapLocationGPS;
import fi.testbed2.domain.MapLocationXY;

/**
 * Service which is used to convert GPS coordinates to
 * x,y positions in the testbed map image.
 */
public interface CoordinateService {

    public MapLocationXY getKnownPositionForTesting();
    public MapLocationXY convertLocationToXyPos(MapLocationGPS location);

}
