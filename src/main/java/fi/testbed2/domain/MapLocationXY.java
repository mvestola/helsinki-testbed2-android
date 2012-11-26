package fi.testbed2.domain;

import lombok.Data;
import lombok.NonNull;

@Data
public class MapLocationXY {

    @NonNull
    private double x;

    @NonNull
    private double y;

}
