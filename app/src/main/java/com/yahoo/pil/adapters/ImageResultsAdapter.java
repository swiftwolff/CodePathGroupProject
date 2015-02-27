package com.yahoo.pil.adapters;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.etsy.android.grid.util.DynamicHeightImageView;
import com.squareup.picasso.Picasso;
import com.yahoo.pil.R;
import com.yahoo.pil.models.Photo;

import java.util.List;
import java.util.Random;


/**
 * Created by srmurthy on 1/28/15.
 */
public class ImageResultsAdapter extends ArrayAdapter<Photo> {

    private final Random mRandom = new Random();
    private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();

    public ImageResultsAdapter(Context context, List<Photo> images) {
        super(context, R.layout.item_image_result, images);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Photo imageResult = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_image_result, parent, false);
        }
        // Lookup view for data population
        DynamicHeightImageView ivImage = (DynamicHeightImageView) convertView.findViewById(R.id.ivImage);

        double positionHeight = getPositionRatio(position);

        ivImage.setHeightRatio(positionHeight);
        TextView tvTitle = (TextView)convertView.findViewById(R.id.tvTitle);
        String title = imageResult.getTitle();
        if(title != null && title.length() > 14) {
            title = title.substring(0,14)+"...";
        }
        tvTitle.setText(title);

        Picasso.with(getContext()).load(imageResult.getSmallImageURL()).fit().into(ivImage);
        return convertView;
    }


    private double getPositionRatio(final int position) {
        double ratio = sPositionHeightRatios.get(position, 0.0);
        // if not yet done generate and stash the columns height
        // in our real world scenario this will be determined by
        // some match based on the known height and width of the image
        // and maybe a helpful way to get the column height!
        if (ratio == 0) {
            ratio = getRandomHeightRatio();
            sPositionHeightRatios.append(position, ratio);
        }
        return ratio;
    }

    private double getRandomHeightRatio() {
        return (mRandom.nextDouble() / 2.0) + 1.0; // height will be 1.0 - 1.5
        // the width
    }
}
