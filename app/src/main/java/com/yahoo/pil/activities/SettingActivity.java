package com.yahoo.pil.activities;

import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.yahoo.pil.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingActivity extends ActionBarActivity {

    String[] options = new String[] {"Mall", "Museum", "Park", "Restaurant", "Sight Seeing"};

    int[] drawables = new int[] {R.drawable.mall, R.drawable.museum, R.drawable.park, R.drawable.restaurant, R.drawable.sightseeing};

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
        SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList, R.layout.setting_item, from, to);

        ListView lvSetting = (ListView) findViewById(R.id.lvSetting);

        lvSetting.setAdapter(adapter);

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
