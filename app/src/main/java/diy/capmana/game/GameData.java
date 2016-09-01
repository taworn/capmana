package diy.capmana.game;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * A GameData class.
 */
public class GameData implements Parcelable {

    public static GameData instance() {
        return singleton;
    }

    private static GameData singleton = null;

    private int score = 0;
    private boolean reverseMode = false;
    private long reverseTime = 0;
    private int divoLife = 1;
    private List<Divo> divoList = new ArrayList<>();

    /**
     * Constructs the game data.
     */
    public GameData() {
        assert singleton == null;
        singleton = this;
        divoList.clear();
    }

    /**
     * Clears data.
     */
    public void clear() {
        reverseMode = false;
        divoLife = 5;
        divoList.clear();
    }

    /**
     * Gets current score.
     */
    public int getScore() {
        return score;
    }

    /**
     * Decrease divo life by one.
     */
    public void divoLifeDecrease() {
        divoLife--;
    }

    /**
     * Checks divo can respawn.
     */
    public boolean divoCanRelife() {
        return divoLife > 0;
    }

    /**
     * Adds divo to list.
     */
    public void addDivo(Divo divo) {
        divoList.add(divo);
    }

    /**
     * Gets divo in list.
     */
    public Divo getDivo(int id) {
        return divoList.get(id);
    }

    /**
     * Gets number of divoes in list.
     */
    public int getDivoCount() {
        return divoList.size();
    }

    /**
     * Clears divo list.  This function should use in Scene.onRestoreInstanceState() only.
     */
    public void clearDivoList() {
        divoList.clear();
    }

    /**
     * Checks that all divoes dead.
     */
    public boolean checkAllDivoDead() {
        int i = 0;
        while (i < divoList.size()) {
            if (!divoList.get(i).isDead())
                return false;
            else
                i++;
        }
        return true;
    }

    /**
     * Retrieves bonus after get item.
     */
    public void getBonus(int item) {
        if (item == 0x01) {
            score += 10;
        }
        else if (item == 0x02) {
            score += 100;
            reverseMode = true;
            reverseTime = 1000 * 3;
        }
    }

    /**
     * Checks current time is reverse mode.
     */
    public boolean isReverseMode() {
        return reverseMode;
    }

    /**
     * Updates current time frame.
     */
    public void update(long timeUsed) {
        if (reverseMode) {
            reverseTime -= timeUsed;
            if (reverseTime <= 0)
                reverseMode = false;
        }
    }

    /**
     * Constructs the game data with parcel.
     */
    protected GameData(Parcel parcel) {
        singleton = this;
        score = parcel.readInt();
        reverseMode = parcel.readByte() != 0;
        reverseTime = parcel.readLong();
        divoLife = parcel.readInt();
        divoList.clear();
    }

    /**
     * Writes data to parcel.
     */
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(score);
        parcel.writeByte((byte) (reverseMode ? 1 : 0));
        parcel.writeLong(reverseTime);
        parcel.writeInt(divoLife);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<GameData> CREATOR = new Parcelable.Creator<GameData>() {
        @Override
        public GameData createFromParcel(Parcel in) {
            return new GameData(in);
        }

        @Override
        public GameData[] newArray(int size) {
            return new GameData[size];
        }
    };

}
