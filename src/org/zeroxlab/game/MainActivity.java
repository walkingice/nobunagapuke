package org.zeroxlab.game;

import com.stickycoding.rokon.DrawPriority;
import com.stickycoding.rokon.RokonActivity;

public class MainActivity extends RokonActivity {

	public static final float GAME_WIDTH = 480f;
	public static final float GAME_HEIGHT = 320f;

        public static final int IN_MENU = 0;
        public static final int IN_GAME = 1;
        private static int sState = IN_MENU;

        private static MenuScene mMenuScene;
	private static GameScene mGameScene;

	public void onCreate() {
		debugMode();
		forceFullscreen();
		forceLandscape();
		setGameSize(GAME_WIDTH, GAME_HEIGHT);
		setDrawPriority(DrawPriority.PRIORITY_VBO);
		setGraphicsPath("textures/");
		createEngine();
	}

	public void onLoadComplete() {
            mGameScene = new GameScene();
            mMenuScene = new MenuScene();
            goMenu();
	}

        public void goGame() {
	    setScene(mGameScene);
            sState = IN_GAME;
        }

        public void goMenu() {
            setScene(mMenuScene);
            sState = IN_MENU;
        }
}
