package com.shaikhutech.flashlight;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.ImageView;

public class DiscoActivity extends AppCompatActivity {

    ImageView disco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disco);
        disco = (ImageView) findViewById(R.id.discoLight);
        startLight();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.screenBrightness = 1.0f;
        getWindow().setAttributes(params);
    }

    @SuppressLint("WrongConstant")
    public void startLight() {
        ObjectAnimator anim = ObjectAnimator.ofInt(disco,"BackgroundColor", Color.BLUE,Color.RED,Color.DKGRAY,Color.CYAN,Color.GREEN,Color.MAGENTA,Color.WHITE,Color.YELLOW);
        anim.setDuration(7000);
        anim.setEvaluator(new ArgbEvaluator());
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        anim.start();
    }
}
