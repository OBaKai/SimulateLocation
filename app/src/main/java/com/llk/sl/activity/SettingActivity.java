package com.llk.sl.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.llk.sl.R;
import com.llk.sl.eventbus.FloatWindowLocationEvent;
import com.llk.sl.util.PreferenceUtil;

import org.greenrobot.eventbus.EventBus;

public class SettingActivity extends AppCompatActivity {
    private EditText x;
    private EditText y;
    private Button confirm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.act_setting);

        x = findViewById(R.id.float_window_x);
        y = findViewById(R.id.float_window_y);
        confirm = findViewById(R.id.float_window_confirm);

        x.setText(PreferenceUtil.getInstance().getInt(PreferenceUtil.DATA_FLOAT_WINDOW_X, 650) + "");
        y.setText(PreferenceUtil.getInstance().getInt(PreferenceUtil.DATA_FLOAT_WINDOW_Y, 80) + "");

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int xLocation = Integer.parseInt(x.getText().toString());
                int yLocation = Integer.parseInt(y.getText().toString());

                PreferenceUtil.getInstance().putInt(PreferenceUtil.DATA_FLOAT_WINDOW_X, xLocation);
                PreferenceUtil.getInstance().putInt(PreferenceUtil.DATA_FLOAT_WINDOW_Y, yLocation);

                EventBus.getDefault().post(new FloatWindowLocationEvent(xLocation, yLocation));
            }
        });
    }
}
