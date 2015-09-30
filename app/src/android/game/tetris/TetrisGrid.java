package android.game.tetris;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class TetrisGrid implements ITetrisConstants  {

    //false when empty-true when occupied
    private boolean[] mCells;

    private CallbackGrid mCallback;
    private boolean mDirty = false;

    public TetrisGrid(boolean[] cells, CallbackGrid callback) {
        mCells = cells;
        mCallback = callback;
        init();
    }

    public void init() {
        for(int i=0;i<mCells.length;i++)
        {
            mCells[i]=false;
        }

        mCallback.onCellUpdated();
    }

    public int getColumn(int index) {
        if(index<0) {
            return -((Math.abs(index)%PLAYFIELD_COLS) + 1);
        } else {
            return (index%PLAYFIELD_COLS);
        }
    }

    public int getRow(int index) {
        if(index < 0) {
            return -((Math.abs(index)/PLAYFIELD_COLS) + 1);
        } else {
            return (index/PLAYFIELD_COLS);
        }
    }

    public boolean IsCellValid( int index ) {
        return (index>=0 && index<mCells.length);
    }

    public boolean IsCellFree(int index) {
        if(IsCellValid(index)) {
            return !mCells[index];
        } else {
            return false;
        }
    }

    private boolean checkForRunOff(int[] from, int[] to) {
        int[] testArray = to.clone();
        for (int i = testArray.length-1; i >= 0; i--) {
            testArray[i] -= testArray[0];
            testArray[i] += START_CELL;
        }

        //normalize the test array to test rowDiff
        for (int i = 0; i < to.length; i++) {
            if(getRow(to[i]) - getRow(to[0]) != getRow(testArray[i]) - getRow(testArray[0]))
                return false;
        }
        return true;
    }

    public boolean tryToMoveCells(int[] from, int[] to) {
        mDirty = false;
        //test grid
        if(!checkForRunOff(from,to)) {
            return false;
        }

        boolean validMove = false;
        for (int i = 0; i < to.length; i++) {
            boolean cellAboveGrid = to[i] < 0; //can happen on init
            if(!IsInArray(to[i], from)) {
                if(IsCellFree(to[i]) || cellAboveGrid) {
                    validMove = true;
                } else {
                    return false;
                }
            }
        }

        //write to grid
        if(validMove) {
            for (int i = 0; i < from.length; i++) {
                if(IsCellValid(from[i])) {
                    mCells[from[i]] = false;
                    mDirty = true;
                }
            }

            for (int i = 0; i < to.length; i++) {
                boolean cellAboveGrid = to[i] < 0; //can happen on init
                if(!cellAboveGrid) {
                    mCells[to[i]] = true;
                    mDirty = true;
                }
            }
        }

        if (mDirty) {
            mDirty = false;
            mCallback.onCellUpdated();
        }

        return validMove;
    }

    public int update() {
        int points = 0;
        mDirty = false;
        for (int row =  PLAYFIELD_ROWS-1; row >= 0; row--) {
            if(CheckRowForSame(row, false)) {
                break;
            }
            if(CheckRowForSame(row, true)) {
                points++;
                SetAllRowTo(row, false);
                MakeGridCollapse(row-1);
                row++; //we collapsed grid onto this row so we need to check it again by cancelling the upcomming decrement
            }
        }

        if (mDirty) {
            mCallback.onCellUpdated();
            mDirty = false;
        }
        return points;
    }

    private void MakeGridCollapse(int row) {
        for (int r =  row; r >= 0; r--) {
            if(CheckRowForSame(r, false)) {
                break;
            }

            ShiftRowBy(r,C_DOWN);
            SetAllRowTo(r, false);
        }
    }

    private void ShiftRowBy(int row, int down) {
        int index;
        for (int i = 0; i < PLAYFIELD_COLS; i++) {
            index = (row*PLAYFIELD_COLS)+i;
            mCells[index+ down] = mCells[index];
        }

        mDirty = true;
    }

    private void SetAllRowTo(int row, boolean b) {
        for (int i = 0; i < PLAYFIELD_COLS; i++) {
            mCells[(row*PLAYFIELD_COLS)+i] = b;
        }

        mDirty = true;
    }

    private boolean CheckRowForSame(int row, boolean b) {
        for (int i = 0; i < PLAYFIELD_COLS; i++) {
            if( mCells[(row*PLAYFIELD_COLS)+i] != b ) {
                return false;
            }
        }
        return true;
    }

    /* from ArrayTool*/
    public static int getMin(int[] array) {
        int min = array[0];
        for (int i = 1; i < array.length; i++) {
            if(min > array[i])
                min = array[i];
        }
        return min;
    }

    public static int getMax(int[] array) {
        int max = array[0];
        for (int i = 1; i < array.length; i++) {
            if(max < array[i])
                max = array[i];
        }
        return max;
    }

    public static boolean IsInArray(int value, int[] array) {
        for (int i = 0; i < array.length; i++) {
            if(value == array[i])
                return true;
        }
        return false;
    }

    interface CallbackGrid {
        public void onCellUpdated();
    }
}
