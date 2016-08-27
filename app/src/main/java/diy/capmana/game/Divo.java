package diy.capmana.game;

import android.graphics.Point;
import android.graphics.PointF;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.Random;

import diy.capmana.Animation;
import diy.capmana.Game;

/**
 * A divo class.
 */
public class Divo extends Movable implements Parcelable {

    private Random random = new Random();
    private int divoId = -1;

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
        this.divoId = divoId;
        getAnimation().add(ACTION_LEFT, (divoId + 1) * 8, (divoId + 1) * 8 + 2, Animation.ON_END_CONTINUE, TIME_PER_ANI_FRAME);
        getAnimation().add(ACTION_RIGHT, (divoId + 1) * 8 + 2, (divoId + 1) * 8 + 4, Animation.ON_END_CONTINUE, TIME_PER_ANI_FRAME);
        getAnimation().add(ACTION_UP, (divoId + 1) * 8 + 4, (divoId + 1) * 8 + 6, Animation.ON_END_CONTINUE, TIME_PER_ANI_FRAME);
        getAnimation().add(ACTION_DOWN, (divoId + 1) * 8 + 6, (divoId + 1) * 8 + 8, Animation.ON_END_CONTINUE, TIME_PER_ANI_FRAME);
        getAnimation().add(ACTION_REVERSE_LEFT, 40, 44, Animation.ON_END_KEEP_LAST_FRAME, TIME_PER_ANI_FRAME);
        getAnimation().add(ACTION_REVERSE_RIGHT, 40, 44, Animation.ON_END_KEEP_LAST_FRAME, TIME_PER_ANI_FRAME);
        getAnimation().add(ACTION_REVERSE_UP, 40, 44, Animation.ON_END_KEEP_LAST_FRAME, TIME_PER_ANI_FRAME);
        getAnimation().add(ACTION_REVERSE_DOWN, 40, 44, Animation.ON_END_KEEP_LAST_FRAME, TIME_PER_ANI_FRAME);
        getAnimation().add(ACTION_DEAD_LEFT, 56, 57, Animation.ON_END_KEEP_LAST_FRAME, TIME_PER_ANI_FRAME);
        getAnimation().add(ACTION_DEAD_RIGHT, 57, 58, Animation.ON_END_KEEP_LAST_FRAME, TIME_PER_ANI_FRAME);
        getAnimation().add(ACTION_DEAD_UP, 58, 59, Animation.ON_END_KEEP_LAST_FRAME, TIME_PER_ANI_FRAME);
        getAnimation().add(ACTION_DEAD_DOWN, 59, 60, Animation.ON_END_KEEP_LAST_FRAME, TIME_PER_ANI_FRAME);
        getAnimation().use(ACTION_LEFT);
    }

    public void nextAction() {
        super.nextAction();
        if (isDead()) {
            if (GameData.instance().divoCanRelife()) {
                Log.d(TAG, "Divo #" + divoId + " is relife");
                GameData.instance().divoLifeDecrease();
                relife();

                Point p = new Point(0, 0);
                PointF pf = new PointF(0, 0);
                getMap().getDivoStartPosition(p, pf);
                setXY(p.x, p.y);
                getAnimation().moveTo(pf.x, pf.y);
                getAnimation().use(ACTION_LEFT);
            }
            else {
                if (GameData.instance().checkAllDivoDead()) {
                    Log.d(TAG, "all Divoes are dead");
                    Game.instance().changeScene(Game.SCENE_WIN);
                }
            }
        }
    }

    public void kill() {
        super.kill();

        Point p = new Point(0, 0);
        PointF pf = new PointF(0, 0);
        getMap().getDivoStartPosition(p, pf);
        moveDirect(p, pf);

        float dX = pf.x - getAnimation().getCurrentX();
        float dY = pf.y - getAnimation().getCurrentY();
        if (Math.abs(dX) >= Math.abs(dY)) {
            if (dX <= 0.0f)
                getAnimation().use(ACTION_DEAD_LEFT);
            else
                getAnimation().use(ACTION_DEAD_RIGHT);
        }
        else {
            if (dY <= 0.0f)
                getAnimation().use(ACTION_DEAD_DOWN);
            else
                getAnimation().use(ACTION_DEAD_UP);
        }
    }

    public void setMap(Map map) {
        super.setMap(map);

        GameData.instance().divoLifeDecrease();
        Point p = new Point(0, 0);
        PointF pf = new PointF(0, 0);
        getMap().getDivoStartPosition(p, pf);
        setXY(p.x, p.y);
        getAnimation().moveTo(pf.x, pf.y);
    }

    public int decision(int moveDirection) {
        Map.IntegerHolder item = new Map.IntegerHolder();
        if (getMap().checkAndGetItem(this, item)) {
            GameData.instance().getBonus(item.value);
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
        divoId = parcel.readInt();
    }

    /**
     * Writes data to parcel.
     */
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        super.writeToParcel(parcel, flags);
        parcel.writeInt(divoId);
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
