package fi.testbed2.domain;

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
