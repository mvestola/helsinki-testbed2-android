package fi.testbed2.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MapLocationGPS {

    private double latitude;
    private double longitude;

    @Override
    public String toString() {
        return "Lat: "+latitude+", Lon: "+longitude;
    }
}
