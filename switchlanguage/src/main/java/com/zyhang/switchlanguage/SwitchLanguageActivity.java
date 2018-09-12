package com.zyhang.switchlanguage;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by zyhang on 2018/9/11.18:07
 */

public class SwitchLanguageActivity extends AutoConfigLanguageActivity implements Application.ActivityLifecycleCallbacks {

    public static final String TAG = "SwitchLanguageActivity";

    private FinishBroadcastReceiver mFinishBroadcastReceiver;
    private String mRecreateActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch_language);

        mFinishBroadcastReceiver = new FinishBroadcastReceiver(this);
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(mFinishBroadcastReceiver, new IntentFilter(TAG));

        long delayMillis = getIntent().getLongExtra("delayMillis", 400);
        Activity recreateActivity = SwitchLanguageStore.getInstance()
                .getActivity();
        if (recreateActivity != null) {
            getApplication().registerActivityLifecycleCallbacks(this);
            new Handler().postDelayed(recreateActivity::recreate, delayMillis);
        } else {
            // recreateActivity已经被回收，不需要手动recreate，直接返回
            new Handler().postDelayed(this::finish, delayMillis);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this)
                .unregisterReceiver(mFinishBroadcastReceiver);

        getApplication().unregisterActivityLifecycleCallbacks(this);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.switch_a_in, R.anim.switch_b_out);
    }

    @Override
    public void onBackPressed() {
        // disable
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private static class FinishBroadcastReceiver extends BroadcastReceiver {

        private SwitchLanguageActivity mSwitchLanguageActivity;

        public FinishBroadcastReceiver(SwitchLanguageActivity switchLanguageActivity) {
            mSwitchLanguageActivity = switchLanguageActivity;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (SwitchLanguageActivity.TAG.equals(intent.getAction())) {
                mSwitchLanguageActivity.finish();
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (savedInstanceState != null && activity.getClass().getName().equals(mRecreateActivity)) {
            SwitchLanguageUtils.endSwitchLanguage(activity);
        }
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
        if (activity == SwitchLanguageStore.getInstance().getActivity()) {
            mRecreateActivity = activity.getClass().getName();
        }
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }
}
