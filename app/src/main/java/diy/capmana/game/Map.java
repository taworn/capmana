package diy.capmana.game;

import android.graphics.Point;
import android.graphics.PointF;
import android.opengl.Matrix;

import java.util.ArrayList;
import java.util.List;

import diy.capmana.Sprite;

/**
 * A map class.
 */
public class Map {

    public static final int MOVE_LEFT = 1;
    public static final int MOVE_RIGHT = 2;
    public static final int MOVE_UP = 4;
    public static final int MOVE_DOWN = 8;

    private class MapData {
        public byte image;
        public byte block;
        public byte eat;
        public byte special;

        public void from(int i) {
            image = (byte) i;
            block = (byte) (i >> 8);
            eat = (byte) (i >> 16);
            special = (byte) (i >> 24);
        }
    }

    private int width = 0;
    private int height = 0;
    private int[] mapData = null;

    List<Float> horzBounds = new ArrayList<Float>();
    List<Float> horzPoints = new ArrayList<Float>();
    List<Float> vertBounds = new ArrayList<Float>();
    List<Float> vertPoints = new ArrayList<Float>();

    Point startDivo = new Point(0, 0);
    Point startPacman = new Point(0, 0);

    /**
     * Constructs the map.
     */
    public Map() {
    }

    /**
     * Loads map data.
     */
    public boolean load() {
        final int w = 16;
        final int h = 16;
        width = w;
        height = h;
        mapData = new int[width * height];

        // for now, emulate first
        int _mapData[] = {
                0x0103, 0x0103, 0x0103, 0x0103, 0x0103, 0x0103, 0x0103, 0x0103, 0x0103, 0x0103, 0x0103, 0x0103, 0x0103, 0x0103, 0x0103, 0x0103,
                0x0103, 0x0002, 0x0001, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0001, 0x0002, 0x0103,
                0x0103, 0x0000, 0x0000, 0x0103, 0x0103, 0x0103, 0x0000, 0x0103, 0x0103, 0x0103, 0x0103, 0x0103, 0x0103, 0x0000, 0x0000, 0x0103,
                0x0103, 0x0000, 0x0000, 0x0103, 0x0000, 0x0103, 0x0000, 0x0103, 0x0000, 0x0000, 0x0000, 0x0000, 0x0103, 0x0000, 0x0000, 0x0103,
                0x0103, 0x0000, 0x0000, 0x0103, 0x0000, 0x0103, 0x0000, 0x0103, 0x0000, 0x0000, 0x0000, 0x0000, 0x0103, 0x0000, 0x0000, 0x0103,
                0x0103, 0x0000, 0x0000, 0x0103, 0x0000, 0x0103, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0103, 0x0000, 0x0000, 0x0103,
                0x0103, 0x0000, 0x0000, 0x0103, 0x0000, 0x0103, 0x0000, 0x0103, 0x0103, 0x0000, 0x0000, 0x0000, 0x0103, 0x0000, 0x0000, 0x0103,
                0x0103, 0x0000, 0x0103, 0x0103, 0x0000, 0x0103, 0x0000, 0x0103, 0x0103, 0x0000, 0x0000, 0x0000, 0x0103, 0x0103, 0x0000, 0x0103,
                0x0103, 0x0000, 0x0103, 0x0103, 0x0000, 0x0000, 0x0000, 0x0103, 0x0103, 0x0000, 0x0000, 0x0000, 0x0103, 0x0103, 0x0000, 0x0103,
                0x0103, 0x0000, 0x0000, 0x0103, 0x0000, 0x0103, 0x0000, 0x0103, 0x0103, 0x0000, 0x0000, 0x0000, 0x0103, 0x0000, 0x0000, 0x0103,
                0x0103, 0x0000, 0x0000, 0x0103, 0x0000, 0x0103, 0x0000, 0x0103, 0x0000, 0x0000, 0x0000, 0x0000, 0x0103, 0x0000, 0x0000, 0x0103,
                0x0103, 0x0000, 0x0000, 0x0103, 0x0000, 0x0103, 0x0000, 0x0103, 0x0000, 0x0000, 0x0000, 0x0000, 0x0103, 0x0000, 0x0000, 0x0103,
                0x0103, 0x0000, 0x0000, 0x0103, 0x0000, 0x0103, 0x0000, 0x0103, 0x0000, 0x0000, 0x0000, 0x0000, 0x0103, 0x0000, 0x0000, 0x0103,
                0x0103, 0x0000, 0x0000, 0x0103, 0x0000, 0x0103, 0x0103, 0x0103, 0x0000, 0x0103, 0x0103, 0x0000, 0x0103, 0x0000, 0x0000, 0x0103,
                0x0103, 0x0002, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0001, 0x0002, 0x0103,
                0x0103, 0x0103, 0x0103, 0x0103, 0x0103, 0x0103, 0x0103, 0x0103, 0x0103, 0x0103, 0x0103, 0x0103, 0x0103, 0x0103, 0x0103, 0x0103,
        };
        System.arraycopy(_mapData, 0, mapData, 0, mapData.length);

        int length;

        length = width + 1;
        horzBounds.clear();
        for (int i = 0; i < length; i++)
            horzBounds.add((float) i / (length - 1) * 2.0f - 1.0f);

        length = width;
        horzPoints.clear();
        for (int i = 0; i < length; i++)
            horzPoints.add((horzBounds.get(i) + horzBounds.get(i + 1)) / 2.0f);

        length = height + 1;
        vertBounds.clear();
        for (int i = 0; i < length; i++)
            vertBounds.add((float) i / (length - 1) * 2.0f - 1.0f);

        length = height;
        vertPoints.clear();
        for (int i = 0; i < length; i++)
            vertPoints.add((vertBounds.get(i) + vertBounds.get(i + 1)) / 2.0f);

        startDivo.x = 8;
        startDivo.y = 1;
        startPacman.x = 8;
        startPacman.y = 10;
        return true;
    }

    /**
     * Gets start position for Divo.
     */
    public void getDivoStartPosition(Point p, PointF pf) {
        p.x = startDivo.x;
        p.y = startDivo.y;
        pf.x = horzPoints.get(p.x);
        pf.y = vertPoints.get(p.y);
    }

    /**
     * Gets start position for Pacman.
     */
    public void getPacmanStartPosition(Point p, PointF pf) {
        p.x = startPacman.x;
        p.y = startPacman.y;
        pf.x = horzPoints.get(p.x);
        pf.y = vertPoints.get(p.y);
    }

    /**
     * Checks if direction is can be pass.
     */
    public boolean canMove(Movable movable, int direction, Point p, PointF pf) {
        MapData data = new MapData();

        if (direction == MOVE_LEFT) {
            int current = movable.getX();
            int next = current - 1;
            if (next >= 0) {
                data.from(mapData[movable.getY() * width + next]);
                boolean block = data.block != 0;
                if (!block) {
                    p.x = next;
                    p.y = movable.getY();
                    pf.x = horzPoints.get(p.x);
                    pf.y = vertPoints.get(p.y);
                    return true;
                }
            }
        }
        else if (direction == MOVE_RIGHT) {
            int current = movable.getX();
            int next = current + 1;
            if (next < width) {
                data.from(mapData[movable.getY() * width + next]);
                boolean block = data.block != 0;
                if (!block) {
                    p.x = next;
                    p.y = movable.getY();
                    pf.x = horzPoints.get(p.x);
                    pf.y = vertPoints.get(p.y);
                    return true;
                }
            }
        }
        else if (direction == MOVE_UP) {
            int current = movable.getY();
            int next = current - 1;
            if (next >= 0) {
                data.from(mapData[next * width + movable.getX()]);
                boolean block = data.block != 0;
                if (!block) {
                    p.x = movable.getX();
                    p.y = next;
                    pf.x = horzPoints.get(p.x);
                    pf.y = vertPoints.get(p.y);
                    return true;
                }
            }
        }
        else if (direction == MOVE_DOWN) {
            int current = movable.getY();
            int next = current + 1;
            if (next < height) {
                data.from(mapData[next * width + movable.getX()]);
                boolean block = data.block != 0;
                if (!block) {
                    p.x = movable.getX();
                    p.y = next;
                    pf.x = horzPoints.get(p.x);
                    pf.y = vertPoints.get(p.y);
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Checks 4-directions which ways can move or not.
     *
     * @return Bit flags in order: left, right, up, down.
     */
    public int canPreviewMove(Movable movable) {
        int x = movable.getX();
        int y = movable.getY();
        int result = 0;
        MapData data = new MapData();

        // left
        data.from(mapData[y * width + x - 1]);
        if (x > 0 && data.block == 0)
            result |= MOVE_LEFT;

        // right
        data.from(mapData[y * width + x + 1]);
        if (x < width - 1 && data.block == 0)
            result |= MOVE_RIGHT;

        // up
        data.from(mapData[(y - 1) * width + x]);
        if (y > 0 && data.block == 0)
            result |= MOVE_UP;

        // down
        data.from(mapData[(y + 1) * width + x]);
        if (y < height - 1 && data.block == 0)
            result |= MOVE_DOWN;

        return result;
    }

    /**
     * Draws map.
     */
    public void draw(Sprite sprite, float[] viewProjectMatrix, float[] scaleMatrix, PointF scaleUp) {
        float[] translateMatrix = new float[16];
        float[] modelMatrix = new float[16];
        float[] mvpMatrix = new float[16];

        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                Matrix.setIdentityM(translateMatrix, 0);
                Matrix.translateM(translateMatrix, 0, horzPoints.get(i), vertPoints.get(j), 0);
                Matrix.multiplyMM(modelMatrix, 0, translateMatrix, 0, scaleMatrix, 0);
                Matrix.multiplyMM(mvpMatrix, 0, viewProjectMatrix, 0, modelMatrix, 0);

                sprite.draw(mvpMatrix, (byte) mapData[j * width + i]);
            }
        }
    }

}
