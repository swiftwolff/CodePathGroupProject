package com.yahoo.pil.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by srmurthy on 1/30/15.
 */
public class SearchSetting implements Parcelable {

    private String size;
    private String color;
    private String type;
    private String site;

    //The following 3 variables are only for re-selecting the spinner values in the fragment
    //There should be a better way to deal with this.
    private int selectedImageSizeArrayIndex;
    private int selectedImageColorArrayIndex;
    private int selectedImageTypeArrayIndex;

    public SearchSetting() {
    }

    public SearchSetting(String size, String color, String type, String site) {
        this.size = size;
        this.color = color;
        this.type = type;
        this.site = site;
    }

    public SearchSetting(Parcel source) {
        this.color = source.readString();
        this.site = source.readString();
        this.size = source.readString();
        this.type = source.readString();
        this.selectedImageSizeArrayIndex = source.readInt();
        this.selectedImageColorArrayIndex = source.readInt();
        this.selectedImageTypeArrayIndex = source.readInt();
    }


    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public int getSelectedImageSizeArrayIndex() {
        return selectedImageSizeArrayIndex;
    }

    public void setSelectedImageSizeArrayIndex(int selectedImageSizeArrayIndex) {
        this.selectedImageSizeArrayIndex = selectedImageSizeArrayIndex;
    }

    public int getSelectedImageColorArrayIndex() {
        return selectedImageColorArrayIndex;
    }

    public void setSelectedImageColorArrayIndex(int selectedImageColorArrayIndex) {
        this.selectedImageColorArrayIndex = selectedImageColorArrayIndex;
    }

    public int getSelectedImageTypeArrayIndex() {
        return selectedImageTypeArrayIndex;
    }

    public void setSelectedImageTypeArrayIndex(int selectedImageTypeArrayIndex) {
        this.selectedImageTypeArrayIndex = selectedImageTypeArrayIndex;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.color);
        dest.writeString(this.site);
        dest.writeString(this.size);
        dest.writeString(this.type);
        dest.writeInt(this.selectedImageSizeArrayIndex);
        dest.writeInt(this.selectedImageColorArrayIndex);
        dest.writeInt(this.selectedImageTypeArrayIndex);
    }

    public static final Parcelable.Creator<SearchSetting> CREATOR
            = new Parcelable.Creator<SearchSetting>() {

        @Override
        public SearchSetting createFromParcel(Parcel source) {
            return new SearchSetting(source);
        }

        @Override
        public SearchSetting[] newArray(int size) {
            return new SearchSetting[size];
        }
    };
}
