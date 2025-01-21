package com.ngdathd.flappybird.helpers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public enum AssetLoader {
    INSTANCE;

    public static final String SOUND_DIE = "sounds/sfx_die.wav";
    public static final String SOUND_HIT = "sounds/sfx_hit.wav";
    public static final String SOUND_POINT = "sounds/sfx_point.wav";
    public static final String SOUND_SWOOSHING = "sounds/sfx_swooshing.wav";
    public static final String SOUND_WING = "sounds/sfx_wing.wav";

    private static final String SKIN_FLAPPY_BIRD_FOLDER = "skin-flappy-bird";

    private final AssetManager assetManager;

    AssetLoader() {
        assetManager = new AssetManager();
    }

    public void loadAssets() {
        // Tải hình ảnh
        assetManager.load(SKIN_FLAPPY_BIRD_FOLDER + "/skin.json", Skin.class);
        // Tải âm thanh
        assetManager.load(SOUND_DIE, Sound.class);
        assetManager.load(SOUND_HIT, Sound.class);
        assetManager.load(SOUND_POINT, Sound.class);
        assetManager.load(SOUND_SWOOSHING, Sound.class);
        assetManager.load(SOUND_WING, Sound.class);
    }

    public Skin getSkin() {
        return assetManager.get(SKIN_FLAPPY_BIRD_FOLDER + "/skin.json", Skin.class);
    }

    public Sound getSound(String soundName) {
        return assetManager.get(soundName, Sound.class);
    }

    public boolean updateLoading() {
        return assetManager.update();
    }

    public float getProgressLoading() {
        return assetManager.getProgress() * 100;
    }

    public void dispose() {
        assetManager.dispose();
    }
}
