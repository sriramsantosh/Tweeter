package com.aripir.apps.tweeter.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.aripir.apps.tweeter.R;

/**
 * Created by saripirala on 10/8/17.
 */

public class ReplyTweetDialogFragment extends DialogFragment implements TextView.OnEditorActionListener{

    private EditText mEtReplyText;
    private Button mBtnTweet;
    private NewTweetDialogFragment.NewTweetDialogListener listener;
    private ImageButton ibCancel;

    public ReplyTweetDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static ReplyTweetDialogFragment newInstance(String title) {
        ReplyTweetDialogFragment frag = new ReplyTweetDialogFragment();
        Bundle args = new Bundle();
        args.putString("Reply tweet", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reply, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        mEtReplyText = (EditText) view.findViewById(R.id.etReply);
        mBtnTweet = (Button) view.findViewById(R.id.newTweetBtn);
        ibCancel = (ImageButton) view.findViewById(R.id.ibCancel);

        listener = (NewTweetDialogFragment.NewTweetDialogListener) getTargetFragment();

        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Compose tweet");
        getDialog().setTitle(title);
        // Show soft keyboard automatically and request focus to field
        mEtReplyText.requestFocus();
        mEtReplyText.setSelection(0);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        mBtnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mEtReplyText.getText().length() > 140) {
                    Toast.makeText(view.getContext(), "Sorry, only 140 characters are allowed!", Toast.LENGTH_LONG).show();
                }
                else {
                    sendBackResult();
                }
            }
        });

        ibCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        return false;
    }



    public interface ReplyTweetDialogListener {
        void onFinishEditDialog(String inputText);
    }


    public void sendBackResult() {
        // Notice the use of `getTargetFragment` which will be set when the dialog is displayed

        listener.onFinishEditDialog(mEtReplyText.getText().toString());
        dismiss();
    }



}

