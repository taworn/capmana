package diy.capmana.scenes;

import android.graphics.PointF;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import diy.capmana.Animation;
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
    private Animation aniHero;
    private Animation[] aniDivoes = new Animation[4];

    public PlayScene(@Nullable Bundle bundle) {
        super(bundle);
        Log.d(TAG, "PlayScene created");
        acquire(bundle);

        map = new Map();
        map.load();
        movDivoes = new Divo[4];
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

        for (int i = 0; i < 4; i++) {
            int j = (i + 1) * 8;
            aniDivoes[i] = new Animation();
            aniDivoes[i].add(0, j, j + 2, TIME);
            aniDivoes[i].add(1, j + 2, j + 4, TIME);
            aniDivoes[i].add(2, j + 4, j + 6, TIME);
            aniDivoes[i].add(3, j + 6, j + 8, TIME);
            aniDivoes[i].use(0);
        }

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
            savedInstanceState.putParcelable("aniHero", aniHero);
        }
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, "onRestoreInstanceState()");
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            aniHero = savedInstanceState.getParcelable("aniHero");
        }
    }

    @Override
    public void onSwipeTop() {
        Log.d(TAG, "onSwipeTop() called");
        aniHero.setVelocity(0.0f, 0.02f);
        aniHero.use(2);
    }

    @Override
    public void onSwipeLeft() {
        Log.d(TAG, "onSwipeLeft() called");
        aniHero.setVelocity(-0.02f, 0.0f);
        aniHero.use(0);
    }

    @Override
    public void onSwipeRight() {
        Log.d(TAG, "onSwipeRight() called");
        aniHero.setVelocity(0.02f, 0.0f);
        aniHero.use(1);
    }

    @Override
    public void onSwipeBottom() {
        Log.d(TAG, "onSwipeBottom() called");
        aniHero.setVelocity(0.0f, -0.02f);
        aniHero.use(3);
    }

    @Override
    public void draw() {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        float[] projectionMatrix = new float[16];
        float[] viewMatrix = new float[16];
        float[] viewProjectMatrix = new float[16];
        float[] translateMatrix = new float[16];
        float[] scaleMatrix = new float[16];
        float[] mvpMatrix = new float[16];
        Matrix.orthoM(projectionMatrix, 0, -1.0f, 1.0f, -1.0f, 1.0f, 1.0f, 25.0f);
        Matrix.setLookAtM(viewMatrix, 0,
                0.0f, 0.0f, 1.5f,    // eye
                0.0f, 0.0f, -15.0f,  // at
                0.0f, 1.0f, 0.0f);   // up
        Matrix.multiplyMM(viewProjectMatrix, 0, projectionMatrix, 0, viewMatrix, 0);

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

        Matrix.setIdentityM(scaleMatrix, 0);
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

        computeFPS();
    }

}
