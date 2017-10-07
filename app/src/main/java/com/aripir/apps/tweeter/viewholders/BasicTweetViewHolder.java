package com.aripir.apps.tweeter.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aripir.apps.tweeter.models.Tweet;

import java.util.List;

/**
 * Created by saripirala on 10/6/17.
 */

public class BasicTweetViewHolder extends RecyclerView.ViewHolder {

    public BasicTweetViewHolder(View itemView, final List<Tweet> tweets) {
        super(itemView);
    }
}
