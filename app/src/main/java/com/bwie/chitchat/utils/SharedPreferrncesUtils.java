package com.bwie.chitchat.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 *
 * @author Bonnenu1t丶
 * 类的作用：这个用共享参数保存的工具类
 */
public class SharedPreferrncesUtils {

    private static final String FILE_NAME="info";
    /**
     * 保存数据的一个方法
     * @param context
     * @param key
     * @param value
     */
    public static void saveParams(Context context, String key, Object value){
        
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        //保存数据
        SharedPreferences.Editor edit = sp.edit();
        String type = value.getClass().getSimpleName();
        if ("Integer".equals(type)) {
            edit.putInt(key, (Integer) value);
        }else if ("Float".equals(type)) {
            edit.putFloat(key, (Float) value);
        }else if ("Boolean".equals(type)) {
            edit.putBoolean(key, (Boolean) value);
        }else if ("Long".equals(type)) {
            edit.putLong(key, (Long) value);
        }else if ("String".equals(type)) {
            edit.putString(key, (String) value);
        }
        edit.commit();
    }
    
    /**
     * 获取相关的参数值
     * @param context
     * @param key
     * @param value
     * @return
     */
    public static Object getParamsValue(Context context,String key,Object value){
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        String type = value.getClass().getSimpleName();
        if ("Integer".equals(type)) {
            return sp.getInt(key, 0);
        }else if ("Float".equals(type)) {
            return sp.getFloat(key, 0f);
        }else if ("Boolean".equals(type)) {
            return sp.getBoolean(key, false);
        }else if ("Long".equals(type)) {
            return sp.getLong(key, 0);
        }else if ("String".equals(type)) {
            return sp.getString(key, "");
        }
        
        return null;
    }
}