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

    public static final int TIME_PER_ANI_FRAME = 250;

    protected Point point = new Point(0, 0);
    protected boolean walking = false;
    protected float distance = 0;
    protected float target = 0;
    protected int currentDirection = 0;
    protected int nextDirection = 0;
    protected long timePerDistance = 350;
    protected long timeUsed = 0;
    protected Animation animation = new Animation();
    protected Map map = null;

    /**
     * Constructs the movable.
     */
    public Movable() {
    }

    /**
     * Sets map.  Used to bind Movable with Map.
     */
    public void setMap(Map map) {
    }

    /**
     * Moves with direction, use constant Map::MOVE_*.
     */
    public void move(int direction) {
        if (!walking) {
            PointF pf = new PointF(0, 0);
            if (direction == Map.MOVE_LEFT) {
                animation.use(0);
                if (map.canMove(this, direction, point, pf)) {
                    distance = animation.getCurrentX() - pf.x;
                    target = pf.x;
                    currentDirection = direction;
                    nextDirection = direction;
                    timeUsed = 0;
                    walking = true;
                }
            }
            else if (direction == Map.MOVE_RIGHT) {
                animation.use(1);
                if (map.canMove(this, direction, point, pf)) {
                    distance = pf.x - animation.getCurrentX();
                    target = pf.x;
                    currentDirection = direction;
                    nextDirection = direction;
                    timeUsed = 0;
                    walking = true;
                }
            }
            else if (direction == Map.MOVE_UP) {
                animation.use(2);
                if (map.canMove(this, direction, point, pf)) {
                    distance = pf.y - animation.getCurrentY();
                    target = pf.y;
                    currentDirection = direction;
                    nextDirection = direction;
                    timeUsed = 0;
                    walking = true;
                }
            }
            else if (direction == Map.MOVE_DOWN) {
                animation.use(3);
                if (map.canMove(this, direction, point, pf)) {
                    distance = animation.getCurrentY() - pf.y;
                    target = pf.y;
                    currentDirection = direction;
                    nextDirection = direction;
                    timeUsed = 0;
                    walking = true;
                }
            }
        }
        else {
            nextDirection = direction;
        }
    }

    /**
     * After move animation completed, it's call this function.
     */
    public void nextMove() {
        move(nextDirection);
    }

    /**
     * Moves with direction, use constant Map::MOVE_*.
     */
    public void play(long timeUsed) {
        if (walking) {
            if (currentDirection == Map.MOVE_LEFT) {
                if (this.timeUsed + timeUsed < timePerDistance) {
                    float d = timeUsed * distance / timePerDistance;
                    animation.moveBy(-d, 0);
                    this.timeUsed += timeUsed;
                }
                else {
                    animation.moveTo(target, animation.getCurrentY());
                    walking = false;
                    nextMove();
                }
            }
            else if (currentDirection == Map.MOVE_RIGHT) {
                if (this.timeUsed + timeUsed < timePerDistance) {
                    float d = timeUsed * distance / timePerDistance;
                    animation.moveBy(d, 0);
                    this.timeUsed += timeUsed;
                }
                else {
                    animation.moveTo(target, animation.getCurrentY());
                    walking = false;
                    nextMove();
                }
            }
            else if (currentDirection == Map.MOVE_UP) {
                if (this.timeUsed + timeUsed < timePerDistance) {
                    float d = timeUsed * distance / timePerDistance;
                    animation.moveBy(0, d);
                    this.timeUsed += timeUsed;
                }
                else {
                    animation.moveTo(animation.getCurrentX(), target);
                    walking = false;
                    nextMove();
                }
            }
            else if (currentDirection == Map.MOVE_DOWN) {
                if (this.timeUsed + timeUsed < timePerDistance) {
                    float d = timeUsed * distance / timePerDistance;
                    animation.moveBy(0, -d);
                    this.timeUsed += timeUsed;
                }
                else {
                    animation.moveTo(animation.getCurrentX(), target);
                    walking = false;
                    nextMove();
                }
            }
        }
    }

    /**
     * Draws movable.
     */
    public void draw(Sprite sprite, float[] viewProjectMatrix, float[] scaleMatrix, PointF scaleUp) {
        float[] translateMatrix = new float[16];
        float[] modelMatrix = new float[16];
        float[] mvpMatrix = new float[16];

        Matrix.setIdentityM(translateMatrix, 0);
        Matrix.translateM(translateMatrix, 0, getCurrentX(), getCurrentY(), 0);
        Matrix.multiplyMM(modelMatrix, 0, translateMatrix, 0, scaleMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, viewProjectMatrix, 0, modelMatrix, 0);

        animation.draw(mvpMatrix, sprite);
    }

    public int getX() {
        return point.x;
    }

    public int getY() {
        return point.y;
    }

    public float getCurrentX() {
        return animation.getCurrentX();
    }

    public float getCurrentY() {
        return animation.getCurrentY();
    }

    public float getVelocityX() {
        return animation.getVelocityX();
    }

    public float getVelocityY() {
        return animation.getVelocityY();
    }

    /**
     * Constructs the movable with parcel.
     */
    protected Movable(Parcel parcel) {
        int x, y;
        x = parcel.readInt();
        y = parcel.readInt();
        point = new Point(x, y);

        walking = parcel.readByte() != 0;
        distance = parcel.readFloat();
        target = parcel.readFloat();
        currentDirection = parcel.readInt();
        nextDirection = parcel.readInt();
        timePerDistance = parcel.readLong();
        timeUsed = parcel.readLong();

        animation = (Animation) parcel.readParcelable(Animation.class.getClassLoader());
    }

    /**
     * Writes data to parcel.
     */
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(point.x);
        parcel.writeInt(point.y);

        parcel.writeByte((byte) (walking ? 1 : 0));
        parcel.writeFloat(distance);
        parcel.writeFloat(target);
        parcel.writeInt(currentDirection);
        parcel.writeInt(nextDirection);
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
