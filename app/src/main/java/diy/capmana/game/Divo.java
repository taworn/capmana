package diy.capmana.game;

import android.graphics.PointF;

import java.util.Random;

/**
 * A divo class.
 */
public class Divo extends Movable {

    private Random random = new Random();

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
<<<<<<< HEAD
                nextDirection = randoms[random.nextInt(2)];
=======
                nextDirection = randoms[random.nextInt() % 2];
>>>>>>> 3ea74d743dba11f58466f3986c39d4b0371fa28b
            }
        }

        else {
            int randoms[] = {0, 0, 0, 0, 0, 0, 0, 0,};
            int end = 0;
            if ((dirs & Map.MOVE_LEFT) == Map.MOVE_LEFT) randoms[end++] = Map.MOVE_LEFT;
            if ((dirs & Map.MOVE_RIGHT) == Map.MOVE_RIGHT) randoms[end++] = Map.MOVE_RIGHT;
            if ((dirs & Map.MOVE_UP) == Map.MOVE_UP) randoms[end++] = Map.MOVE_UP;
            if ((dirs & Map.MOVE_DOWN) == Map.MOVE_DOWN) randoms[end++] = Map.MOVE_DOWN;
            if (nextDirection != 0) randoms[end++] = nextDirection;
<<<<<<< HEAD
            nextDirection = randoms[random.nextInt(end)];
=======
            nextDirection = randoms[random.nextInt() % end];
>>>>>>> 3ea74d743dba11f58466f3986c39d4b0371fa28b
        }

        move(nextDirection);
    }

    /**
     * Checks whether divo is walking or stand still.
     */
    public boolean isIdle() {
        return !walking;
    }

}
