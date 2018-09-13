package com.zyhang.switchlanguage;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import java.util.ArrayDeque;

/**
 * Created by zyhang on 2018/9/13.14:15
 */

public class ActivityDequeHelper {

    @NonNull
    private ArrayDeque<Activity> mActivityDeque = new ArrayDeque<>();

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
                        mActivityDeque.add(activity);
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

    @NonNull
    public ArrayDeque<Activity> getActivityDeque() {
        return mActivityDeque;
    }

    public void recreateAll() {
        for (Activity activity : mActivityDeque) {
            activity.recreate();
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
