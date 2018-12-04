package com.zyhang.switchlanguage;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.zyhang.activitydeque.ActivityDeque;

import java.util.Locale;
import java.util.StringTokenizer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by zyhang on 2018/9/12.09:29
 */

class SwitchLanguageHelper {

    private static final String TAG = "SwitchLanguageHelper";

    @Nullable
    private Locale mLocale;

    private static class LazyLoad {
        private static final SwitchLanguageHelper INSTANCE = new SwitchLanguageHelper();
    }

    static SwitchLanguageHelper getInstance() {
        return LazyLoad.INSTANCE;
    }

    void startSwitch(@NonNull Locale locale, long delayMillis) {
        mLocale = locale;
        // get the top activity from stack
        Activity topActivity = ActivityDeque.getInstance().getTopActivity();
        if (topActivity == null) {
            System.out.println("ActivityDeque.getInstance().getTopActivity() == null");
            return;
        }
        // store to sp
        storeLocal(topActivity.getApplicationContext(), locale);
        // delay recreate
        new RecreateHandler(topActivity.getApplicationContext())
                .sendEmptyMessageDelayed(1, delayMillis);
        // start switch anim
        SwitchLanguageActivity.start(topActivity);
    }

    @Nullable
    Locale getLocale(@NonNull Context context) {
        return mLocale != null ? mLocale : getStoreLocal(context);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private static void storeLocal(@NonNull Context context, @NonNull Locale locale) {
        String languageTag;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            languageTag = locale.toLanguageTag();
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(locale.getLanguage());
            if (!TextUtils.isEmpty(locale.getCountry())) {
                sb.append("_");
                sb.append(locale.getCountry());
            }
            languageTag = sb.toString();
        }
        PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext())
                .edit()
                .putString(TAG, languageTag)
                .apply();
    }

    @Nullable
    private static Locale getStoreLocal(@NonNull Context context) {
        String languageTag = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext())
                .getString(TAG, null);
        if (TextUtils.isEmpty(languageTag)) {
            return null;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return Locale.forLanguageTag(languageTag);
        } else {
            String lang = "", country = "";
            StringTokenizer st = new StringTokenizer(languageTag, "_");
            if (st.hasMoreTokens()) {
                lang = st.nextToken();
            }
            if (st.hasMoreTokens()) {
                country = st.nextToken();
            }
            return new Locale(lang, country);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private static class RecreateHandler extends Handler {

        private Context mApplicationContext;

        private RecreateHandler(Context applicationContext) {
            mApplicationContext = applicationContext;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ActivityDeque.getInstance().recreateAll(SwitchLanguageActivity.class);
            SwitchLanguageUtils.endSwitchLanguage(mApplicationContext);
        }
    }
}
