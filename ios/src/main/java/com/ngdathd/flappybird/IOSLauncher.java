package com.ngdathd.flappybird;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import com.ngdathd.flappybird.Main;
import com.ngdathd.flappybird.base.IosInterface;

import org.robovm.apple.coregraphics.CGRect;
import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.uikit.UIApplication;
import org.robovm.apple.uikit.UIColor;
import org.robovm.apple.uikit.UIDevice;
import org.robovm.apple.uikit.UIScene;
import org.robovm.apple.uikit.UIStatusBarManager;
import org.robovm.apple.uikit.UIView;
import org.robovm.apple.uikit.UIViewAutoresizing;
import org.robovm.apple.uikit.UIWindow;
import org.robovm.apple.uikit.UIWindowScene;

/** Launches the iOS (RoboVM) application. */
public class IOSLauncher extends IOSApplication.Delegate implements IosInterface {
    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        config.useGL30 = true;
        config.orientationLandscape = false;
        config.preferredFramesPerSecond = 60;
        config.useAccelerometer = false;
        config.useCompass = false;
        config.statusBarVisible = true;
        config.hideHomeIndicator = true;
        Main flappyBird = new Main();
        flappyBird.setIosInterface(this);
        return new IOSApplication(flappyBird, config);
    }

    public static void main(String[] argv) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
    }

    private UIColor colorFromHexString(String hexString) {
        // Xoá ký tự # nếu có
        if (hexString.startsWith("#")) {
            hexString = hexString.substring(1);
        }

        // Xác định độ dài chuỗi hex
        int length = hexString.length();
        int red, green, blue, alpha = 255; // Mặc định alpha là 255

        try {
            if (length == 6) {
                // Hex không có alpha
                red = Integer.parseInt(hexString.substring(0, 2), 16);
                green = Integer.parseInt(hexString.substring(2, 4), 16);
                blue = Integer.parseInt(hexString.substring(4, 6), 16);
            } else if (length == 8) {
                // Hex có alpha
                alpha = Integer.parseInt(hexString.substring(0, 2), 16);
                red = Integer.parseInt(hexString.substring(2, 4), 16);
                green = Integer.parseInt(hexString.substring(4, 6), 16);
                blue = Integer.parseInt(hexString.substring(6, 8), 16);
            } else {
                // Chuỗi hex không hợp lệ
                return null;
            }
        } catch (NumberFormatException e) {
            // Chuỗi hex không hợp lệ
            return null;
        }
        return new UIColor(red / 255.0, green / 255.0, blue / 255.0, alpha / 255.0);
    }

    @SuppressWarnings("deprecation")
    private UIWindow getCurrentWindow() {
        String systemVersion = UIDevice.getCurrentDevice().getSystemVersion();
        String[] versionComponents = systemVersion.split("\\.");
        int majorVersion = Integer.parseInt(versionComponents[0]);
        if (majorVersion >= 13) {
            for (UIScene scene : UIApplication.getSharedApplication().getConnectedScenes()) {
                if (scene instanceof UIWindowScene) {
                    UIWindowScene windowScene = (UIWindowScene) scene;
                    for (UIWindow window : windowScene.getWindows()) {
                        if (window.isKeyWindow()) {
                            return window;
                        }
                    }
                }
            }
        } else {
            return UIApplication.getSharedApplication().getKeyWindow();
        }
        return null;
    }

    private double getStatusBarHeight(UIWindow window) {
        if (window.getWindowScene() != null) {
            UIStatusBarManager statusBarManager = window.getWindowScene().getStatusBarManager();
            if (statusBarManager != null) {
                return statusBarManager.getStatusBarFrame().getHeight();
            }
        }
        return 0;
    }

    @Override
    public void setStatusBarColor(String colorString) {
        UIColor color = colorFromHexString(colorString);
        if (color == null) {
            return;
        }

        UIWindow window = getCurrentWindow();
        if (window == null) {
            return;
        }

        double windowWidth = window.getBounds().getWidth();
        double statusBarHeight = getStatusBarHeight(window);

        // Tạo UIView phủ lên thanh trạng thái và đặt màu
        UIView statusBarOverlay = new UIView(new CGRect(0, 0, windowWidth, statusBarHeight));
        statusBarOverlay.setBackgroundColor(color);
        // UIView co giãn theo chiều rộng
        statusBarOverlay.setAutoresizingMask(UIViewAutoresizing.FlexibleWidth);

        // Thêm lớp phủ vào UIWindow
        window.addSubview(statusBarOverlay);
    }
}
