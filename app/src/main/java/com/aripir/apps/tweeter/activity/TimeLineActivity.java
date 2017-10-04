package com.aripir.apps.tweeter.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;


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


import cz.msebera.android.httpclient.Header;

public class TimeLineActivity extends AppCompatActivity {


    private static String maxId;
    Handler myHandler = new Handler();  // android.os.Handler


    TweetsListFragment tweetsListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        tweetsListFragment = (TweetsListFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_timeline);


        setToolBar();
    }



    public void onNewTweet(View view) {
        //Toast.makeText(getApplicationContext(), "Floater clicked", Toast.LENGTH_LONG ).show();
        FragmentManager fm = getSupportFragmentManager();
        NewTweetDialogFragment newTweetDialogFragment = NewTweetDialogFragment.newInstance("Compose Tweet");
        newTweetDialogFragment.show(fm, "fragment_edit_name");
        //editNameDialogFragment.show(fm, "fragment_edit_name");
    }


    private void setToolBar(){
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setLogo(R.drawable.ic_twitter_icon);
//        setSupportActionBar(toolbar);
    }
}
