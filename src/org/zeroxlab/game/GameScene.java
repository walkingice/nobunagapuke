package org.zeroxlab.game;

import android.view.MotionEvent;
import android.os.SystemClock;

import com.stickycoding.rokon.Scene;
import com.stickycoding.rokon.Sprite;
import com.stickycoding.rokon.background.FixedBackground;
import com.stickycoding.rokon.TextureAtlas;
import com.stickycoding.rokon.Texture;
import com.stickycoding.rokon.Drawable;
import com.stickycoding.rokon.Rokon;

import android.game.tetris.ITetrisConstants;
import android.game.tetris.TetrisGame;

public class GameScene extends Scene implements TetrisGame.GameCallback, ITetrisConstants {

    private FixedBackground background;
    private Sprite mBtnRight;
    private Sprite mBtnLeft;
    private Sprite mBtnDown;
    private Sprite mBtnRotate;
    private Sprite mBorder;
    private Sprite mNext;
    private Texture backgroundTexture;
    private Texture cellTexture;
    private Texture btnRight;
    private Texture btnLeft;
    private Texture btnDown;
    private Texture btnRotate;
    private Texture borderNormal;
    private Texture borderPuke;
    private Texture nextS;
    private Texture next2;
    private Texture nextL;
    private Texture nextJ;
    private Texture nextT;
    private Texture nextI;
    private Texture nextO;
    private Texture nextNull;

    private static float sSceneWidth = 480f;
    private static float sSceneHeight = 320f;

    private static boolean[] mCells = new boolean[PLAYFIELD_COLS * PLAYFIELD_ROWS];

    private boolean mPuking = false;
    private static long mPukeTime;
    private static long mNow;
    final private static long sPukePeriod = 600;

    private Board mBoard;
    private TetrisGame mGame;

    public GameScene() {
        super(2, 10);

        TextureAtlas atlas = new TextureAtlas();
        backgroundTexture = new Texture("background.png");
        cellTexture = new Texture("cell.png");
        btnRight  = new Texture("btn_right.png");
        btnLeft   = new Texture("btn_left.png");
        btnDown   = new Texture("btn_down.png");
        btnRotate = new Texture("btn_rotate.png");
        borderNormal = new Texture("border_normal.png");
        borderPuke   = new Texture("border_puke.png");
        nextS = new Texture("next_a.png");
        next2 = new Texture("next_b.png");
        nextL = new Texture("next_c.png");
        nextJ = new Texture("next_d.png");
        nextT = new Texture("next_e.png");
        nextI = new Texture("next_f.png");
        nextO = new Texture("next_g.png");
        nextNull = new Texture("next_null.png");
        atlas.insert(backgroundTexture);
        atlas.insert(cellTexture);
        atlas.insert(btnRight);
        atlas.insert(btnLeft);
        atlas.insert(btnDown);
        atlas.insert(btnRotate);
        atlas.insert(borderNormal);
        atlas.insert(borderPuke);
        atlas.insert(nextS);
        atlas.insert(next2);
        atlas.insert(nextL);
        atlas.insert(nextJ);
        atlas.insert(nextT);
        atlas.insert(nextI);
        atlas.insert(nextO);
        atlas.insert(nextNull);
        atlas.complete();

        setBackground(background = new FixedBackground(backgroundTexture));

        float bX = sSceneWidth  * 0.38f;
        float bY = 0;
        float bW = sSceneHeight * 0.64f;
        float bH = sSceneHeight;
        mBorder = new Sprite(bX, bY, bW, bH);
        mBorder.setTexture(borderNormal);
        add(1, mBorder);
        bX = bX + bW * 0.065f;
        bY = bY + bH * 0.28f;
        bW = bW * 0.87f;
        bH = bH * 0.66f;
        mBoard = new Board(bX, bY, bW, bH, mCells);
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

        mBtnLeft.setTouchable();
        mBtnRight.setTouchable();
        mBtnDown.setTouchable();
        mBtnRotate.setTouchable();

        add(1, mBtnLeft);
        add(1, mBtnRight);
        add(1, mBtnDown);
        add(1, mBtnRotate);

        mNext = new Sprite(sSceneWidth * 0.9f, sSceneHeight * 0.1f, btnSize * 1.2f, btnSize*1.2f);
        mNext.setTexture(nextNull);
        add(1, mNext);

        mGame = new TetrisGame(Rokon.getActivity(), this);
    }

    @Override
    public void onGameLoop() {
        if (mPuking) {
            mNow = SystemClock.uptimeMillis();
            if ((mNow - mPukeTime) > sPukePeriod) {
                mPuking = false;
                mBorder.setTexture(borderNormal);
            }
        }

        mGame.runRound();
    }

    @Override
    public void onTouchDown(Drawable object, float x, float y, MotionEvent event, int pointerCount, int pointerId) {
        if (object == mBtnRotate) {
            mGame.actionRotate();
        } else if (object == mBtnRight){
            mGame.actionRight();
        } else if (object == mBtnLeft){
            mGame.actionLeft();
        } else if (object == mBtnDown) {
            mGame.actionFall();
        }
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
        mGame.setGameFocus(false);
    }

    @Override
    public void onResume() {
        mGame.setGameFocus(true);
    }

    @Override
    public void onReady() {
        mGame.setGameFocus(true);
    }

    public void onGameOver() {
        mNext.setTexture(nextNull);
    }

    public void onRedraw() {
    }

    public void onShapeChanged(int current, int next) {
        mPuking = true;
        mPukeTime = SystemClock.uptimeMillis();
        mBorder.setTexture(borderPuke);
        if (next == TYPE_LONG) {
            mNext.setTexture(nextL);
        } else if (next == TYPE_BL) {
            mNext.setTexture(nextJ);
        } else if (next == TYPE_L) {
            mNext.setTexture(nextL);
        } else if (next == TYPE_SQUARE) {
            mNext.setTexture(nextO);
        } else if (next == TYPE_S) {
            mNext.setTexture(nextS);
        } else if (next == TYPE_BS) {
            mNext.setTexture(next2);
        } else if (next == TYPE_T) {
            mNext.setTexture(nextT);
        } else {
            mNext.setTexture(nextNull);
        }
    }

    public void onCellUpdated(boolean[] cells, int rows, int cols) {
        for (int i = 0; i < cells.length; i++) {
            mCells[i] = cells[i];
        }
    }
}
