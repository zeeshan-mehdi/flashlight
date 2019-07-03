package com.shaikhutech.flashlight;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class FlashLightActivity extends AppCompatActivity {

    boolean flag = true;

    ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_light);
        constraintLayout = findViewById(R.id.main);


    }

    public void OnClick(View view) {

        if(flag){
            constraintLayout.setBackgroundColor(getResources().getColor(R.color.white));
        }else{
            constraintLayout.setBackgroundColor(getResources().getColor(R.color.black));
        }

        flag = !flag;

    }
}
