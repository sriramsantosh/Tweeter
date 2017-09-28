package com.aripir.apps.tweeter.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aripir.apps.tweeter.models.Tweet;
import com.bumptech.glide.Glide;
import com.codepath.apps.tweeter.R;

import java.util.List;

/**
 * Created by saripirala on 9/26/17.
 */

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {
    // Pass in tweets array in the constructor

    // for each row, inflate the layout and cache references into viewholder.

    private Context context;

    List <Tweet> mTweets;

    public TweetAdapter(List<Tweet> tweets ){
        this.mTweets = tweets;
    }


    //Gets invoked for every now row
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context =parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);
        ViewHolder viewHolder  = new ViewHolder(tweetView);
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Tweet tweet = mTweets.get(position);
        holder.tvUserName.setText(tweet.user.name);
        holder.tvBody.setText(tweet.body);

        //Essentially if the user is not verified then don't display the blue tick
        if(!tweet.user.isVerified)
            holder.ivIsVerifiedImage.setVisibility(View.INVISIBLE);

        holder.tvTwitterHandle.setText("@" +tweet.user.screenName);
        holder.tvCreatedAt.setText(tweet.createdAt);

        Glide.with(context)
                .load(tweet.user.profieImageUrl)
                .into(holder.ivProfileImage);
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView ivProfileImage;
        public TextView tvUserName;
        public TextView tvBody;
        public ImageView ivIsVerifiedImage;
        public TextView tvTwitterHandle;
        public TextView tvCreatedAt;

        public ViewHolder(View itemView){

            super(itemView);

            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            ivIsVerifiedImage = (ImageView) itemView.findViewById(R.id.ivVerified);
            tvTwitterHandle = (TextView) itemView.findViewById(R.id.tvHandle);
            tvCreatedAt = (TextView) itemView.findViewById(R.id.tvCreatedAt);

        }

    }
}
