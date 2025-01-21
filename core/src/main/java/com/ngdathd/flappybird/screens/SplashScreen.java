package com.ngdathd.flappybird.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.ngdathd.flappybird.Main;
import com.ngdathd.flappybird.base.BaseScreen;
import com.ngdathd.flappybird.helpers.AssetLoader;

public class SplashScreen extends BaseScreen {
    private final AssetLoader assetLoader;
    private final Main game;

    public SplashScreen(Main game) {
        assetLoader = AssetLoader.INSTANCE;
        this.game = game;
    }

    @Override
    public void show() {
        super.show();
        assetLoader.loadAssets();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (assetLoader.updateLoading()) {
            game.showGameScreen();
            stage.dispose();
        } else {
            // Phần trăm tải thành công
            float progress = assetLoader.getProgressLoading();
            // Vẽ tiến trình tải lên màn hình nếu cần
            Gdx.app.log(SplashScreen.class.getName(), "Loading... " + progress + "%");
        }
    }
}
