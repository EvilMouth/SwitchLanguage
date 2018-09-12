package com.zyhang.switchlanguage;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.lang.ref.WeakReference;
import java.util.Locale;
import java.util.StringTokenizer;

/**
 * Created by zyhang on 2018/9/12.09:29
 */

class SwitchLanguageHelper {

    private static final String TAG = "SwitchLanguageHelper";

    @Nullable
    private WeakReference<Activity> mRecreateActivityWeakReference;
    @Nullable
    private Locale mLocale;

    private static class LazyLoad {
        private static final SwitchLanguageHelper INSTANCE = new SwitchLanguageHelper();
    }

    static SwitchLanguageHelper getInstance() {
        return LazyLoad.INSTANCE;
    }

    void startSwitch(@NonNull Activity recreateActivity, @NonNull Locale locale, long delayMillis) {
        mRecreateActivityWeakReference = new WeakReference<>(recreateActivity);
        mLocale = locale;

        // store to sp
        storeLocal(recreateActivity, locale);

        // start switch
        final Context applicationContext = recreateActivity.getApplicationContext();
        new Handler().postDelayed(() -> {
            Activity activity = getRecreateActivity();
            if (activity != null) {
                activity.recreate();
            }
            SwitchLanguageUtils.endSwitchLanguage(applicationContext);
        }, delayMillis);
    }

    @Nullable
    private Activity getRecreateActivity() {
        return mRecreateActivityWeakReference != null ? mRecreateActivityWeakReference.get() : null;
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
}
