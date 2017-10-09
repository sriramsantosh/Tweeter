package com.aripir.apps.tweeter.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.aripir.apps.tweeter.adapters.TweetAdapter;
import com.aripir.apps.tweeter.models.Tweet;
import com.aripir.apps.tweeter.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by saripirala on 10/3/17.
 */

public class TweetsListFragment extends Fragment {

    protected TweetAdapter tweetAdapter;
    protected LinearLayoutManager layoutManager;
    protected RecyclerView rvTweets;
    protected List<Tweet> tweets;

    protected SwipeRefreshLayout swipeContainer;
    protected ProgressBar pbLoadMore;
    protected ProgressBar pbLoading;
    protected FloatingActionButton newTweetBtn;
    static String maxId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_tweets_list, container, false);

        rvTweets = (RecyclerView) view.findViewById(R.id.rvTweet);
        tweets =  new ArrayList<>();
        tweetAdapter = new TweetAdapter(tweets);

        layoutManager = new LinearLayoutManager(getContext());
        rvTweets.setLayoutManager(layoutManager);
        rvTweets.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));

        rvTweets.setAdapter(tweetAdapter);

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        pbLoading = (ProgressBar) view.findViewById(R.id.pbLoading);
        pbLoadMore = (ProgressBar) view.findViewById(R.id.pbLoadMore);
        newTweetBtn =  (FloatingActionButton) view.findViewById(R.id.newTweetBtn);

        return view;
    }

    public void addItems(JSONArray response){

        for(int i=0; i<response.length(); i++){
            try {
                JSONObject responseJson = response.getJSONObject(i);
                Tweet tweet = Tweet.fromJSON(responseJson);
                tweets.add(tweet);

            }catch (JSONException e){
                Log.d("DEBUG", e.getLocalizedMessage());
            }

        }
        maxId = getCurrentMaxId();
        tweetAdapter.notifyDataSetChanged();
    }

    protected void clearTweets(){
        tweets.clear();
    }

    protected String getCurrentMaxId(){
        Tweet tweet = tweets.get(tweets.size()-1);
        return tweet.id;
    }

    protected void notifyAdapterUpdate(){
        tweetAdapter.notifyDataSetChanged();
    }


    protected void activateLoading(){
        if(pbLoading==null)
            return;
        pbLoading.setVisibility(View.VISIBLE);
    }

    protected void activateLoadMore(){
        if(pbLoadMore==null)
            return;
        pbLoadMore.setVisibility(View.VISIBLE);
    }

    protected void quitLoading(){
        if(pbLoading==null)
            return;
        pbLoading.setVisibility(View.GONE);
    }

    protected void quitLoadMore(){
        if(pbLoadMore==null)
            return;
        pbLoadMore.setVisibility(View.GONE);
    }

}
