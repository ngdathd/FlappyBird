package com.ngdathd.flappybird.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.ngdathd.flappybird.common.Constants;
import com.ngdathd.flappybird.helpers.AssetLoader;

public class HighScorePanel extends Group {
    private static final float IMG_HIGH_SCORE_PANEL_WIDTH = 238f;
    private static final float IMG_HIGH_SCORE_PANEL_HEIGHT = 126f;

    private final float heightHighScorePanelImg;
    private final float posYHighScorePanel;
    private final float posYHighScore1;
    private final float posYHighScore3;
    private final float posYHighScore5;

    private final Image highScorePanelImg;
    private final ScoreSmall highScore1;
    private final ScoreSmall highScore2;
    private final ScoreSmall highScore3;
    private final ScoreSmall highScore4;
    private final ScoreSmall highScore5;
    private final ScoreSmall highScore6;

    public HighScorePanel(Stage stage) {
        Skin skin = AssetLoader.INSTANCE.getSkin();

        float worldWidth = stage.getViewport().getWorldWidth();
        float worldHeight = stage.getViewport().getWorldHeight();

        float widthHighScorePanelImg = IMG_HIGH_SCORE_PANEL_WIDTH * worldWidth / Constants.IMG_BG_WIDTH;
        heightHighScorePanelImg = IMG_HIGH_SCORE_PANEL_HEIGHT * widthHighScorePanelImg / IMG_HIGH_SCORE_PANEL_WIDTH;
        posYHighScorePanel = worldHeight / 2f - heightHighScorePanelImg / 2f;

        highScorePanelImg = new Image(skin, "high-score-panel");
        highScorePanelImg.setSize(widthHighScorePanelImg, heightHighScorePanelImg);
        highScorePanelImg.setScaling(Scaling.fit);
        highScorePanelImg.setPosition(
            worldWidth / 2f - widthHighScorePanelImg / 2f,
            posYHighScorePanel
        );
        addActor(highScorePanelImg);

        float posXHighScore1 = 12f * worldWidth / Constants.BASELINE_WORLD_WIDTH;
        float posXHighScore2 = highScorePanelImg.getWidth() / 2f
            + 12f * worldWidth / Constants.BASELINE_WORLD_WIDTH;
        posYHighScore1 = 42f * worldWidth / Constants.BASELINE_WORLD_WIDTH;
        posYHighScore3 = 28f * worldWidth / Constants.BASELINE_WORLD_WIDTH;
        posYHighScore5 = 14f * worldWidth / Constants.BASELINE_WORLD_WIDTH;

        highScore1 = new ScoreSmall(stage);
        highScore1.setupScoreSmall(0);
        highScore1.setPosition(
            highScorePanelImg.getX() + posXHighScore1,
            highScorePanelImg.getY() + posYHighScore1
        );
        addActor(highScore1);

        highScore2 = new ScoreSmall(stage);
        highScore2.setupScoreSmall(0);
        highScore2.setPosition(
            highScorePanelImg.getX() + posXHighScore2,
            highScorePanelImg.getY() + posYHighScore1
        );
        addActor(highScore2);

        highScore3 = new ScoreSmall(stage);
        highScore3.setupScoreSmall(0);
        highScore3.setPosition(
            highScorePanelImg.getX() + posXHighScore1,
            highScorePanelImg.getY() + posYHighScore3
        );
        addActor(highScore3);

        highScore4 = new ScoreSmall(stage);
        highScore4.setupScoreSmall(0);
        highScore4.setPosition(
            highScorePanelImg.getX() + posXHighScore2,
            highScorePanelImg.getY() + posYHighScore3
        );
        addActor(highScore4);

        highScore5 = new ScoreSmall(stage);
        highScore5.setupScoreSmall(0);
        highScore5.setPosition(
            highScorePanelImg.getX() + posXHighScore1,
            highScorePanelImg.getY() + posYHighScore5
        );
        addActor(highScore5);

        highScore6 = new ScoreSmall(stage);
        highScore6.setupScoreSmall(0);
        highScore6.setPosition(
            highScorePanelImg.getX() + posXHighScore2,
            highScorePanelImg.getY() + posYHighScore5
        );
        addActor(highScore6);

        addAction(Actions.alpha(0f));
    }

    public void showHighScorePanel() {
        setupHighScore();
        clearActions();
        addAction(Actions.sequence(
            Actions.fadeIn(0.1f)
        ));
    }

    public void hideHighScorePanelFadeOut() {
        clearActions();
        addAction(Actions.sequence(
            Actions.fadeOut(0.1f)
        ));
    }

    public void hideHighScorePanelMoveTo() {
        clearActions();
        addAction(Actions.sequence(
            Actions.delay(0.3f),
            Actions.run(() -> {
                float y = -heightHighScorePanelImg;
                highScorePanelImg.addAction(Actions.moveTo(highScorePanelImg.getX(), y, 0.2f));
                highScore1.addAction(Actions.moveTo(highScore1.getX(), y + posYHighScore1, 0.2f));
                highScore2.addAction(Actions.moveTo(highScore2.getX(), y + posYHighScore1, 0.2f));
                highScore3.addAction(Actions.moveTo(highScore3.getX(), y + posYHighScore3, 0.2f));
                highScore4.addAction(Actions.moveTo(highScore4.getX(), y + posYHighScore3, 0.2f));
                highScore5.addAction(Actions.moveTo(highScore5.getX(), y + posYHighScore5, 0.2f));
                highScore6.addAction(Actions.moveTo(highScore6.getX(), y + posYHighScore5, 0.2f));
            }),
            Actions.delay(0.2f),
            Actions.alpha(0f),
            Actions.run(() -> {
                float y = posYHighScorePanel;
                highScorePanelImg.addAction(Actions.moveTo(highScorePanelImg.getX(), y, 0.2f));
                highScore1.addAction(Actions.moveTo(highScore1.getX(), y + posYHighScore1, 0.2f));
                highScore2.addAction(Actions.moveTo(highScore2.getX(), y + posYHighScore1, 0.2f));
                highScore3.addAction(Actions.moveTo(highScore3.getX(), y + posYHighScore3, 0.2f));
                highScore4.addAction(Actions.moveTo(highScore4.getX(), y + posYHighScore3, 0.2f));
                highScore5.addAction(Actions.moveTo(highScore5.getX(), y + posYHighScore5, 0.2f));
                highScore6.addAction(Actions.moveTo(highScore6.getX(), y + posYHighScore5, 0.2f));
            })
        ));
    }

    private void setupHighScore() {
        Preferences preferences = Gdx.app.getPreferences(Constants.PREFS_NAME);

        Array<Integer> highScores = new Array<>(6);
        for (int i = 0; i < 6; i++) {
            int highScore = preferences.getInteger(Constants.PREFS_HIGH_SCORE_KEY + (i + 1), 0);
            highScores.add(highScore);
        }

        highScore1.setupScoreSmall(highScores.get(0));
        highScore2.setupScoreSmall(highScores.get(1));
        highScore3.setupScoreSmall(highScores.get(2));
        highScore4.setupScoreSmall(highScores.get(3));
        highScore5.setupScoreSmall(highScores.get(4));
        highScore6.setupScoreSmall(highScores.get(5));
    }
}
