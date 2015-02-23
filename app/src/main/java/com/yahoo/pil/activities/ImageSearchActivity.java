package com.yahoo.pil.activities;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.etsy.android.grid.StaggeredGridView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.yahoo.pil.fragments.ImageSearchSetting;
import com.yahoo.pil.adapters.EndlessScrollListener;
import com.yahoo.pil.adapters.ImageResultsAdapter;
import com.yahoo.pil.models.ImageResult;
import com.yahoo.pil.R;
import com.yahoo.pil.models.ImageSearchApiClient;
import com.yahoo.pil.models.SearchSetting;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ImageSearchActivity extends ActionBarActivity implements ImageSearchSetting.OnFragmentInteractionListener {

    private StaggeredGridView sgvResults;
    private List<ImageResult> imageResults;
    private ImageResultsAdapter aImageResultsAdapter;
    private SearchSetting searchSettingParcelable;
    private int currentPage;
    private String currentSearchQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_search);
        setupViews();
        this.imageResults = new ArrayList<>();
        this.aImageResultsAdapter = new ImageResultsAdapter(this, imageResults);
        this.sgvResults.setAdapter(aImageResultsAdapter);
        this.searchSettingParcelable = new SearchSetting();
        this.sgvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                ImageSearchActivity.this.currentPage = page;
                loadImageGridView();
            }
        });
    }

    private void setupViews() {
        this.sgvResults = (StaggeredGridView) findViewById(R.id.sgvResults);
        this.sgvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent newIntent = new Intent(ImageSearchActivity.this, ImageDisplayActivity.class);
                ImageResult result = ImageSearchActivity.this.imageResults.get(position);
                newIntent.putExtra("result", result);
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
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Its weird that this method is called twice when the "Enter" key is hit.
                //To avoid duplicate data load, ignore the second invocation if the search query hasn't changed.
                if (ImageSearchActivity.this.currentSearchQuery != null && ImageSearchActivity.this.currentSearchQuery.equals(query)) {
                    return false;
                }
                ImageSearchActivity.this.currentSearchQuery = query;
                //This is the first time. So clear grid view adapter to start fresh.
                clearGridViewAdapter();
                loadImageGridView();
                ImageSearchActivity.this.currentPage++;
                //Load the second page data also to fill the blanks in the first page.
                loadImageGridView();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.searchSetting) {
//            displaySettingsDialog();
            Toast.makeText(this,"Clicked Setting!",Toast.LENGTH_LONG).show();
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
                    JSONArray imageResultsJson = response.getJSONObject("responseData").getJSONArray("results");
                    List<ImageResult> newImageResults = ImageResult.fromJSONArray(imageResultsJson);
                    ImageSearchActivity.this.aImageResultsAdapter.addAll(newImageResults);
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
        ImageSearchApiClient.searchImages(this.currentSearchQuery, this.currentPage, this.searchSettingParcelable, responseHandler);
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
        if(this.currentSearchQuery != null && this.currentSearchQuery.isEmpty() == false) {
            clearGridViewAdapter();
            this.currentPage = 0;
            this.loadImageGridView();
        }
    }
}
