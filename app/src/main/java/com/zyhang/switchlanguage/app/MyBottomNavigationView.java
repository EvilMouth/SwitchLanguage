package com.zyhang.switchlanguage.app;

import android.content.Context;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.util.AttributeSet;

import java.lang.reflect.Field;

/**
 * Created by zyhang on 2018/9/11.14:13
 */

@SuppressWarnings("ALL")
public class MyBottomNavigationView extends BottomNavigationView {
    public MyBottomNavigationView(Context context) {
        super(context);
        init();
    }

    public MyBottomNavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyBottomNavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        try {
            BottomNavigationMenuView mMenuView = getField(BottomNavigationView.class, this, "mMenuView");
            setField(mMenuView.getClass(), mMenuView, "mShiftingMode", false);

            BottomNavigationItemView[] mButtons = getField(mMenuView.getClass(), mMenuView, "mButtons");
            for (BottomNavigationItemView button : mButtons) {
                button.setShiftingMode(false);
            }

            mMenuView.updateMenuView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static <T> T getField(Class targetClass, Object instance, String fieldName) throws Exception {
        Field field = targetClass.getDeclaredField(fieldName);
        field.setAccessible(true);
        return (T) field.get(instance);
    }

    private static void setField(Class targetClass, Object instance, String fieldName, Object value) throws Exception {
        Field field = targetClass.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(instance, value);
    }
}
