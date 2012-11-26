package fi.testbed2.data;

import lombok.Data;
import lombok.NonNull;

@Data
public class MapLocationGPS {

    @NonNull
    private double latitude;

    @NonNull
    private double longitude;

    @Override
    public String toString() {
        return "Lat: "+latitude+", Lon: "+longitude;
    }
}
