package com.example.orion.datatracker;

import android.content.Context;
import android.os.Environment;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button start_freestyle;
    Button start_backstroke;
    Button start_breaststroke;
    Button start_butterfly;
    Button start_treadingwater;
    Button start_sidestroke;
    Button start_flipturns;

    int newlocation;

    Button stop_freestyle;
    Button stop_backstroke;
    Button stop_breaststroke;
    Button stop_butterfly;
    Button stop_treadingwater;
    Button stop_sidestroke;
    Button stop_flipturns;

    Button create_participant;
    TextView participant_number;
    Vibrator vibrate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vibrate = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Tag");
        wl.acquire();
        participant_number = findViewById(R.id.participantcount);
        File root = Environment.getExternalStorageDirectory();
        File aquatictrackerlocation = new File(root.getAbsolutePath()+'/'+"TrackerAquaSwimming");
        if(aquatictrackerlocation.listFiles().length == 0){
            newlocation = aquatictrackerlocation.listFiles().length;
            participant_number.setText("Participant: " + String.valueOf(newlocation));
        }
        else{
            newlocation = aquatictrackerlocation.listFiles().length - 1;
            participant_number.setText("Participant: " + String.valueOf(newlocation));
        }
        start_freestyle = findViewById(R.id.start_freestyle);
        start_backstroke = findViewById(R.id.start_backstroke);
        start_breaststroke = findViewById(R.id.start_breaststroke);
        start_butterfly = findViewById(R.id.start_butterfly);
        start_treadingwater = findViewById(R.id.start_treadingwater);
        start_sidestroke = findViewById(R.id.start_sidestroke);
        start_flipturns = findViewById(R.id.start_flipturns);

        start_freestyle.setOnClickListener(this);
        start_backstroke.setOnClickListener(this);
        start_breaststroke.setOnClickListener(this);
        start_butterfly.setOnClickListener(this);
        start_treadingwater.setOnClickListener(this);
        start_sidestroke.setOnClickListener(this);
        start_flipturns.setOnClickListener(this);

        stop_freestyle = findViewById(R.id.stop_freestyle);
        stop_backstroke = findViewById(R.id.stop_backstroke);
        stop_breaststroke = findViewById(R.id.stop_breaststroke);
        stop_butterfly = findViewById(R.id.stop_butterfly);
        stop_treadingwater = findViewById(R.id.stop_treadingwater);
        stop_sidestroke = findViewById(R.id.stop_sidestroke);
        stop_flipturns = findViewById(R.id.stop_flipturns);

        stop_freestyle.setOnClickListener(this);
        stop_backstroke.setOnClickListener(this);
        stop_breaststroke.setOnClickListener(this);
        stop_butterfly.setOnClickListener(this);
        stop_treadingwater.setOnClickListener(this);
        stop_sidestroke.setOnClickListener(this);
        stop_flipturns.setOnClickListener(this);

        create_participant = findViewById(R.id.new_participant);
        create_participant.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        String time_stamp = String.valueOf(System.currentTimeMillis());
        switch (v.getId()){
            //------------------------- START -------------------------------------
            case R.id.start_freestyle:
                append_to_file("START","FREESTYLE",time_stamp);
                break;
            case R.id.start_backstroke:
                append_to_file("START","BACKSTROKE",time_stamp);
                break;
            case R.id.start_breaststroke:
                append_to_file("START","BREASTSTROKE",time_stamp);
                break;
            case R.id.start_butterfly:
                append_to_file("START","BUTTERFLY",time_stamp);
                break;
            case R.id.start_treadingwater:
                append_to_file("START","TREADINGWATER",time_stamp);
                break;
            case R.id.start_sidestroke:
                append_to_file("START","SIDESTROKE",time_stamp);
                break;
            case R.id.start_flipturns:
                append_to_file("START","FLIPTURN",time_stamp);
                break;

            //------------------------- STOP -------------------------------------
            case R.id.stop_freestyle:
                append_to_file("STOP","FREESTYLE",time_stamp);
                break;
            case R.id.stop_backstroke:
                append_to_file("STOP","BACKSTROKE",time_stamp);
                break;
            case R.id.stop_breaststroke:
                append_to_file("STOP","BREASTSTROKE",time_stamp);
                break;
            case R.id.stop_butterfly:
                append_to_file("STOP","BUTTERFLY",time_stamp);
                break;
            case R.id.stop_treadingwater:
                append_to_file("STOP","TREADINGWATER",time_stamp);
                break;
            case R.id.stop_sidestroke:
                append_to_file("STOP","SIDESTROKE",time_stamp);
                break;
            case R.id.stop_flipturns:
                append_to_file("STOP","FLIPTURN",time_stamp);
                break;

            //------------------------- Create Participant -------------------------------------
            case R.id.new_participant:
                newlocation++;
                participant_number.setText("Participant: " + String.valueOf(newlocation));
                break;
        }
        vibrate.vibrate(1000);
    }

    public void append_to_file(String start_stop, String Stroke_type, String timestamp){
        String filename = "participants";
        String fileContents = start_stop + ',' + Stroke_type + ',' + timestamp;
        FileOutputStream fos;
        File root = Environment.getExternalStorageDirectory();
        File aquatictrackerlocation = new File(root.getAbsolutePath()+'/'+"TrackerAquaSwimming");
        filename = filename + String.valueOf(newlocation) + ".txt";

        try {
            fos = new FileOutputStream(aquatictrackerlocation.getAbsolutePath()+"/" + filename, true);

            FileWriter fWriter;

            try {
                fWriter = new FileWriter(fos.getFD());
                fWriter.append(fileContents);
                fWriter.append("\n");
                fWriter.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                fos.getFD().sync();
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
