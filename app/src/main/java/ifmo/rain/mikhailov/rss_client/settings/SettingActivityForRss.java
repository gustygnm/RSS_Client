package ifmo.rain.mikhailov.rss_client.settings;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import ifmo.rain.mikhailov.rss_client.MapDatabase;
import ifmo.rain.mikhailov.rss_client.R;

public class SettingActivityForRss extends Activity {
    final String local = "nameOfGroup";
    public String name = null;
    public static ChannelRSS selectedRss = null;
    ListView rssListView;
    MapDatabase database = MapDatabase.getInstance(this);
    List<ChannelRSS> ListRss;


    private static final String KEY_OF_GROUP = "KEY_OF_GROUP";

    @Override
    public void onSaveInstanceState(Bundle saveInstanceState) {
        saveInstanceState.putString(KEY_OF_GROUP, name);
        super.onSaveInstanceState(saveInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        name = savedInstanceState.getString(KEY_OF_GROUP);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SQLiteDatabase db = database.getReadableDatabase();

        if (name == null) {
            if (savedInstanceState == null) {
                Bundle extras = getIntent().getExtras();
                if (extras == null) {
                    name = null;
                } else {
                    name = extras.getString(local);
                }
            } else {
                name = savedInstanceState.getString(KEY_OF_GROUP);
                if (name == null) {
                    name = (String) savedInstanceState.getSerializable(local);
                }
            }
        }
        setContentView(R.layout.activity_setting_for_rss);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("ifmo.rain.mikhailov.settings.AddRssChanel");
                intent.putExtra("nameOfGroup", name);
                //intent.putExtra(local, nameOfGroup);
                startActivity(intent);
            }
        });
        List<Pair<String, String>> pairOfRss = new ArrayList<>();
        ListRss = new ArrayList<>();
        try {
            pairOfRss = database.get(db, name);
        } catch (FileNotFoundException e) {
            pairOfRss.add(new Pair<>("no one chanel founded", "no one chanel founded"));
        }
        if (pairOfRss.size() == 0)
            pairOfRss.add(new Pair<>("no one chanel founded", "no one chanel founded"));
        rssListView = (ListView) findViewById(R.id.rssSettingsView);
        rssListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> av, View view, int index,
                                    long arg3) {
                selectedRss = ListRss.get(index);
                Intent intent = new Intent("ifmo.rain.mikhailov.settings.EditRssChanel");
                intent.putExtra("nameOfRss", selectedRss.rssName);
                intent.putExtra("linkToRss", selectedRss.linkToRss);
                intent.putExtra("nameOfGroup", name);
                startActivity(intent);
            }
        });
        for (int i = 0; i < pairOfRss.size(); i++) {
            ListRss.add(new ChannelRSS(pairOfRss.get(i).second, pairOfRss.get(i).first));
        }


        ArrayAdapter<ChannelRSS> adapter = new ArrayAdapter<>(this, R.layout.settings_list_item, ListRss);
        rssListView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SQLiteDatabase db = database.getReadableDatabase();
        List<Pair<String, String>> pairOfRss = new ArrayList<>();
        ListRss = new ArrayList<>();
        try {
            pairOfRss = database.get(db, name);
        } catch (FileNotFoundException e) {
            pairOfRss.add(new Pair<>("no one chanel founded", "no one chanel founded"));
        }
        for (int i = 0; i < pairOfRss.size(); i++) {
            ListRss.add(new ChannelRSS(pairOfRss.get(i).second, pairOfRss.get(i).first));
        }


        ArrayAdapter<ChannelRSS> adapter = new ArrayAdapter<>(this, R.layout.settings_list_item, ListRss);
        rssListView.setAdapter(adapter);
    }


}
