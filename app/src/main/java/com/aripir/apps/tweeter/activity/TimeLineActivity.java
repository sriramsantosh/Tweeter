package com.aripir.apps.tweeter.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.aripir.apps.tweeter.adapters.TweetsPagerAdapter;
import com.aripir.apps.tweeter.R;
import com.aripir.apps.tweeter.fragments.ReplyTweetDialogFragment;
import com.aripir.apps.tweeter.network.TwitterApplication;
import com.aripir.apps.tweeter.network.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class TimeLineActivity extends AppCompatActivity implements ReplyTweetDialogFragment.OnCompleteListener {

    private TabLayout tabLayout;
    private TwitterClient twitterClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager(), this));

        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        twitterClient = TwitterApplication.getRestClient();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    public void onProfileView(MenuItem item) {
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
        finish();
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home_selected);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_mentions);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_direct_message);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0)
                    tabLayout.getTabAt(0).setIcon(R.drawable.ic_home_selected);
                else if (tab.getPosition() == 1)
                    tabLayout.getTabAt(1).setIcon(R.drawable.ic_mentions_selected);
                else if (tab.getPosition() == 2)
                    tabLayout.getTabAt(2).setIcon(R.drawable.ic_direct_message_selected);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

                if (tab.getPosition() == 0)
                    tabLayout.getTabAt(0).setIcon(R.drawable.ic_home);

                if (tab.getPosition() == 1)
                    tabLayout.getTabAt(1).setIcon(R.drawable.ic_mentions);

                if (tab.getPosition() == 2)
                    tabLayout.getTabAt(2).setIcon(R.drawable.ic_direct_message);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

                if (tab.getPosition() == 0) {
                    tabLayout.getTabAt(0).setIcon(R.drawable.ic_home_selected);
                    tabLayout.getTabAt(1).setIcon(R.drawable.ic_mentions);
                    tabLayout.getTabAt(2).setIcon(R.drawable.ic_direct_message);
                } else if (tab.getPosition() == 1) {
                    tabLayout.getTabAt(0).setIcon(R.drawable.ic_home);
                    tabLayout.getTabAt(1).setIcon(R.drawable.ic_mentions_selected);
                    tabLayout.getTabAt(2).setIcon(R.drawable.ic_direct_message);
                } else if (tab.getPosition() == 2) {
                    tabLayout.getTabAt(0).setIcon(R.drawable.ic_home);
                    tabLayout.getTabAt(1).setIcon(R.drawable.ic_mentions);
                    tabLayout.getTabAt(2).setIcon(R.drawable.ic_direct_message_selected);
                }

            }
        });


    }


    @Override
    public void onComplete(String replyText, String id) {
        Log.d("DEBUG", replyText);
        replyToTweet(replyText, id);
    }

    private void replyToTweet(String status, String id){

        twitterClient.sendReply(status, id, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("SUCCESS", response.toString());
                Toast.makeText(getApplicationContext(), "Tweet successfully sent", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("FAILURE", throwable.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("FAILURE", errorResponse.toString());
                Log.d("FAILURE", throwable.toString());
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("FAILURE", errorResponse.toString());
                Log.d("FAILURE", throwable.toString());            }
        });

    }

}