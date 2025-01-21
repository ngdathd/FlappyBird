package com.ngdathd.flappybird.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.ngdathd.flappybird.common.Constants;
import com.ngdathd.flappybird.common.Utilities;
import com.ngdathd.flappybird.helpers.AssetLoader;
import com.ngdathd.flappybird.interfaces.OnClickScorePanelListener;

public class ScorePanel extends Group {
    private static final float IMG_GAME_OVER_WIDTH = 204f;
    private static final float IMG_GAME_OVER_HEIGHT = 54f;
    private static final float IMG_SCORE_PANEL_WIDTH = 238f;
    private static final float IMG_SCORE_PANEL_HEIGHT = 126f;
    private static final float IMG_MEDAL_WIDTH = 44f;
    private static final float IMG_MEDAL_HEIGHT = 44f;
    private static final float IMG_BTN_PLAY_WIDTH = 116f;
    private static final float IMG_BTN_PLAY_HEIGHT = 70f;

    private final Skin skin;

    private final float worldWidth;
    private final float worldHeight;
    private final float heightScorePanelImg;
    private final float posYMedalImgWithScorePanel;
    private final float posYCurrentScoreWithScorePanel;
    private final float posYBestScoreWithScorePanel;
    private final float posYBtnPlayImgWithScorePanel;

    private final Image gameOverImg;
    private final Image scorePanelImg;
    private final Image medalImg;
    private final ScoreSmall currentScore;
    private final ScoreSmall bestScore;
    private final ImageButton btnPlay;
    private final ImageButton btnRank;

    public ScorePanel(Stage stage, OnClickScorePanelListener onClickScorePanelListener) {
        skin = AssetLoader.INSTANCE.getSkin();

        worldWidth = stage.getViewport().getWorldWidth();
        worldHeight = stage.getViewport().getWorldHeight();

        float widthScorePanelImg = IMG_SCORE_PANEL_WIDTH * worldWidth / Constants.IMG_BG_WIDTH;
        heightScorePanelImg = IMG_SCORE_PANEL_HEIGHT * widthScorePanelImg / IMG_SCORE_PANEL_WIDTH;

        scorePanelImg = new Image(skin, "score-panel");
        scorePanelImg.setSize(widthScorePanelImg, heightScorePanelImg);
        scorePanelImg.setScaling(Scaling.fit);
        scorePanelImg.setPosition(
            worldWidth / 2f - widthScorePanelImg / 2f,
            -heightScorePanelImg
        );
        addActor(scorePanelImg);

        float widthMedalImg = IMG_MEDAL_WIDTH * worldWidth / Constants.IMG_BG_WIDTH;
        float heightMedalImg = IMG_MEDAL_HEIGHT * widthMedalImg / IMG_MEDAL_WIDTH;
        posYMedalImgWithScorePanel = 17.75f * worldWidth / Constants.BASELINE_WORLD_WIDTH;

        medalImg = new Image();
        medalImg.setSize(widthMedalImg, heightMedalImg);
        medalImg.setScaling(Scaling.fit);
        medalImg.setPosition(
            worldWidth / 2f - widthScorePanelImg / 2f
                + 15f * worldWidth / Constants.BASELINE_WORLD_WIDTH,
            scorePanelImg.getY() + posYMedalImgWithScorePanel
        );
        addActor(medalImg);

        posYCurrentScoreWithScorePanel = 35.25f * worldWidth / Constants.BASELINE_WORLD_WIDTH;

        currentScore = new ScoreSmall(stage);
        currentScore.setupScoreSmall(0);
        currentScore.setPosition(
            worldWidth / 2f - currentScore.getWidth()
                + 43f * worldWidth / Constants.BASELINE_WORLD_WIDTH,
            scorePanelImg.getY() + posYCurrentScoreWithScorePanel
        );
        addActor(currentScore);

        posYBestScoreWithScorePanel = 15.5f * worldWidth / Constants.BASELINE_WORLD_WIDTH;

        bestScore = new ScoreSmall(stage);
        bestScore.setupScoreSmall(0);
        bestScore.setPosition(
            worldWidth / 2f - bestScore.getWidth()
                + 43f * worldWidth / Constants.BASELINE_WORLD_WIDTH,
            scorePanelImg.getY() + posYBestScoreWithScorePanel
        );
        addActor(bestScore);

        float widthGameOverImg = IMG_GAME_OVER_WIDTH * worldWidth / Constants.IMG_BG_WIDTH;
        float heightGameOverImg = IMG_GAME_OVER_HEIGHT * widthGameOverImg / IMG_GAME_OVER_WIDTH;

        gameOverImg = new Image(skin, "game-over");
        gameOverImg.setSize(widthGameOverImg, heightGameOverImg);
        gameOverImg.setScaling(Scaling.fit);
        gameOverImg.setPosition(
            worldWidth / 2f - widthGameOverImg / 2f,
            worldHeight / 2f - heightScorePanelImg / 2f + heightScorePanelImg
                + 5f * worldWidth / Constants.BASELINE_WORLD_WIDTH
        );
        gameOverImg.addAction(Actions.alpha(0f));
        addActor(gameOverImg);

        float widthBtnPlayImg = IMG_BTN_PLAY_WIDTH * worldWidth / Constants.IMG_BG_WIDTH;
        float heightBtnPlayImg = IMG_BTN_PLAY_HEIGHT * widthBtnPlayImg / IMG_BTN_PLAY_WIDTH;
        posYBtnPlayImgWithScorePanel = -heightBtnPlayImg
            - 10f * worldWidth / Constants.BASELINE_WORLD_WIDTH;
        float amountYBtnPlayImg = 2f * worldWidth / Constants.BASELINE_WORLD_WIDTH;

        btnPlay = new ImageButton(skin, "btn-play");
        btnPlay.setSize(widthBtnPlayImg, heightBtnPlayImg);
        btnPlay.setPosition(
            worldWidth / 2f - widthBtnPlayImg,
            scorePanelImg.getY() + posYBtnPlayImgWithScorePanel
        );
        btnPlay.setOrigin(widthBtnPlayImg / 2f, heightBtnPlayImg / 2f);
        btnPlay.setTransform(true);
        btnPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (btnPlay.getColor().a == 1f) {
                    btnPlay.addAction(Actions.sequence(
                        Actions.moveBy(0f, -amountYBtnPlayImg, 0.05f, Interpolation.sine), // Di chuyển xuống
                        Actions.moveBy(0f, amountYBtnPlayImg, 0.05f) // Di chuyển lên lại vị trí ban đầu
                    ));
                    onClickScorePanelListener.onButtonPlayClicked();
                }
            }
        });
        btnPlay.addAction(Actions.alpha(0f));
        addActor(btnPlay);

        btnRank = new ImageButton(skin, "btn-rank");
        btnRank.setSize(widthBtnPlayImg, heightBtnPlayImg);
        btnRank.setPosition(
            worldWidth / 2f,
            scorePanelImg.getY() + posYBtnPlayImgWithScorePanel
        );
        btnRank.setOrigin(widthBtnPlayImg / 2f, heightBtnPlayImg / 2f);
        btnRank.setTransform(true);
        btnRank.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (btnRank.getColor().a == 1f) {
                    btnRank.addAction(Actions.sequence(
                        Actions.moveBy(0f, -amountYBtnPlayImg, 0.05f, Interpolation.sine), // Di chuyển xuống
                        Actions.moveBy(0f, amountYBtnPlayImg, 0.05f) // Di chuyển lên lại vị trí ban đầu
                    ));
                    onClickScorePanelListener.onButtonRankClicked();
                }
            }
        });
        btnRank.addAction(Actions.alpha(0f));
        addActor(btnRank);
    }

    public void showScorePanel(int score) {
        clearActions();
        addAction(Actions.sequence(
            Actions.run(() -> gameOverImg.addAction(Actions.fadeIn(0.3f))),
            Actions.delay(0.3f),
            Actions.run(() -> {
                float y = worldHeight / 2f - heightScorePanelImg / 2f;
                scorePanelImg.addAction(Actions.moveTo(scorePanelImg.getX(), y, 0.2f));
                setupScorePanel(score);
                medalImg.addAction(Actions.moveTo(
                    medalImg.getX(),
                    y + posYMedalImgWithScorePanel,
                    0.2f
                ));
                currentScore.addAction(Actions.moveTo(
                    worldWidth / 2f - currentScore.getWidth()
                        + 43f * worldWidth / Constants.BASELINE_WORLD_WIDTH,
                    y + posYCurrentScoreWithScorePanel,
                    0.2f
                ));
                bestScore.addAction(Actions.moveTo(
                    worldWidth / 2f - bestScore.getWidth()
                        + 43f * worldWidth / Constants.BASELINE_WORLD_WIDTH,
                    y + posYBestScoreWithScorePanel,
                    0.2f
                ));
                btnPlay.setY(y + posYBtnPlayImgWithScorePanel);
                btnRank.setY(y + posYBtnPlayImgWithScorePanel);
            }),
            Actions.delay(0.3f),
            Actions.run(() -> {
                btnPlay.addAction(Actions.fadeIn(0.3f));
                btnRank.addAction(Actions.fadeIn(0.3f));
            })
        ));
    }

    public void hideScorePanel() {
        clearActions();
        addAction(Actions.sequence(
            Actions.run(() -> {
                btnPlay.addAction(Actions.fadeOut(0.3f));
                btnRank.addAction(Actions.fadeOut(0.3f));
            }),
            Actions.delay(0.3f),
            Actions.run(() -> {
                float y = -heightScorePanelImg;
                scorePanelImg.addAction(Actions.moveTo(scorePanelImg.getX(), y, 0.2f));
                medalImg.addAction(Actions.moveTo(
                    medalImg.getX(),
                    y + posYMedalImgWithScorePanel,
                    0.2f
                ));
                // Đặt lại scoreCurrent = 0
                currentScore.setupScoreSmall(0);
                currentScore.addAction(Actions.moveTo(
                    worldWidth / 2f - currentScore.getWidth()
                        + 43f * worldWidth / Constants.BASELINE_WORLD_WIDTH,
                    y + posYCurrentScoreWithScorePanel,
                    0.2f
                ));
                bestScore.addAction(Actions.moveTo(
                    worldWidth / 2f - bestScore.getWidth()
                        + 43f * worldWidth / Constants.BASELINE_WORLD_WIDTH,
                    y + posYBestScoreWithScorePanel,
                    0.2f
                ));
                btnPlay.setY(y + posYBtnPlayImgWithScorePanel);
                btnRank.setY(y + posYBtnPlayImgWithScorePanel);
            }),
            Actions.run(() -> gameOverImg.addAction(Actions.fadeOut(0.3f)))
        ));
    }

    private void setupScorePanel(int score) {
        Preferences preferences = Gdx.app.getPreferences(Constants.PREFS_NAME);

        int highScore1 = preferences.getInteger(Constants.PREFS_HIGH_SCORE_KEY + '1', 0);
        int highScore2 = preferences.getInteger(Constants.PREFS_HIGH_SCORE_KEY + '2', 0);
        // So sánh số điểm hiện tại với các số điểm cao trước đó
        // Nếu xếp thứ nhất hiện huy chương vàng, thứ hai hiện huy chương bạc
        // Nếu không thì không hiện
        if (score > 0 && score >= highScore1) {
            medalImg.setDrawable(skin, "medal-gold");
            // Hiện số điểm hiện tại
            currentScore.setupScoreSmall(score);
            // Hiện số điểm cao nhất
            bestScore.setupScoreSmall(score);
        } else if (score > 0 && score >= highScore2) {
            medalImg.setDrawable(skin, "medal-silver");
            // Hiện số điểm hiện tại
            currentScore.setupScoreSmall(score);
            // Hiện số điểm cao nhất
            bestScore.setupScoreSmall(highScore1);
        } else {
            medalImg.setDrawable(null);
            // Hiện số điểm hiện tại
            currentScore.setupScoreSmall(score);
            // Hiện số điểm cao nhất
            bestScore.setupScoreSmall(highScore1);
        }

        // new high score
        Array<Integer> highScores = new Array<>(6);
        for (int i = 0; i < 6; i++) {
            int highScore = preferences.getInteger(Constants.PREFS_HIGH_SCORE_KEY + (i + 1), 0);
            highScores.add(highScore);
        }

        // Thêm điểm mới vào danh sách
        highScores.add(score);

        // Sắp xếp lại danh sách từ cao đến thấp
        Utilities.quickSort(highScores, 0, highScores.size - 1, false);

        // Loại bỏ các điểm số trùng lặp
        Array<Integer> uniqueScores = new Array<>();
        for (int i = 0; i < highScores.size; i++) {
            if (i == 0 || !highScores.get(i).equals(highScores.get(i - 1))) {
                uniqueScores.add(highScores.get(i));
            }
        }

        // Đảm bảo uniqueScores có đủ 6 phần tử
        while (uniqueScores.size < 6) {
            uniqueScores.add(0); // Thêm 0 nếu số lượng chưa đủ 6
        }

        // Giữ lại 6 điểm cao nhất
        if (uniqueScores.size > 6) {
            uniqueScores.removeRange(6, uniqueScores.size - 1);
        }

        // Nếu newScore lớn hơn điểm thấp nhất trong 6 điểm cao nhất thì cập nhật danh sách điểm cao
        if (score > uniqueScores.get(uniqueScores.size - 1)) {
            for (int i = 0; i < uniqueScores.size; i++) {
                preferences.putInteger(Constants.PREFS_HIGH_SCORE_KEY + (i + 1), uniqueScores.get(i));
            }
            preferences.flush();
        }
    }
}
