package com.aripir.apps.tweeter.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;


import com.aripir.apps.tweeter.adapters.TweetsPagerAdapter;
import com.aripir.apps.tweeter.fragments.TweetsListFragment;
import com.aripir.apps.tweeter.network.TwitterApplication;
import com.aripir.apps.tweeter.network.TwitterClient;
import com.aripir.apps.tweeter.adapters.TweetAdapter;
import com.aripir.apps.tweeter.fragments.NewTweetDialogFragment;
import com.aripir.apps.tweeter.listeners.EndlessRecyclerViewScrollListener;
import com.aripir.apps.tweeter.models.Tweet;
import com.codepath.apps.tweeter.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.List;
import android.os.Handler;
import android.widget.TableLayout;


import cz.msebera.android.httpclient.Header;

public class TimeLineActivity extends AppCompatActivity implements NewTweetDialogFragment.OnCompleteListener {


    private static String maxId;
    Handler myHandler = new Handler();  // android.os.Handler
    private TabLayout tabLayout;


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


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0)
                    tabLayout.getTabAt(0).setIcon(R.drawable.ic_home_selected);
                else if (tab.getPosition() == 1)
                    tabLayout.getTabAt(1).setIcon(R.drawable.ic_mentions_selected);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

                if (tab.getPosition() == 0)
                    tabLayout.getTabAt(0).setIcon(R.drawable.ic_home);

                if (tab.getPosition() == 1)
                    tabLayout.getTabAt(1).setIcon(R.drawable.ic_mentions);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

                if (tab.getPosition() == 0) {
                    tabLayout.getTabAt(0).setIcon(R.drawable.ic_home_selected);
                    tabLayout.getTabAt(1).setIcon(R.drawable.ic_mentions);
                } else if (tab.getPosition() == 1) {
                    tabLayout.getTabAt(0).setIcon(R.drawable.ic_home);
                    tabLayout.getTabAt(1).setIcon(R.drawable.ic_mentions_selected);
                }

            }
        });

    }

    public void onNewTweet(View view) {
        //Toast.makeText(getApplicationContext(), "Floater clicked", Toast.LENGTH_LONG ).show();
        FragmentManager fm = getSupportFragmentManager();
        NewTweetDialogFragment newTweetDialogFragment = NewTweetDialogFragment.newInstance("Compose Tweet");
        newTweetDialogFragment.show(fm, "fragment_edit_name");
        //editNameDialogFragment.show(fm, "fragment_edit_name");
    }

    @Override
    public void onComplete(String tweetText) {
       Log.d("Debug", tweetText);
    }
}