package com.aripir.apps.tweeter.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.aripir.apps.tweeter.fragments.HomeTimeLineFragment;
import com.aripir.apps.tweeter.fragments.MentionsTimeLineFragment;
import com.aripir.apps.tweeter.R;
import com.aripir.apps.tweeter.fragments.MessagesFragment;

/**
 * Created by saripirala on 10/4/17.
 */

public class TweetsPagerAdapter extends FragmentPagerAdapter{

   // private String [] tabTitles = new String[]{"Home", "Mentions"};
    private Context context;
    private static int NUM_ITEMS = 3;

    public TweetsPagerAdapter(FragmentManager fm, Context ctx){
        super(fm);
        this.context = ctx;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new HomeTimeLineFragment();
            case 1:
                return new MentionsTimeLineFragment();
            case 2:
                return new MessagesFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return null;
        //return tabTitles[position];
    }
}
