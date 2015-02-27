package com.yahoo.pil.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.etsy.android.grid.StaggeredGridView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.yahoo.pil.R;
import com.yahoo.pil.adapters.ImageResultsAdapter;
import com.yahoo.pil.fragments.ImageSearchSetting;
import com.yahoo.pil.models.ImageSearchApiClient;
import com.yahoo.pil.models.Photo;
import com.yahoo.pil.models.SearchSetting;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ImageSearchActivity extends ActionBarActivity implements ImageSearchSetting.OnFragmentInteractionListener,
        LocationListener {

    private StaggeredGridView sgvResults;
    private List<Photo> imageResults;
    private ImageResultsAdapter aImageResultsAdapter;
    private SearchSetting searchSettingParcelable;
    private LocationManager locationManager;
    private String provider;

    private static final String[] SEARCH_CATEGORIES = new String[]{"626047@N23", "1938854@N23", "57634850@N00", "72717767@N00", "83029234@N00"};
    private double latitude;
    private double longitude;


    /* Request updates at startup */
    @Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(provider, 400, 1, this);
        aImageResultsAdapter.clear();
        loadImageGridView();
    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        int lat = (int) (location.getLatitude());
        int lng = (int) (location.getLongitude());
        Toast.makeText(this, String.valueOf(lat) + " " + String.valueOf(lng), Toast.LENGTH_LONG);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_search);
        setupViews();
        this.imageResults = new ArrayList<>();
        this.aImageResultsAdapter = new ImageResultsAdapter(this, imageResults);
        this.sgvResults.setAdapter(aImageResultsAdapter);
        this.searchSettingParcelable = new SearchSetting();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);

        // Initialize the location fields
        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged(location);
        } else {
            Toast.makeText(this, "Location not available", Toast.LENGTH_LONG);
        }
        clearGridViewAdapter();
        loadImageGridView();
//        this.sgvResults.setOnScrollListener(new EndlessScrollListener() {
//            @Override
//            public void onLoadMore(int page, int totalItemsCount) {
//                ImageSearchActivity.this.currentPage = page;
//                loadImageGridView();
//            }
//        });
    }

    private void setupViews() {
        this.sgvResults = (StaggeredGridView) findViewById(R.id.sgvResults);
        this.sgvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent newIntent = new Intent(ImageSearchActivity.this, ImageDisplayActivity.class);
                Photo photo = ImageSearchActivity.this.imageResults.get(position);
                newIntent.putExtra("photo", photo);
                startActivity(newIntent);
            }
        });
    }

    private void clearGridViewAdapter() {
        ImageSearchActivity.this.aImageResultsAdapter.clear();
        ImageSearchActivity.this.aImageResultsAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_image_search, menu);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.searchSetting) {
            Intent i = new Intent(this, SettingActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadImageGridView() {
        JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                try {
                    JSONArray imageResultsJson = response.getJSONObject("photos").getJSONArray("photo");
                    List<Photo> newImageResults = Photo.fromJSONArray(imageResultsJson);
                    for (Photo eachPhoto : newImageResults) {
                        loadDifferentSizeImages(eachPhoto);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                throwable.printStackTrace();
            }
        };

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        List<String> selectionList = new ArrayList<>(5);
        if ("yes".equals(pref.getString("Mall", null))) {
            selectionList.add("83029234@N00");
        }
        if ("yes".equals(pref.getString("Museum", null))) {
            selectionList.add("1938854@N23");
        }
        if ("yes".equals(pref.getString("Park", null))) {
            selectionList.add("72717767@N00");
        }
        if ("yes".equals(pref.getString("Restaurant", null))) {
            selectionList.add("57634850@N00");
        }
        if ("yes".equals(pref.getString("Sight Seeing", null))) {
            selectionList.add("626047@N23");
        }

        if (selectionList.size() == 0) {
            selectionList.addAll(Arrays.asList(SEARCH_CATEGORIES));
        }

        for (String eachCategory : selectionList) {
            ImageSearchApiClient.searchImages(eachCategory, this.latitude, this.longitude, this.searchSettingParcelable, responseHandler);
        }
    }

    private void loadDifferentSizeImages(final Photo photo) {
        JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                try {
                    JSONArray imageSizesResultJson = response.getJSONObject("sizes").getJSONArray("size");
                    for (int i = 0; i < imageSizesResultJson.length(); i++) {
                        JSONObject eachSizeObject = (JSONObject) imageSizesResultJson.get(i);
                        String imageLabel = eachSizeObject.getString("label");
                        if (imageLabel != null && imageLabel.equals("Medium")) {
                            photo.setSmallImageURL(eachSizeObject.getString("source"));
                        } else if (imageLabel != null && imageLabel.equals("Large")) {
                            photo.setBigImageURL(eachSizeObject.getString("source"));
                        }

                    }
                    ImageSearchActivity.this.aImageResultsAdapter.add(photo);
                    ImageSearchActivity.this.aImageResultsAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                throwable.printStackTrace();
            }
        };
        ImageSearchApiClient.getDifferentSizeImages(photo.getId(), responseHandler);
    }


    @Override
    public void onFragmentInteraction(SearchSetting searchSetting) {
        //Set the new settings and fire the grid view image loading
        this.searchSettingParcelable = searchSetting;
        //Received a new setting. Clear the adapter and start over.
        clearGridViewAdapter();
        this.loadImageGridView();
    }
}