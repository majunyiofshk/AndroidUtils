package com.mjy.androidutils.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;
import android.view.WindowManager;

public class DisplayUtils {

    private static Point sPoint;

    private static void checkNull(){
        if (sPoint == null){
            sPoint = new Point();
        }
    }

    /**
     * @param context
     * @return 屏幕总宽度
     */
    public static int getScreenWidth(Context context) {
        WindowManager windowManager =
                (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager == null) {
            return 0;
        } else {
            checkNull();
            windowManager.getDefaultDisplay().getRealSize(sPoint);
            return sPoint.x;
        }
    }

    /**
     * @param context
     * @return 屏幕总高度
     */
    public static int getScreenHeigth(Context context){
        WindowManager windowManager =
                (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager == null){
            return 0;
        }else {
            checkNull();
            windowManager.getDefaultDisplay().getRealSize(sPoint);
            return sPoint.y;
        }
    }

    /**
     * @param context
     * @return 屏幕高度,但不包括导航栏
     */
    public static int getScreenHeigthNoNavigationBar(Context context){
        WindowManager windowManager =
                (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager == null){
            return 0;
        }else {
            checkNull();
            windowManager.getDefaultDisplay().getSize(sPoint);
            return sPoint.y;
        }
    }

    /**
     * @param context
     * @return 状态栏高度
     */
    public static int getStatusBarHeight(Context context){
        int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resId);
    }

    /**
     * 导航栏高度
     */
    public static int getNavigationBarHeight(Context context) {
        if (!isNavigationBarShow(context)) {
            return 0;
        }
        int resId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resId);
    }

    /**
     * @param context
     * @return 厂商是否屏蔽了导航栏
     */
    @SuppressLint("ObsoleteSdkInt")
    public static boolean isNavigationBarShow(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            Point size = new Point();
            Point realSize = new Point();
            display.getSize(size);
            display.getRealSize(realSize);
            return realSize.y != size.y;
        } else {
            boolean menu = ViewConfiguration.get(context).hasPermanentMenuKey();
            boolean back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            return !menu && !back;
        }
    }

    /**
     * @param context
     * @return 当前可用的界面大小(一般是屏幕高度减去状态栏与导航栏的高度)
     */
    public static int getCurrentScreenHeight(Context context){
        return dip2px(context, context.getResources().getConfiguration().screenHeightDp);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param context
     * @param pxValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param context
     * @param spValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

}
