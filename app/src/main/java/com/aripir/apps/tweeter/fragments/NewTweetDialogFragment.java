package com.aripir.apps.tweeter.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.tweeter.R;

/**
 * Created by saripirala on 9/26/17.
 */

public class NewTweetDialogFragment extends DialogFragment implements TextView.OnEditorActionListener{

    private EditText mEtTweetBody;
    private Button mBtnTweet;
    private TextView mEtCharacterCount;
    private OnCompleteListener mListener;
    private ImageButton ibCancel;


    public NewTweetDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static NewTweetDialogFragment newInstance(String title) {
        NewTweetDialogFragment frag = new NewTweetDialogFragment();
        Bundle args = new Bundle();
        args.putString("Compose tweet", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_tweet, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        mEtTweetBody = (EditText) view.findViewById(R.id.ptTweetText);
        mBtnTweet = (Button) view.findViewById(R.id.tweetBtn);
        mEtCharacterCount = (TextView) view.findViewById(R.id.etCharacterCount);
        ibCancel = (ImageButton) view.findViewById(R.id.ibCancel);

        this.mListener = (OnCompleteListener) getActivity();

        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Compose tweet");
        getDialog().setTitle(title);
        // Show soft keyboard automatically and request focus to field
        mEtTweetBody.requestFocus();
        mEtTweetBody.setSelection(0);
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        mBtnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mEtTweetBody.getText().length() > 140) {
                    Toast.makeText(view.getContext(), "Sorry, only 140 characters are allowed!", Toast.LENGTH_LONG).show();
                }
                else {
                    mListener.onComplete(mEtTweetBody.getText().toString());
                    dismiss();
                }
            }
        });


        mEtTweetBody.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d("DEBUG", "Here");

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int length = charSequence.length();
                mEtCharacterCount.setText(""+ (140-length));

                if(length>140)
                    mEtCharacterCount.setTextColor(Color.RED);
                else mEtCharacterCount.setTextColor(Color.GRAY);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d("DEBUG", "Here");
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

    public static interface OnCompleteListener {
        public abstract void onComplete(String tweetText);
    }


}
