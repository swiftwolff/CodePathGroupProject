package com.yahoo.pil.models;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by srmurthy on 1/30/15.
 */

public class ImageSearchApiClient {
    private final static String API_BASE_URL = "https://ajax.googleapis.com/ajax/services/search/images";
    private static final AsyncHttpClient client = new AsyncHttpClient();

    public static void searchImages(String searchQuery, int page, SearchSetting searchSetting, JsonHttpResponseHandler handler) {
        //Google Image search API will only return a maximum of 64 images. (0-7 pages with 8 images each).
        //So after page 7, reset the page number to 0 to start over, giving the illusion of infinite scroll.
        if(page > 7) {
            page = 0;
        }
        RequestParams requestParams = prepareRequestParams(searchQuery, page, searchSetting);
        client.get(API_BASE_URL, requestParams, handler);
    }

    private static RequestParams prepareRequestParams(String searchQuery, int page, SearchSetting searchSetting) {
        RequestParams requestParams = new RequestParams();
        requestParams.add("start", String.valueOf(page * 8));
        requestParams.add("rsz", "8");
        requestParams.add("v", "1.0");
        requestParams.add("q", searchQuery);

        String size = searchSetting.getSize();
        String color = searchSetting.getColor();
        String type = searchSetting.getType();
        String site = searchSetting.getSite();

        if(size != null && size.isEmpty() == false && "any".equalsIgnoreCase(size) == false) {
            requestParams.add("imgsz", size);
        }
        if(color != null && color.isEmpty() == false && "any".equalsIgnoreCase(color) == false) {
            requestParams.add("imgcolor", color);
        }
        if(type != null && type.isEmpty() == false && "any".equalsIgnoreCase(type) == false) {
            requestParams.add("imgtype", type);
        }
        if(site != null && site.isEmpty() == false) {
            requestParams.add("as_sitesearch", site);
        }
        return requestParams;
    }
}