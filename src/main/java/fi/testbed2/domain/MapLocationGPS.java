package fi.testbed2.domain;

import lombok.Data;

@Data
public class MapLocationGPS {

    private double latitude;
    private double longitude;

    @Override
    public String toString() {
        return "Lat: "+latitude+", Lon: "+longitude;
    }
}
