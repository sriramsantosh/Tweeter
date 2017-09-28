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

public class TimeLineActivity extends AppCompatActivity implements NewTweetDialogFragment.OnCompleteListener {

    private TwitterClient twitterClient;
    private TweetAdapter tweetAdapter;
    List<Tweet> tweets;
    RecyclerView rvTweets;
    private static String maxId;
    static final int POLL_INTERVAL = 3000; // milliseconds
    Handler myHandler = new Handler();  // android.os.Handler
    private ProgressBar pbLoadMore;
    private ProgressBar pbLoading;
    private SwipeRefreshLayout swipeContainer;

    private EndlessRecyclerViewScrollListener scrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        twitterClient = TwitterApplication.getRestClient();

        rvTweets = (RecyclerView) findViewById(R.id.rvTweet);
        tweets =  new ArrayList<>();
        tweetAdapter = new TweetAdapter(tweets);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvTweets.setLayoutManager(layoutManager);

        rvTweets.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));

        rvTweets.setAdapter(tweetAdapter);
        pbLoading = (ProgressBar) findViewById(R.id.pbLoading);
        pbLoadMore = (ProgressBar) findViewById(R.id.pbLoadMore);

        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                pbLoadMore.setVisibility(View.VISIBLE);
                populateTimeLine(getCurrentMaxId());
            }
        };

        rvTweets.addOnScrollListener(scrollListener);


        populateTimeLine(null);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                swipeToRefresh();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        setToolBar();
    }

    private String getCurrentMaxId(){
        Tweet tweet = tweets.get(tweets.size()-1);
        return tweet.id;
    }

    private void populateTimeLine(String maxId) {
     twitterClient.getHomeTimeline(maxId, new JsonHttpResponseHandler(){

         @Override
         public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
             Log.d("TwitterClient", response.toString());
         }

         @Override
         public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
             Log.d("TwitterClient", response.toString());

             for(int i=0; i<response.length(); i++){
                 try {
                     JSONObject responseJson = response.getJSONObject(i);
                     Tweet tweet = Tweet.fromJSON(responseJson);
                     tweets.add(tweet);
                     tweetAdapter.notifyDataSetChanged();
                     //tweetAdapter.notifyItemInserted(tweets.size()-1);

                 }catch (JSONException e){
                    Log.d("DEBUG", e.getLocalizedMessage());
                 }

             }

             pbLoading.setVisibility(View.GONE);
             pbLoadMore.setVisibility(View.GONE);
         }

         @Override
         public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
             Log.d("TwitterClient", responseString);
             throwable.printStackTrace();
         }

         @Override
         public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
             Log.d("TwitterClient", errorResponse.toString());
             throwable.printStackTrace();
         }


         @Override
         public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
             Log.d("TwitterClient", errorResponse.toString());
             throwable.printStackTrace();
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
        tweets.clear();

        pbLoading.setVisibility(View.VISIBLE);
//        Toast.makeText(this, tweetText, Toast.LENGTH_LONG ).show();
        postTweet(tweetText);
    }


    private void postTweet(String tweetText){

        twitterClient.composeTweet(tweetText, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d("SUCCESS", new String(responseBody));
                //tweets.clear();
                populateTimeLine(null);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("FAILURE", new String(responseBody));

            }
        });

    }

    public void swipeToRefresh() {
        tweets.clear();
        tweetAdapter.notifyDataSetChanged();
        populateTimeLine(null); // This will bring the latest 25 tweets
        swipeContainer.setRefreshing(false);
    }


    private void setToolBar(){
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setLogo(R.drawable.ic_twitter_icon);
//        setSupportActionBar(toolbar);
    }
}
