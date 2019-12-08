package com.example.cs125finalproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
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

    private File folder = new File(Environment.getExternalStorageDirectory() + "/SoundRecorder");

    private String datapath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.3gp";



    private File file = new File(datapath);

    private List<File> entrieslist = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chunk_recordings);
        file = getFilesDir();

    }
    public void updateUI() {
        LinearLayout parent = findViewById(R.id.recordingsList);
        parent.removeAllViews();

        for (final File entries : file.listFiles()) {
            String name = Recording.getFilename();
            View recordings = getLayoutInflater().inflate(R.layout.chunk_recordings, parent, false);
            TextView title = recordings.findViewById(R.id.recordingtitle);
            title.setText(name);
            TextView length = recordings.findViewById(R.id.timelength);
            Button play = recordings.findViewById(R.id.play);
            play.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    //play the recording
                    MediaPlayer mPlayer = new MediaPlayer();
                    try {
                        mPlayer.setDataSource(Recording.getUri());
                        mPlayer.prepare();
                        mPlayer.start();
                    } catch (IOException e) {
                        Log.e("LOG_TAG", "prepare() failed.");
                    }
                }

            });
            Button delete = recordings.findViewById(R.id.delete);
            delete.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    //delete recording
                    entrieslist.remove(entries);
                    updateUI();

                }

            });
            parent.addView(recordings);

        }
    }
}
