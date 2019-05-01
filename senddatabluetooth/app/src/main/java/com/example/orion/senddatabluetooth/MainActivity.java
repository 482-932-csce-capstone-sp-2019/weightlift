package com.example.orion.senddatabluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;
import java.time.*;

public class MainActivity extends AppCompatActivity implements Handler.Callback{
    private static final String TAG = "BluetoothChat";
    private Button send_data;
    private TextView DataViewRight;
    private TextView DataView;
    private TextView squatResponse;

    private Handler mhandler = null;
    private Handler mhandlerRight = null;
    private BluetoothChatService mChatService = null;
    private BluetoothChatService mChatServiceRight = null;
    private BluetoothAdapter mBluetoothAdapter = null;
    private String mConnectedDeviceLeft = "B8:27:EB:B9:CB:4C";
    private String mConnectedDeviceRight ="B8:27:EB:C1:70:29";
    private final static int REQUEST_ENABLE_BT = 1;
    private int buttonStat = 0;

    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("onCreate","Start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DataView = (TextView)findViewById(R.id.connectionstatus);
        DataViewRight = (TextView)findViewById(R.id.connectionstatus2);
        squatResponse = (TextView)findViewById(R.id.serveReply);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

        mhandler = new Handler(this);
//        mhandlerRight = new Handler(this);

        mChatService = new BluetoothChatService(this,mhandler,true);
        //mChatServiceRight = new BluetoothChatService(this,mhandler,false);

        for(BluetoothDevice bldevice : pairedDevices){
            //bldevice.getAddress()

            if(bldevice.getAddress().contains(mConnectedDeviceLeft)){
                mChatService.connect(bldevice,false);
            }
//            if(bldevice.getAddress().contains(mConnectedDeviceRight)){
//                mChatServiceRight.connect(bldevice,false);
//            }

        }
        send_data = (Button) findViewById(R.id.senddata);
        send_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_data.setBackground(getResources().getDrawable(R.drawable.buttonselector));
                long millis = System.currentTimeMillis();
                if(buttonStat == 0){
                    if(mChatService.getState() == mChatService.STATE_CONNECTED){

                        String stringdate = new Date().toString();
                        ParsePosition pos = new ParsePosition(0);
                        SimpleDateFormat simpledateformat = sdf;
                        Date stringDate = simpledateformat.parse(stringdate, pos);

                        //long milliSex = mdate.getTime();
                        mChatService.write((String.valueOf(millis)+", s").getBytes());
                    }
//                    if(mChatServiceRight.getState() == mChatServiceRight.STATE_CONNECTED){
//                        String stringdate = sdf.format(new Date());
//                        Date mDate = null;
//                        try {
//                            mDate = sdf.parse(stringdate);
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//                        //long milliSex = mdate.getTime();
//                        mChatServiceRight.write((String.valueOf(millis)+", s").getBytes());
//                    }
                    send_data.setText("Stop");
                    buttonStat = 1;
                }
                else if(buttonStat == 1) {
                    if(mChatService.getState() == mChatService.STATE_CONNECTED){
                        String stringdate = sdf.format(new Date());
                        Date mDate = null;
                        try {
                            mDate = sdf.parse(stringdate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        //long milliSex = mdate.getTime();
                        mChatService.write((String.valueOf(millis)+", p").getBytes());
                    }
//                    if(mChatServiceRight.getState() == mChatServiceRight.STATE_CONNECTED){
//                        String stringdate = sdf.format(new Date());
//                        Date mDate = null;
//                        try {
//                            mDate = sdf.parse(stringdate);
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//                        //long milliSex = mdate.getTime();
//                        mChatServiceRight.write((String.valueOf(millis)+", p").getBytes());
//                    }
                    send_data.setText("Start");
                    buttonStat = 0;
                }
            }
        });
        Log.d("onCreate","End");

    }

    @Override
    public void onStart() {
        super.onStart();
        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
           startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        } else if (mChatService == null || mChatServiceRight == null) {
//
Log.d("onStart","else if mChatService == null");

        }
    }
    @Override
    public void onDestroy() {
        Log.d("onDestroy","start");

        super.onDestroy();
        if (mChatService != null) {
            mChatService.stop();
        }
//        if (mChatServiceRight != null) {
//            mChatServiceRight.stop();
//        }

    }
    @Override
    public void onResume() {
       Log.d("onResume","start");

        super.onResume();

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mChatService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
                // Start the Bluetooth chat services
                mChatService.start();
            }
        }
//        if (mChatServiceRight != null) {
//            // Only if the state is STATE_NONE, do we know that we haven't started already
//            if (mChatServiceRight.getState() == BluetoothChatService.STATE_NONE) {
//                // Start the Bluetooth chat services
//                mChatServiceRight.start();
//            }
//        }
    }

    @Override
    public boolean handleMessage(Message message) {
        String[] MessageStatus = new String[]{"STATE_NONE","STATE_LISTEN","STATE_CONNECTING","STATE_CONNECTED"};

        switch (message.what) {
           case Constants.MESSAGE_STATE_CHANGE_Left:
               if(MessageStatus[message.arg1] == "STATE_LISTEN")
               {
                   mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                   Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
                   for(BluetoothDevice bldevice : pairedDevices){
                       if(bldevice.getName().contains(mConnectedDeviceLeft)){
                           mChatService.connect(bldevice,true);
                       }
                   }
               }
               DataView.setText(MessageStatus[message.arg1]);
               break;

            case Constants.MESSAGE_STATE_CHANGE_Right:
//                if(MessageStatus[message.arg1] == "STATE_LISTEN")
//                {
//                    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//                    Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
//                    for(BluetoothDevice bldevice : pairedDevices){
//                        if(bldevice.getName().contains(mConnectedDeviceRight)){
//                            mChatServiceRight.connect(bldevice,true);
//                        }
//                    }
//                }
                DataViewRight.setText(MessageStatus[message.arg1]);
                break;
            case Constants.MESSAGE_READ:
                if(message.arg1 != 0){
                    String s = new String((byte[]) message.obj);
                    squatResponse.setText(s);}
                else{
                    squatResponse.setText("No Data");
                    }
                break;
        }
        return true;
    }
}
