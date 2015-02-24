package com.yahoo.pil.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by srmurthy on 2/22/15.
 */


public class Photo implements Parcelable {

    private String id;
    private String owner;
    private String secret;
    private String server;
    private Integer farm;
    private String title;
    private Integer ispublic;
    private Integer isfriend;
    private Integer isfamily;
    private String label;
    private String smallImageURL;
    private String bigImageURL;

    /**
     * @return The id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return The owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     * @param owner The owner
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * @return The secret
     */
    public String getSecret() {
        return secret;
    }

    /**
     * @param secret The secret
     */
    public void setSecret(String secret) {
        this.secret = secret;
    }

    /**
     * @return The server
     */
    public String getServer() {
        return server;
    }

    /**
     * @param server The server
     */
    public void setServer(String server) {
        this.server = server;
    }

    /**
     * @return The farm
     */
    public Integer getFarm() {
        return farm;
    }

    /**
     * @param farm The farm
     */
    public void setFarm(Integer farm) {
        this.farm = farm;
    }

    /**
     * @return The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return The ispublic
     */
    public Integer getIspublic() {
        return ispublic;
    }

    /**
     * @param ispublic The ispublic
     */
    public void setIspublic(Integer ispublic) {
        this.ispublic = ispublic;
    }

    /**
     * @return The isfriend
     */
    public Integer getIsfriend() {
        return isfriend;
    }

    /**
     * @param isfriend The isfriend
     */
    public void setIsfriend(Integer isfriend) {
        this.isfriend = isfriend;
    }

    /**
     * @return The isfamily
     */
    public Integer getIsfamily() {
        return isfamily;
    }

    /**
     * @param isfamily The isfamily
     */
    public void setIsfamily(Integer isfamily) {
        this.isfamily = isfamily;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getSmallImageURL() {
        return smallImageURL;
    }

    public void setSmallImageURL(String smallImageURL) {
        this.smallImageURL = smallImageURL;
    }

    public String getBigImageURL() {
        return bigImageURL;
    }

    public void setBigImageURL(String bigImageURL) {
        this.bigImageURL = bigImageURL;
    }

    public Photo(JSONObject json) {
        try {
            this.id = json.getString("id");
            this.owner = json.getString("owner");
            this.secret = json.getString("secret");
            this.server = json.getString("server");
            this.farm = json.getInt("farm");
            this.title = json.getString("title");
            this.ispublic = json.getInt("ispublic");
            this.isfriend = json.getInt("isfriend");
            this.isfamily = json.getInt("isfamily");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Photo(Parcel parcel) {
        this.id = parcel.readString();
        this.owner = parcel.readString();
        this.secret = parcel.readString();
        this.server = parcel.readString();
        this.farm = parcel.readInt();
        this.title = parcel.readString();
        this.ispublic = parcel.readInt();
        this.isfriend = parcel.readInt();
        this.isfamily = parcel.readInt();
        this.smallImageURL = parcel.readString();
        this.bigImageURL = parcel.readString();
    }

    public static List<Photo> fromJSONArray(JSONArray array) {
        List<Photo> results = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            try {
                results.add(new Photo(array.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.owner);
        dest.writeString(this.secret);
        dest.writeString(this.server);
        dest.writeInt(this.farm);
        dest.writeString(this.title);
        dest.writeInt(this.ispublic);
        dest.writeInt(this.isfriend);
        dest.writeInt(this.isfamily);
        dest.writeString(this.smallImageURL);
        dest.writeString(this.bigImageURL);
    }

    public static final Parcelable.Creator<Photo> CREATOR
            = new Parcelable.Creator<Photo>() {

        @Override
        public Photo createFromParcel(Parcel source) {
            return new Photo(source);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };
}