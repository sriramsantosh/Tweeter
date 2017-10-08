package com.aripir.apps.tweeter.models;

import android.text.format.DateUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLOutput;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by saripirala on 9/25/17.
 */

public class Tweet {

    public String body;
    public String id;
    public String createdAt;
    public long retweetCount;
    public long favoriteCount;
    public String tweetImageUrl;
    public boolean isFavorited;
    public boolean isRetweeted;


    public User user;

    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException{
        Tweet tweet = new Tweet();

        tweet.body =jsonObject.getString("text");
        tweet.id =jsonObject.getString("id_str");
        tweet.createdAt = getRelativeTimeAgo(jsonObject.getString("created_at"));
        tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        tweet.tweetImageUrl= null;
        tweet.retweetCount = jsonObject.getLong("retweet_count");
        tweet.favoriteCount = jsonObject.getLong("favorite_count");
        tweet.isFavorited = jsonObject.getBoolean("favorited");
        tweet.isRetweeted = jsonObject.getBoolean("retweeted");

        if(jsonObject.has("entities")) {
            JSONObject entities = jsonObject.getJSONObject("entities");
            if(entities.has("media")) {
                JSONArray media = entities.getJSONArray("media");

                if (media.length() > 0)
                    tweet.tweetImageUrl = media.getJSONObject(0).getString("media_url_https");
                System.out.println(tweet.tweetImageUrl);
            }
        }
        return tweet;
    }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Special Case: Yesterday
        if (relativeDate.equals("Yesterday")) {
            String res = "1d";
            //Log.d("DEBUG: relativeDate = ", relativeDate);
            //Log.d("DEBUG: relativeDate = ", res);
            return res;
        }

        // Case: X days ago
        //       X hours ago
        //       X minutes ago
        //       X seconds ago
        int pos;
        for(pos = 0; pos < relativeDate.length(); pos++) {
            if (( relativeDate.charAt(pos) == 'd' ||
                    relativeDate.charAt(pos) == 'h' ||
                    relativeDate.charAt(pos) == 'm' ||
                    relativeDate.charAt(pos) == 's')
                    && pos + 1 < relativeDate.length()) {
                //Log.d("DEBUG: relativeDate = ", relativeDate);
                //Log.d("DEBUG: relativeDate = ", relativeDate.substring(0, pos+1));
                return relativeDate.substring(0, pos+1);
            }
        }

        // Case: more than 7 days ago
        SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yy");
        String reformattedStr = "";
        try {
            reformattedStr = myFormat.format(sf.parse(rawJsonDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //Log.d("DEBUG: relativeDate = ", relativeDate);
        //Log.d("DEBUG: reformatted = ", reformattedStr);
        return reformattedStr.replace(" ", "");
    }

}
