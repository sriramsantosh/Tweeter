package com.aripir.apps.tweeter.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by saripirala on 10/8/17.
 */

@Parcel
public class Sender {


    public Sender(){

    }
    private String name;
    private String profileImageUrl;
    private String handle;
    private boolean isVerified;


    public String getName() {
        return name;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getHandle() {
        return handle;
    }

    public boolean isVerified() {
        return isVerified;
    }


    public static Sender fromJSON(JSONObject jsonObject) throws JSONException {
        Sender sender =  new Sender();
        sender.name = jsonObject.getString("name");
        sender.handle = jsonObject.getString("screen_name");
        sender.profileImageUrl = jsonObject.getString("profile_image_url");
        sender.isVerified = jsonObject.getBoolean("verified");

        return sender;
    }
}
