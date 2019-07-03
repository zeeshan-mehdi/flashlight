package com.shaikhutech.flashlight;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        getSupportActionBar().setTitle("About");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            this.startActivity(new Intent(AboutActivity.this,MainActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void app_telegram(View view) {
        String url = getString(R.string.telegramlink);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    public void app_insta(View view) {
        String url = getString(R.string.instagramlink);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    public void app_rate(View view) {
        Uri uri = Uri.parse(getString(R.string.appratelink));
        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);
        likeIng.setPackage("com.android.vending");
        try {
            startActivity(likeIng);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(getString(R.string.appratelink))));
        }
    }

    public void app_share(View view) {
        Intent shareintent = new Intent(  );
        shareintent.setAction( Intent.ACTION_SEND );
        shareintent.putExtra( Intent.EXTRA_TEXT, getString(R.string.app_name) );
        shareintent.setType( "text/plain" );
        startActivity( shareintent );
    }

    public void app_donate(View view) {
        Intent cnn = new Intent(AboutActivity.this, DonateActivity.class);
        startActivity(cnn);
    }
}
