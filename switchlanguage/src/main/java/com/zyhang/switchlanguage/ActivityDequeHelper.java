package com.zyhang.switchlanguage;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by zyhang on 2018/9/13.14:15
 */

public class ActivityDequeHelper {

    private ArrayDeque<Activity> mActivityDeque = new ArrayDeque<>();
    private WeakReference<Activity> mCurrentActivity;

    private static class LazyLoad {
        private static final ActivityDequeHelper INSTANCE = new ActivityDequeHelper();
    }

    public static ActivityDequeHelper getInstance() {
        return LazyLoad.INSTANCE;
    }

    void init(Context context) {
        // collect stack
        ((Application) context.getApplicationContext())
                .registerActivityLifecycleCallbacks(new EmptyActivityLifecycleCallbacks() {
                    @Override
                    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                        if (activity instanceof SwitchLanguageActivity) {
                            return;
                        }
                        mActivityDeque.addFirst(activity);
                    }

                    @Override
                    public void onActivityResumed(Activity activity) {
                        mCurrentActivity = new WeakReference<>(activity);
                    }

                    @Override
                    public void onActivityDestroyed(Activity activity) {
                        if (activity instanceof SwitchLanguageActivity) {
                            return;
                        }
                        mActivityDeque.remove(activity);
                    }
                });
    }

    /**
     * getActivityDeque
     *
     * @return activity队列
     */
    @NonNull
    public ArrayDeque<Activity> getActivityDeque() {
        return mActivityDeque;
    }

    /**
     * getTopActivity
     *
     * @return 栈顶activity
     */
    @NonNull
    public Activity getTopActivity() {
        Activity activity = mActivityDeque.peekFirst();
        if (activity == null) {
            throw new NoSuchElementException("not allow multi process");
        }
        return activity;
    }

    /**
     * getCurrentActivity
     *
     * @return 当前activity
     */
    @Nullable
    public Activity getCurrentActivity() {
        return mCurrentActivity != null ? mCurrentActivity.get() : null;
    }

    /**
     * getCurrentActivityTop
     *
     * @return 当前activity / 栈顶activity
     */
    @NonNull
    public Activity getCurrentActivityTop() {
        Activity current = getCurrentActivity();
        return current != null ? current : getTopActivity();
    }

    /**
     * 重建队列里所有activity
     */
    public void recreateAll() {
        Iterator<Activity> iterator = mActivityDeque.descendingIterator();
        while (iterator.hasNext()) {
            iterator.next().recreate();
        }
    }

    private static class EmptyActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        }

        @Override
        public void onActivityStarted(Activity activity) {
        }

        @Override
        public void onActivityResumed(Activity activity) {
        }

        @Override
        public void onActivityPaused(Activity activity) {
        }

        @Override
        public void onActivityStopped(Activity activity) {
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
        }
    }
}
