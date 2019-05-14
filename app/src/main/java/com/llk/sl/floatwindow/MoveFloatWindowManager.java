package com.llk.sl.floatwindow;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.WindowManager;

import com.llk.sl.eventbus.FloatWindowLocationEvent;
import com.llk.sl.util.DimensionUtil;
import com.llk.sl.util.PreferenceUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;

public class MoveFloatWindowManager {
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mParams;

    private WeakReference<Context> mRef;

    private DirectionView directionView;

    private boolean isShowing = false;

    public static MoveFloatWindowManager getInstance() {
        return InnerHolder.mInstance;
    }

    private static class InnerHolder {
        private static MoveFloatWindowManager mInstance = new MoveFloatWindowManager();
    }

    public void init(Context context){
        mRef = new WeakReference<>(context);

        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        mParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= 26) {
            mParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            mParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        mParams.format = PixelFormat.RGBA_8888;
        mParams.gravity = Gravity.BOTTOM | Gravity.START;
        // 设置不阻塞事件传递到后面的窗口
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mParams.width = DimensionUtil.dp2px(context, 50);
        mParams.height = DimensionUtil.dp2px(context, 50);
        mParams.x = 650;
        mParams.y = 80;

        directionView = new DirectionView(context);
    }

    public void showFloatWindow() {
        if (!isShowing) {
            EventBus.getDefault().register(this);

            mParams.x = PreferenceUtil.getInstance().getInt(PreferenceUtil.DATA_FLOAT_WINDOW_X, 650);
            mParams.y = PreferenceUtil.getInstance().getInt(PreferenceUtil.DATA_FLOAT_WINDOW_Y, 80);
            mWindowManager.addView(directionView, mParams);
            isShowing = true;
        }
    }

    public void removeFloatWindow() {
        if (isShowing) {
            EventBus.getDefault().unregister(this);

            mWindowManager.removeView(directionView);
            isShowing = false;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(FloatWindowLocationEvent event) {
        mParams.x = event.getX();
        mParams.y = event.getY();
        mWindowManager.updateViewLayout(directionView, mParams);
    }
}
