package com.ngdathd.flappybird.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class BaseScreen implements Screen {
    private static final float MIN_WORLD_WIDTH = 1080f; // 135.0f, 1080f
    private static final float MIN_WORLD_HEIGHT = 1920f; // 202.5f, 1920f
    private final ExtendViewport viewport;
    protected Stage stage;

    public BaseScreen() {
        // Khởi tạo viewport với các kích thước thế giới tối thiểu được định nghĩa trong Constants
        viewport = new ExtendViewport(MIN_WORLD_WIDTH, MIN_WORLD_HEIGHT);
        viewport.setMaxWorldWidth(MIN_WORLD_WIDTH);
        // Khởi tạo Stage với viewport vừa tạo
        stage = new Stage(viewport);
    }

    @Override
    public void show() {
        // Đặt stage làm bộ xử lý đầu vào hiện tại để nhận các sự kiện đầu vào
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {
        // Cập nhật viewport với kích thước mới
        viewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        // Giải phóng tài nguyên của Stage
        stage.dispose();
    }
}
