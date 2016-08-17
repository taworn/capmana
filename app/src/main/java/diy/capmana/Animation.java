package diy.capmana;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * An animation class.
 */
public class Animation implements Parcelable {

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
    public Animation(@NonNull Sprite s) {
        sprite = s;
        for (int i = 0; i < PLAYING_MAX; i++)
            plays[i] = new PLAYING();
        currentPlaying = -1;
        currentImage = 0;
    }

    /**
     * Adds a playing animation, only 16 set allow.
     */
    public void add(int number, int start, int end, int time) {
        assert number >= 0 && number < PLAYING_MAX;
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
    public void draw(@NonNull float[] mvpMatrix) {
        sprite.draw(mvpMatrix, currentImage);

        long usage = System.currentTimeMillis() - timeStart;
        if (usage > plays[currentPlaying].time) {
            currentImage++;
            if (currentImage >= plays[currentPlaying].end)
                currentImage = plays[currentPlaying].start;
            timeStart = System.currentTimeMillis();
        }
    }

    /**
     * Constructs an animation with parcel.
     */
    protected Animation(Parcel parcel) {
        for (int i = 0; i < PLAYING_MAX; i++) {
            plays[i].start = parcel.readInt();
            plays[i].end = parcel.readInt();
            plays[i].time = parcel.readInt();
        }
        currentPlaying = parcel.readInt();
        currentImage = parcel.readInt();
        timeStart = System.currentTimeMillis();
    }

    /**
     * Sets sprite.
     */
    public void setSprite(@NonNull Sprite s) {
        sprite = s;
        use(currentPlaying);
    }

    /**
     * Writes data to parcel.
     */
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        for (int i = 0; i < PLAYING_MAX; i++) {
            parcel.writeInt(plays[i].start);
            parcel.writeInt(plays[i].end);
            parcel.writeInt(plays[i].time);
        }
        parcel.writeInt(currentPlaying);
        parcel.writeInt(currentImage);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Animation> CREATOR = new Creator<Animation>() {
        @Override
        public Animation createFromParcel(Parcel in) {
            return new Animation(in);
        }

        @Override
        public Animation[] newArray(int size) {
            return new Animation[size];
        }
    };

}
