package fi.testbed2.service;

import android.location.Location;
import com.jhlabs.map.Point2D;

/**
 * Service which is used to convert GPS coordinates to
 * x,y positions in the testbed map image.
 */
public interface CoordinateService {

    public static final String STATIC_PROVIDER_NAME = "dummy_provider";

    public Point2D.Double getKnownPositionForTesting();

    public Point2D.Double convertLocationToXyPos(Location location);

}
