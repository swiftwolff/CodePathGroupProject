package com.yahoo.pil.models;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by srmurthy on 1/30/15.
 */

public class ImageSearchApiClient {
    private final static String API_BASE_URL = "https://api.flickr.com/services/rest";
    private static final AsyncHttpClient client = new AsyncHttpClient();

    public static void searchImages(String searchCategory, double latitude, double longitude, SearchSetting searchSetting, JsonHttpResponseHandler handler) {

        RequestParams requestParams = prepareBaseRequestParams();
        requestParams.add("method", "flickr.photos.search");
        requestParams.add("lat", "37.3711");
        requestParams.add("lon", "-122.0375");
        requestParams.add("radius", "10");
        requestParams.add("radius_units", "Miles");
        requestParams.add("per_page", "5");
        requestParams.add("sort", "Interesting");

        requestParams.add("group_id", searchCategory);
        client.get(API_BASE_URL, requestParams, handler);
    }

    public static void getDifferentSizeImages(String photoId, JsonHttpResponseHandler handler) {

        RequestParams requestParams = prepareBaseRequestParams();
        requestParams.add("method", "flickr.photos.getSizes");
        requestParams.add("photo_id", photoId);

        client.get(API_BASE_URL, requestParams, handler);
    }


    public static void getPlaceId(String photoId, JsonHttpResponseHandler handler) {
        //https://api.flickr.com/services/rest/?method=flickr.photos.getInfo&api_key=3cdc7b474e95ce9c3e5b0a71557bc18a&photo_id=3444862213&format=json&nojsoncallback=1
        RequestParams requestParams = prepareBaseRequestParams();
        requestParams.add("method", "flickr.photos.getInfo");
        requestParams.add("photo_id", photoId);
        client.get(API_BASE_URL, requestParams, handler);
    }
    
    public static void getPlaceDetails(String placeId, JsonHttpResponseHandler handler) {
        //https://api.flickr.com/services/rest/?method=flickr.photos.getInfo&api_key=3cdc7b474e95ce9c3e5b0a71557bc18a&photo_id=3444862213&format=json&nojsoncallback=1
        RequestParams requestParams = prepareBaseRequestParams();
        requestParams.add("method", "flickr.photos.getSizes");
        requestParams.add("place_id", placeId);
    }

    private static RequestParams prepareBaseRequestParams() {
        RequestParams requestParams = new RequestParams();

        requestParams.add("api_key", "b186fce65e089ac4f6a1e71162d0f6f9");
        requestParams.add("format", "json");
        requestParams.add("nojsoncallback", "1");

        return requestParams;
    }
}