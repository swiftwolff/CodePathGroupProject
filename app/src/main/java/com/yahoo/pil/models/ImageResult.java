package com.yahoo.pil.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by srmurthy on 1/28/15.
 */
public class ImageResult implements Parcelable {
    private String fullUrl;
    private String thumbUrl;
    private String title;


    public ImageResult(Parcel parcel) {
        fullUrl = parcel.readString();
        thumbUrl = parcel.readString();
        title = parcel.readString();
    }

    public ImageResult(JSONObject json) {
        try {
            this.fullUrl = json.getString("url");
            this.thumbUrl = json.getString("tbUrl");
            this.title = json.getString("title");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static List<ImageResult> fromJSONArray(JSONArray array) {
        List<ImageResult> results = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            try {
                results.add(new ImageResult(array.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }

    public String getFullUrl() {
        return fullUrl;
    }

    public void setFullUrl(String fullUrl) {
        this.fullUrl = fullUrl;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fullUrl);
        dest.writeString(thumbUrl);
        dest.writeString(title);
    }

    public static final Parcelable.Creator<ImageResult> CREATOR
            = new Parcelable.Creator<ImageResult>() {

        @Override
        public ImageResult createFromParcel(Parcel source) {
            return new ImageResult(source);
        }

        @Override
        public ImageResult[] newArray(int size) {
            return new ImageResult[size];
        }
    };
}
