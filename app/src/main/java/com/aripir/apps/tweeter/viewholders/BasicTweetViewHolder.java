package com.aripir.apps.tweeter.viewholders;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aripir.apps.tweeter.R;
import com.aripir.apps.tweeter.activity.UserProfileActivity;
import com.aripir.apps.tweeter.models.Tweet;
import com.aripir.apps.tweeter.network.TwitterApplication;
import com.aripir.apps.tweeter.network.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by saripirala on 10/6/17.
 */

public class BasicTweetViewHolder extends RecyclerView.ViewHolder {

    public ImageView ivProfileImage;
    public TextView tvUserName;
    public TextView tvBody;
    public ImageView ivIsVerifiedImage;
    public TextView tvTwitterHandle;
    public TextView tvCreatedAt;
    public ImageView ivReplyImage;
    public TextView tvReplyCount;
    public ImageView ivRetweetImage;
    public TextView tvRetweetCount;
    public ImageView ivLikeImage;
    public TextView tvLikeCount;
    private TwitterClient client;

    public BasicTweetViewHolder(View itemView, final List<Tweet> tweets) {
        super(itemView);
        client = TwitterApplication.getRestClient();

        ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
        tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
        tvBody = (TextView) itemView.findViewById(R.id.tvBody);
        ivIsVerifiedImage = (ImageView) itemView.findViewById(R.id.ivVerified);
        tvTwitterHandle = (TextView) itemView.findViewById(R.id.tvHandle);
        tvCreatedAt = (TextView) itemView.findViewById(R.id.tvCreatedAt);

        ivReplyImage = (ImageView) itemView.findViewById(R.id.ivReply) ;
        tvReplyCount = (TextView) itemView.findViewById(R.id.tvTweetReplies);

        ivRetweetImage = (ImageView) itemView.findViewById(R.id.ivRetweet) ;
        tvRetweetCount = (TextView) itemView.findViewById(R.id.tvRTCount);

        ivLikeImage = (ImageView) itemView.findViewById(R.id.ivLoveTweet) ;
        tvLikeCount = (TextView) itemView.findViewById(R.id.tvLoveCount);

        ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tweet tweet = tweets.get(getLayoutPosition());
                Intent intent = new Intent(view.getContext(), UserProfileActivity.class);
                intent.putExtra("screen_name", tweet.user.screenName);
                view.getContext().startActivity(intent);

            }
        });

        ivLikeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Tweet tweet = tweets.get(getLayoutPosition());
                String id = tweet.id;
                if(tweet.isFavorited) {
                    tweet.isFavorited = false;
                    unFavoriteTweet(id);
                }
                else {
                    tweet.isFavorited = true;
                    favoriteTweet(id);
                }
            }
        });

        ivRetweetImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Tweet tweet = tweets.get(getLayoutPosition());
                String id = tweet.id;
                if(tweet.isRetweeted) {
                    tweet.isRetweeted = false;
                    //Unfortunately there is no api to undo a retweet
                }
                else {
                    tweet.isRetweeted = true;
                    reTweet(id);
                }
            }
        });
    }

    private void favoriteTweet(String id) {

        client.favoriteTweet(id, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("SUCCESS", response.toString());

                ivLikeImage.setImageResource(R.drawable.ic_love_selected);
                try {
                    Tweet tweet = Tweet.fromJSON(response);
                    tvLikeCount.setText("" + tweet.favoriteCount);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("FAILURE", throwable.toString());
            }
        });

    }


    private void unFavoriteTweet(String id){

        client.unFavoriteTweet(id, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("SUCCESS", response.toString());

                ivLikeImage.setImageResource(R.drawable.ic_love);
                try {
                    Tweet tweet = Tweet.fromJSON(response);
                    tvLikeCount.setText(""+ tweet.favoriteCount);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

    private void reTweet(String id){

        client.reTweet(id, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("SUCCESS", response.toString());

                ivRetweetImage.setImageResource(R.drawable.ic_retweet_selected);
                try {
                    Tweet tweet = Tweet.fromJSON(response);
                    tvRetweetCount.setText(""+ tweet.retweetCount);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
                Log.d("FAILURE", throwable.toString());
            }
        });

    }

}


