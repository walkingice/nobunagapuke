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
    private Texture bobTexture;

    public GameScene() {
        super(1, 1);

        TextureAtlas atlas = new TextureAtlas();
        backgroundTexture = new Texture("background.png");
        bobTexture = new Texture("bob.png");
        atlas.insert(backgroundTexture);
        atlas.insert(bobTexture);
        atlas.complete();

        setBackground(background = new FixedBackground(backgroundTexture));

        // Create Sprite
        bob = new Sprite(100, 220, bobTexture.getWidth(), bobTexture.getHeight());
        bob.setTexture(bobTexture);

        // Add the Bob sprite to the first layer.
        add(0, bob);
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

        // Here we'll basically just make Bob follow your finger.
        bob.x = x - (bobTexture.getWidth()/2);
        bob.y = y - (bobTexture.getHeight()/2);
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
