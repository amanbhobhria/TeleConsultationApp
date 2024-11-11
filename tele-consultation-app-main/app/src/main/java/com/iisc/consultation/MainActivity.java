package com.iisc.consultation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.iisc.R;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String PREFS_KEY_FIRST_LAUNCH = "IsFirstLaunch";
    private static final int SPLASH_SCREEN_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        try {
            TextView version = findViewById(R.id.versionView);
            String versionName = "Version " + getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            version.setText(versionName);
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
        new Handler().postDelayed(() -> {
            SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            boolean isFirstLaunch = sharedPreferences.getBoolean(PREFS_KEY_FIRST_LAUNCH, true);
            Intent intent;
            if (isFirstLaunch) {
                intent = new Intent(MainActivity.this, OnBoardActivity.class);
            } else {
                intent = new Intent(MainActivity.this, AskRole.class);
            }
            startActivity(intent);
            finish();
        }, SPLASH_SCREEN_TIME_OUT);
    }

}