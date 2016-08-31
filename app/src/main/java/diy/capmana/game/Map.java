package diy.capmana.game;

import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.opengl.Matrix;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import diy.capmana.Game;
import diy.capmana.R;
import diy.capmana.Sprite;

/**
 * A map class.
 */
public class Map implements Parcelable {

    private static final String TAG = Map.class.getSimpleName();

    private class MapData {
        public boolean block;
        public boolean eat;

        public void from(int i) {
            block = (i & 0x01) == 0x01;
            eat = (i & 0x10) == 0x10;
        }
    }

    protected static class IntegerHolder {
        public int value;

        public IntegerHolder() {
            value = 0;
        }
    }

    private int width = 0;
    private int height = 0;
    private int[] mapData = null;
    private int[] imageData = null;
    private int itemCount = 0;

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
        // opens file
        Context context = Game.instance().getContext();
        InputStream stream = context.getResources().openRawResource(R.raw.debug);
        DataInputStream in = new DataInputStream(stream);

        try {
            // checks file header, 8 bytes
            byte check[] = {0x43, 0x41, 0x50, 0x4D, 0x41, 0x50, 0x00, 0x00,};
            byte header[] = {0, 0, 0, 0, 0, 0, 0, 0,};
            if (in.read(header) != 8) {
                throw new IOException("Header is not acceptable.");
            }
            for (int i = 0; i < 8; i++) {
                if (header[i] != check[i]) {
                    throw new IOException("Header is not acceptable.");
                }
            }

            // reads header information
            // 6 int = width, height, divo start width, divo start height, pacman start width, pacman start height
            int w = swapInt(in.readInt());
            int h = swapInt(in.readInt());
            if (w != 16 && h != 16)
                throw new IOException("Width and Height must be 16x16 for now.");
            int dx = swapInt(in.readInt());
            int dy = swapInt(in.readInt());
            int px = swapInt(in.readInt());
            int py = swapInt(in.readInt());
            int size = w * h;

            // mapData and imageData are for game, not used map editor, just copying
            int[] mapData = new int[size];
            for (int i = 0; i < size; i++)
                mapData[i] = in.readByte();
            int[] imageData = new int[size];
            for (int i = 0; i < size; i++)
                imageData[i] = swapInt(in.readInt());

            // copying data
            this.width = w;
            this.height = h;
            this.startDivo.x = dx;
            this.startDivo.y = dy;
            this.startPacman.x = px;
            this.startPacman.y = py;
            this.mapData = mapData;
            this.imageData = imageData;

            /*
            final int w = 16;
            final int h = 16;
            width = w;
            height = h;
            mapData = new int[width * height];
            imageData = new int[width * height];

            // for now, emulate first
            int _mapData[] = {
                    0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01,
                    0x01, 0x10, 0x10, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x10, 0x10, 0x01,
                    0x01, 0x00, 0x00, 0x01, 0x01, 0x01, 0x00, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x00, 0x00, 0x01,
                    0x01, 0x00, 0x00, 0x01, 0x00, 0x01, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x01,
                    0x01, 0x00, 0x00, 0x01, 0x00, 0x01, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x01,
                    0x01, 0x00, 0x00, 0x01, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x01,
                    0x01, 0x00, 0x00, 0x01, 0x00, 0x01, 0x00, 0x01, 0x01, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x01,
                    0x01, 0x00, 0x01, 0x01, 0x00, 0x01, 0x00, 0x01, 0x01, 0x00, 0x00, 0x00, 0x01, 0x01, 0x00, 0x01,
                    0x01, 0x00, 0x01, 0x01, 0x00, 0x00, 0x00, 0x01, 0x01, 0x00, 0x00, 0x00, 0x01, 0x01, 0x00, 0x01,
                    0x01, 0x00, 0x00, 0x01, 0x00, 0x01, 0x00, 0x01, 0x01, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x01,
                    0x01, 0x00, 0x00, 0x01, 0x00, 0x01, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x01,
                    0x01, 0x00, 0x00, 0x01, 0x00, 0x01, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x01,
                    0x01, 0x00, 0x00, 0x01, 0x00, 0x01, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x01,
                    0x01, 0x00, 0x00, 0x01, 0x00, 0x01, 0x01, 0x01, 0x00, 0x01, 0x01, 0x00, 0x01, 0x00, 0x00, 0x01,
                    0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x10, 0x10, 0x01,
                    0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01,
            };
            int _imageData[] = {
                    0x03, 0x03, 0x03, 0x03, 0x03, 0x03, 0x03, 0x03, 0x03, 0x03, 0x03, 0x03, 0x03, 0x03, 0x03, 0x03,
                    0x03, 0x02, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x02, 0x03,
                    0x03, 0x00, 0x00, 0x03, 0x03, 0x03, 0x00, 0x03, 0x03, 0x03, 0x03, 0x03, 0x03, 0x00, 0x00, 0x03,
                    0x03, 0x00, 0x00, 0x03, 0x00, 0x03, 0x00, 0x03, 0x00, 0x00, 0x00, 0x00, 0x03, 0x00, 0x00, 0x03,
                    0x03, 0x00, 0x00, 0x03, 0x00, 0x03, 0x00, 0x03, 0x00, 0x00, 0x00, 0x00, 0x03, 0x00, 0x00, 0x03,
                    0x03, 0x00, 0x00, 0x03, 0x00, 0x03, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x03, 0x00, 0x00, 0x03,
                    0x03, 0x00, 0x00, 0x03, 0x00, 0x03, 0x00, 0x03, 0x03, 0x00, 0x00, 0x00, 0x03, 0x00, 0x00, 0x03,
                    0x03, 0x00, 0x03, 0x03, 0x00, 0x03, 0x00, 0x03, 0x03, 0x00, 0x00, 0x00, 0x03, 0x03, 0x00, 0x03,
                    0x03, 0x00, 0x03, 0x03, 0x00, 0x00, 0x00, 0x03, 0x03, 0x00, 0x00, 0x00, 0x03, 0x03, 0x00, 0x03,
                    0x03, 0x00, 0x00, 0x03, 0x00, 0x03, 0x00, 0x03, 0x03, 0x00, 0x00, 0x00, 0x03, 0x00, 0x00, 0x03,
                    0x03, 0x00, 0x00, 0x03, 0x00, 0x03, 0x00, 0x03, 0x00, 0x00, 0x00, 0x00, 0x03, 0x00, 0x00, 0x03,
                    0x03, 0x00, 0x00, 0x03, 0x00, 0x03, 0x00, 0x03, 0x00, 0x00, 0x00, 0x00, 0x03, 0x00, 0x00, 0x03,
                    0x03, 0x00, 0x00, 0x03, 0x00, 0x03, 0x00, 0x03, 0x00, 0x00, 0x00, 0x00, 0x03, 0x00, 0x00, 0x03,
                    0x03, 0x00, 0x00, 0x03, 0x00, 0x03, 0x03, 0x03, 0x00, 0x03, 0x03, 0x00, 0x03, 0x00, 0x00, 0x03,
                    0x03, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x02, 0x03,
                    0x03, 0x03, 0x03, 0x03, 0x03, 0x03, 0x03, 0x03, 0x03, 0x03, 0x03, 0x03, 0x03, 0x03, 0x03, 0x03,
            };
            System.arraycopy(_mapData, 0, mapData, 0, mapData.length);
            System.arraycopy(_imageData, 0, imageData, 0, imageData.length);
            */

            itemCount = 0;
            for (int i = 0; i < w * h; i++)
                if ((mapData[i] & 0x10) == 0x10)
                    itemCount++;

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
                vertBounds.add((float) (length - 1 - i) / (length - 1) * 2.0f - 1.0f);

            length = height;
            vertPoints.clear();
            for (int i = 0; i < length; i++)
                vertPoints.add((vertBounds.get(i) + vertBounds.get(i + 1)) / 2.0f);

            //startDivo.x = 4;
            //startDivo.y = 4;
            //startPacman.x = 9;
            //startPacman.y = 1;
            return true;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                stream.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private int swapInt(int i) {
        int b0 = (i & 0x000000FF) << 24;
        int b1 = (i & 0x0000FF00) << 8;
        int b2 = (i & 0x00FF0000) >> 8;
        int b3 = (i & 0xFF000000) >> 24;
        return b0 | b1 | b2 | b3;
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

        if (direction == Movable.MOVE_LEFT) {
            int current = movable.getX();
            int next = current - 1;
            if (next >= 0) {
                data.from(mapData[movable.getY() * width + next]);
                if (!data.block) {
                    p.x = next;
                    p.y = movable.getY();
                    pf.x = horzPoints.get(p.x);
                    pf.y = vertPoints.get(p.y);
                    return true;
                }
            }
        }
        else if (direction == Movable.MOVE_RIGHT) {
            int current = movable.getX();
            int next = current + 1;
            if (next < width) {
                data.from(mapData[movable.getY() * width + next]);
                if (!data.block) {
                    p.x = next;
                    p.y = movable.getY();
                    pf.x = horzPoints.get(p.x);
                    pf.y = vertPoints.get(p.y);
                    return true;
                }
            }
        }
        else if (direction == Movable.MOVE_UP) {
            int current = movable.getY();
            int next = current - 1;
            if (next >= 0) {
                data.from(mapData[next * width + movable.getX()]);
                if (!data.block) {
                    p.x = movable.getX();
                    p.y = next;
                    pf.x = horzPoints.get(p.x);
                    pf.y = vertPoints.get(p.y);
                    return true;
                }
            }
        }
        else if (direction == Movable.MOVE_DOWN) {
            int current = movable.getY();
            int next = current + 1;
            if (next < height) {
                data.from(mapData[next * width + movable.getX()]);
                if (!data.block) {
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
        if (x > 0 && !data.block)
            result |= Movable.MOVE_LEFT;

        // right
        data.from(mapData[y * width + x + 1]);
        if (x < width - 1 && !data.block)
            result |= Movable.MOVE_RIGHT;

        // up
        data.from(mapData[(y - 1) * width + x]);
        if (y > 0 && !data.block)
            result |= Movable.MOVE_UP;

        // down
        data.from(mapData[(y + 1) * width + x]);
        if (y < height - 1 && !data.block)
            result |= Movable.MOVE_DOWN;

        return result;
    }

    /**
     * Checks whether that the floor has item.
     */
    public boolean checkAndGetItem(Movable movable, IntegerHolder item) {
        int index = movable.getY() * width + movable.getX();
        MapData data = new MapData();
        if ((mapData[index] & 0x10) == 0x10) {
            item.value = imageData[index];
            imageData[index] = 0;
            mapData[index] &= ~0x10;
            itemCount--;

            if (itemCount <= 0) {
                Log.d(TAG, "Game Over because Divoes eat all items");
                Game.instance().changeScene(Game.SCENE_GAMEOVER);
            }

            return true;
        }
        return false;
    }

    /**
     * Draws map.
     */
    public void draw(Sprite sprite, float[] viewProjectMatrix, float[] scaleMatrix) {
        float[] translateMatrix = new float[16];
        float[] modelMatrix = new float[16];
        float[] mvpMatrix = new float[16];

        Matrix.setIdentityM(translateMatrix, 0);
        Matrix.translateM(translateMatrix, 0, 0, 0, 0);
        Matrix.multiplyMM(modelMatrix, 0, translateMatrix, 0, scaleMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, viewProjectMatrix, 0, modelMatrix, 0);
        sprite.drawBatch(mvpMatrix, horzBounds, vertBounds, 0.0f, imageData);
    }

    /**
     * Constructs the map with parcel.
     */
    protected Map(Parcel parcel) {
        width = parcel.readInt();
        height = parcel.readInt();
        parcel.readIntArray(mapData);
        parcel.readIntArray(imageData);

        int length;
        length = parcel.readInt();
        horzBounds.clear();
        for (int i = 0; i < length - 1; i++)
            horzBounds.add(parcel.readFloat());
        length = parcel.readInt();
        horzPoints.clear();
        for (int i = 0; i < length - 1; i++)
            horzPoints.add(parcel.readFloat());
        length = parcel.readInt();
        vertBounds.clear();
        for (int i = 0; i < length - 1; i++)
            vertBounds.add(parcel.readFloat());
        length = parcel.readInt();
        vertPoints.clear();
        for (int i = 0; i < length - 1; i++)
            vertPoints.add(parcel.readFloat());

        int x, y;
        x = parcel.readInt();
        y = parcel.readInt();
        startDivo = new Point(x, y);
        x = parcel.readInt();
        y = parcel.readInt();
        startPacman = new Point(x, y);
    }

    /**
     * Writes data to parcel.
     */
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(width);
        parcel.writeInt(height);
        parcel.writeIntArray(mapData);
        parcel.writeIntArray(imageData);

        parcel.writeInt(horzBounds.size());
        for (int i = 0; i < horzBounds.size() - 1; i++)
            parcel.writeFloat(horzBounds.get(i));
        parcel.writeInt(horzPoints.size());
        for (int i = 0; i < horzPoints.size() - 1; i++)
            parcel.writeFloat(horzPoints.get(i));
        parcel.writeInt(vertBounds.size());
        for (int i = 0; i < vertBounds.size() - 1; i++)
            parcel.writeFloat(vertBounds.get(i));
        parcel.writeInt(vertPoints.size());
        for (int i = 0; i < vertPoints.size() - 1; i++)
            parcel.writeFloat(vertPoints.get(i));

        parcel.writeInt(startDivo.x);
        parcel.writeInt(startDivo.y);
        parcel.writeInt(startPacman.x);
        parcel.writeInt(startPacman.y);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Map> CREATOR = new Creator<Map>() {
        @Override
        public Map createFromParcel(Parcel in) {
            return new Map(in);
        }

        @Override
        public Map[] newArray(int size) {
            return new Map[size];
        }
    };

}
