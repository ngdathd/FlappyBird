package com.ngdathd.flappybird.actors;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Scaling;
import com.ngdathd.flappybird.common.Constants;
import com.ngdathd.flappybird.helpers.AssetLoader;

public class Score extends Group {
    private static final float IMG_SCORE_NO_1_WIDTH = 16f;
    private static final float IMG_SCORE_WIDTH = 24f;
    private static final float IMG_SCORE_HEIGHT = 36f;

    private final Skin skin;

    private final float worldWidth;
    private final float worldHeight;

    private final float widthScoreNo1Img;
    private final float widthScoreImg;
    private final float heightScoreImg;

    private final Array<Image> listDigitsImage;
    private final Pool<Image> poolDigitsImage;

    private int score;

    private final Sound soundPoint;

    public Score(Stage stage) {
        skin = AssetLoader.INSTANCE.getSkin();

        worldWidth = stage.getViewport().getWorldWidth();
        worldHeight = stage.getViewport().getWorldHeight();

        widthScoreNo1Img = IMG_SCORE_NO_1_WIDTH * worldWidth / Constants.IMG_BG_WIDTH;
        widthScoreImg = IMG_SCORE_WIDTH * worldWidth / Constants.IMG_BG_WIDTH;
        heightScoreImg = IMG_SCORE_HEIGHT * widthScoreImg / IMG_SCORE_WIDTH;

        listDigitsImage = new Array<>();
        poolDigitsImage = new Pool<Image>() {
            @Override
            protected Image newObject() {
                return new Image();
            }
        };

        score = 0;

        soundPoint = AssetLoader.INSTANCE.getSound(AssetLoader.SOUND_POINT);
    }

    public int getScore() {
        return score;
    }

    public void handleScore() {
        soundPoint.play();
        score++;
        setupScore();
    }

    public void resetScore() {
        score = 0;
        setupScore();
    }

    private void setupScore() {
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
            image.setDrawable(skin, "no-" + c + "-large");
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

        // Vị trí x của Score Group, căn giữa toàn màn hình
        // Vị trí y của Score Group, cách cạnh trên màn hình bằng 3 lần heightScoreImg
        setPosition((worldWidth - totalWidth) / 2f, worldHeight - 3f * heightScoreImg);
    }
}
