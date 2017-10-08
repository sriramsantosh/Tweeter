package com.aripir.apps.tweeter.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aripir.apps.tweeter.models.Tweet;
import com.aripir.apps.tweeter.viewholders.BasicTweetViewHolder;
import com.aripir.apps.tweeter.viewholders.TweetImageViewHolder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.aripir.apps.tweeter.R;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by saripirala on 9/26/17.
 */

public class TweetAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;

    private final int TWEET_TYPE_BASIC = 0;
    private final int TWEET_TYPE_IMAGE = 1;

    List <Tweet> mTweets;

    public TweetAdapter(List<Tweet> tweets ){
        this.mTweets = tweets;
    }

    //Gets invoked for every now row
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context =parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        RecyclerView.ViewHolder viewHolder = new BasicTweetViewHolder(inflater.inflate(R.layout.item_tweet, parent, false), mTweets);

        switch (viewType){
            case TWEET_TYPE_BASIC:
                View basicTweetView = inflater.inflate(R.layout.item_tweet, parent, false);
                viewHolder = new BasicTweetViewHolder(basicTweetView, mTweets);
                break;
            case TWEET_TYPE_IMAGE:
                View tweetImageView = inflater.inflate(R.layout.item_tweet_image, parent, false);
                viewHolder = new TweetImageViewHolder(tweetImageView, mTweets);
                break;
        }

        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        switch (holder.getItemViewType()){
            case TWEET_TYPE_BASIC:
                configureBasicTweetViewHolder(holder,position);
                break;
            case TWEET_TYPE_IMAGE:
                configureTweetImageHolder(holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }


    @Override
    public int getItemViewType(int position) {
        Tweet tweet = mTweets.get(position);

        if(tweet.tweetImageUrl != null)
            return TWEET_TYPE_IMAGE;
        else
            return TWEET_TYPE_BASIC;

        //return super.getItemViewType(position);
    }

    public void configureBasicTweetViewHolder(RecyclerView.ViewHolder viewHolder, int position)
    {
        Tweet tweet = mTweets.get(position);

        ((BasicTweetViewHolder) viewHolder).tvUserName.setText(tweet.user.name);
        ((BasicTweetViewHolder) viewHolder).tvBody.setText(tweet.body);
        enablePattern(((BasicTweetViewHolder) viewHolder).tvBody);


        //Essentially if the user is not verified then don't display the blue tick
        if(!tweet.user.isVerified)
            ((BasicTweetViewHolder) viewHolder).ivIsVerifiedImage.setVisibility(View.INVISIBLE);

        ((BasicTweetViewHolder) viewHolder).tvTwitterHandle.setText("@" +tweet.user.screenName);
        ((BasicTweetViewHolder) viewHolder).tvCreatedAt.setText(tweet.createdAt);
        ((BasicTweetViewHolder) viewHolder).tvRetweetCount.setText(""+tweet.retweetCount);
        ((BasicTweetViewHolder) viewHolder).tvLikeCount.setText(""+ tweet.favoriteCount);

        if(tweet.isFavorited)
            ((BasicTweetViewHolder) viewHolder).ivLikeImage.setImageResource(R.drawable.ic_love_selected);
        if(tweet.isRetweeted)
            ((BasicTweetViewHolder) viewHolder).ivRetweetImage.setImageResource(R.drawable.ic_retweet_selected);

        Glide.with(context)
                .load(tweet.user.profieImageUrl)
                .into(((BasicTweetViewHolder) viewHolder).ivProfileImage);

    }

    public void configureTweetImageHolder(final RecyclerView.ViewHolder viewHolder, int position)
    {
        Tweet tweet = mTweets.get(position);

        ((TweetImageViewHolder) viewHolder).tvUserName.setText(tweet.user.name);
        ((TweetImageViewHolder) viewHolder).tvBody.setText(tweet.body);

        enablePattern(((TweetImageViewHolder) viewHolder).tvBody);

        //Essentially if the user is not verified then don't display the blue tick
        if(!tweet.user.isVerified)
            ((TweetImageViewHolder) viewHolder).ivIsVerifiedImage.setVisibility(View.INVISIBLE);

        ((TweetImageViewHolder) viewHolder).tvTwitterHandle.setText("@" +tweet.user.screenName);
        ((TweetImageViewHolder) viewHolder).tvCreatedAt.setText(tweet.createdAt);

        Glide.with(context)
                .load(tweet.user.profieImageUrl)
                .into(((TweetImageViewHolder) viewHolder).ivProfileImage);

        Glide.with(context)
                .load(tweet.tweetImageUrl)
                //.fitCenter()
                .centerCrop()
                .into(((TweetImageViewHolder) viewHolder).ivTweetImage);

        Glide.with(context).load(tweet.tweetImageUrl).asBitmap().centerCrop().into(new BitmapImageViewTarget(((TweetImageViewHolder) viewHolder).ivTweetImage) {
            @Override
            protected void setResource(Bitmap res) {
                RoundedBitmapDrawable bitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(), res);
                bitmapDrawable.setCircular(true);//comment this line and uncomment the next line if you dont want it fully cricular
                bitmapDrawable.setCornerRadius(10);
                ((TweetImageViewHolder) viewHolder).ivTweetImage.setImageDrawable(bitmapDrawable);
            }
        });

        ((TweetImageViewHolder) viewHolder).tvRetweetCount.setText(""+tweet.retweetCount);
        ((TweetImageViewHolder) viewHolder).tvLikeCount.setText(""+ tweet.favoriteCount);

        if(tweet.isFavorited)
            ((TweetImageViewHolder) viewHolder).ivLikeImage.setImageResource(R.drawable.ic_love_selected);
        if(tweet.isRetweeted)
            ((TweetImageViewHolder) viewHolder).ivRetweetImage.setImageResource(R.drawable.ic_retweet_selected);


    }

    private void enablePattern(TextView textView)
    {
        new PatternEditableBuilder().
                addPattern(Pattern.compile("\\@(\\w+)"), Color.BLUE,
                        new PatternEditableBuilder.SpannableClickedListener() {
                            @Override
                            public void onSpanClicked(String text) {
                                Toast.makeText(context, "Clicked username: " + text,
                                        Toast.LENGTH_SHORT).show();
                            }
                        }).into(textView);

    }

}
