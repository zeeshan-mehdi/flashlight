package com.shaikhutech.flashlight;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.crashlytics.android.Crashlytics;
import com.shaikhutech.flashlight.Compass.CompassActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.jaygoo.widget.OnRangeChangedListener;
import com.jaygoo.widget.RangeSeekBar;

import io.fabric.sdk.android.Fabric;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener{

    RangeSeekBar rangeSeekBar;

    Activity mActivity;

    boolean flag= false;

    Timer timer;
    TimerTask timerTask;


    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    Toolbar mToolbar;

    int left=0,right=0;
    private ImageView torchh;
    private boolean lightIsOn = false;
    Camera cam;
    private CameraManager camManager;
    Camera.Parameters para;
    private InterstitialAd mInterstitialAd;
    private AdView mAdView;

    ArrayList<Integer> delays;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        torchh = (ImageView) findViewById(R.id.torch);

        mActivity = this;

        rangeSeekBar = findViewById(R.id.range);


        delays = new ArrayList<>();


        delays.add(2000);
        delays.add(1800);
        delays.add(1600);
        delays.add(1400);
        delays.add(1200);
        delays.add(1000);
        delays.add(800);
        delays.add(600);
        delays.add(400);
        delays.add(200);

        CharSequence cs[];

        cs = new String[10];

        cs[0]= "0";
        cs[1]= "1";
        cs[2]= "2";
        cs[3]= "3";
        cs[4]= "4";
        cs[5]= "5";
        cs[6]= "6";
        cs[7]= "7";
        cs[8]= "8";
        cs[9]= "9";


        initDrawerMenu(true);


        rangeSeekBar.setTickMarkTextArray(cs);

        rangeSeekBar.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                left = (int) leftValue;
                right = (int) rightValue;

            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {

                if(left >0){
                    flag = true;
                    if(timer==null)
                        runTimer();
                    else{
                        timer.cancel();
                        timer.purge();
                        timer = null;
                        runTimer();

                    }

                }else{
                    flag= false;
                    if(timer!=null) {

                        timer.cancel();
                        timer.purge();
                        timer = null;

                        if(lightIsOn){
                            flashLightOff();
                            torchh.setImageResource(R.drawable.light_turn_off_);

                            lightIsOn = ! lightIsOn;
                        }
                    }
                }
            }
        });

        torchh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();

                flag= false;
                if(timer!=null) {
                    left = 0;
                    timer.cancel();
                    timer.purge();
                    timer = null;
                    rangeSeekBar.setValue(left);
                }


                if (id == R.id.torch) {
                    if (lightIsOn) {
                        flashLightOff();
                        torchh.setImageResource(R.drawable.light_turn_off_);
                        if (mInterstitialAd.isLoaded()) {
                            mInterstitialAd.show();
                        } else {
                            Log.d("TAG", "The interstitial wasn't loaded yet.");
                        }
                    } else {
                        flashLightOn();
                        torchh.setImageResource(R.drawable.main_button);
                    }
                    lightIsOn = !lightIsOn;
                }
            }
        });

        prepareAd();
        ScheduledExecutorService scheduled = Executors.newSingleThreadScheduledExecutor();
        scheduled.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mInterstitialAd.isLoaded()) {
                            mInterstitialAd.show();
                        } else {
                            Log.d("TAG", "The interstitial wasn't loaded yet.");
                        }
                        prepareAd();
                    }
                });
            }
        },10,30, TimeUnit.SECONDS);
    }

    private void prepareAd() {
        MobileAds.initialize(this,
                "ca-app-pub-6403649411849064/6065339651");
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-6403649411849064/6065339651");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void flashLightOn() {
        rangeSeekBar.setActivated(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (camManager == null) {
                camManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
            }
            try {
                String cameraId = camManager.getCameraIdList()[0];
                camManager.setTorchMode(cameraId, true);
            } catch (Exception e) {
                showError(e);
            }
            return;
        }
        try {
            if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                cam = Camera.open();
                Camera.Parameters p = cam.getParameters();
                p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                cam.setParameters(p);
                cam.startPreview();
            }
        } catch (Exception e) {
            showError(e);
        }
    }
    private void flashLightOff() {
        rangeSeekBar.setActivated(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                String cameraId = camManager.getCameraIdList()[0];
                camManager.setTorchMode(cameraId, false);



            } catch (Exception e) {
                showError(e);
            }
            return;
        }
        try {
            if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                cam.stopPreview();
                cam.release();
                cam = null;
            }


        } catch (Exception e) {
            showError(e);
        }
    }
    private void showError(Exception e) {
        final String message = "Error: " + e.getMessage();
        // create a handler to post messages to the main thread
        Handler mHandler = new Handler(getMainLooper());
        mHandler.post(new Runnable() {
            @Override
            public void run() {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);

                alertDialogBuilder.setTitle("An Error Occured");
                alertDialogBuilder.setMessage(message);
                alertDialogBuilder.setCancelable(true);

                alertDialogBuilder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        left = 0;
                        flag = false;

                        if(timer !=null){
                            timer.cancel();
                            timer.purge();
                            timer = null;
                        }

                        rangeSeekBar.setValue(left);
                        dialog.cancel();
                        // dialog.dismiss();
                    }
                });

                alertDialogBuilder.create().show();
            }
        });

    }

    public void police(View view) {
        Intent cnn = new Intent( MainActivity.this,PoliceActivity.class );
        startActivity( cnn );
    }
    public void light(View view) {
        Intent cnn = new Intent( MainActivity.this,ScreenLightActivity.class );
        startActivity( cnn );
    }
    public void warning() {
        Intent cnn = new Intent( MainActivity.this,WarningActivity.class );
        startActivity( cnn );
    }
    public void compass(View view) {
        Intent cnn = new Intent( MainActivity.this,CompassActivity.class );
        startActivity( cnn );
    }
    public void disco() {
        Intent cnn = new Intent( MainActivity.this,DiscoActivity.class );
        startActivity( cnn );
    }
    public void about() {
        Intent cnn = new Intent( MainActivity.this,AboutActivity.class );
        startActivity( cnn );
    }

    public void runTimer(){
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {

                if(!flag){
                    timer.cancel();
                    timer.purge();
                    timer = null;
                }

                if(lightIsOn){
                    flashLightOff();

                }else{
                    flashLightOn();
                }
                lightIsOn = !lightIsOn;
            }
        };


        if(timer!=null&&flag){
            timer.schedule(timerTask,0,delays.get(left-1));
        }
    }

    public void sos(View view) {

        try {
            left = 1;
            flag = true;
            if (timer == null)
                runTimer();
            else {
                timer.cancel();
                timer.purge();
                timer = null;
                runTimer();

            }

            rangeSeekBar.setValue(left);
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public void initDrawerMenu(boolean enable) {

        mToolbar = findViewById(R.id.toolbar);

        try {

            setSupportActionBar(mToolbar);

            mToolbar.setTitle(getString(R.string.app_name));
        }catch (Exception e){
            e.printStackTrace();
        }

        // Initialize drawer view
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        if(enable) {
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle
                    (this, mDrawerLayout, mToolbar, R.string.openDrawer, R.string.closeDrawer) {
                public void onDrawerClosed(View view) {
                    super.onDrawerClosed(view);

                }

                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);

                }
            };

            mDrawerLayout.setDrawerListener(toggle);
            toggle.syncState();

            mNavigationView = (NavigationView) findViewById(R.id.navigationDrawer);
            mNavigationView.setItemIconTintList(null);
            getNavigationView().setNavigationItemSelectedListener(this);
        } else {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_home) {
            disco();

        } else if (id == R.id.action_page1) {
            about();
        } else if (id == R.id.action_page2) {
            //show warning here

            warning();
        }

        // support
        else if (id == R.id.action_message) {
            AppUtils.sendSMS(mActivity, AppConstant.SMS_NUMBER, AppConstant.SMS_TEXT);
        } else if (id == R.id.action_messenger) {
            AppUtils.invokeMessengerBot(mActivity);
        } else if (id == R.id.action_email) {
            AppUtils.sendEmail(mActivity, AppConstant.EMAIL_ADDRESS, AppConstant.EMAIL_SUBJECT, AppConstant.EMAIL_BODY);
        }

        // others
        else if (id == R.id.action_share) {
            AppUtils.shareApp(mActivity);
        } else if (id == R.id.action_rate_app) {
            AppUtils.rateThisApp(mActivity); // this feature will only work after publish the app
        } else if (id == R.id.action_exit) {
            AppUtils.appClosePrompt(mActivity);
        }

        if (getDrawerLayout() != null && getDrawerLayout().isDrawerOpen(GravityCompat.START)) {
            getDrawerLayout().closeDrawer(GravityCompat.START);
        }

        return true;

    }


    @Override
    public void onBackPressed() {
        if (getDrawerLayout() != null && getDrawerLayout().isDrawerOpen(GravityCompat.START)) {
            getDrawerLayout().closeDrawer(GravityCompat.START);
        }

        super.onBackPressed();
    }

    public DrawerLayout getDrawerLayout(){
        return  mDrawerLayout;
    }

    public NavigationView getNavigationView(){
        return mNavigationView;
    }
}