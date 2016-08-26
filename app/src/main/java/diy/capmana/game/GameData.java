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

    private List<Divo> divoList = new ArrayList<>();

    /**
     * Constructs the gamedata.
     */
    public GameData() {
        assert singleton == null;
        singleton = this;
    }

    /**
     * Clears data.
     */
    public void clear() {
        divoList.clear();
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
     * Constructs the game data with parcel.
     */
    protected GameData(Parcel parcel) {
    }

    /**
     * Writes data to parcel.
     */
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
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
