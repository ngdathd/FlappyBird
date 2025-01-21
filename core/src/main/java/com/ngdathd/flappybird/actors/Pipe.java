package com.ngdathd.flappybird.actors;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Scaling;
import com.ngdathd.flappybird.common.Constants;
import com.ngdathd.flappybird.helpers.AssetLoader;

public class Pipe extends Group {
    private static final float IMG_PIPE_WIDTH = 52f;
    private static final float IMG_PIPE_HEIGHT = 320f;

    private final float widthPipeImg;
    private final float heightPipeImg;

    private final Vector2 posPipeUp;
    private final Vector2 posPipeDown;

    private final Image pipeUp;
    private final Image pipeDown;

    private final float heightSpace;
    private final float pipeSpace;

    private final float initialPosX;
    private final float yMin;
    private final float yMax;

    private final float scrollSpeed;
    private boolean isRunning;
    private boolean scored; // Thêm biến này để kiểm tra xem điểm đã được tính hay chưa

    public Pipe(Stage stage, Land land, float indexPipe) {
        Skin skin = AssetLoader.INSTANCE.getSkin();

        float worldWidth = stage.getViewport().getWorldWidth();
        float worldHeight = stage.getViewport().getWorldHeight();

        widthPipeImg = IMG_PIPE_WIDTH * worldWidth / Constants.IMG_BG_WIDTH;
        heightPipeImg = widthPipeImg * IMG_PIPE_HEIGHT / IMG_PIPE_WIDTH;

        // Tung độ của Land, có thể hiểu là mặt đất
        float landHeight = land.getCollideY();

        // Chiều cao tối thiểu của ống phải hiện ra
        float heightPipeMin = heightPipeImg / 7f;

        // Khoảng trống giữa 2 ống trên dưới, Bird bay qua sẽ được tính điểm
        heightSpace = 45f * worldWidth / Constants.BASELINE_WORLD_WIDTH;

        /*
         * y là tung độ (cạnh dưới) của pipeUp - ống ở bên trên, y sẽ có giá trị ngẫu nhiên
         *
         * Điều kiện 1: cạnh trên pipeUp luôn nằm trên cạnh trên màn hình
         * (y + heightPipeImg) là tung độ cạnh trên của pipeUp
         * (y + heightPipeImg) >= worldHeight
         * <=> y >= worldHeight - heightPipeImg
         *
         * Điều kiện 2: pipeUp luôn hiện ra tối thiểu heightPipeMin
         * (y + heightPipeMin) là tung độ điểm trên tối thiểu của pipeUp
         * (y + heightPipeMin) <= worldHeight
         * <=> y <= worldHeight - heightPipeMin
         *
         * Điều kiện 3: cạnh dưới pipeDown luôn nằm dưới tung độ của Land
         * (y - heightSpace - heightPipeImg) là tung độ (cạnh dưới) của pipeDown
         * (y - heightSpace - heightPipeImg) <= landHeight
         * <=> y <= landHeight + heightSpace + heightPipeImg
         *
         * Điều kiện 4: pipeDown luôn hiện ra tối thiểu heightPipeMin
         * (y - heightSpace - heightPipeMin) là tung độ điểm dưới tối thiểu của pipeUp
         * (y - heightSpace - heightPipeMin) >= landHeight
         * <=> y >= landHeight + heightSpace + heightPipeMin
         *
         * */

        // Kết hợp điều kiện 1 và 4, chọn điều kiện 4 vì có yMin nhỏ hơn
        yMin = landHeight + heightSpace + heightPipeMin;
        // Kết hợp điều kiện 2 và 3, chọn điều kiện 2 vì có yMax lớn hơn
        yMax = worldHeight - heightPipeMin;

        // Khoảng cách giữa 2 ống liền nhau
        pipeSpace = 49f * worldWidth / Constants.BASELINE_WORLD_WIDTH;

        initialPosX = worldWidth / 3f - widthPipeImg / 2f + worldWidth
            + indexPipe * (pipeSpace + widthPipeImg);
        float y = MathUtils.random(yMin, yMax);

        posPipeUp = new Vector2(initialPosX, y);
        posPipeDown = new Vector2(initialPosX, y - heightSpace - heightPipeImg);

        pipeUp = new Image(skin, "pipe-up");
        pipeUp.setSize(widthPipeImg, heightPipeImg);
        pipeUp.setScaling(Scaling.fit);
        pipeUp.setPosition(posPipeUp.x, posPipeUp.y);
        addActor(pipeUp);

        pipeDown = new Image(skin, "pipe-down");
        pipeDown.setSize(widthPipeImg, heightPipeImg);
        pipeDown.setScaling(Scaling.fit);
        pipeDown.setPosition(posPipeDown.x, posPipeDown.y);
        addActor(pipeDown);

        scrollSpeed = Constants.SCROLL_SPEED * worldWidth / Constants.BASELINE_WORLD_WIDTH;
        isRunning = false;
        scored = false;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (isRunning) {
            posPipeUp.x += scrollSpeed * delta;
            posPipeDown.x += scrollSpeed * delta;

            if (posPipeUp.x + pipeUp.getWidth() <= 0) {
                posPipeUp.x = (pipeSpace + widthPipeImg) * 2 + pipeSpace;
                posPipeUp.y = MathUtils.random(yMin, yMax);

                posPipeDown.x = (pipeSpace + widthPipeImg) * 2 + pipeSpace;
                posPipeDown.y = posPipeUp.y - heightSpace - heightPipeImg;

                scored = false;
            }

            pipeUp.setPosition(posPipeUp.x, posPipeUp.y);
            pipeDown.setPosition(posPipeDown.x, posPipeDown.y);
        }
    }

    public void startPipeRun() {
        isRunning = true;  // Bắt đầu di chuyển
    }

    public void stopPipeRun() {
        isRunning = false;  // Dừng di chuyển
    }

    public void resetPipe() {
        posPipeUp.x = initialPosX;
        posPipeUp.y = MathUtils.random(yMin, yMax);

        posPipeDown.x = initialPosX;
        posPipeDown.y = posPipeUp.y - heightSpace - heightPipeImg;

        pipeUp.setPosition(posPipeUp.x, posPipeUp.y);
        pipeDown.setPosition(posPipeDown.x, posPipeDown.y);

        scored = false;
    }

    public Rectangle getBoundingRectangleUp() {
        return new Rectangle(posPipeUp.x, posPipeUp.y, pipeUp.getWidth(), pipeUp.getHeight());
    }

    public Rectangle getBoundingRectangleDown() {
        return new Rectangle(posPipeDown.x, posPipeDown.y, pipeDown.getWidth(), pipeDown.getHeight());
    }

    public boolean isScored() {
        return scored;
    }

    public void setScored(boolean scored) {
        this.scored = scored;
    }

    public float getScoreX() {
        return posPipeUp.x + widthPipeImg / 2f;
    }
}
