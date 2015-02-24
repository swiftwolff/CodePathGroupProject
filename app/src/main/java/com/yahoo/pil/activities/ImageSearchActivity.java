package com.yahoo.pil.activities;

import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.etsy.android.grid.StaggeredGridView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
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
import java.util.List;


public class ImageSearchActivity extends ActionBarActivity implements ImageSearchSetting.OnFragmentInteractionListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private StaggeredGridView sgvResults;
    private List<Photo> imageResults;
    private ImageResultsAdapter aImageResultsAdapter;
    private SearchSetting searchSettingParcelable;
    private GoogleApiClient mGoogleApiClient;

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;


    private static final String[] SEARCH_CATEGORIES = new String[]{"626047@N23", "1938854@N23", "57634850@N00", "72717767@N00", "83029234@N00"};
    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_search);
        setupViews();
        this.imageResults = new ArrayList<>();
        this.aImageResultsAdapter = new ImageResultsAdapter(this, imageResults);
        this.sgvResults.setAdapter(aImageResultsAdapter);
        this.searchSettingParcelable = new SearchSetting();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();

        connectClient();

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

        for (String eachCategory : SEARCH_CATEGORIES) {
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


    public void displaySettingsDialog() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ImageSearchSetting searchSettingFragment = ImageSearchSetting.newInstance(this.searchSettingParcelable);
        ft.replace(R.id.settings_fragment_placeholder, searchSettingFragment);
        ft.commit();
    }

    @Override
    public void onFragmentInteraction(SearchSetting searchSetting) {
        //Set the new settings and fire the grid view image loading
        this.searchSettingParcelable = searchSetting;
        //Received a new setting. Clear the adapter and start over.
        clearGridViewAdapter();
        this.loadImageGridView();
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onConnected(Bundle bundle) {
        // Display the connection status
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location != null) {
            this.latitude = location.getLatitude();
            this.longitude = location.getLongitude();
        } else {
           // Toast.makeText(this, "Current location was null, enable GPS on emulator!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (i == CAUSE_SERVICE_DISCONNECTED) {
            Toast.makeText(this, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
        } else if (i == CAUSE_NETWORK_LOST) {
            Toast.makeText(this, "Network lost. Please re-connect.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        connectClient();
    }

    protected void connectClient() {
        // Connect the client.
        if (isGooglePlayServicesAvailable() && mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    private boolean isGooglePlayServicesAvailable() {
        // Check that Google Play services is available
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d("Location Updates", "Google Play services is available.");
            return true;
        } else {
            Log.d("Location Updates", "Google Play services is NOT available.");

            return false;
        }
    }

    @Override
    protected void onStop() {
        // Disconnecting the client invalidates it.
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
/*
         * Google Play services can resolve some errors it detects. If the error
		 * has a resolution, try sending an Intent to start a Google Play
		 * services activity that can resolve error.
		 */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
				 * Thrown if Google Play services canceled the original
				 * PendingIntent
				 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(),
                    "Sorry. Location services not available to you", Toast.LENGTH_LONG).show();
        }
    }
}
