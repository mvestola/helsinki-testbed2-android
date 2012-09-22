package fi.testbed2.util;

import fi.testbed2.app.MainApplication;

import java.math.BigDecimal;

public class SeekBarUtil {

    public static int getFrameNumberFromSeekBarValue(int seekBarProgress) {

        double mapImagesCount = MainApplication.getTestbedParsedPage().getAllTestbedImages().size();
        int frameNumberToGo = 0;
        if (seekBarProgress!=0) {
            double frameNumberToGoDouble = (mapImagesCount-1)/(100.0/seekBarProgress);
            BigDecimal bd = new BigDecimal(frameNumberToGoDouble);
            BigDecimal rounded = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
            frameNumberToGo = rounded.intValue();
        }

        return frameNumberToGo;

    }

    public static int getSeekBarValueFromFrameNumber(int frameNumber) {

        int seekBarValue;
        double mapImagesCount = MainApplication.getTestbedParsedPage().getAllTestbedImages().size();
        double seekBarValueDouble = 100*(frameNumber/(mapImagesCount-1));
        BigDecimal bd = new BigDecimal(seekBarValueDouble);
        BigDecimal rounded = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        seekBarValue = rounded.intValue();
        return seekBarValue;

    }

}
