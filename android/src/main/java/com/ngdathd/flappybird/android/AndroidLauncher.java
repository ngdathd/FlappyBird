package com.ngdathd.flappybird.android;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.ngdathd.flappybird.Main;
import com.ngdathd.flappybird.base.AndroidInterface;

/** Launches the Android application. */
public class AndroidLauncher extends AndroidApplication implements AndroidInterface {
    private Window window;
    private String statusBarColor;
    private String navigationBarColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useGL30 = true;
        config.useImmersiveMode = false;
        config.useAccelerometer = false;
        config.useCompass = false;
        Main flappyBird = new Main();
        flappyBird.setAndroidInterface(this);
        View gameView = initializeForView(flappyBird, config);

        RelativeLayout layout = new RelativeLayout(this);
        layout.addView(gameView);
        setContentView(layout);

        window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    private boolean isGestureNavigation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // For Android 10 and above, check the system setting for navigation mode
            int systemNavigationMode = Settings.Secure.getInt(
                getContentResolver(),
                "navigation_mode",
                -1
            );
            return systemNavigationMode == 2; // 2 indicates gesture navigation
        } else {
            // For devices below Android 10, gesture navigation is not supported
            return false;
        }
    }

    private boolean isFoldStateOpen(Configuration configuration) {
        return (configuration.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)
            == Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (isFoldStateOpen(newConfig)) {
            window.setStatusBarColor(Color.BLACK);
            window.setNavigationBarColor(Color.BLACK);
        } else {
            window.setStatusBarColor(Color.parseColor(statusBarColor));
            window.setNavigationBarColor(Color.parseColor(navigationBarColor));
        }
    }

    @Override
    public void setStatusBarColor(String statusBarColor) {
        this.statusBarColor = statusBarColor;
        if (!isFoldStateOpen(getResources().getConfiguration())) {
            runOnUiThread(() -> window.setStatusBarColor(Color.parseColor(statusBarColor)));
        }
    }

    @Override
    public void setNavigationBarColor(String navigationBarColor) {
        this.navigationBarColor = navigationBarColor;
        if (isGestureNavigation() && !isFoldStateOpen(getResources().getConfiguration())) {
            runOnUiThread(() -> window.setNavigationBarColor(Color.parseColor(navigationBarColor)));
        }
    }
}
