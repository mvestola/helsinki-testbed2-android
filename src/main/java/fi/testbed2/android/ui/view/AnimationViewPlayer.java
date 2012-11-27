package fi.testbed2.android.ui.view;

import fi.testbed2.android.app.Logging;
import lombok.Getter;
import lombok.Setter;

/**
 * Player which controls the state of the animation in the
 * Animation view class.
 */
public class AnimationViewPlayer {

    private AnimationView view;

    @Getter
    private boolean isPlaying;

    @Getter @Setter
    private int currentFrame;

    @Getter @Setter
    private int frames;

    @Getter @Setter
    private int frameDelay;

    public AnimationViewPlayer(AnimationView view) {
        this.view = view;
    }

    public void playOrPause() {
        Logging.debug("AnimationViewPlayer playOrPause");
        if(isPlaying) {
            isPlaying = false;
        } else {
            isPlaying = true;
            view.next();
        }
        view.invalidate();
    }

    public void play() {
        Logging.debug("AnimationViewPlayer isPlaying");
        isPlaying = true;
        view.invalidate();
    }

    public void pause() {
        Logging.debug("AnimationViewPlayer pause");
        isPlaying = false;
        view.invalidate();
    }

    public void previous() {
        Logging.debug("AnimationViewPlayer previous");
        this.isPlaying = false;
        currentFrame--;
        if(currentFrame < 0)
            currentFrame = Math.abs((frames - 1) - currentFrame);
        view.invalidate();
    }

    public void goToNextFrame() {

        currentFrame++;

        if(currentFrame > frames)
            currentFrame = 0;

        view.invalidate();
        view.next();

    }

    public void goToFrame(int frameNumber) {
        this.isPlaying = false;
        currentFrame=frameNumber;

        if(currentFrame > frames)
            currentFrame = 0;

        view.invalidate();
    }

}
