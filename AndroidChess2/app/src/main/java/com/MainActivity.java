package com;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import java.io.FileOutputStream;

// ##### System.out ----> Log.e("MainActivity","Insert Message");

public class MainActivity extends AppCompatActivity {

    public static int gameNumber;
    private String filename = "savegames";
    private FileOutputStream outputStream;
    public static boolean record = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gameNumber++;
        //needs buttons
        Button newGameButton = (Button) findViewById(R.id.button);
        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newGameIntent = new Intent(MainActivity.this, GameScreen.class);
                startActivity(newGameIntent);
            }
        });
        CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox);
        checkBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (((CheckBox) v).isChecked())
                    record = true;
            }
        });

        Button replayBtn = (Button) findViewById(R.id.button2);
        replayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent replayIntent = new Intent(MainActivity.this, ListGamesActivity.class);
                startActivity(replayIntent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }
}
