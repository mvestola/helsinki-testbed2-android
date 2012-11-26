package fi.testbed2.data;

import lombok.Data;
import lombok.NonNull;

@Data
public class MapLocationXY {

    @NonNull
    private double x;

    @NonNull
    private double y;

}
