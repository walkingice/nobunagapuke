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
import com.stickycoding.rokon.audio.RokonMusic;

public class MenuScene extends Scene {

    public final static String sMenuMusic = "audio/forgotten_hero.mp3";

    private static float sSceneWidth = 480f;
    private static float sSceneHeight = 320f;
    private FixedBackground background;
    private Texture menuPuke;
    private Texture backTexture;
    private Sprite mPuke;

    public MenuScene() {
        super(1, 3);

        TextureAtlas atlas = new TextureAtlas();
        backTexture = new Texture("menu_background.png");
        menuPuke    = new Texture("puke.png");
        atlas.insert(backTexture);
        atlas.insert(menuPuke);
        atlas.complete();

        setBackground(background = new FixedBackground(backTexture));

        float bX = sSceneWidth  * 0.38f;
        float bY = sSceneHeight * 0.6f;;
        float bW = sSceneHeight * 0.64f;
        float bH = sSceneHeight * 0.2f;
        mPuke = new Sprite(bX, bY, bW, bH);
        mPuke.setTexture(menuPuke);
        mPuke.setTouchable();
        add(0, mPuke);
    }

    @Override
    public void onGameLoop() {
    }

    @Override
    public void onTouchDown(Drawable object, float x, float y, MotionEvent event, int pointerCount, int pointerId) {
        if (object == mPuke) {
            MainActivity main = (MainActivity)super.activity;
            main.goGame();
        }
    }

    @Override
    public void onPause() {
        stopMusic();
    }

    @Override
    public void onResume() {
        playMusic();
    }

    @Override
    public void onReady() {
    }

    @Override
    protected void onSetScene() {
        super.onSetScene();
        playMusic();
    }

    @Override
    protected void onEndScene() {
        stopMusic();
    }

    private void playMusic() {
        boolean loop = true;
        RokonMusic.play(sMenuMusic, loop);
    }

    private void stopMusic() {
        RokonMusic.stop();
    }
}
