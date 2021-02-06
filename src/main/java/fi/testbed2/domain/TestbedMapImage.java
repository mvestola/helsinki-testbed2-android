package fi.testbed2.domain;

import fi.testbed2.util.TimeUtil;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

/**
 * Represents the image object read from the testbed website.
 */
@EqualsAndHashCode(exclude={"localTimestamp"})
public class TestbedMapImage {

    @Getter @NonNull
    private final String imageURL;

    @Getter @NonNull
    private final String timestamp;

    @Getter(lazy=true)
    private final String localTimestamp = convertTimestampToLocalTimestamp();

    public TestbedMapImage(String imageURL, String timestamp) {
        this.imageURL = imageURL;
        this.timestamp = timestamp;
    }

    private String convertTimestampToLocalTimestamp() {
        return TimeUtil.getLocalTimestampFromGMTTimestamp(this.timestamp);
    }

}
