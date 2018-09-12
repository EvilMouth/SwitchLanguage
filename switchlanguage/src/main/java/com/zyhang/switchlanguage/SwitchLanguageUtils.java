package com.zyhang.switchlanguage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;

import java.util.Locale;

/**
 * Created by zyhang on 2018/9/11.18:09
 */

public class SwitchLanguageUtils {

    public static Context configLanguage(@NonNull Context newBase) {
        Configuration configuration = Resources.getSystem().getConfiguration();

        Locale locale = SwitchLanguageStore.getInstance()
                .getLocale(newBase);
        if (locale != null) {
            configuration.setLocale(locale);
        }

        return newBase.createConfigurationContext(configuration);
    }

    public static void startSwitchLanguage(@NonNull Activity recreateActivity, @NonNull Locale locale, @IntRange(from = 400) long delayMillis) {
        SwitchLanguageStore.getInstance()
                .store(recreateActivity, locale);

        Intent intent = new Intent(recreateActivity, SwitchLanguageActivity.class);
        intent.putExtra("delayMillis", delayMillis);
        recreateActivity.startActivity(intent);
        recreateActivity.overridePendingTransition(R.anim.switch_b_in, R.anim.switch_a_out);
    }

    public static void endSwitchLanguage(@NonNull Context context) {
        LocalBroadcastManager.getInstance(context)
                .sendBroadcast(new Intent(SwitchLanguageActivity.TAG));
    }
}
