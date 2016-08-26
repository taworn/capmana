package diy.capmana.game;

import android.graphics.Point;
import android.graphics.PointF;
import android.opengl.Matrix;
import android.os.Parcel;
import android.os.Parcelable;

import diy.capmana.Animation;
import diy.capmana.Sprite;

/**
 * A movable class.
 */
public class Movable implements Parcelable {

    public static final String TAG = Movable.class.getSimpleName();

    public static final int MOVE_LEFT = 1;
    public static final int MOVE_RIGHT = 2;
    public static final int MOVE_UP = 4;
    public static final int MOVE_DOWN = 8;

    public static final int ACTION_LEFT = 0;
    public static final int ACTION_RIGHT = 1;
    public static final int ACTION_UP = 2;
    public static final int ACTION_DOWN = 3;
    public static final int ACTION_DEAD = 4;

    public static final int TIME_PER_ANI_FRAME = 250;

    /**
     * Point (x, y) in integer.
     */
    private Point point = new Point(0, 0);

    // state flags
    private boolean dead = false;
    private boolean animating = false;

    // moving animation
    private float distanceX = 0.0f;
    private float distanceY = 0.0f;
    private float targetX = 0.0f;
    private float targetY = 0.0f;
    private int currentDirection = 0;
    private int nextDirection = 0;
    private long timePerMove = 250;
    private long timePerDead = 750;
    private long timePerDistance = 0;
    private long timeUsed = 0;
    private Animation animation = new Animation();

    private Map map = null;

    /**
     * Constructs the movable.
     */
    public Movable() {
    }

    /**
     * Moves by direction, use constant Map::MOVE_*.
     */
    public void move(int direction) {
        if (!dead) {
            if (!animating) {
                PointF pf = new PointF(0, 0);

                if (direction == MOVE_LEFT)
                    animation.use(ACTION_LEFT);
                else if (direction == MOVE_RIGHT)
                    animation.use(ACTION_RIGHT);
                else if (direction == MOVE_UP)
                    animation.use(ACTION_UP);
                else if (direction == MOVE_DOWN)
                    animation.use(ACTION_DOWN);

                if (map.canMove(this, direction, point, pf)) {
                    distanceX = pf.x - animation.getCurrentX();
                    distanceY = pf.y - animation.getCurrentY();
                    targetX = pf.x;
                    targetY = pf.y;
                    currentDirection = direction;
                    nextDirection = direction;
                    timePerDistance = timePerMove;
                    timeUsed = 0;
                    animating = true;
                }
            }
            else {
                // for Pacman, use this for controller
                nextDirection = direction;
            }
        }
    }

    /**
     * Moves to (x, y) directly.
     */
    public void moveDirect(Point p, PointF pf) {
        distanceX = pf.x - animation.getCurrentX();
        distanceY = pf.y - animation.getCurrentY();
        targetX = pf.x;
        targetY = pf.y;
        timePerDistance = timePerDead;
        timeUsed = 0;
        animating = true;
    }

    /**
     * Chooses next action.  This function is called after play() is completed.
     */
    public void nextAction() {
        if (!dead) {
            move(decision(nextDirection));
        }
    }

    /**
     * Plays animation after call move() or moveDirectly().
     */
    public void play(long timeUsed) {
        if (animating) {
            if (this.timeUsed + timeUsed < timePerDistance) {
                float dx = timeUsed * distanceX / timePerDistance;
                float dy = timeUsed * distanceY / timePerDistance;
                animation.moveBy(dx, dy);
                this.timeUsed += timeUsed;
            }
            else {
                animation.moveTo(targetX, targetY);
                animating = false;
                nextAction();
            }
        }
    }

    /**
     * Kills this movable.  Inherit class should derived this function.
     */
    public void kill() {
        dead = true;
    }

    /**
     * Checks whether movable is dead.
     */
    public boolean isDead() {
        return dead;
    }

    /**
     * Checks whether movable is busing or idling.
     */
    public boolean isIdle() {
        return !animating;
    }

    /**
     * Gets X-coordinates in integer.
     */
    public int getX() {
        return point.x;
    }

    /**
     * Gets Y-coordinates in integer.
     */
    public int getY() {
        return point.y;
    }

    /**
     * Gets X-coordinates in float.
     */
    public float getCurrentX() {
        return animation.getCurrentX();
    }

    /**
     * Gets Y-coordinates in float.
     */
    public float getCurrentY() {
        return animation.getCurrentY();
    }

    /**
     * Sets map.  Used to bind Movable with Map.
     */
    public void setMap(Map map) {
        assert map != null;
        this.map = map;
    }

    /**
     * Draws movable.
     */
    public void draw(Sprite sprite, float[] viewProjectMatrix, float[] scaleMatrix) {
        float[] translateMatrix = new float[16];
        float[] modelMatrix = new float[16];
        float[] mvpMatrix = new float[16];

        Matrix.setIdentityM(translateMatrix, 0);
        Matrix.translateM(translateMatrix, 0, getCurrentX(), getCurrentY(), 0);
        Matrix.multiplyMM(modelMatrix, 0, translateMatrix, 0, scaleMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, viewProjectMatrix, 0, modelMatrix, 0);

        animation.draw(mvpMatrix, sprite);
    }

    /**
     * Chooses which action after animation is completed.
     */
    protected int decision(int moveDirection) {
        return moveDirection;
    }

    /**
     * Sets start position.
     */
    protected void setXY(int x, int y) {
        point.x = x;
        point.y = y;
    }

    /**
     * Sets times in case of you want difference times table.
     */
    protected void setTimes(long timePerMove, long timePerDead) {
        this.timePerMove = timePerMove;
        this.timePerDead = timePerDead;
    }

    protected Animation getAnimation() {
        return animation;
    }

    protected Map getMap() {
        return map;
    }

    /**
     * Constructs the movable with parcel.
     */
    protected Movable(Parcel parcel) {
        int x, y;
        x = parcel.readInt();
        y = parcel.readInt();
        point = new Point(x, y);

        dead = parcel.readByte() != 0;
        animating = parcel.readByte() != 0;
        distanceX = parcel.readFloat();
        distanceY = parcel.readFloat();
        targetX = parcel.readFloat();
        targetY = parcel.readFloat();
        currentDirection = parcel.readInt();
        nextDirection = parcel.readInt();
        timePerMove = parcel.readLong();
        timePerDead = parcel.readLong();
        timePerDistance = parcel.readLong();
        timeUsed = parcel.readLong();

        animation = parcel.readParcelable(Animation.class.getClassLoader());
    }

    /**
     * Writes data to parcel.
     */
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(point.x);
        parcel.writeInt(point.y);

        parcel.writeByte((byte) (dead ? 1 : 0));
        parcel.writeByte((byte) (animating ? 1 : 0));
        parcel.writeFloat(distanceX);
        parcel.writeFloat(distanceY);
        parcel.writeFloat(targetX);
        parcel.writeFloat(targetY);
        parcel.writeInt(currentDirection);
        parcel.writeInt(nextDirection);
        parcel.writeLong(timePerMove);
        parcel.writeLong(timePerDead);
        parcel.writeLong(timePerDistance);
        parcel.writeLong(timeUsed);

        parcel.writeParcelable(animation, 0);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Movable> CREATOR = new Creator<Movable>() {
        @Override
        public Movable createFromParcel(Parcel in) {
            return new Movable(in);
        }

        @Override
        public Movable[] newArray(int size) {
            return new Movable[size];
        }
    };

}
