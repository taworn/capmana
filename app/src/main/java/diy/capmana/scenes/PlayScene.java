package diy.capmana.scenes;

import android.graphics.PointF;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import diy.capmana.Game;
import diy.capmana.R;
import diy.capmana.Sprite;
import diy.capmana.game.Divo;
import diy.capmana.game.Map;
import diy.capmana.game.Pacman;

/**
 * Playing game scene.
 */
public class PlayScene extends Scene {

    private static final String TAG = PlayScene.class.getSimpleName();

    private Sprite spriteMap;
    private Sprite spritePacman;
    private Map map;
    private Divo[] movDivoes;
    private Pacman movHero;
    private long timeStart;
<<<<<<< HEAD
=======
    private Animation aniHero;
    private Animation[] aniDivoes = new Animation[4];
>>>>>>> 3ea74d743dba11f58466f3986c39d4b0371fa28b

    public PlayScene(@Nullable Bundle bundle) {
        super(bundle);
        Log.d(TAG, "PlayScene created");
        acquire(bundle);

        map = new Map();
        map.load();
        movDivoes = new Divo[4];
<<<<<<< HEAD
=======
        for (int i = 0; i < 4; i++) {
            movDivoes[i] = new Divo();
            movDivoes[i].setId(i);
            movDivoes[i].setMap(map);
        }
        movHero = new Pacman();
        movHero.setMap(map);
        timeStart = System.currentTimeMillis();

        final int TIME = 300;
        aniHero = new Animation();
        aniHero.add(0, 0, 2, TIME);
        aniHero.add(1, 2, 4, TIME);
        aniHero.add(2, 4, 6, TIME);
        aniHero.add(3, 6, 8, TIME);
        aniHero.use(0);

>>>>>>> 3ea74d743dba11f58466f3986c39d4b0371fa28b
        for (int i = 0; i < 4; i++) {
            movDivoes[i] = new Divo();
            movDivoes[i].setId(i);
            movDivoes[i].setMap(map);
        }
        movHero = new Pacman();
        movHero.setMap(map);
        timeStart = System.currentTimeMillis();

        if (bundle != null) {
            onRestoreInstanceState(bundle);
        }
    }

    @Override
    public void acquire(@Nullable Bundle bundle) {
        Log.d(TAG, "acquire() called");
        super.acquire(bundle);
        spriteMap = new Sprite(Game.instance().getContext(), R.drawable.map, 2, 2);
        spritePacman = new Sprite(Game.instance().getContext(), R.drawable.pacman, 8, 8);
    }

    @Override
    public void release() {
        Log.d(TAG, "release() called");
        spritePacman.release();
        spriteMap.release();
        super.release();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause()");
        super.onPause();
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume()");
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, "onSaveInstanceState()");
        if (savedInstanceState != null) {
            savedInstanceState.putParcelable("map", map);
            savedInstanceState.putParcelableArray("movDivoes", movDivoes);
            savedInstanceState.putParcelable("movHero", movHero);
        }
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, "onRestoreInstanceState()");
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            map = savedInstanceState.getParcelable("map");
            movDivoes = (Divo[]) savedInstanceState.getParcelableArray("movDivoes");
            movHero = savedInstanceState.getParcelable("movHero");
            timeStart = System.currentTimeMillis();
        }
    }

    @Override
    public void onSwipeTop() {
        Log.d(TAG, "onSwipeTop() called");
        movHero.move(Map.MOVE_UP);
    }

    @Override
    public void onSwipeLeft() {
        Log.d(TAG, "onSwipeLeft() called");
        movHero.move(Map.MOVE_LEFT);
    }

    @Override
    public void onSwipeRight() {
        Log.d(TAG, "onSwipeRight() called");
        movHero.move(Map.MOVE_RIGHT);
    }

    @Override
    public void onSwipeBottom() {
        Log.d(TAG, "onSwipeBottom() called");
        movHero.move(Map.MOVE_DOWN);
    }

    @Override
    public void draw() {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        // sets timing
        long timeUsed = System.currentTimeMillis() - timeStart;
        timeStart = System.currentTimeMillis();
        //Log.d(TAG, "used " + timeUsed + " ms");
        movDivoes[0].play(timeUsed);
        movDivoes[1].play(timeUsed);
        movDivoes[2].play(timeUsed);
        movDivoes[3].play(timeUsed);
        movHero.play(timeUsed);

        // combines viewing and projecting matrices
        float[] projectionMatrix = new float[16];
        float[] viewMatrix = new float[16];
        float[] viewProjectMatrix = new float[16];
<<<<<<< HEAD
=======
        float[] translateMatrix = new float[16];
>>>>>>> 3ea74d743dba11f58466f3986c39d4b0371fa28b
        float[] scaleMatrix = new float[16];
        Matrix.orthoM(projectionMatrix, 0, -1.0f, 1.0f, -1.0f, 1.0f, 1.0f, 25.0f);
        Matrix.setLookAtM(viewMatrix, 0,
                0.0f, 0.0f, 1.5f,    // eye
                0.0f, 0.0f, -15.0f,  // at
                0.0f, 1.0f, 0.0f);   // up
        Matrix.multiplyMM(viewProjectMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
<<<<<<< HEAD
=======

        // sets scaling
        Matrix.setIdentityM(scaleMatrix, 0);
        Matrix.scaleM(scaleMatrix, 0, 0.0625f, 0.0625f, 1.0f);
        PointF scaleUp = new PointF(16.0f, 16.0f);

        // drawing
        map.draw(spriteMap, viewProjectMatrix, scaleMatrix, scaleUp);
        movDivoes[0].draw(spritePacman, viewProjectMatrix, scaleMatrix, scaleUp);
        movDivoes[1].draw(spritePacman, viewProjectMatrix, scaleMatrix, scaleUp);
        movDivoes[2].draw(spritePacman, viewProjectMatrix, scaleMatrix, scaleUp);
        movDivoes[3].draw(spritePacman, viewProjectMatrix, scaleMatrix, scaleUp);
        movHero.draw(spritePacman, viewProjectMatrix, scaleMatrix, scaleUp);


        float[] tempMatrix = new float[16];
>>>>>>> 3ea74d743dba11f58466f3986c39d4b0371fa28b

        // sets scaling
        Matrix.setIdentityM(scaleMatrix, 0);
<<<<<<< HEAD
        Matrix.scaleM(scaleMatrix, 0, 0.0625f, 0.0625f, 1.0f);
        PointF scaleUp = new PointF(16.0f, 16.0f);

        // drawing
        map.draw(spriteMap, viewProjectMatrix, scaleMatrix, scaleUp);
        movDivoes[0].draw(spritePacman, viewProjectMatrix, scaleMatrix, scaleUp);
        movDivoes[1].draw(spritePacman, viewProjectMatrix, scaleMatrix, scaleUp);
        movDivoes[2].draw(spritePacman, viewProjectMatrix, scaleMatrix, scaleUp);
        movDivoes[3].draw(spritePacman, viewProjectMatrix, scaleMatrix, scaleUp);
        movHero.draw(spritePacman, viewProjectMatrix, scaleMatrix, scaleUp);

        // checks idling
        for (int i = 0; i < 4; i++) {
            if (movDivoes[i].isIdle())
                movDivoes[i].nextMove();
        }
=======
        Matrix.scaleM(scaleMatrix, 0, 0.05f, 0.05f, 1.0f);
        Matrix.setIdentityM(translateMatrix, 0);
        Matrix.translateM(translateMatrix, 0, aniHero.getCurrentX(), aniHero.getCurrentY(), 0);
        Matrix.multiplyMM(tempMatrix, 0, translateMatrix, 0, scaleMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, viewProjectMatrix, 0, tempMatrix, 0);
        boolean enableX = false, enableY = false;
        if (aniHero.getVelocityX() > 0.0f && aniHero.getCurrentX() < 0.95f)
            enableX = true;
        else if (aniHero.getVelocityX() < 0.0f && aniHero.getCurrentX() > -0.95f)
            enableX = true;
        if (aniHero.getVelocityY() > 0.0f && aniHero.getCurrentY() < 0.95f)
            enableY = true;
        else if (aniHero.getVelocityY() < 0.0f && aniHero.getCurrentY() > -0.95f)
            enableY = true;
        aniHero.playFrame(enableX, enableY);
        aniHero.draw(mvpMatrix, spritePacman);

        Matrix.setIdentityM(translateMatrix, 0);
        Matrix.translateM(translateMatrix, 0, -0.5f, 0.5f, 0);
        Matrix.multiplyMM(tempMatrix, 0, translateMatrix, 0, scaleMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, viewProjectMatrix, 0, tempMatrix, 0);
        aniDivoes[0].draw(mvpMatrix, spritePacman);

        Matrix.setIdentityM(translateMatrix, 0);
        Matrix.translateM(translateMatrix, 0, 0.5f, 0.5f, 0);
        Matrix.multiplyMM(tempMatrix, 0, translateMatrix, 0, scaleMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, viewProjectMatrix, 0, tempMatrix, 0);
        aniDivoes[1].draw(mvpMatrix, spritePacman);

        Matrix.setIdentityM(translateMatrix, 0);
        Matrix.translateM(translateMatrix, 0, -0.5f, -0.5f, 0);
        Matrix.multiplyMM(tempMatrix, 0, translateMatrix, 0, scaleMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, viewProjectMatrix, 0, tempMatrix, 0);
        aniDivoes[2].draw(mvpMatrix, spritePacman);

        Matrix.setIdentityM(translateMatrix, 0);
        Matrix.translateM(translateMatrix, 0, 0.5f, -0.5f, 0);
        Matrix.multiplyMM(tempMatrix, 0, translateMatrix, 0, scaleMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, viewProjectMatrix, 0, tempMatrix, 0);
        aniDivoes[3].draw(mvpMatrix, spritePacman);
>>>>>>> 3ea74d743dba11f58466f3986c39d4b0371fa28b

        computeFPS();
    }

}
