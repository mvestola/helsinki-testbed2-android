package fi.testbed2.data;

import android.location.Location;
import com.jhlabs.map.Point2D;
import fi.testbed2.service.LocationService;
import lombok.Data;
import lombok.NonNull;

/**
 * Represents a municipality ("Kunta" in Finnish)
 */
@Data
public class Municipality {

    @NonNull
    private String name;

    @NonNull
    private MapLocationGPS gpsPos;

    @NonNull
    private MapLocationXY xyPos;

}
