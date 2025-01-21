package com.ngdathd.flappybird.actors;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Scaling;
import com.ngdathd.flappybird.common.Constants;
import com.ngdathd.flappybird.helpers.AssetLoader;

public class Tutorial extends Group {
    private static final float IMG_GAME_READY_WIDTH = 196f;
    private static final float IMG_GAME_READY_HEIGHT = 62f;
    private static final float IMG_TUTORIAL_WIDTH = 114f;
    private static final float IMG_TUTORIAL_HEIGHT = 98f;

    public Tutorial(Stage stage) {
        Skin skin = AssetLoader.INSTANCE.getSkin();

        float worldWidth = stage.getViewport().getWorldWidth();
        float worldHeight = stage.getViewport().getWorldHeight();

        float widthTutorialImg = IMG_TUTORIAL_WIDTH * worldWidth / Constants.IMG_BG_WIDTH;
        float heightTutorialImg = IMG_TUTORIAL_HEIGHT * widthTutorialImg / IMG_TUTORIAL_WIDTH;

        Image tutorialImg = new Image(skin, "tutorial");
        tutorialImg.setSize(widthTutorialImg, heightTutorialImg);
        tutorialImg.setScaling(Scaling.fit);
        tutorialImg.setPosition(
            worldWidth / 2f - widthTutorialImg / 2f,
            worldHeight / 2f - heightTutorialImg / 2f
                - 5f * worldWidth / Constants.BASELINE_WORLD_WIDTH
        );
        addActor(tutorialImg);

        float widthGameReadyImg = IMG_GAME_READY_WIDTH * worldWidth / Constants.IMG_BG_WIDTH;
        float heightGameReadyImg = IMG_GAME_READY_HEIGHT * widthGameReadyImg / IMG_GAME_READY_WIDTH;

        Image gameReadyImg = new Image(skin, "game-ready");
        gameReadyImg.setSize(widthGameReadyImg, heightGameReadyImg);
        gameReadyImg.setScaling(Scaling.fit);
        gameReadyImg.setPosition(
            worldWidth / 2f - widthGameReadyImg / 2f,
            tutorialImg.getY() + heightTutorialImg
                + 5f * worldWidth / Constants.BASELINE_WORLD_WIDTH
        );
        addActor(gameReadyImg);
    }

    public void showTutorial() {
        clearActions();
        addAction(Actions.sequence(
            Actions.fadeIn(0.3f)
        ));
    }

    public void hideTutorial() {
        clearActions();
        addAction(Actions.sequence(
            Actions.fadeOut(0.3f)
        ));
    }
}
