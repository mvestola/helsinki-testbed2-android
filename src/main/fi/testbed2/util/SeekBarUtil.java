package fi.testbed2.util;

import fi.testbed2.app.MainApplication;

import java.math.BigDecimal;

public class SeekBarUtil {

    /**
     * Returns the frame index from the given seek bar progress value. In other words,
     * from the total number of frames, returns the frame number which should be displayed when
     * giving a progress value between 0-100. Value 100 means the last frame, value 0 the first frame.
     * The seek bar progress value must be a value between 0-100.
     *
     * @param seekBarProgress Progress between 0...100
     * @param totalFrames Total number of frames, must be greater than 0
     * @return Frame number, starting from zero. Rounds up to the closes frame. Returns -1 if given
     * parameters are invalid.
     */
    public static int getFrameIndexFromSeekBarValue(int seekBarProgress, int totalFrames) {

        if (totalFrames==0 || seekBarProgress<0 || seekBarProgress>100) {
            return -1;
        }

        int frameNumberToGo = 0;
        if (seekBarProgress!=0) {
            double frameNumberToGoDouble = (totalFrames-1)/(100.0/seekBarProgress);
            BigDecimal bd = new BigDecimal(frameNumberToGoDouble);
            BigDecimal rounded = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
            frameNumberToGo = rounded.intValue();
        }

        return frameNumberToGo;

    }

    /**
     * Returns the seek bar value from the given frame number. In other words,
     * returns a progress value between 0...100 for the given frame index.
     * For example, if there are total of 10 frames and the current frame index is
     * 4 (5th frame), return 50.
     *
     * @param currentFrameIndex
     * @param totalFrames
     * @return
     */
    public static int getSeekBarValueFromFrameNumber(int currentFrameIndex, int totalFrames) {

        if (totalFrames==0 || currentFrameIndex>=totalFrames) {
            return -1;
        }

        if (totalFrames==1) {
            return 0;
        }

        int seekBarValue;
        double seekBarValueDouble = 100.0*(1.0*currentFrameIndex/(1.0*totalFrames-1));
        BigDecimal bd = new BigDecimal(seekBarValueDouble);
        BigDecimal rounded = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        seekBarValue = rounded.intValue();
        return seekBarValue;

    }

}
