package com.aripir.apps.tweeter.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by saripirala on 9/25/17.
 */

public class User {

    public String name;
    public long uid;
    public String screenName;
    public String profieImageUrl;
    public boolean isVerified;


    public static  User fromJSON(JSONObject jsonObject) throws JSONException{
        User user =  new User();

        user.name = jsonObject.getString("name");
        user.uid = jsonObject.getLong("id");
        user.screenName = jsonObject.getString("screen_name");
        user.profieImageUrl = jsonObject.getString("profile_image_url");
        user.isVerified = jsonObject.getBoolean("verified");

        return user;
    }

}
