package com.ngdathd.flappybird.actors;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Scaling;
import com.ngdathd.flappybird.base.AndroidInterface;
import com.ngdathd.flappybird.base.IosInterface;
import com.ngdathd.flappybird.common.Constants;
import com.ngdathd.flappybird.common.Utilities;
import com.ngdathd.flappybird.helpers.AssetLoader;

public class Background extends Group {
    private final AndroidInterface androidInterface;
    private final IosInterface iosInterface;

    private final Skin skin;
    private final String[] backgroundColors;

    private final float worldWidth;
    private final float worldHeight;
    private final float gutterTopY;

    private final Image bgImgGutter;
    private final Image bgImg;

    public Background(Stage stage, AndroidInterface androidInterface, IosInterface iosInterface) {
        this.androidInterface = androidInterface;
        this.iosInterface = iosInterface;

        skin = AssetLoader.INSTANCE.getSkin();
        backgroundColors = new String[]{"day", "night"};

        worldWidth = stage.getViewport().getWorldWidth();
        worldHeight = stage.getViewport().getWorldHeight();
        float imgBgHeight = Constants.IMG_BG_HEIGHT * worldWidth / Constants.IMG_BG_WIDTH;
        gutterTopY = MathUtils.ceil((worldHeight - imgBgHeight) / 2f);

        bgImgGutter = new Image();
        addActor(bgImgGutter);

        bgImg = new Image();
        addActor(bgImg);
    }

    public void randomizeBackground() {
        String selectedColor = backgroundColors[MathUtils.random(backgroundColors.length - 1)];
        String randomBg = "bg-" + selectedColor;

        if (androidInterface != null) {
            androidInterface.setStatusBarColor(Utilities.colorToHexString(skin.getColor(randomBg)));
            androidInterface.setNavigationBarColor(Utilities.colorToHexString(skin.getColor("land")));
        }

        if (iosInterface != null) {
            iosInterface.setStatusBarColor(Utilities.colorToHexString(skin.getColor(randomBg)));
        }

        bgImgGutter.setDrawable(skin, randomBg);
        bgImgGutter.setSize(worldWidth, worldHeight);
        bgImgGutter.setScaling(Scaling.fit);
        bgImgGutter.setPosition(0, gutterTopY);

        bgImg.setDrawable(skin, randomBg);
        bgImg.setSize(worldWidth, worldHeight);
        bgImg.setScaling(Scaling.fit);
        bgImg.setPosition(0, 0);
    }
}
