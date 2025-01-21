package com.ngdathd.flappybird.actors;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Scaling;
import com.ngdathd.flappybird.common.Constants;
import com.ngdathd.flappybird.helpers.AssetLoader;

public class ScoreSmall extends Group {
    private static final float IMG_SCORE_SMALL_NO_1_WIDTH = 6f;
    private static final float IMG_SCORE_SMALL_WIDTH = 12f;
    private static final float IMG_SCORE_SMALL_HEIGHT = 14f;

    private final Skin skin;

    private final float widthScoreNo1Img;
    private final float widthScoreImg;
    private final float heightScoreImg;

    private final Array<Image> listDigitsImage;
    private final Pool<Image> poolDigitsImage;

    public ScoreSmall(Stage stage) {
        skin = AssetLoader.INSTANCE.getSkin();

        float worldWidth = stage.getViewport().getWorldWidth();

        widthScoreNo1Img = IMG_SCORE_SMALL_NO_1_WIDTH * worldWidth / Constants.IMG_BG_WIDTH;
        widthScoreImg = IMG_SCORE_SMALL_WIDTH * worldWidth / Constants.IMG_BG_WIDTH;
        heightScoreImg = IMG_SCORE_SMALL_HEIGHT * widthScoreImg / IMG_SCORE_SMALL_WIDTH;

        listDigitsImage = new Array<>();
        poolDigitsImage = new Pool<Image>() {
            @Override
            protected Image newObject() {
                return new Image();
            }
        };
    }

    public void setupScoreSmall(int score) {
        if (score < 0) {
            return;
        }
        String scoreStr = String.valueOf(score);

        // Đảm bảo số lượng Image trong listDigitsImage đủ với số chữ số mới
        while (listDigitsImage.size < scoreStr.length()) {
            listDigitsImage.add(poolDigitsImage.obtain());
        }

        float totalWidth = 0f;
        // Cập nhật lại các Image trong listDigitsImage
        for (int i = 0; i < scoreStr.length(); i++) {
            char c = scoreStr.charAt(i);
            Image image = listDigitsImage.get(i);
            image.setDrawable(skin, "no-" + c + "-small");
            image.setScaling(Scaling.fit);
            // Đặt kích thước và tính vị trí x cho từng Image dựa trên kích thước của nó
            if (c == '1') {
                image.setSize(widthScoreNo1Img, heightScoreImg);
                image.setX(totalWidth); // Đặt vị trí x cho Image này
                totalWidth += widthScoreNo1Img;
            } else {
                image.setSize(widthScoreImg, heightScoreImg);
                image.setX(totalWidth); // Đặt vị trí x cho Image này
                totalWidth += widthScoreImg;
            }
            addActor(image);
        }

        // Giải phóng và xóa các Image dư thừa
        for (int i = listDigitsImage.size - 1; i >= scoreStr.length(); i--) {
            Image image = listDigitsImage.removeIndex(i); // Xóa Image khỏi danh sách
            image.clear();
            image.remove();
            poolDigitsImage.free(image); // Đưa Image về pool
        }

        // Đặt kích thước cho ScoreSmall để sử dụng với getWidth() và getHeight()
        setSize(totalWidth, heightScoreImg);
    }
}
