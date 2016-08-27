package diy.capmana.scenes;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import diy.capmana.Game;
import diy.capmana.R;
import diy.capmana.Sprite;
import diy.capmana.game.Divo;
import diy.capmana.game.GameData;
import diy.capmana.game.Map;
import diy.capmana.game.Movable;
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

    public PlayScene(@Nullable Bundle bundle) {
        super(bundle);
        Log.d(TAG, "PlayScene created");
        acquire(bundle);

        GameData.instance().clear();
        map = new Map();
        map.load();
        movDivoes = new Divo[4];
        for (int i = 0; i < 4; i++) {
            movDivoes[i] = new Divo();
            movDivoes[i].setId(i);
            movDivoes[i].setMap(map);
            GameData.instance().addDivo(movDivoes[i]);
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
            //savedInstanceState.putParcelable("gameData", GameData.instance());
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
            //savedInstanceState.getParcelable("gameData");
            GameData.instance().clearDivoList();
            for (int i = 0; i < 4; i++) {
                GameData.instance().addDivo(movDivoes[i]);
            }
            timeStart = System.currentTimeMillis();
        }
    }

    @Override
    public void onSwipeTop() {
        Log.d(TAG, "onSwipeTop() called");
        movHero.move(Movable.MOVE_UP);
    }

    @Override
    public void onSwipeLeft() {
        Log.d(TAG, "onSwipeLeft() called");
        movHero.move(Movable.MOVE_LEFT);
    }

    @Override
    public void onSwipeRight() {
        Log.d(TAG, "onSwipeRight() called");
        movHero.move(Movable.MOVE_RIGHT);
    }

    @Override
    public void onSwipeBottom() {
        Log.d(TAG, "onSwipeBottom() called");
        movHero.move(Movable.MOVE_DOWN);
    }

    @Override
    public void draw() {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        // sets timing
        long timeUsed = System.currentTimeMillis() - timeStart;
        timeStart = System.currentTimeMillis();
        //Log.d(TAG, "used " + timeUsed + " ms");
        GameData.instance().update(timeUsed);
        movDivoes[0].play(timeUsed);
        movDivoes[1].play(timeUsed);
        movDivoes[2].play(timeUsed);
        movDivoes[3].play(timeUsed);
        movHero.play(timeUsed);
        movHero.detect();

        // combines viewing and projecting matrices
        float[] projectionMatrix = new float[16];
        float[] viewMatrix = new float[16];
        float[] viewProjectMatrix = new float[16];
        float[] scaleMatrix = new float[16];
        Matrix.orthoM(projectionMatrix, 0, -1.0f, 1.0f, -1.0f, 1.0f, 1.0f, 25.0f);
        Matrix.setLookAtM(viewMatrix, 0,
                //movHero.getCurrentX(), movHero.getCurrentY(), 1.5f,    // eye
                //movHero.getCurrentX(), movHero.getCurrentY(), -15.0f,  // at
                0.0f, 0.0f, 1.5f,    // eye
                0.0f, 0.0f, -15.0f,  // at
                0.0f, 1.0f, 0.0f);   // up
        Matrix.multiplyMM(viewProjectMatrix, 0, projectionMatrix, 0, viewMatrix, 0);

        // drawing map
        Matrix.setIdentityM(scaleMatrix, 0);
        Matrix.scaleM(scaleMatrix, 0, 1.0f, 1.0f, 1.0f);
        map.draw(spriteMap, viewProjectMatrix, scaleMatrix);

        // drawing movables
        Matrix.setIdentityM(scaleMatrix, 0);
        Matrix.scaleM(scaleMatrix, 0, 0.0625f, 0.0625f, 1.0f);
        movDivoes[0].draw(spritePacman, viewProjectMatrix, scaleMatrix);
        movDivoes[1].draw(spritePacman, viewProjectMatrix, scaleMatrix);
        movDivoes[2].draw(spritePacman, viewProjectMatrix, scaleMatrix);
        movDivoes[3].draw(spritePacman, viewProjectMatrix, scaleMatrix);
        movHero.draw(spritePacman, viewProjectMatrix, scaleMatrix);

        // checks idling
        for (int i = 0; i < 4; i++) {
            if (movDivoes[i].isIdle())
                movDivoes[i].nextAction();
        }

        computeFPS();
    }

}
