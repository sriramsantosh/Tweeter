package com.aripir.apps.tweeter.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.aripir.apps.tweeter.listeners.EndlessRecyclerViewScrollListener;
import com.aripir.apps.tweeter.network.TwitterApplication;
import com.aripir.apps.tweeter.network.TwitterClient;
import com.codepath.apps.tweeter.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by saripirala on 10/3/17.
 */

public class HomeTimeLineFragment extends TweetsListFragment implements NewTweetDialogFragment.OnCompleteListener{

    private TwitterClient twitterClient;
    private EndlessRecyclerViewScrollListener scrollListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        twitterClient = TwitterApplication.getRestClient();
        populateTimeLine(null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);

        pbLoading.setVisibility(View.VISIBLE);


        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                pbLoadMore.setVisibility(View.VISIBLE);
                populateTimeLine(getCurrentMaxId());
            }
        };

        rvTweets.addOnScrollListener(scrollListener);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.

                Log.d("DEBUG", "SWIPE");
                  swipeToRefresh();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        Log.d("DEBUG", "onViewCreated");
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

                addItems(response);
                quitLoading();
                quitLoadMore();
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


    @Override
    public void onComplete(String tweetText) {
        clearTweets();
        activateLoading();
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


}
