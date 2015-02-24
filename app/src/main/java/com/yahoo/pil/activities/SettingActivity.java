package com.yahoo.pil.activities;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.yahoo.pil.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingActivity extends ActionBarActivity {

    String[] options = new String[] {"Mall", "Museum", "Park", "Restaurant", "Sight Seeing"};

    int[] drawables = new int[] {R.drawable.mall, R.drawable.museum, R.drawable.park, R.drawable.restaurant, R.drawable.sightseeing};
    SharedPreferences pref;
    ListView lvSetting;
    SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();
        for(int i=0;i<options.length;i++){
            HashMap<String, String> hm = new HashMap<String,String>();
            hm.put("option", options[i]);
            hm.put("drawable", Integer.toString(drawables[i]) );
            aList.add(hm);
        }

        String[] from = {"drawable","option"};
        int[] to = {R.id.setting_image,R.id.setting_text};
        adapter = new SimpleAdapter(getBaseContext(), aList, R.layout.setting_item, from, to);

        lvSetting = (ListView) findViewById(R.id.lvSetting);

        lvSetting.setAdapter(adapter);

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        setPreferences();

        lvSetting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                TextView tvName = (TextView) view.findViewById(R.id.setting_text);

                if(pref.getString(tvName.getText().toString(),null)!=null && pref.getString(tvName.getText().toString(),null).equals("yes")) {
                    Toast.makeText(getBaseContext(), "Unset this option", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor edit = pref.edit();
                    edit.putString(tvName.getText().toString(),"no");
                    edit.commit();
                    DisplayMetrics displayMetrics = getBaseContext().getResources().getDisplayMetrics();
                    int heightPx = Math.round(95 * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
                    tvName.getLayoutParams().height = heightPx;
                    tvName.getLayoutParams().width = -1;
                    ViewGroup.MarginLayoutParams mparams = (ViewGroup.MarginLayoutParams)tvName.getLayoutParams();
                    mparams.setMargins(0,-302,0,0);

                    tvName.setLayoutParams(mparams);
                    tvName.setBackgroundColor(Color.parseColor("#80000000"));
                    return;
                }
                tvName.getLayoutParams().height = -1;
                tvName.getLayoutParams().width = -1;
                ViewGroup.MarginLayoutParams mparams = (ViewGroup.MarginLayoutParams)tvName.getLayoutParams();
                mparams.setMargins(50,-107,0,0);

                tvName.setLayoutParams(mparams);
                tvName.setBackgroundColor(Color.parseColor("#4D000000"));
                SharedPreferences.Editor edit = pref.edit();
                edit.putString(tvName.getText().toString(),"yes");
                edit.commit();
                Toast.makeText(getBaseContext(), "set this option", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setPreferences() {

        final int firstListItemPosition = lvSetting.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + lvSetting.getChildCount() - 1;
        View view = null;
        if (0 < firstListItemPosition || 0 > lastListItemPosition ) {
            view = lvSetting.getAdapter().getView(0, null, lvSetting);
        } else {
            final int childIndex = 0 - firstListItemPosition;
            view =  lvSetting.getChildAt(childIndex);
        }

        TextView tvName = (TextView) view.findViewById(R.id.setting_text);
        tvName.getLayoutParams().height = -1;
        tvName.getLayoutParams().width = -1;
        ViewGroup.MarginLayoutParams mparams = (ViewGroup.MarginLayoutParams)tvName.getLayoutParams();
        mparams.setMargins(50,-105,0,0);

        tvName.setLayoutParams(mparams);
        tvName.setBackgroundColor(Color.parseColor("#4D000000"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
