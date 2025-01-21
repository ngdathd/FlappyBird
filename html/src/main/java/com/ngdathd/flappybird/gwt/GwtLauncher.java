package com.ngdathd.flappybird.gwt;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.ngdathd.flappybird.Main;

/** Launches the GWT application. */
public class GwtLauncher extends GwtApplication {
    @Override
    public GwtApplicationConfiguration getConfig() {
        // Resizable application, uses available space in browser with no padding:
        GwtApplicationConfiguration config;
        if (GwtApplication.isMobileDevice()) {
            config = new GwtApplicationConfiguration(true);
        } else {
            config = new GwtApplicationConfiguration(360, 640);
        }
        config.useGL30 = true;
        config.useAccelerometer = false;
        return config;
        // If you want a fixed size application, comment out the above resizable section,
        // and uncomment below:
        //return new GwtApplicationConfiguration(640, 480);
    }

    @Override
    public ApplicationListener createApplicationListener() {
        return new Main();
    }
}
