package com.zyhang.switchlanguage;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by zyhang on 2018/9/11.18:07
 */

public class SwitchLanguageActivity extends AutoConfigLanguageActivity {

    private static final String TAG = "SwitchLanguageActivity";

    private FinishBroadcastReceiver mFinishBroadcastReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch_language);
        fullScreen();

        mFinishBroadcastReceiver = new FinishBroadcastReceiver(this);
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(mFinishBroadcastReceiver, new IntentFilter(TAG));
    }

    private void fullScreen() {
        int visibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            visibility |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }
        getWindow().getDecorView().setSystemUiVisibility(visibility);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this)
                .unregisterReceiver(mFinishBroadcastReceiver);
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

    static void start(@NonNull Activity activity) {
        Intent intent = new Intent(activity, SwitchLanguageActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.switch_b_in, R.anim.switch_a_out);
    }

    /**
     * end switching language
     * finish this activity
     *
     * @param context context to get localBroadcastManager
     */
    static void destroy(@NonNull Context context) {
        LocalBroadcastManager.getInstance(context)
                .sendBroadcast(new Intent(TAG));
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
}
