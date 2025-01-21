package com.ngdathd.flappybird;

import com.badlogic.gdx.Game;
import com.ngdathd.flappybird.base.AndroidInterface;
import com.ngdathd.flappybird.base.IosInterface;
import com.ngdathd.flappybird.helpers.AssetLoader;
import com.ngdathd.flappybird.screens.GameScreen;
import com.ngdathd.flappybird.screens.SplashScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    private AndroidInterface androidInterface;
    private IosInterface iosInterface;

    private GameScreen gameScreen;

    public void setAndroidInterface(AndroidInterface androidInterface) {
        this.androidInterface = androidInterface;
    }

    public void setIosInterface(IosInterface iosInterface) {
        this.iosInterface = iosInterface;
    }

    @Override
    public void create() {
        setScreen(new SplashScreen(this));
    }

    public void showGameScreen() {
        if (gameScreen == null) {
            gameScreen = new GameScreen(androidInterface, iosInterface);
        }
        setScreen(gameScreen);
    }

    @Override
    public void dispose() {
        AssetLoader.INSTANCE.dispose();
        super.dispose();
    }
}
