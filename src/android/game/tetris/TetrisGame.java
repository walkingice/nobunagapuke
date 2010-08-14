package android.game.tetris;

import android.app.Activity;
import android.game.score.ScoreManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.KeyEvent;
import android.view.View;

public class TetrisGame implements ITetrisConstants, Runnable, TetrisShape.CallbackShape, TetrisGrid.CallbackGrid {

    //members
    private boolean mHasFocus; 	//gameView is the focus and can be updated with setter below
    private long mNextUpdate; 	//time stamp when next update can be called
    private long mLastGravity;	//allow updates of shape independently from gravity by checking this
    private int mTicks;			//number of ticks that have been calculated
    private int mSeconds;		//number of seconds of game play
    private Activity mActivityHandle; //save reference to activity to be able to quit from here
    private ScoreManager scoreManager;

    private GameCallback mCallback;
    private View mDrawingView;

    //game specific
    private TetrisGrid grid; 			//game play field/grid
    private TetrisShape currentShape;	//current shape controllable by the user
    private int currentAction;			//current game action fired by player

    private Thread  mGameThread;
    private boolean mRun = true;

    private boolean[] mCells;

    private String mType = "";

    /**
     * Constructor
     *
     * Init View object
     * Instantiate objects
     *
     * @param context - param needed for View superclass
     */
    public TetrisGame(Activity context, GameCallback callback) {
        mCallback = callback;
        mCells = makeCells();
        grid = new TetrisGrid(mCells, this);
        currentShape = new TetrisShape(grid, this);
        scoreManager = new ScoreManager(context);

        mGameThread = new Thread(this);
        //initialize
        init();
    }

    public static int getPlayfieldRows() {
        return PLAYFIELD_ROWS;
    }

    public static int getPlayfieldCols() {
        return PLAYFIELD_COLS;
    }

    public static boolean[] makeCells() {
        return new boolean[PLAYFIELD_ROWS*PLAYFIELD_COLS];
    }

    public void gameStart() {
        mGameThread.start();
    }

    /* If you have your own game thread
       call this method at every round */
    public void runRound() {
        update();
        mCallback.onRedraw();
    }

    public void run() {
        while(mRun) {
            try {
                mGameThread.sleep(30);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (mHasFocus) {
                runRound();
            }
        }
    }

    public  void setGameFocus(boolean hasFocus){
        mHasFocus = hasFocus;
    }

    /**
     * Initialize members
     */
    private void init() {

        //initialize members
        currentShape.isInited = false;
        currentAction = ACTION_NONE;
        mNextUpdate = 0;
        mTicks = 0;
        mSeconds = 0;
        mLastGravity = 1;

        grid.init();

        scoreManager.currentScore = 1;
        scoreManager.scoreWasSaved = false;

    }

    public void restartGame() {
        init();
        currentShape.isGameOver = false;
    }


    public void quitGame() {
        mActivityHandle.finish();
    }

    public void actionLeft() {
        currentAction = ACTION_STRAFE_LEFT;
    }

    public void actionRight() {
        currentAction = ACTION_STRAFE_RIGHT;
    }

    public void actionRotateL() {
        currentAction = ACTION_ROTATE_L;
    }

    public void actionRotateR() {
        currentAction = ACTION_ROTATE_R;
    }

    public void actionRotate() {
        actionRotateR();
    }

    public void actionFall() {
        currentAction = ACTION_MAKE_FALL;
    }

    /**
     * Update user actions
     * Update engine actions (gravity and line check)
     */
    public void update() {
        long time = System.currentTimeMillis();

        if( mHasFocus )
        {
            //manage gameOver
            if(currentShape.isGameOver)
            {
                mRun = false;
                mCallback.onGameOver();
            }
            //normal state
            else if( time > mNextUpdate )
            {
                mNextUpdate = time + 1000 / FRAME_RATE;
                mTicks++;
                currentShape.update(currentAction);
                currentAction = ACTION_NONE;
                if(time - mLastGravity > GRAVITY_RATE || currentShape.IsFalling())
                {
                    mLastGravity = time;
                    boolean shapeIsLocked = currentShape.addGravity();
                    if(shapeIsLocked)
                    {
                        int points = grid.update();
                        if(points != 0)
                            scoreManager.currentScore += points;
                    }
                }

                if(mTicks/FRAME_RATE > mSeconds)
                {
                    mSeconds = mTicks/FRAME_RATE;
                }
            }
        }
        else
        {
            //if paused you don't want to rush into a loop when exiting pause
            mNextUpdate = time + (1000 / OUT_OF_PAUSE_DELAY);
        }
        return;
    }

    public int getCurrentScore() {
        return scoreManager.currentScore;
    }

    public void manageScoreSave(boolean saveToDB, String player) {
        scoreManager.scoreWasSaved = true;
        if(saveToDB && player != null )
            scoreManager.saveScoreIfTopScore(player);
    }

    public String getShape() {
        return mType;
    }

    public void onShapeChanged(int current, int next) {
        mCallback.onShapeChanged(current, next);
    }

    public void onCellUpdated() {
        mCallback.onCellUpdated(mCells, PLAYFIELD_ROWS, PLAYFIELD_COLS);
    }

    public interface GameCallback {
        public void onGameOver();
        public void onRedraw();
        public void onShapeChanged(int current, int next);
        public void onCellUpdated(boolean[] cells, int rows, int cols);
    }
}
