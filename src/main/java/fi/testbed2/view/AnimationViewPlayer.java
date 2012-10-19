package fi.testbed2.view;

import fi.testbed2.app.Logging;

/**
 * Player which controls the state of the animation in the
 * Animation view class.
 */
public class AnimationViewPlayer {

    private AnimationView view;

    private boolean play;
    private int currentFrame;
    private int frames;
    private int frameDelay;

    public AnimationViewPlayer(AnimationView view) {
        this.view = view;
    }

    public void playOrPause() {
        Logging.debug("AnimationViewPlayer playOrPause");
        if(play) {
            play = false;
        } else {
            play = true;
            view.next();
        }
        view.invalidate();
    }

    public void play() {
        Logging.debug("AnimationViewPlayer play");
        play = true;
        view.invalidate();
    }

    public void pause() {
        Logging.debug("AnimationViewPlayer pause");
        play = false;
        view.invalidate();
    }

    public void previous() {
        Logging.debug("AnimationViewPlayer previous");
        this.play = false;
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
        this.play = false;
        currentFrame=frameNumber;

        if(currentFrame > frames)
            currentFrame = 0;

        view.invalidate();
    }

    /**
     * Getters / setters
     */

    public int getCurrentFrame() {
        return currentFrame;
    }

    public int getFrames() {
        return frames;
    }

    public void setCurrentFrame(int frame) {
        this.currentFrame = frame;
    }

    public void setFrames(int frames) {
        this.frames = frames;
    }

    public boolean isPlaying() {
        return play;
    }

    public int getFrameDelay() {
        return frameDelay;
    }

    public void setFrameDelay(int frameDelay) {
        this.frameDelay = frameDelay;
    }
}
