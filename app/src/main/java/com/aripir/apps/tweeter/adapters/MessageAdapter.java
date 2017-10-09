package com.aripir.apps.tweeter.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aripir.apps.tweeter.R;
import com.aripir.apps.tweeter.activity.UserProfileActivity;
import com.aripir.apps.tweeter.models.DirectMessage;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by saripirala on 10/8/17.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private Context context;

    List<DirectMessage> mDirectMessages;

    public MessageAdapter (List<DirectMessage> mDirectMessages){
        this.mDirectMessages = mDirectMessages;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        DirectMessage dm = mDirectMessages.get(position);

        holder.tvMessage.setText(dm.getMessageText());
        holder.tvTwitterHandle.setText("@" + dm.getSender().getHandle());
        holder.tvUserName.setText(dm.getSender().getName());

        Glide.with(context)
                .load(dm.getSender().getProfileImageUrl())
                .into(holder.ivProfileImage);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        return new ViewHolder(inflater.inflate(R.layout.item_direct_message, parent,false), mDirectMessages);
    }

    @Override
    public int getItemCount() {
        return mDirectMessages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivProfileImage;
        public TextView tvUserName;
        public TextView tvMessage;
        public TextView tvTwitterHandle;

        public ViewHolder(View itemView, final List<DirectMessage> mDirectMessages) {
            super(itemView);

            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUserName = (TextView) itemView.findViewById(R.id.tvScreenName);
            tvTwitterHandle = (TextView) itemView.findViewById(R.id.tvHandle);
            tvMessage = (TextView) itemView.findViewById(R.id.tvMessage);

            ivProfileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DirectMessage dm = mDirectMessages.get(getLayoutPosition());
                    Intent intent = new Intent(view.getContext(), UserProfileActivity.class);
                    intent.putExtra("screen_name", dm.getSender().getName());
                    view.getContext().startActivity(intent);
                }
            });

        }
    }
}
