package com.ngdathd.flappybird.common;

public class Constants {
    // We will use the 3.6 inch Retina iPhone (640x960) as our baseline.
    // Game width is ~135 pixels, scaled accordingly (by factor of 4.75x on iPhone), 640/4.75 ~ 135
    public static final float BASELINE_WORLD_WIDTH = 135.0f;
    public static final float IMG_BG_WIDTH = 288f;
    public static final float IMG_BG_HEIGHT = 512f;
    public static final float SCROLL_SPEED = -46f;

    public static final String PREFS_NAME = "@FLAPPY_THING";
    public static final String PREFS_HIGH_SCORE_KEY = "@HIGH_SCORE_";
}
