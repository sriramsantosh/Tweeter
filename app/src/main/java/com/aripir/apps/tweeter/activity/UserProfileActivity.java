package com.aripir.apps.tweeter.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.aripir.apps.tweeter.R;
import com.aripir.apps.tweeter.fragments.UserTimeLineFragment;
import com.aripir.apps.tweeter.models.DirectMessage;
import com.aripir.apps.tweeter.models.User;
import com.aripir.apps.tweeter.network.TwitterApplication;
import com.aripir.apps.tweeter.network.TwitterClient;
import com.aripir.apps.tweeter.utils.CommonLib;
import com.bumptech.glide.Glide;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;


import cz.msebera.android.httpclient.Header;

/**
 * Created by saripirala on 10/7/17.
 */

public class UserProfileActivity extends AppCompatActivity {

    TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        DirectMessage dm = Parcels.unwrap(getIntent().getParcelableExtra("dm"));

        String screenName = dm.getSender().getHandle();

        UserTimeLineFragment userTimeLineFragment = UserTimeLineFragment.newInstance(screenName);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flContainer, userTimeLineFragment);
        ft.commit();

        client = TwitterApplication.getRestClient();

        client.getUserLookupInfo(screenName, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try{
                    User user = User.fromJSON(response.getJSONObject(0));
                    populateUserHeadline(user);
                }catch (JSONException e){

                }
            }
        });
    }

    public void populateUserHeadline(User user){

        TextView tvName = (TextView) findViewById(R.id.tvName);
        TextView tvTagLine = (TextView) findViewById(R.id.tvTagLine);

        TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
        TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);

        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);

        tvName.setText(user.name);
        tvTagLine.setText(user.tagLine);
        Double followersCount = 1.0* (user.followersCount);
        String followersCountStr = CommonLib.withSuffix(followersCount);

        tvFollowers.setText(followersCountStr + " Followers");
        tvFollowing.setText(user.followingCount + " Following");
        Glide.with(this).load(user.profieImageUrl).centerCrop().into(ivProfileImage);
    }
}
