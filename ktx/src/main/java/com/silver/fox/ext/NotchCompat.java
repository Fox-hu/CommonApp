package com.silver.fox.ext;

import android.graphics.Rect;
import android.os.Build;
import android.view.DisplayCutout;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

/**
 * @Author fox.hu
 * @Date 2021/7/5 4:19 下午
 */
public class NotchCompat {
    private static INotchScreenSupport mNotchScreenSupport = null;

    /**
     * 判断屏幕是否为凹口屏
     * WindowInsets在View Attach到Window上之后才会创建
     * 因此想要获得正确的结果，方法的调用时机应在DecorView Attach之后
     */
    public static boolean hasDisplayCutout(@NonNull Window window) {
        checkScreenSupportInit();
        return mNotchScreenSupport.hasNotchInScreen(window);
    }

    public static boolean hasDisplayCutoutHardware(@NonNull Window window) {
        checkScreenSupportInit();
        return mNotchScreenSupport.hasNotchInScreenHardware(window);
    }

    /**
     * 获取凹口屏大小
     */
    @NonNull
    public static List<Rect> getDisplayCutoutSize(@NonNull Window window) {
        checkScreenSupportInit();
        return mNotchScreenSupport.getNotchSize(window);
    }

    @NonNull
    public static List<Rect> getDisplayCutoutSizeHardware(@NonNull Window window) {
        checkScreenSupportInit();
        return mNotchScreenSupport.getNotchSizeHardware(window);
    }

    /**
     * 设置始终使用凹口屏区域
     */
    public static void immersiveDisplayCutout(@NonNull Window window) {
        checkScreenSupportInit();
        mNotchScreenSupport.setWindowLayoutAroundNotch(window);
    }

    /**
     * 设置始终不使用凹口屏区域
     */
    public static void blockDisplayCutout(Window window) {
        checkScreenSupportInit();
        mNotchScreenSupport.setWindowLayoutBlockNotch(window);
    }

    /**
     * 将刘海屏 flag 重置
     */
    public static void resetDisplayCutout(Window window) {
        checkScreenSupportInit();
        mNotchScreenSupport.setWindowLayoutNotchDefault(window);
    }

    public static void onWindowConfigChanged(Window window) {
        checkScreenSupportInit();
        mNotchScreenSupport.onWindowConfigChanged(window);
    }

    private static void checkScreenSupportInit() {
        if (mNotchScreenSupport != null) return;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            mNotchScreenSupport = new DefaultNotchScreenSupport();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            mNotchScreenSupport = new PNotchScreenSupport();
        }
    }

    interface INotchScreenSupport {
        boolean hasNotchInScreen(@NonNull Window window);

        /**
         * 硬件上屏幕是否存在凹口，{@link #hasNotchInScreen(Window)}用判断当前状态是否存在凹口，
         * Android p在调用了{@link #setWindowLayoutBlockNotch(Window)}认为当前状态不存在凹口
         *
         * @param window window
         * @return false, 不存在凹口，true，存在凹口
         */
        boolean hasNotchInScreenHardware(@NonNull Window window);

        @NonNull
        List<Rect> getNotchSize(@NonNull Window window);

        /**
         * 获取硬件上屏幕凹口size，{@link #getNotchSize(Window)},获取当前状态凹口size
         * Android p在调用了{@link #setWindowLayoutBlockNotch(Window)}认为当前状态不存在凹口
         * 所以获取不到size
         *
         * @param window window
         * @return Rect
         */
        List<Rect> getNotchSizeHardware(@NonNull Window window);

        void setWindowLayoutAroundNotch(@NonNull Window window);

        void setWindowLayoutBlockNotch(@NonNull Window window);

        /**
         * 重置刘海屏 flag
         */
        void setWindowLayoutNotchDefault(@NonNull Window window);

        /**
         * 折叠屏 config change
         * @param window
         */
        void onWindowConfigChanged(@NonNull Window window);
    }


    static class DefaultNotchScreenSupport implements INotchScreenSupport {
        @Override
        public boolean hasNotchInScreen(@NonNull Window window) {
            return false;
        }

        @Override
        public boolean hasNotchInScreenHardware(@NonNull Window window) {
            return hasNotchInScreen(window);
        }

        @NonNull
        @Override
        public List<Rect> getNotchSize(@NonNull Window window) {
            return new ArrayList<>();
        }

        @Override
        public List<Rect> getNotchSizeHardware(@NonNull Window window) {
            return getNotchSize(window);
        }

        @Override
        public void setWindowLayoutAroundNotch(@NonNull Window window) {
        }

        @Override
        public void setWindowLayoutBlockNotch(@NonNull Window window) {
        }

        @Override
        public void setWindowLayoutNotchDefault(@NonNull Window window) {

        }

        /**
         * 折叠屏
         * @param window
         */
        @Override
        public void onWindowConfigChanged(@NonNull Window window) {

        }
    }

    static final class PNotchScreenSupport extends DefaultNotchScreenSupport {
        private static final String TAG = "PNotch";

        private boolean mHardwareHasNotch;
        private boolean mAlreadyObtainHardwareNotch;
        private List<Rect> mHardwareNotchSize = new ArrayList<>();

        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
        public boolean hasNotchInScreen(@NonNull Window window) {
            ensureHardwareNotch(window);
            final View decorView = window.getDecorView();
            final WindowInsets windowInsets = decorView.getRootWindowInsets();

            StringBuilder sb = new StringBuilder("hasNotchInScreen info\n");
            sb.append("brand: ").append(Build.BRAND).append("\n");
            sb.append("model: ").append(Build.MODEL).append("\n");
            sb.append("attribute: ").append(window.getAttributes().layoutInDisplayCutoutMode).append("\n");
            sb.append("decorView: ").append(decorView.toString()).append("\n");
            sb.append("windowInsets: ").append(windowInsets == null ? null : windowInsets.isConsumed()).append("\n");


            final DisplayCutout dct = windowInsets == null ? null : windowInsets.getDisplayCutout();
            final boolean hasNotch = dct != null && (dct.getSafeInsetTop() != 0
                                                     || dct.getSafeInsetBottom() != 0
                                                     || dct.getSafeInsetLeft() != 0
                                                     || dct.getSafeInsetRight() != 0);
            sb.append("displayCutout: ").append(dct == null ? null : dct.toString()).append("\n");
            sb.append("hasNotch: ").append(hasNotch).append("\n");
            sb.append("hasNotchHardware: ").append(mHardwareHasNotch).append("\n");
            sb.append("alreadyObtainHardwareNotch: ").append(mAlreadyObtainHardwareNotch);

            return hasNotch;
        }

        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
        public boolean hasNotchInScreenHardware(@NonNull Window window) {
            ensureHardwareNotch(window);
            return mHardwareHasNotch;
        }

        @NonNull
        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
        public List<Rect> getNotchSize(@NonNull Window window) {
            ensureHardwareNotch(window);
            List<Rect> result = new ArrayList<>();
            final View decorView = window.getDecorView();
            final WindowInsets windowInsets = decorView.getRootWindowInsets();
            if (windowInsets == null) return result;
            DisplayCutout dct = windowInsets.getDisplayCutout();
            if (dct != null) {
                result.addAll(dct.getBoundingRects());
            }
            return result;
        }

        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
        public List<Rect> getNotchSizeHardware(@NonNull Window window) {
            ensureHardwareNotch(window);
            return mHardwareNotchSize;
        }

        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
        public void setWindowLayoutAroundNotch(@NonNull Window window) {
            ensureHardwareNotch(window);
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            window.setAttributes(attributes);
        }

        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
        public void setWindowLayoutBlockNotch(@NonNull Window window) {
            ensureHardwareNotch(window);
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER;
            window.setAttributes(attributes);
        }

        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
        public void setWindowLayoutNotchDefault(@NonNull Window window) {
            ensureHardwareNotch(window);
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT;
            window.setAttributes(attributes);
        }

        @RequiresApi(api = Build.VERSION_CODES.P)
        private synchronized void ensureHardwareNotch(Window window) {
            if (mAlreadyObtainHardwareNotch) {
                return;
            }

            // decorView没有attachToWindow，windowInset为null
            final View decorView = window.getDecorView();
            final WindowInsets windowInsets = decorView.getRootWindowInsets();
            if (windowInsets == null) {
                return;
            }

            // LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER模式下，android系统认为没有cutout，windowInsets.getDisplayCutout()为null
            if (window.getAttributes().layoutInDisplayCutoutMode == WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER) {
                return;
            }

            DisplayCutout dct = windowInsets.getDisplayCutout();
            mHardwareHasNotch = dct != null && (dct.getSafeInsetTop() != 0
                                                || dct.getSafeInsetBottom() != 0
                                                || dct.getSafeInsetLeft() != 0
                                                || dct.getSafeInsetRight() != 0);
            if (mHardwareHasNotch) {
                mHardwareNotchSize.addAll(dct.getBoundingRects());
            }
            mAlreadyObtainHardwareNotch = true;
            // 偶现在有刘海的设备上，mHardwareHasNotch为false
            if (!mHardwareHasNotch) {
            }
        }
    }
}

