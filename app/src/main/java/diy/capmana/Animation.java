package diy.capmana;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * An animation class.
 */
public class Animation implements Parcelable {

    public static final int ON_END_CONTINUE = 0;
    public static final int ON_END_KEEP_LAST_FRAME = 1;
    public static final int ON_END_HIDDEN = 2;

    private static final int PLAYING_MAX = 16;

    private static class PLAYING {
        int start;
        int end;
        int onEnd;
        int time;
    }

    private PLAYING[] plays = new PLAYING[PLAYING_MAX];
    private int currentPlaying;
    private int currentImage;
    private boolean ending;

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
        ending = false;
        currentX = 0;
        currentY = 0;
        velocityX = 0;
        velocityY = 0;
        timeStart = System.currentTimeMillis();
    }

    /**
     * Adds a playing animation, only 16 set allow.
     */
    public void add(int number, int start, int end, int onEnd, int time) {
        assert number >= 0 && number < PLAYING_MAX;
        plays[number].start = start;
        plays[number].end = end;
        plays[number].onEnd = onEnd;
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
            ending = false;
            timeStart = System.currentTimeMillis();
        }
    }

    /**
     * Draws animation.
     */
    public void draw(@NonNull final float[] mvpMatrix, @NonNull Sprite sprite) {
        if (!ending) {
            sprite.draw(mvpMatrix, currentImage);

            long usage = System.currentTimeMillis() - timeStart;
            if (usage > plays[currentPlaying].time) {
                currentImage++;
                if (currentImage >= plays[currentPlaying].end) {
                    switch (plays[currentPlaying].onEnd) {
                        default:
                        case ON_END_CONTINUE:
                            currentImage = plays[currentPlaying].start;
                            break;
                        case ON_END_KEEP_LAST_FRAME:
                            currentImage--;
                            ending = true;
                            break;
                        case ON_END_HIDDEN:
                            ending = true;
                            break;
                    }
                }
                timeStart = System.currentTimeMillis();
            }
        }
        else {
            if (plays[currentPlaying].onEnd == ON_END_KEEP_LAST_FRAME)
                sprite.draw(mvpMatrix, currentImage);
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

    public boolean isEnded() {
        return ending;
    }

    /**
     * Constructs an animation with parcel.
     */
    protected Animation(Parcel parcel) {
        for (int i = 0; i < PLAYING_MAX; i++) {
            plays[i].start = parcel.readInt();
            plays[i].end = parcel.readInt();
            plays[i].onEnd = parcel.readInt();
            plays[i].time = parcel.readInt();
        }
        currentPlaying = parcel.readInt();
        currentImage = parcel.readInt();
        ending = parcel.readByte() != 0;
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
            parcel.writeInt(plays[i].onEnd);
            parcel.writeInt(plays[i].time);
        }
        parcel.writeInt(currentPlaying);
        parcel.writeInt(currentImage);
        parcel.writeByte((byte) (ending ? 1 : 0));
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
