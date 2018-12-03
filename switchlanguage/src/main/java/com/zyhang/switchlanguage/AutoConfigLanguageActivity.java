package com.zyhang.switchlanguage;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by zyhang on 2018/9/12.10:58
 */

abstract public class AutoConfigLanguageActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(SwitchLanguageUtils.configLanguage(newBase));
    }
}
