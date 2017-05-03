package com;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;


public class ListGamesActivity extends ActionBarActivity implements OnItemClickListener {

    ListView listView1;
    ArrayList<String> lines;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_savegameactivity);
        listView1 = (ListView) findViewById(R.id.gamelist);

        try {
            FileInputStream inputStream = this.openFileInput("savegames");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder finalString = new StringBuilder();
            String line;
            lines = new ArrayList<String>();
            while ((line = bufferedReader.readLine()) != null) {
                finalString.append(line);
                if (!(line.contains("position"))) {
                    lines.add(line);
                } else {
                    lines.removeAll(Arrays.asList(null, ""));
                }
            }
            System.out.println(lines);
            // Sort list according to date.
            Collections.sort(lines, new Comparator<String>() {
                DateFormat df = new SimpleDateFormat("dd MMM");
                @Override
                public int compare(String s1, String s2) {
                    try {
                        Date d1 = df.parse(s1.split(":")[0].trim());
                        Date d2 = df.parse(s2.split(":")[0].trim());
                        return d1.compareTo(d2);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return 0;
                    }
                }
            });
            System.out.println(lines);
            bufferedReader.close();
            inputStream.close();
            inputStreamReader.close();

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lines);

            listView1.setAdapter(adapter);


        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "Click the saved game you want", Toast.LENGTH_LONG).show();
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //this is the game being selected

                String selectedFromList = (String) (listView1.getItemAtPosition(position));
                Intent newReplayintent = new Intent(ListGamesActivity.this, ReplayActivity.class);
                Bundle b = new Bundle();
                newReplayintent.putExtras(b);
                ReplayActivity.fixList(selectedFromList);
                startActivity(newReplayintent);
                //System.out.println(selectedFromList);

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}