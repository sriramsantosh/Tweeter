package com.aripir.apps.tweeter.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by saripirala on 10/8/17.
 */

public class DirectMessage {

    private String messageText;
    private String id;

    private Sender sender;

    public static DirectMessage fromJSON(JSONObject jsonObject) throws JSONException {
        DirectMessage dm = new DirectMessage();

        dm.messageText =jsonObject.getString("text");
        dm.id =jsonObject.getString("id_str");
        dm.sender = Sender.fromJSON(jsonObject.getJSONObject("sender"));

        return dm;
    }


    public String getMessageText() {
        return messageText;
    }

    public String getId() {
        return id;
    }

    public Sender getSender(){
        return sender;
    }
}
