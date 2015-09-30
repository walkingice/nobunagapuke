/*
 * Authored By Julian Chu <walkingice@0xlab.org>
 *
 * Copyright (c) 2009 0xlab.org - http://0xlab.org/
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.zeroxlab.game;

import android.util.Log;

import javax.microedition.khronos.opengles.GL10;
import com.stickycoding.rokon.Rokon;
import com.stickycoding.rokon.GameObject;
import android.game.tetris.ITetrisConstants;

public class Board extends GameObject implements ITetrisConstants{

    private final static int sCols = PLAYFIELD_COLS;
    private final static int sRows = PLAYFIELD_ROWS;
    private static float sBorderWidth = 1f;
    private static float sBorderHeight = 1f;
    private static float sCellWidth  = 1f;
    private static float sCellHeight = 1f;

    private final static float sR = 242f / 255f;
    private final static float sG = 251f / 255f;
    private final static float sB = 157f / 255f;

    private boolean[] mCells;

    private final static int sColorCount = 4;
    private final static int sColorSpace = 3; // R, G, B
    private final static float[][] sColor = new float[sRows * sCols][sColorSpace];
    private final static float[][] sColorTable = {
        {77f/255f, 74f/255f, 81f/255f},
        {48f/255f, 44f/255f, 58f/255f},
        {146f/255f, 146f/255f,67f/255f},
        {150f/255f, 162f/255f, 43f/255f}
    };

    Board(float x, float y, float width, float height, boolean[] cells) {
        super(x, y, width, height);
        this.fill = true;
        this.setRGB(0.8f, 0.2f, 0.3f);
        this.setSize(width, height);
        mCells = cells;
        initColor();
    }

    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);
        sBorderWidth  = width;
        sBorderHeight = height;
        resetCellSize();
    }

    @Override
    public void setWidth(float width) {
        super.setWidth(width);
        sBorderWidth = width;
        resetCellSize();
    }

    @Override
    public void setHeight(float height) {
        super.setHeight(height);
        sBorderHeight = height;
        resetCellSize();
    }

    @Override
    protected void onDrawVBO(GL10 gl) {
        super.onDrawVBO(gl);
        onDrawBoard(gl, true);
    }

    @Override
    protected void onDrawNormal(GL10 gl) {
        super.onDrawNormal(gl);
        onDrawBoard(gl, false);
    }

    protected void onDrawBoard(GL10 gl, boolean drawVBO) {
        float x = getX();
        float y = getY();
        float w = getWidth();
        float h = getHeight();
        boolean drawTexture = (texture != null);
        int count = 0;

        for (int i = 0; i < sCols; i++) {
            for (int j = 0; j < sRows; j++) {
                count = j * sCols + i;
                if (mCells[count] == true) {
                    super.setRGB(sColor[count][0], sColor[count][1], sColor[count][2]);
                    super.setWidth(sCellWidth);
                    super.setHeight(sCellHeight);
                    super.setX(x + sCellWidth  * i);
                    super.setY(y + sCellHeight * j);
                    if (drawVBO) {
                        super.onDrawVBO(gl);
                    } else {
                        super.onDrawNormal(gl);
                    }
                }
            }
        }

        super.setRGB(sR, sG, sB);
        super.setX(x);
        super.setY(y);
        super.setWidth(w);
        super.setHeight(h);
    }

    private void resetCellSize() {
        sCellWidth  = sBorderWidth / sCols;
        sCellHeight = sBorderHeight / sRows;
    }

    /* Fill color to arry in random */
    private void initColor() {
        int pointer = 0;
        for (int i = 0; i < sRows * sCols; i++) {
            pointer = (int)(Math.random() * 4);

            for (int j = 0; j < sColorSpace; j++) {
                sColor[i][j] = sColorTable[pointer][j];
            }
        }
    }
}

