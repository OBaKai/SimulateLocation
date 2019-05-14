package com.llk.sl.floatwindow;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.kongqw.rockerlibrary.view.RockerView;
import com.llk.sl.R;


public class DirectionView extends FrameLayout {
    private final String tag = this.getClass().getSimpleName();

    private Handler mHandler = new Handler(Looper.getMainLooper());
    private LocationChangeRunnable runnable;
    private RockerView.Direction direction = RockerView.Direction.DIRECTION_CENTER;

    private RockerView rockerView;

    public DirectionView(@NonNull Context context) {
        super(context);

        initRunnable();
        initView(context);
    }

    public DirectionView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initRunnable();
        initView(context);
    }

    public DirectionView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initRunnable();
        initView(context);
    }

    private void initRunnable() {
        if (runnable == null) {
            runnable = new LocationChangeRunnable();
        }
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.window_direction, this);

        rockerView = view.findViewById(R.id.rockerview);
        rockerView.setCallBackMode(RockerView.CallBackMode.CALL_BACK_MODE_STATE_CHANGE);
        rockerView.setOnShakeListener(RockerView.DirectionMode.DIRECTION_4_ROTATE_45, new RockerView.OnShakeListener() {
            @Override
            public void onStart() {
                Log.i(tag, "onStart!!!");
                mHandler.post(runnable);
            }

            @Override
            public void direction(RockerView.Direction direction) {
                Log.i(tag, "direction!!! direction=" + direction);
                DirectionView.this.direction = direction;
            }

            @Override
            public void onFinish() {
                Log.i(tag, "onFinish!!!");
                mHandler.removeCallbacks(runnable);
                direction = RockerView.Direction.DIRECTION_CENTER;
            }
        });
    }

    private class LocationChangeRunnable implements Runnable {
        @Override
        public void run() {
            switch (direction) {
                case DIRECTION_UP:
                    LocationMoveManager.getInstance().setLatitude(LocationMoveManager.getInstance().getLatitude() + 0.00005);
                    break;
                case DIRECTION_DOWN:
                    LocationMoveManager.getInstance().setLatitude(LocationMoveManager.getInstance().getLatitude() - 0.00005);
                    break;
                case DIRECTION_LEFT:
                    LocationMoveManager.getInstance().setLongitude(LocationMoveManager.getInstance().getLongitude() - 0.00005);
                    break;
                case DIRECTION_RIGHT:
                    LocationMoveManager.getInstance().setLongitude(LocationMoveManager.getInstance().getLongitude() + 0.00005);
                    break;
            }


            mHandler.postDelayed(this, 100);
        }
    }
}
