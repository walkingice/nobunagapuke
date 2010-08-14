package android.game.tetris;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.KeyEvent;
import android.view.View;

public class TetrisView extends View implements ITetrisConstants, TetrisGame.GameCallback{

    private TetrisGame mGame;

    //members
    private Paint mPaint;		//paint object to use in draws.

    private boolean mRun = true;

    private boolean[] mCells;
    private int mGridTileW  = 0;
    private int mGridTileH  = 0;
    private int mGridLeft   = 0;
    private int mGridTop    = 0;
    private int mGridRight  = 0;
    private int mGridBottom = 0;

    private String mNextType;

    /**
     * Constructor
     *
     * Init View object
     * Instantiate objects
     *
     * @param context - param needed for View superclass
     */
    public TetrisView(Activity context) {
        //init view obj
        super(context);
        setBackgroundColor(Color.BLACK);
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();

        //inst objs
        mCells = TetrisGame.makeCells();
        mGame = new TetrisGame(context, this);
        mPaint = new Paint();
    }

    public  void setGameFocus(boolean hasFocus) {
        mGame.setGameFocus(hasFocus);
    }

    public void setGridDimension(int w, int h) {
        if( PLAYFIELD_USE_MARGINS ) {
            mGridLeft = MARGIN_LEFT;
            mGridTop  = MARGIN_TOP;
            w -= mGridLeft + MARGIN_RIGHT;
            h -= mGridTop  + MARGIN_BOTTOM;
        } else {
            mGridTop  = 0;
            mGridLeft = 0;
        }

        int cols = PLAYFIELD_COLS;
        int rows = PLAYFIELD_ROWS;
        mGridTileW = w / cols;
        mGridTileH = h / rows;

        mGridRight  = mGridLeft + (mGridTileW * PLAYFIELD_COLS);
        mGridBottom = mGridTop  + (mGridTileH * PLAYFIELD_ROWS);

    }

    public void manageScoreSave(boolean saveToDB, String player) {
        mGame.manageScoreSave(saveToDB, player);
    }

    public void restartGame() {
        mGame.restartGame();
    }

    public void quitGame() {
        mGame.quitGame();
    }

    /**
     * recalculate grid pixel size
     *
     * @note this WILL be called at least once on init
     *
     * @param are all fired by environment
     */
    @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            setGridDimension(w, h);
            super.onSizeChanged(w, h, oldw, oldh);
            mGame.gameStart();
        }

    /**
     * Handle key presses (make sure view is focusable)
     *
     * @param are all fired by environment
     */
    @Override
        public boolean onKeyDown(int keyCode, KeyEvent event) {

            //if DISREGARD_MULTIPLE_KEYPRESSED then check if its the first press of this key
            if(DISREGARD_MULTIPLE_KEYPRESSED && event.getRepeatCount() < 1)
            {
                switch(keyCode)
                {
                    case KeyEvent.KEYCODE_DPAD_LEFT:
                    case KeyEvent.KEYCODE_4:
                        {
                            mGame.actionLeft();
                            break;
                        }
                    case KeyEvent.KEYCODE_DPAD_RIGHT:
                    case KeyEvent.KEYCODE_6:
                        {
                            mGame.actionRight();
                            break;
                        }
                    case KeyEvent.KEYCODE_DPAD_UP:
                    case KeyEvent.KEYCODE_2:
                        {
                            mGame.actionRotateL();
                            break;
                        }
                    case KeyEvent.KEYCODE_DPAD_DOWN:
                    case KeyEvent.KEYCODE_8:
                        {
                            mGame.actionRotateR();
                            break;
                        }
                    case KeyEvent.KEYCODE_5:
                    case KeyEvent.KEYCODE_ENTER:
                    case KeyEvent.KEYCODE_SPACE:
                    case KeyEvent.KEYCODE_DPAD_CENTER:
                        {
                            mGame.actionFall();
                            break;
                        }
                }
            }
            return super.onKeyDown(keyCode, event);
        }

    /**
     * Paint game
     */
    @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            //paint elements
            paintGrid(canvas, mPaint);

            int right = getRight();
            int top   = getTop();
            int x = right - HUD_SCORE_TEXT_OFFSET;
            int y = top + MARGIN_TOP+HUD_SCORE_Y_START;
            mPaint.setTextAlign(Paint.Align.RIGHT);
            mPaint.setColor(HUD_SCORE_WORD_COLOR);
            canvas.drawText("Score", x, y, mPaint);
            y += HUD_SCORE_INTERLINE;
            mPaint.setColor(HUD_SCORE_NUM_COLOR);
            canvas.drawText(""+ mGame.getCurrentScore() , x, y, mPaint);

            /* Draw Next Shape */
            y = MARGIN_TOP + HUD_NEXT_WORD_Y_START;
            mPaint.setTextAlign(Paint.Align.RIGHT);
            mPaint.setColor(HUD_NEXT_WORD_COLOR);
            canvas.drawText("Next:" + mNextType, x, y, mPaint);
        }

    private void paintGrid(Canvas canvas, Paint paint) {
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(mGridLeft, mGridTop, mGridRight, mGridBottom, paint);

        //paint elems
        int l,t,r,b;
        for(int i=0;i<mCells.length;i++) {
            l = mGridLeft + (i % PLAYFIELD_COLS) * mGridTileW;
            t = mGridTop  + (i / PLAYFIELD_COLS) * mGridTileH;
            r = l + mGridTileW;
            b = t + mGridTileH;
            paint.setColor(Color.YELLOW);
            paint.setStyle((mCells[i]) ? Paint.Style.FILL: Paint.Style.STROKE);
            canvas.drawRect(l, t, r, b, paint);
        }

        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(mGridLeft, mGridTop, mGridRight, mGridBottom, paint);
    }

    public void onGameOver() {
        android.util.Log.i("Ouch", "Game over");
    }

    public void onRedraw() {
        this.postInvalidate();
    }

    public void onShapeChanged(int current, int next) {
        if (next == TYPE_LONG) {
            mNextType = "I";
        } else if (next == TYPE_BL) {
            mNextType = "J";
        } else if (next == TYPE_L) {
            mNextType = "L";
        } else if (next == TYPE_SQUARE) {
            mNextType = "O";
        } else if (next == TYPE_S) {
            mNextType = "S";
        } else if (next == TYPE_BS) {
            mNextType = "2";
        } else if (next == TYPE_T) {
            mNextType = "T";
        } else {
            mNextType = "?";
        }
    }

    public void onCellUpdated(boolean[] cells, int rows, int cols) {
        for (int i = 0; i < rows * cols; i++) {
            mCells[i] = cells[i];
        }
    }
}
