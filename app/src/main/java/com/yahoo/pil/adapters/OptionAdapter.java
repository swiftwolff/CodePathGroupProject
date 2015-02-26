package com.yahoo.pil.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yahoo.pil.R;
import com.yahoo.pil.models.Option;

import java.util.ArrayList;

/**
 * Created by jeffhsu on 2/24/15.
 */
public class OptionAdapter extends ArrayAdapter<Option> {
    SharedPreferences pref;

    public OptionAdapter(Context context, ArrayList<Option> options) {
        super(context, R.layout.setting_item, options);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        pref = PreferenceManager.getDefaultSharedPreferences(getContext());

        Option option = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.setting_item, parent, false);
        }
        // Lookup data
        TextView tvName = (TextView) convertView.findViewById(R.id.setting_text);
        ImageView imgSetting = (ImageView) convertView.findViewById(R.id.setting_image);
        tvName.setText(option.getName());
        imgSetting.setImageResource(0);
        Picasso.with(getContext()).load(option.getDrawID()).into(imgSetting);
        if(pref.getString(option.getName(),null)!=null && pref.getString(option.getName(),null).equals("yes")) {
            tvName.getLayoutParams().height = -1;
            tvName.getLayoutParams().width = -1;
            ViewGroup.MarginLayoutParams mparams = (ViewGroup.MarginLayoutParams)tvName.getLayoutParams();
            mparams.setMargins(50,-107,0,0);

            tvName.setLayoutParams(mparams);
            tvName.setBackgroundColor(Color.parseColor("#4D000000"));
        }
        return convertView;
    }
}
