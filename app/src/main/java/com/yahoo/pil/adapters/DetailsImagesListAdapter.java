package com.yahoo.pil.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.yahoo.pil.R;
import com.yahoo.pil.models.DetailsScrollImage;

import java.util.ArrayList;

/**
 * Created by nehadike on 2/21/15.
 */
public class DetailsImagesListAdapter extends ArrayAdapter<DetailsScrollImage> {
    
    public DetailsImagesListAdapter(Context context, ArrayList<DetailsScrollImage> images) {
        super(context, 0, images);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DetailsScrollImage imageItem = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_details_image_small, parent, false);
        }
        // Lookup view for data population
        ImageView ivImageView = (ImageView) convertView.findViewById(R.id.ivImageItem);

        // Populate the data into the template view using the data object
        Picasso.with(getContext()).load(imageItem.urlString).into(ivImageView);
        // Return the completed view to render on screen
        return convertView;
    }
}
