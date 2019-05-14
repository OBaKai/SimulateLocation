package com.llk.sl.floatwindow;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.kongqw.rockerlibrary.view.RockerView;
import com.llk.sl.R;

public class DirectionView extends FrameLayout {
    private RockerView rockerView;

    public DirectionView(@NonNull Context context) {
        super(context);

        initView(context);
    }

    public DirectionView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initView(context);
    }

    public DirectionView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.window_direction, this);

        rockerView = view.findViewById(R.id.rockerview);
    }
}
