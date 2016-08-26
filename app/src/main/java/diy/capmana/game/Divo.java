package diy.capmana.game;

import android.graphics.Point;
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
        getAnimation().add(ACTION_LEFT, (divoId + 1) * 8, (divoId + 1) * 8 + 2, TIME_PER_ANI_FRAME);
        getAnimation().add(ACTION_RIGHT, (divoId + 1) * 8 + 2, (divoId + 1) * 8 + 4, TIME_PER_ANI_FRAME);
        getAnimation().add(ACTION_UP, (divoId + 1) * 8 + 4, (divoId + 1) * 8 + 6, TIME_PER_ANI_FRAME);
        getAnimation().add(ACTION_DOWN, (divoId + 1) * 8 + 6, (divoId + 1) * 8 + 8, TIME_PER_ANI_FRAME);
        getAnimation().add(ACTION_DEAD, 56, 60, TIME_PER_ANI_FRAME);
        getAnimation().use(ACTION_LEFT);
    }

    public void kill() {
        super.kill();
        getAnimation().use(ACTION_DEAD);

        Point p = new Point(0, 0);
        PointF pf = new PointF(0, 0);
        getMap().getDivoStartPosition(p, pf);
        moveDirect(p, pf);
    }

    public void setMap(Map map) {
        super.setMap(map);

        Point p = new Point(0, 0);
        PointF pf = new PointF(0, 0);
        getMap().getDivoStartPosition(p, pf);
        setXY(p.x, p.y);
        getAnimation().moveTo(pf.x, pf.y);
    }

    public int decision(int moveDirection) {
        if (getMap().hasItem(this)) {

        }

        // checks directions can move
        int dirs = getMap().canPreviewMove(this);
        int count = 0;
        if ((dirs & MOVE_LEFT) == MOVE_LEFT) count++;
        if ((dirs & MOVE_RIGHT) == MOVE_RIGHT) count++;
        if ((dirs & MOVE_UP) == MOVE_UP) count++;
        if ((dirs & MOVE_DOWN) == MOVE_DOWN) count++;

        if (count <= 0)
            return moveDirection;
        else if (count == 1) {
            moveDirection = dirs;
            return moveDirection;
        }

        // if movable direction >= 2, deleted opposite direction
        if (count >= 2 && moveDirection != 0) {
            int opposite = 0;
            if (moveDirection == MOVE_LEFT) opposite = MOVE_RIGHT;
            else if (moveDirection == MOVE_RIGHT) opposite = MOVE_LEFT;
            else if (moveDirection == MOVE_UP) opposite = MOVE_DOWN;
            else if (moveDirection == MOVE_DOWN) opposite = MOVE_UP;
            dirs &= ~opposite;
        }

        if (count <= 2) {
            if (!(moveDirection != 0 && (dirs & moveDirection) == moveDirection)) {
                int randoms[] = {0, 0};
                int end = 0;
                if ((dirs & MOVE_LEFT) == MOVE_LEFT) randoms[end++] = MOVE_LEFT;
                if ((dirs & MOVE_RIGHT) == MOVE_RIGHT) randoms[end++] = MOVE_RIGHT;
                if ((dirs & MOVE_UP) == MOVE_UP) randoms[end++] = MOVE_UP;
                if ((dirs & MOVE_DOWN) == MOVE_DOWN) randoms[end++] = MOVE_DOWN;
                moveDirection = randoms[random.nextInt(2)];
            }
        }
        else {
            int randoms[] = {0, 0, 0, 0, 0, 0, 0, 0,};
            int end = 0;
            if (moveDirection != 0) {
                randoms[end++] = moveDirection;
                randoms[end++] = moveDirection;
            }
            if ((dirs & MOVE_LEFT) == MOVE_LEFT) randoms[end++] = MOVE_LEFT;
            if ((dirs & MOVE_RIGHT) == MOVE_RIGHT) randoms[end++] = MOVE_RIGHT;
            if ((dirs & MOVE_UP) == MOVE_UP) randoms[end++] = MOVE_UP;
            if ((dirs & MOVE_DOWN) == MOVE_DOWN) randoms[end++] = MOVE_DOWN;
            moveDirection = randoms[random.nextInt(end)];
        }
        return moveDirection;
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
