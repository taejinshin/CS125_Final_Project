package com.example.cs125finalproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.os.Handler;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.io.IOException;
import java.lang.Runnable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class recordinglist extends AppCompatActivity {

    private File directory = Environment.getExternalStorageDirectory();



    private File file = new File(directory.getAbsolutePath() + "/FilePath");

    private ArrayList<File> entrieslist = new ArrayList<File>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chunk_recordings);
        file.mkdirs();

        updateUI();
    }


    public void updateUI() {
        Intent intent = getIntent();
        String recordingtitle = intent.getStringExtra("title");
        String recordinglength = intent.getStringExtra("time");
        final String recording = file.getAbsolutePath() + "/" + recordingtitle + ".3gp";
        LinearLayout parent = findViewById(R.id.recordingslist);
        parent.removeAllViews();
        File files[] = directory.listFiles();
        int count = 0;
        for (final File entries : files) {
            entrieslist.add(entries);

            String name = Recording.getFilename();
            View recordings = getLayoutInflater().inflate(R.layout.chunk_recordings, parent, false);
            TextView title = recordings.findViewById(R.id.recordingtitle);
            title.setText(name);
            TextView length = recordings.findViewById(R.id.timelength);

            ImageButton play = recordings.findViewById(R.id.play);
            play.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    //play the recording
                    MediaPlayer mPlayer = new MediaPlayer();
                    try {
                        mPlayer.setDataSource(recording);
                        mPlayer.prepare();
                        mPlayer.start();
                    } catch (IOException e) {
                        Log.e("LOG_TAG", "prepare() failed.");
                    }
                }

            });
            ImageButton delete = recordings.findViewById(R.id.delete);
            delete.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    //delete recording
                    entrieslist.remove(entries);
                    updateUI();

                }

            });
            parent.addView(recordings);
            count++;

        }
    }
}
