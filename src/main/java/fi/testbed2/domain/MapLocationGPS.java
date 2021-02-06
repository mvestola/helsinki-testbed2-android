package fi.testbed2.domain;

import androidx.annotation.NonNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MapLocationGPS {

    private double latitude;
    private double longitude;

    @NonNull
    @Override
    public String toString() {
        return "Lat: " + latitude + ", Lon: " + longitude;
    }
}
