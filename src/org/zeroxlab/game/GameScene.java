package org.zeroxlab.game;

import android.view.MotionEvent;

import com.stickycoding.rokon.Scene;
import com.stickycoding.rokon.Sprite;
import com.stickycoding.rokon.background.FixedBackground;
import com.stickycoding.rokon.TextureAtlas;
import com.stickycoding.rokon.Texture;

public class GameScene extends Scene {

    private FixedBackground background;
    private Sprite mBtnRight;
    private Sprite mBtnLeft;
    private Sprite mBtnDown;
    private Sprite mBtnRotate;
    private Texture backgroundTexture;
    private Texture cellTexture;
    private Texture btnRight;
    private Texture btnLeft;
    private Texture btnDown;
    private Texture btnRotate;

    private static float sSceneWidth = 480f;
    private static float sSceneHeight = 320f;

    private Board mBoard;

    public GameScene() {
        super(2, 10);

        TextureAtlas atlas = new TextureAtlas();
        backgroundTexture = new Texture("background.png");
        cellTexture = new Texture("cell.png");
        btnRight  = new Texture("btn_right.png");
        btnLeft   = new Texture("btn_left.png");
        btnDown   = new Texture("btn_down.png");
        btnRotate = new Texture("btn_rotate.png");
        atlas.insert(backgroundTexture);
        atlas.insert(cellTexture);
        atlas.insert(btnRight);
        atlas.insert(btnLeft);
        atlas.insert(btnDown);
        atlas.insert(btnRotate);
        atlas.complete();

        setBackground(background = new FixedBackground(backgroundTexture));

        float bW = sSceneHeight * 0.69f;
        float bH = sSceneHeight * 0.75f;
        float bX = sSceneWidth  * 0.37f;
        float bY = sSceneHeight * 0.2f;
        mBoard = new Board(bX, bY, bW, bH);
        mBoard.setTexture(cellTexture);
        mBoard.show();
        add(0, mBoard);

        float btnSize = sSceneHeight * 0.2f;
        mBtnLeft   = new Sprite(sSceneWidth * 0.05f, sSceneHeight * 0.4f, btnSize, btnSize);
        mBtnRight  = new Sprite(sSceneWidth * 0.21f, sSceneHeight * 0.7f, btnSize, btnSize);
        mBtnDown   = new Sprite(sSceneWidth * 0.21f, sSceneHeight * 0.2f, btnSize * 0.8f, btnSize * 0.8f);
        mBtnRotate = new Sprite(sSceneWidth * 0.9f, sSceneHeight * 0.5f, btnSize, btnSize);

        mBtnLeft.setTexture(btnLeft);
        mBtnRight.setTexture(btnRight);
        mBtnDown.setTexture(btnDown);
        mBtnRotate.setTexture(btnRotate);

        add(1, mBtnLeft);
        add(1, mBtnRight);
        add(1, mBtnDown);
        add(1, mBtnRotate);
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
