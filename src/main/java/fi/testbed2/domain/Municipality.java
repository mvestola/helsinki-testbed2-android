package fi.testbed2.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Represents a municipality ("Kunta" in Finnish)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Municipality {

    @NonNull
    private String name;

    @NonNull
    private MapLocationGPS gpsPos;

    @NonNull
    private MapLocationXY xyPos;

}
