package diy.capmana.game;

import android.graphics.Point;
import android.graphics.PointF;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import diy.capmana.Animation;

/**
 * A pacman class.
 */
public class Pacman extends Movable implements Parcelable {

    /**
     * Constructs the pacman.
     */
    public Pacman() {
        setTimes(200, 1000);
        getAnimation().add(ACTION_LEFT, 0, 2, Animation.ON_END_CONTINUE, TIME_PER_ANI_FRAME);
        getAnimation().add(ACTION_RIGHT, 2, 4, Animation.ON_END_CONTINUE, TIME_PER_ANI_FRAME);
        getAnimation().add(ACTION_UP, 4, 6, Animation.ON_END_CONTINUE, TIME_PER_ANI_FRAME);
        getAnimation().add(ACTION_DOWN, 6, 8, Animation.ON_END_CONTINUE, TIME_PER_ANI_FRAME);
        getAnimation().use(ACTION_LEFT);
    }

    /**
     * Detects enemies within rectangle.
     */
    public void detect() {
        final float RANGE = 0.03125f;
        float x = getCurrentX();
        float y = getCurrentY();
        float left = x - RANGE;
        float top = y + RANGE;
        float right = x + RANGE;
        float bottom = y - RANGE;

        GameData gameData = GameData.instance();
        int count = gameData.getDivoCount();
        int i = 0;
        boolean detected = false;
        while (i < count) {
            Divo divo = gameData.getDivo(i);
            float divoX = divo.getCurrentX();
            float divoY = divo.getCurrentY();

            if (!divo.isDead()) {
                if (left < divoX && top > divoY && divoX < right && divoY > bottom) {
                    detected = true;
                    break;
                }
            }

            i++;
        }

        if (detected) {
            Divo divo = gameData.getDivo(i);
            divo.kill();
            Log.d(TAG, "eat Divo #" + i);
        }
    }

    public void setMap(Map map) {
        super.setMap(map);

        Point p = new Point(0, 0);
        PointF pf = new PointF(0, 0);
        getMap().getPacmanStartPosition(p, pf);
        setXY(p.x, p.y);
        getAnimation().moveTo(pf.x, pf.y);
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
