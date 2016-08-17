package diy.capmana;

/**
 * An animation class.
 */
public class Animation {

    private Sprite sprite;

    private static final int PLAYING_MAX = 16;

    private static class PLAYING {
        int start;
        int end;
        int time;
    }

    private PLAYING[] plays = new PLAYING[PLAYING_MAX];
    private int currentPlaying;
    private int currentImage;
    private long timeStart;

    /**
     * Constructs an animation.
     */
    public Animation(Sprite s) {
        sprite = s;
        assert sprite != null;
        currentPlaying = -1;
        currentImage = 0;
    }

    /**
     * Adds a playing animation, only 16 set allow.
     */
    public void add(int number, int start, int end, int time) {
        assert number >= 0 && number < PLAYING_MAX;
        if (plays[number] == null)
            plays[number] = new PLAYING();
        plays[number].start = start;
        plays[number].end = end;
        plays[number].time = time;
    }

    /**
     * Uses a playing animation.
     */
    public void use(int number) {
        assert number >= 0 && number < PLAYING_MAX;
        if (number != currentPlaying) {
            currentPlaying = number;
            currentImage = plays[number].start;
            timeStart = System.currentTimeMillis();
        }
    }

    /**
     * Draws animation.
     */
    public void draw(float[] mvpMatrix) {
        sprite.draw(mvpMatrix, currentImage);

        long usage = System.currentTimeMillis() - timeStart;
        if (usage > plays[currentPlaying].time) {
            currentImage++;
            if (currentImage >= plays[currentPlaying].end)
                currentImage = plays[currentPlaying].start;
            timeStart = System.currentTimeMillis();
        }
    }

}
