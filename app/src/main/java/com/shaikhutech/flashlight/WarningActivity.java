package com.shaikhutech.flashlight;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;

public class WarningActivity extends AppCompatActivity {

    protected boolean mWarningLightFlicker;
    protected boolean mWarningLightState;
    protected ImageView imgWarmingup;
    protected ImageView imgWarmingdown;
    protected int mCurrentWarningLightInterval = 500;
    protected SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warning);
        mWarningLightFlicker = true;
        imgWarmingup = (ImageView) findViewById(R.id.img_warming_on);
        imgWarmingdown = (ImageView) findViewById(R.id.img_warming_off);
        new WarningLightThread().start();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.screenBrightness = 1.0f;
        getWindow().setAttributes(params);
    }

    class WarningLightThread extends Thread {
        @Override
        public void run() {
            mWarningLightFlicker = true;
            while (mWarningLightFlicker) {
                try {
                    Thread.sleep(mCurrentWarningLightInterval);
                    mWarningHandler.sendEmptyMessage(0);
                } catch (Exception e) {

                }
            }
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mWarningHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (mWarningLightState) {
                imgWarmingup.setImageResource(R.color.warning);
                imgWarmingdown.setImageResource(R.color.warningy);
                mWarningLightState = false;
            } else {
                imgWarmingup.setImageResource(R.color.warningy);
                imgWarmingdown.setImageResource(R.color.warning);
                mWarningLightState = true;
            }
        }
    };
}
