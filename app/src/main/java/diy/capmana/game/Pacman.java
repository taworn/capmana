package diy.capmana.game;

import android.graphics.PointF;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * A pacman class.
 */
public class Pacman extends Movable implements Parcelable {

    /**
     * Constructs the pacman.
     */
    public Pacman() {
        timePerDistance = 200;
        animation.add(0, 0, 2, TIME_PER_ANI_FRAME);
        animation.add(1, 2, 4, TIME_PER_ANI_FRAME);
        animation.add(2, 4, 6, TIME_PER_ANI_FRAME);
        animation.add(3, 6, 8, TIME_PER_ANI_FRAME);
        animation.use(0);
    }

    /**
     * Sets map.
     */
    public void setMap(Map map) {
        assert (map != null);
        this.map = map;

        PointF pf = new PointF(0, 0);
        map.getPacmanStartPosition(point, pf);
        animation.moveTo(pf.x, pf.y);
    }

    /**
     * Constructs the pacman with parcel.
     */
    protected Pacman(Parcel parcel) {
        super(parcel);
    }

    /**
     * Writes data to parcel.
     */
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        super.writeToParcel(parcel, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Pacman> CREATOR = new Creator<Pacman>() {
        @Override
        public Pacman createFromParcel(Parcel in) {
            return new Pacman(in);
        }

        @Override
        public Pacman[] newArray(int size) {
            return new Pacman[size];
        }
    };

}
