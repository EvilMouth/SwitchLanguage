package com.zyhang.switchlanguage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

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

        mFinishBroadcastReceiver = new FinishBroadcastReceiver(this);
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(mFinishBroadcastReceiver, new IntentFilter(TAG));
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
        super.onBackPressed();
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
