package fi.testbed2.service;

import android.location.Location;
import com.jhlabs.map.Point2D;
import fi.testbed2.data.MapLocationGPS;
import fi.testbed2.data.MapLocationXY;

/**
 * Service which is used to convert GPS coordinates to
 * x,y positions in the testbed map image.
 */
public interface CoordinateService {

    public MapLocationXY getKnownPositionForTesting();
    public MapLocationXY convertLocationToXyPos(MapLocationGPS location);

}
