package diy.capmana;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * An animation class.
 */
public class Animation implements Parcelable {

    private static final int PLAYING_MAX = 16;

    private static class PLAYING {
        int start;
        int end;
        int time;
    }

    private PLAYING[] plays = new PLAYING[PLAYING_MAX];
    private int currentPlaying;
    private int currentImage;

    private float currentX, currentY;
    private float velocityX, velocityY;

    private long timeStart;

    /**
     * Constructs an animation.
     */
    public Animation() {
        for (int i = 0; i < PLAYING_MAX; i++)
            plays[i] = new PLAYING();
        currentPlaying = -1;
        currentImage = 0;
        currentX = 0;
        currentY = 0;
        velocityX = 0;
        velocityY = 0;
        timeStart = System.currentTimeMillis();
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
    public void draw(@NonNull float[] mvpMatrix, @NonNull Sprite sprite) {
        sprite.draw(mvpMatrix, currentImage);

        long usage = System.currentTimeMillis() - timeStart;
        if (usage > plays[currentPlaying].time) {
            currentImage++;
            if (currentImage >= plays[currentPlaying].end)
                currentImage = plays[currentPlaying].start;
            timeStart = System.currentTimeMillis();
        }
    }

    public float getCurrentX() {
        return currentX;
    }

    public float getCurrentY() {
        return currentY;
    }

    public float getVelocityX() {
        return velocityX;
    }

    public float getVelocityY() {
        return velocityY;
    }

    public void moveTo(float x, float y) {
        currentX = x;
        currentY = y;
    }

    public void moveBy(float dx, float dy) {
        currentX += dx;
        currentY += dy;
    }

    public void setVelocity(float dx, float dy) {
        velocityX = dx;
        velocityY = dy;
    }

    public void playFrame(boolean enableX, boolean enableY) {
        if (enableX)
            currentX += velocityX;
        if (enableY)
            currentY += velocityY;
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
        currentX = parcel.readInt();
        currentY = parcel.readInt();
        velocityX = parcel.readInt();
        velocityY = parcel.readInt();
        timeStart = System.currentTimeMillis();
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
        parcel.writeFloat(currentX);
        parcel.writeFloat(currentY);
        parcel.writeFloat(velocityX);
        parcel.writeFloat(velocityY);
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
