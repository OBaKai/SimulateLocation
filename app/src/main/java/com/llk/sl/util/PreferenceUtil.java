package com.llk.sl.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2018/9/17.
 */

public class PreferenceUtil {
    private static final String DATA = "data";

    public static final String DATA_FLOAT_WINDOW_X = "float_window_x";
    public static final String DATA_FLOAT_WINDOW_Y = "float_window_y";

    public static PreferenceUtil getInstance() {
        return InnerHolder.mInstance;
    }

    private static class InnerHolder {
        private static PreferenceUtil mInstance = new PreferenceUtil();
    }

    private SharedPreferences preferences;

    public void init(Context context){
        preferences = context.getSharedPreferences(DATA, Context.MODE_PRIVATE);
    }

    /**
     * 保存数据
     * @param dataKey 数据键值
     * @param data json字符串数据
     */
    public void putString(String dataKey, String data){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(dataKey, data);
        editor.apply();
    }

    /**
     * 保存数据
     * @param dataKey 数据键值
     * @param data
     */
    public void putInt(String dataKey, int data){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(dataKey, data);
        editor.apply();
    }

    /**
     * 获取保存的数据
     * @param dataKey 数据键值
     * @param defaultValue 默认值
     * @return json字符串数据
     */
    public String getString(String dataKey, String defaultValue){
        return preferences.getString(dataKey, defaultValue);
    }

    /**
     * 获取保存的数据
     * @param dataKey 数据键值
     * @param defaultValue 默认值
     * @return json字符串数据
     */
    public int getInt(String dataKey, int defaultValue){
        return preferences.getInt(dataKey, defaultValue);
    }
}
