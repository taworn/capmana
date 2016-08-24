package diy.capmana.game;

import android.graphics.PointF;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Random;

/**
 * A divo class.
 */
public class Divo extends Movable implements Parcelable {

    private Random random = new Random();

    /**
     * Constructs the divo.
     */
    public Divo() {
    }

    /**
     * Sets divo identifer, just used to distint animation's set.
     */
    public void setId(int divoId) {
        assert (divoId >= 0 && divoId < 4);
        animation.add(0, (divoId + 1) * 8, (divoId + 1) * 8 + 2, TIME_PER_ANI_FRAME);
        animation.add(1, (divoId + 1) * 8 + 2, (divoId + 1) * 8 + 4, TIME_PER_ANI_FRAME);
        animation.add(2, (divoId + 1) * 8 + 4, (divoId + 1) * 8 + 6, TIME_PER_ANI_FRAME);
        animation.add(3, (divoId + 1) * 8 + 6, (divoId + 1) * 8 + 8, TIME_PER_ANI_FRAME);
        animation.use(0);
    }

    /**
     * Sets map.
     */
    public void setMap(Map map) {
        assert (map != null);
        this.map = map;

        PointF pf = new PointF(0, 0);
        map.getDivoStartPosition(point, pf);
        animation.moveTo(pf.x, pf.y);
    }

    /**
     * After move animation completed, it's call this function.
     */
    public void nextMove() {
        if (map.hasItem(this)) {

        }

        int dirs = map.canPreviewMove(this);
        int count = 0;
        if ((dirs & Map.MOVE_LEFT) == Map.MOVE_LEFT) count++;
        if ((dirs & Map.MOVE_RIGHT) == Map.MOVE_RIGHT) count++;
        if ((dirs & Map.MOVE_UP) == Map.MOVE_UP) count++;
        if ((dirs & Map.MOVE_DOWN) == Map.MOVE_DOWN) count++;

        if (count <= 0)
            return;

        else if (count == 1)
            nextDirection = dirs;

        else if (count == 2) {
            if (!(nextDirection != 0 && (dirs & nextDirection) == nextDirection)) {
                int randoms[] = {0, 0};
                int end = 0;
                if ((dirs & Map.MOVE_LEFT) == Map.MOVE_LEFT) randoms[end++] = Map.MOVE_LEFT;
                if ((dirs & Map.MOVE_RIGHT) == Map.MOVE_RIGHT) randoms[end++] = Map.MOVE_RIGHT;
                if ((dirs & Map.MOVE_UP) == Map.MOVE_UP) randoms[end++] = Map.MOVE_UP;
                if ((dirs & Map.MOVE_DOWN) == Map.MOVE_DOWN) randoms[end] = Map.MOVE_DOWN;
                nextDirection = randoms[random.nextInt(2)];
            }
        }

        else {
            int randoms[] = {0, 0, 0, 0, 0, 0, 0, 0,};
            int end = 0;
            int opposite = 0;
            if (nextDirection != 0) {
                randoms[end++] = nextDirection;
                randoms[end++] = nextDirection;
                if (nextDirection == Map.MOVE_LEFT) opposite = Map.MOVE_RIGHT;
                if (nextDirection == Map.MOVE_RIGHT) opposite = Map.MOVE_LEFT;
                if (nextDirection == Map.MOVE_UP) opposite = Map.MOVE_DOWN;
                if (nextDirection == Map.MOVE_DOWN) opposite = Map.MOVE_UP;
            }
            if ((dirs & Map.MOVE_LEFT) == Map.MOVE_LEFT && opposite != Map.MOVE_LEFT)
                randoms[end++] = Map.MOVE_LEFT;
            if ((dirs & Map.MOVE_RIGHT) == Map.MOVE_RIGHT && opposite != Map.MOVE_RIGHT)
                randoms[end++] = Map.MOVE_RIGHT;
            if ((dirs & Map.MOVE_UP) == Map.MOVE_UP && opposite != Map.MOVE_UP)
                randoms[end++] = Map.MOVE_UP;
            if ((dirs & Map.MOVE_DOWN) == Map.MOVE_DOWN && opposite != Map.MOVE_DOWN)
                randoms[end++] = Map.MOVE_DOWN;
            nextDirection = randoms[random.nextInt(end)];
        }

        move(nextDirection);
    }

    /**
     * Checks whether divo is walking or stand still.
     */
    public boolean isIdle() {
        return !walking;
    }

    /**
     * Constructs the divo with parcel.
     */
    protected Divo(Parcel parcel) {
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

    public static final Creator<Divo> CREATOR = new Creator<Divo>() {
        @Override
        public Divo createFromParcel(Parcel in) {
            return new Divo(in);
        }

        @Override
        public Divo[] newArray(int size) {
            return new Divo[size];
        }
    };

}
