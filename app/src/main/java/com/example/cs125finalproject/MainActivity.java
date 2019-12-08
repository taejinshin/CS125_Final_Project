package com.example.cs125finalproject;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.UriMatcher;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
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





public class MainActivity extends AppCompatActivity {


    /**
     * Buttons to be displayed when user first opens the app.
     */
    private Button list;

    /**
     * Buttons with images on them.
     */

    private ImageButton record, play, stop;

    /**
     * File path for where the recording is to be saved.
     */

    private String datapath = null;

    /**
     * The recorder to be used.
     */

    private MediaRecorder recorder;

    /**
     * shown when a recording is finished.
     */

    private PopupWindow popup;

    /**
     * shown when user has to enter password.
     */

    private PopupWindow passwordpopup;

    /**
     * The user's saved password.
     */

    private String password;

    /**
     * Discard the previous recording.
     */

    private Button delete;

    /**
     * Save the previous recording.
     */

    private Button save;

    /**
     * timer for when recording begins.
     */

    private Chronometer chronometer;


    /**
     * code for permissions.
     */

    private int RECORD_AUDIO_REQUEST_CODE = 1;

    private Handler handler;

    private long millisec, start, savedtime, buff, update = 0L;
    private int sec, min, millisecond;
    private boolean resume;



    /**
     * the starting UI when the app is opened.
     * Code found at https://medium.com/@ssaurel/create-an-audio-recorder-for-android-94dc7874f3d
     * @param savedInstanceState
     */

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permission();
        stop = findViewById(R.id.stop);
        record = findViewById(R.id.record);
        list = findViewById(R.id.recordingsList);
        chronometer = findViewById(R.id.chronometer);
        stop.setVisibility(View.GONE);
        list.setVisibility(View.VISIBLE);
        handler = new Handler();
        datapath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.3gp";
        String file_path = getApplicationContext().getFilesDir().getPath();
        final File file = new File(file_path);
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        recorder.setOutputFile(datapath);

        if (file.exists() == false) {
            file.mkdir();
        }

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    recorder.prepare();
                    recorder.start();
                } catch (IllegalStateException ise) {
                    Toast.makeText(getApplicationContext(), "Oops. Something went wrong.", Toast.LENGTH_LONG).show();
                } catch (IOException ioe) {
                    Toast.makeText(getApplicationContext(), "Oops. Something went wrong.", Toast.LENGTH_LONG).show();
                }
                if (resume == false) {
                    start = SystemClock.uptimeMillis();
                    handler.postDelayed(runnable, 0);
                    chronometer.start();
                    resume = true;
                }
                record.setVisibility(View.GONE);
                stop.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Recording started.", Toast.LENGTH_LONG).show();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recorder.stop();
                recorder.release();
                recorder = null;
                buff += millisec;
                handler.removeCallbacks(runnable);
                chronometer.stop();
                savedtime = chronometer.getBase() - SystemClock.elapsedRealtime();
                millisec = 0L;
                start = 0L;
                buff = 0L;
                update = 0L;
                sec = 0;
                min = 0;
                millisecond = 0;
                chronometer.setText("00:00:00");
                resume = false;
                record.setVisibility(View.VISIBLE);
                stop.setVisibility(View.GONE);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                final EditText editText = new EditText(MainActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                editText.setLayoutParams(lp);
                builder.setView(editText);


                builder.setTitle("Save recording?");
                builder.setMessage("You can set the recording's title below.")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // set title, save to device's storage
                        String uri = editText.getText().toString();


                        Intent intent = new Intent(getApplicationContext(), recordinglist.class);
                        startActivity(intent);


                    }
                })
                        .setNegativeButton("No, discard", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // exit dialog

                            }
                        });
                final AlertDialog dialog = builder.create();
                dialog.show();
                Toast.makeText(getApplicationContext(), "Audio recorded successfully.", Toast.LENGTH_LONG).show();
            }
        });


        list.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), recordinglist.class);

                startActivity(intent);
            }

        });

    }

    public Runnable runnable = new Runnable() {
        public void run() {
            millisec = SystemClock.uptimeMillis() - start;
            update = buff + millisec;
            sec = (int) (update / 100);
            min = sec/ 60;
            sec = sec % 60;
            millisec = (int) (update % 100);
            chronometer.setText(String.format("%02d",min) + ":" + String.format("%02d", sec) + ":" + String.format("%02d", millisec));
            handler.postDelayed(this, 60);
        }

    };


    /**
     * asks for permission to use storage and microphone.
     */

    public void permission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
        || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

        }
        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                RECORD_AUDIO_REQUEST_CODE);
    }

    /**
     * callback for when permission() is handled.
     * @param requestcode
     * @param permissions
     * @param results
     */

    public void permissionresult(int requestcode, String[] permissions, int[] results) {
        if (requestcode == RECORD_AUDIO_REQUEST_CODE) {
            if (results.length == 3 &&
                    results[0] == PackageManager.PERMISSION_GRANTED
                    && results[1] == PackageManager.PERMISSION_GRANTED
                    && results[2] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Recording permission granted. You may change this in your device's settings.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "You must request permission to record audio to use this application. Exiting...", Toast.LENGTH_SHORT).show();
                System.exit(0);
            }
        }
    }



}
