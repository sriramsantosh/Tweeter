package com.aripir.apps.tweeter.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.aripir.apps.tweeter.R;
import com.aripir.apps.tweeter.adapters.MessageAdapter;
import com.aripir.apps.tweeter.listeners.EndlessRecyclerViewScrollListener;
import com.aripir.apps.tweeter.models.DirectMessage;
import com.aripir.apps.tweeter.network.TwitterApplication;
import com.aripir.apps.tweeter.network.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by saripirala on 10/8/17.
 */

public class MessagesFragment extends Fragment {

    private MessageAdapter messageAdapter;
    private LinearLayoutManager layoutManager;
    private RecyclerView rvMessages;
    private List<DirectMessage> mDirectMessages;

    private SwipeRefreshLayout swipeContainer;
    private ProgressBar pbLoadMore;
    private ProgressBar pbLoading;
    private TwitterClient twitterClient;
    private EndlessRecyclerViewScrollListener scrollListener;
    private static String maxId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_direct_messages_list, container, false);

        twitterClient = TwitterApplication.getRestClient();
        rvMessages = (RecyclerView) view.findViewById(R.id.rvDirectMessage);
        mDirectMessages =  new ArrayList<>();
        messageAdapter = new MessageAdapter(mDirectMessages);
        rvMessages.setAdapter(messageAdapter);

        layoutManager = new LinearLayoutManager(getContext());
        rvMessages.setLayoutManager(layoutManager);
        rvMessages.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        pbLoading = (ProgressBar) view.findViewById(R.id.pbLoading);
        pbLoadMore = (ProgressBar) view.findViewById(R.id.pbLoadMore);
        pbLoading.setVisibility(View.VISIBLE);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        populateMessages(null);

        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                pbLoadMore.setVisibility(View.VISIBLE);
                if(!getCurrentMaxId().equalsIgnoreCase(maxId))
                    populateMessages(getCurrentMaxId());
            }
        };

        rvMessages.addOnScrollListener(scrollListener);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                Log.d("DEBUG", "SWIPE");
                swipeToRefresh();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }


    public void addItems(JSONArray response){

        for(int i=0; i<response.length(); i++){
            try {
                JSONObject responseJson = response.getJSONObject(i);
                DirectMessage directMessage = DirectMessage.fromJSON(responseJson);
                mDirectMessages.add(directMessage);
            }catch (JSONException e){
                Log.d("DEBUG", e.getLocalizedMessage());
            }
        }
        maxId = getCurrentMaxId();
        messageAdapter.notifyDataSetChanged();
    }



    private void populateMessages(String maxId) {
        twitterClient.getDirectMessages(maxId, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("TwitterClient", response.toString());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("TwitterClient", response.toString());

                addItems(response);
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

    private void swipeToRefresh() {
        mDirectMessages.clear();
        messageAdapter.notifyDataSetChanged();
        populateMessages(null);
        swipeContainer.setRefreshing(false);
    }

    private String getCurrentMaxId(){
        DirectMessage directMessage = mDirectMessages.get(mDirectMessages.size()-1);
        return directMessage.getId();
    }

}
