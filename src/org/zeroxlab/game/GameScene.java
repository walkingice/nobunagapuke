package org.zeroxlab.game;

import android.view.MotionEvent;

import com.stickycoding.rokon.Scene;
import com.stickycoding.rokon.Sprite;
import com.stickycoding.rokon.background.FixedBackground;
import com.stickycoding.rokon.TextureAtlas;
import com.stickycoding.rokon.Texture;

public class GameScene extends Scene {

    private FixedBackground background;
    private Sprite bob;
    private Texture backgroundTexture;
    private Texture cellTexture;

    private Board mBoard;

    public GameScene() {
        super(1, 1);

        TextureAtlas atlas = new TextureAtlas();
        backgroundTexture = new Texture("background.png");
        cellTexture = new Texture("cell.png");
        atlas.insert(backgroundTexture);
        atlas.insert(cellTexture);
        atlas.complete();

        setBackground(background = new FixedBackground(backgroundTexture));

        mBoard = new Board(200, 10, 300, 300);
        mBoard.setTexture(cellTexture);
        mBoard.show();
        add(0, mBoard);
    }

    @Override
    public void onGameLoop() {
    }

    @Override
    public void onTouchDown(float x, float y, MotionEvent event, int pointerCount, int pointerId) {
        // This is called when you press down on the screen.
    }

    @Override
    public void onTouchMove(float x, float y, MotionEvent event, int pointerCount, int pointerId) {
        // This is called when you move your finger over the screen.
        //(ie pretty much every frame if your holding your finger down)

    }

    @Override
    public void onTouchUp(float x, float y, MotionEvent event, int pointerCount, int pointerId) {
        // And this is called when you stop pressing.
    }

    @Override
    public void onPause() {
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onReady() {
    }
}
