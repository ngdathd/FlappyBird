package com.ngdathd.flappybird.actors;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Scaling;
import com.ngdathd.flappybird.common.Constants;
import com.ngdathd.flappybird.helpers.AssetLoader;

public class Land extends Group {
    private static final float IMG_LAND_WIDTH = 333f;
    private static final float IMG_LAND_HEIGHT = 111f;

    private final float overlapLandImg;  // Chờm lên ảnh phía trước tạo sự liền mạch

    private final Vector2 posLandImg1;
    private final Vector2 posLandImg2;

    private final Image landImg1;
    private final Image landImg2;

    private final float scrollSpeed;  // Tốc độ di chuyển của ảnh
    private boolean isRunning;  // Biến để điều khiển di chuyển

    public Land(Stage stage) {
        Skin skin = AssetLoader.INSTANCE.getSkin();

        float worldWidth = stage.getViewport().getWorldWidth();
        float worldHeight = stage.getViewport().getWorldHeight();
        float imgBgHeight = Constants.IMG_BG_HEIGHT * worldWidth / Constants.IMG_BG_WIDTH;
        float gutterTopY = MathUtils.ceil((worldHeight - imgBgHeight) / 2f);

        float widthLandImg = IMG_LAND_WIDTH * worldWidth / Constants.IMG_BG_WIDTH;
        float heightLandImg = IMG_LAND_HEIGHT * widthLandImg / IMG_LAND_WIDTH;

        float posY = imgBgHeight / 5f + gutterTopY - heightLandImg
            + 4.5f * worldWidth / Constants.BASELINE_WORLD_WIDTH;

        overlapLandImg = 10f * worldWidth / Constants.BASELINE_WORLD_WIDTH;

        posLandImg1 = new Vector2(0, posY);
        posLandImg2 = new Vector2(widthLandImg - overlapLandImg, posY);

        Image landImgGutter = new Image(skin, "land");
        landImgGutter.setSize(widthLandImg, heightLandImg);
        landImgGutter.setScaling(Scaling.fit);
        landImgGutter.setPosition(
            0,
            posY - heightLandImg + 2 * worldWidth / Constants.BASELINE_WORLD_WIDTH
        );
        landImgGutter.setOrigin(widthLandImg / 2f, heightLandImg / 2f);
        landImgGutter.setRotation(-180f);
        addActor(landImgGutter);

        landImg1 = new Image(skin, "land");
        landImg1.setSize(widthLandImg, heightLandImg);
        landImg1.setScaling(Scaling.fit);
        landImg1.setPosition(posLandImg1.x, posLandImg1.y);
        addActor(landImg1);

        landImg2 = new Image(skin, "land");
        landImg2.setSize(widthLandImg, heightLandImg);
        landImg2.setScaling(Scaling.fit);
        landImg2.setPosition(posLandImg2.x, posLandImg2.y);
        addActor(landImg2);

        scrollSpeed = Constants.SCROLL_SPEED * worldWidth / Constants.BASELINE_WORLD_WIDTH;
        isRunning = true;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (isRunning) {
            /*
             * posLandImg1, posLandImg2 di chuyển ngang với vận tốc không đổi bằng scrollSpeed
             * Sau mỗi lần act() được gọi, posLandImg1.x tăng thêm scrollSpeed * delta
             *
             * Ví dụ: Tọa độ ban đầu của posLandImg1 là (100, 200), scrollSpeed là 50 (đơn vị là pixel/giây).
             * delta là 0.016 giây (tương ứng với khoảng 1/60 giây, tốc độ khung hình điển hình của trò chơi là 60 FPS).
             * posLandImg1.x = posLandImg1.x + scrollSpeed * delta
             * posLandImg1.x = 100 + 50 * 0.016 = 100.8
             * Sau khung hình đầu tiên, tọa độ mới của posLandImg1 là (100.8, 200).
             *
             * */
            posLandImg1.x += scrollSpeed * delta;
            posLandImg2.x += scrollSpeed * delta;

            // Kiểm tra nếu ảnh đã cuộn hết sang trái
            if (posLandImg1.x + landImg1.getWidth() <= overlapLandImg) {
                posLandImg1.x = posLandImg2.x + landImg2.getWidth() - overlapLandImg;
            }
            if (posLandImg2.x + landImg2.getWidth() <= overlapLandImg) {
                posLandImg2.x = posLandImg1.x + landImg1.getWidth() - overlapLandImg;
            }

            // Cập nhật vị trí thực tế của các ảnh
            landImg1.setPosition(posLandImg1.x, posLandImg1.y);
            landImg2.setPosition(posLandImg2.x, posLandImg2.y);
        }
    }

    public void startLandRun() {
        isRunning = true;  // Bắt đầu di chuyển
    }

    public void stopLandRun() {
        isRunning = false;  // Dừng di chuyển
    }

    public float getCollideY() {
        return landImg1.getY() + landImg1.getHeight();
    }
}
