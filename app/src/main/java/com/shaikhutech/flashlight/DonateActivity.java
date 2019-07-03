package com.shaikhutech.flashlight;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardedVideoAd;

public class DonateActivity extends AppCompatActivity implements AppData {


    CardView cookie,dietcola,coffee,fastfoot,gift,video;
    CardView button;
    BillingProcessor bp;
    private boolean readyToPurchase = false;
    private RewardedVideoAd mRewardedVideoAd;
    private static final String LOG_TAG = "iabv3";
    SharedPreferences prefs;
    private InterstitialAd mInterstitialAd;
    float userPoints;
    BillingProcessor billingProcessor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);

        getSupportActionBar().setTitle(getString(R.string.support_development));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);

        MobileAds.initialize(this,
                "ca-app-pub-6403649411849064/6065339651");

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-6403649411849064/6065339651");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


        cookie = (CardView) findViewById(R.id.cookie);
        coffee = (CardView) findViewById(R.id.coffee);
        fastfoot = (CardView) findViewById(R.id.fastfoot);
        gift = (CardView) findViewById(R.id.gift);
        video = (CardView) findViewById(R.id.video);

        prefs = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        ////// billing processor

        if(!BillingProcessor.isIabServiceAvailable(this)) {

        }

        bp = new BillingProcessor(this, GOOGLE_BILLING_KEY, new BillingProcessor.IBillingHandler() {
            @Override
            public void onProductPurchased(String productId, TransactionDetails details) {


                if (productId.equals(BUY_COOKIE)){
                }
                if (productId.equals(BUY_COFFEE)){
                }
                if (productId.equals(BUY_FASTFOOD)){
                }
                if (productId.equals(BUY_GIFT)){
                }


            }
            @Override
            public void onBillingError(int errorCode, Throwable error) {


            }
            @Override
            public void onBillingInitialized() {
                readyToPurchase = true;

            }
            @Override
            public void onPurchaseHistoryRestored() {
                for(String sku : bp.listOwnedProducts())
                    System.out.println("Owned Managed Product: " + sku);
                for(String sku : bp.listOwnedSubscriptions())
                    System.out.println("Owned Subscription: " + sku);
                // updateTextViews();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            this.startActivity(new Intent(DonateActivity.this,AboutActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        this.startActivity(new Intent(DonateActivity.this,AboutActivity.class));
        finish();
    }

    public void setCookie(View view){
        bp.purchase(DonateActivity.this,BUY_COOKIE);
    }


    public void setCoffee(View view){
        bp.purchase(DonateActivity.this,BUY_COFFEE);
    }


    public void setFastfoot(View view){
        bp.purchase(DonateActivity.this,BUY_FASTFOOD);
    }


    public void setGift(View view){
        bp.purchase(DonateActivity.this,BUY_GIFT);
    }

    public void consumeInapp (String productId){
        Boolean consumed = bp.consumePurchase(productId);
    }


    @Override
    public void onDestroy() {
        if (bp != null)
            bp.release();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
    }


    public void setVideo(View view) {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }
    }
}

