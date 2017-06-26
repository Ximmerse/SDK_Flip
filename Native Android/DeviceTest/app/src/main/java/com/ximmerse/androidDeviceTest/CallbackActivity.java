package com.ximmerse.androidDeviceTest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.ximmerse.hardware.DeviceConnection;
import com.ximmerse.input.ControllerInput;
import com.ximmerse.input.PositionalTracking;
import com.ximmerse.sdk.DeviceVersion;
import com.ximmerse.sdk.LaunchMode;
import com.ximmerse.sdk.XDeviceApi;
import com.ximmerse.sdk.XDeviceConstants;

import java.util.Timer;
import java.util.TimerTask;

public class CallbackActivity extends Activity implements  OnClickListener {
    protected TextView mTextID2 = null;
    protected TextView mTextID0 = null;
    protected TextView mTextID1 = null;

    protected Button[] mButtonArray = new Button[11];

    protected int handleControllerLeft;  // left controller
    protected int handleControllerRight;  // right controller
    protected int handleHeadTrack;  //  head blob
    protected PositionalTracking mHeadTrack = null;
    protected ControllerInput mControllerInputLeft = null;
    protected ControllerInput mControllerInputRight = null;


    @Override
    protected void onDestroy()
    {
        XDeviceApi.exit();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextID2 = (TextView)findViewById(R.id.textviewID2);
        mTextID0 = (TextView)findViewById(R.id.textviewID0);
        mTextID1 = (TextView)findViewById(R.id.textviewID1);
        mButtonArray[0] = (Button)findViewById(R.id.buttonID0VibratorStart);
        mButtonArray[1]  = (Button)findViewById(R.id.buttonID0VibratorStop);
        mButtonArray[2]  = (Button)findViewById(R.id.buttonID0Recenter);
        mButtonArray[3]  = (Button)findViewById(R.id.buttonID0DeviceStatus);
        mButtonArray[4]  = (Button)findViewById(R.id.buttonID0Battery);

        mButtonArray[5] = (Button)findViewById(R.id.buttonID1VibratorStart);
        mButtonArray[6] = (Button)findViewById(R.id.buttonID1VibratorStop);
        mButtonArray[7] = (Button)findViewById(R.id.buttonID1Recenter);
        mButtonArray[8]  = (Button)findViewById(R.id.buttonID1DeviceStatus);
        mButtonArray[9]  = (Button)findViewById(R.id.buttonID1Battery);

        mButtonArray[10]  = (Button)findViewById(R.id.buttonID2DeviceStatus);

        //XDeviceApi.startLogHelper(this, LogCatHelper.DEBUG, null);
        XDeviceApi.init(this, DeviceVersion.DEVELOPMENT_KIT_4, LaunchMode.ANDROID_DEFAULT);

        handleHeadTrack = XDeviceApi.getInputDeviceHandle("XHawk-0");
        handleControllerLeft = XDeviceApi.getInputDeviceHandle("XCobra-0");
        handleControllerRight = XDeviceApi.getInputDeviceHandle("XCobra-1");

        DeviceConnection cnn=XDeviceApi.sDeviceManager.getDevice(handleHeadTrack);
        if(cnn!=null){
            cnn.setOnDeviceConnectedStateListener(new HeadTrackStateListener());
        }
        cnn=XDeviceApi.sDeviceManager.getDevice(handleControllerLeft);
        if(cnn!=null){
            cnn.setOnDeviceConnectedStateListener(new ControllerLeftStateListener());
        }
        cnn=XDeviceApi.sDeviceManager.getDevice(handleControllerRight);
        if(cnn!=null){
            cnn.setOnDeviceConnectedStateListener(new ControllerRightStateListener());
        }

        mHeadTrack = new PositionalTracking(handleHeadTrack,"XHawk-0");
        mControllerInputLeft = new ControllerInput(handleControllerLeft,"XCobra-0");
        mControllerInputRight = new ControllerInput(handleControllerRight,"XCobra-1");

        for (Button b :mButtonArray ){b.setOnClickListener(this);}


        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                CallbackActivity.this.runOnUiThread(updateUI);
            }
        }, 500, 100);
    }

    protected Runnable updateUI = new Runnable() {
        @Override
        public void run() {
            mControllerInputLeft.updateState();
            mControllerInputRight.updateState();
            mHeadTrack.updateState();
           // mHeadTrack.timestamp = 1;

            mTextID2.setText(String.format("{HeadTrack %d position=%.3f %.3f %.3f \r\n", mHeadTrack.timestamp, mHeadTrack.getPositionX(2), mHeadTrack.getPositionY(2), mHeadTrack.getPositionZ(2)));
            mTextID0.setText(mControllerInputLeft.toString());
            mTextID1.setText(mControllerInputRight.toString());
        }
    };
   // protected  String  sState="";
    public class ControllerLeftStateListener  implements DeviceConnection.DeviceConnectedState{
       String  sState="";
        @Override
        public void onDeviceState(int state) {
            Log.e("wuyc", "onDeviceState:"+state);
            if (state == DeviceConnection.CONNECTED){
                sState = "Connected";
            }else{
                sState = "Disconnect";
            }

            CallbackActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mButtonArray[3].setText(sState);
                }
            });
        }
    }

    public class ControllerRightStateListener  implements DeviceConnection.DeviceConnectedState{
        String  sState="";
        @Override
        public void onDeviceState(int state) {
            if (state == DeviceConnection.CONNECTED){
                sState = "Connected";
            }else{
                sState = "Disconnect";
            }

            CallbackActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mButtonArray[8].setText(sState);
                }
            });
        }
    }


    public class HeadTrackStateListener  implements DeviceConnection.DeviceConnectedState{
        String  sState="";
        @Override
        public void onDeviceState(int state) {
            if (state == DeviceConnection.CONNECTED){
                sState = "Connected";
            }else{
                sState = "Disconnect";
            }

            CallbackActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mButtonArray[10].setText(sState);
                }
            });
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonID2DeviceStatus:
                XDeviceApi.updateInputState(handleHeadTrack);
                int status = XDeviceApi.getInt(handleHeadTrack, XDeviceConstants.kField_ConnectionStateInt, 0);
                switch (status) {
                    case XDeviceConstants.kConnectionState_Disconnected:  mButtonArray[10].setText("1"); break;
                    case XDeviceConstants.kConnectionState_Scanning:       mButtonArray[10].setText("2");break;
                    case XDeviceConstants.kConnectionState_Connecting:    mButtonArray[10].setText("3"); break;
                    case XDeviceConstants.kConnectionState_Connected:     mButtonArray[10].setText("3"); break;
                    case XDeviceConstants.kConnectionState_Error:          mButtonArray[10].setText("5("+XDeviceApi.getInt(handleHeadTrack, XDeviceConstants.kField_ConnectionStateInt, 0)+")"); break;
                    default:break;
                }
                break;
            case R.id.buttonID0VibratorStart:
                int strength = 50; //(20~100) %
                int frequency = 0; //(default 0)
                int duration = 2; //ms
                XDeviceApi.sendMessage(handleControllerLeft, XDeviceConstants.kMessage_TriggerVibration,
                        (int)((strength <= 0 ? 20 : strength) | ((frequency << 16) & 0xFFFF0000)),
                        (int)(duration * 1000));
                break;

            case R.id.buttonID0VibratorStop:
                XDeviceApi.sendMessage(handleControllerLeft, XDeviceConstants.kMessage_TriggerVibration, -1, 0);
                break;

            case R.id.buttonID0Recenter:
                XDeviceApi.sendMessage(handleControllerLeft, XDeviceConstants.kMessage_RecenterSensor, 0, 0);
                break;

            case R.id.buttonID1VibratorStart:
                int strength1 = 50; //(20~100) %
                int frequency1 = 0; //(default 0)
                int duration1 = 2; //ms
                XDeviceApi.sendMessage(handleControllerRight, XDeviceConstants.kMessage_TriggerVibration,
                        (int)((strength1 <= 0 ? 20 : strength1) | ((frequency1 << 16) & 0xFFFF0000)),
                        (int)(duration1 * 1000));
                break;

            case R.id.buttonID1VibratorStop:
                XDeviceApi.sendMessage(handleControllerRight, XDeviceConstants.kMessage_TriggerVibration, -1, 0);
                break;

            case R.id.buttonID1Recenter:
                XDeviceApi.sendMessage(handleControllerRight, XDeviceConstants.kMessage_RecenterSensor, 0, 0);
                break;

            case R.id.buttonID0Battery:
                XDeviceApi.updateInputState(handleControllerLeft);
                mButtonArray[4].setText("B("+XDeviceApi.getInt(handleControllerLeft, XDeviceConstants.kField_BatteryLevelInt, 0)+"%)");
                break;

            case R.id.buttonID1Battery:
                XDeviceApi.updateInputState(handleControllerRight);
                mButtonArray[9].setText("B("+XDeviceApi.getInt(handleControllerRight, XDeviceConstants.kField_BatteryLevelInt, 0)+"%)");
                break;

            default:
                break;
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (event.getKeyCode()==KeyEvent.KEYCODE_BACK)
        {
            System.exit(0);
        }

        return true;
    }
}
