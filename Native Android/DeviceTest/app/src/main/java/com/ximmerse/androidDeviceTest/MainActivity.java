package com.ximmerse.androidDeviceTest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.ximmerse.input.ControllerInput;
import com.ximmerse.input.PositionalTracking;
import com.ximmerse.os.LogCatHelper;
import com.ximmerse.sdk.XDeviceApi;
import com.ximmerse.sdk.XDeviceConstants;

import java.util.Timer;
import java.util.TimerTask;

import static com.ximmerse.sdk.XDeviceApi.startLogHelper;

public class MainActivity extends Activity implements  OnClickListener {
    protected TextView mTextID2 = null;
    protected TextView mTextID0 = null;
    protected TextView mTextID1 = null;
   private String  TAG=MainActivity.class.getCanonicalName();
    protected PositionalTracking mHeadTrack = null;
    protected ControllerInput mControllerInputLeft = null;
   protected ControllerInput mControllerInputRight = null;
    protected Button[] mButtonArray = new Button[11];
    protected int handleHeadTrack;
    protected int handleControllerLeft;
  protected int handleControllerRight;
    private Timer mTimer;
    protected static int logLevel = LogCatHelper.DEBUG;
    @Override
    protected void onDestroy()
    {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        XDeviceApi.exit();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate");
 if(logLevel < LogCatHelper.OFF) {
            startLogHelper(this, logLevel, null);
        }
        setContentView(R.layout.activity_main);
        XDeviceApi.init(this);
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

        handleHeadTrack = XDeviceApi.getInputDeviceHandle("XHawk-0");
        handleControllerLeft = XDeviceApi.getInputDeviceHandle("XCobra-0");
      handleControllerRight = XDeviceApi.getInputDeviceHandle("XCobra-1");

        mHeadTrack = new PositionalTracking(handleHeadTrack,"XHawk-0");
        mControllerInputLeft = new ControllerInput(handleControllerLeft,"XCobra-0");
       mControllerInputRight = new ControllerInput(handleControllerRight,"XCobra-1");

        for (Button b :mButtonArray ){b.setOnClickListener(this);}
        if (mTimer == null) {
            mTimer = new Timer();
        }
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mControllerInputLeft.updateState();
                mControllerInputRight.updateState();
                mHeadTrack.updateState();
                mHeadTrack.timestamp = 1;
                MainActivity.this.runOnUiThread(updateUI);
            }
        }, 0,10);
        ControllerInput.State dest=new  ControllerInput.State();
       float a[]= mControllerInputLeft.getState(dest).accelerometer;
    }


    protected Runnable updateUI = new Runnable() {
        @Override
        public void run() {
            mTextID2.setText(String.format("{HeadTrack position=%.3f %.3f %.3f \r\n", mHeadTrack.getPositionX(2), mHeadTrack.getPositionY(2), mHeadTrack.getPositionZ(2)));
            mTextID0.setText(mControllerInputLeft.toString());
          mTextID1.setText(mControllerInputRight.toString());

        }
    };

    @Override
    public void onClick(View view) {
        int status;
        switch (view.getId()) {
            case R.id.buttonID2DeviceStatus:
                XDeviceApi.updateInputState(handleHeadTrack);
                status = XDeviceApi.getInt(handleHeadTrack, XDeviceConstants.kField_ConnectionStateInt, 0);

                switch (status) {
                    case XDeviceConstants.kConnectionState_Disconnected: mButtonArray[10].setText("Disconnect"); break;
                    case XDeviceConstants.kConnectionState_Scanning:      mButtonArray[10].setText("Scanning"); break;
                    case XDeviceConstants.kConnectionState_Connecting:    mButtonArray[10].setText("Connecting"); break;
                    case XDeviceConstants.kConnectionState_Connected:     mButtonArray[10].setText("Connected"); break;
                    case XDeviceConstants.kConnectionState_Error:          mButtonArray[10].setText("Error("+XDeviceApi.getInt(handleHeadTrack, XDeviceConstants.kField_ConnectionStateInt, 0)+")"); break;
                    default:break;
                }
                break;


            case R.id.buttonID0DeviceStatus:
                XDeviceApi.updateInputState(handleControllerLeft);
                status = XDeviceApi.getInt(handleControllerLeft, XDeviceConstants.kField_ConnectionStateInt, 0);
                switch (status) {
                    case XDeviceConstants.kConnectionState_Disconnected: mButtonArray[3].setText("Disconnect"); break;
                    case XDeviceConstants.kConnectionState_Scanning:      mButtonArray[3].setText("Scanning"); break;
                    case XDeviceConstants.kConnectionState_Connecting:    mButtonArray[3].setText("Connecting"); break;
                    case XDeviceConstants.kConnectionState_Connected:     mButtonArray[3].setText("Connected"); break;
                    case XDeviceConstants.kConnectionState_Error:          mButtonArray[3].setText("Error("+XDeviceApi.getInt(handleControllerLeft, XDeviceConstants.kField_ConnectionStateInt, 0)+")"); break;
                    default:break;
                }
                break;

            case R.id.buttonID0Battery:
                XDeviceApi.updateInputState(handleControllerLeft);
                mButtonArray[4].setText("B("+XDeviceApi.getInt(handleControllerLeft, XDeviceConstants.kField_BatteryLevelInt, 0)+"%)");
                break;

            case R.id.buttonID0VibratorStart:
                int strength = 50; //(20~100) %
                int frequency = 0; //(default 0)
                int duration = 0; //ms
                XDeviceApi.sendMessage(handleControllerLeft, XDeviceConstants.kMessage_TriggerVibration,
                        (int)((strength <= 0 ? 20 : strength) | ((frequency << 16) & 0xFFFF0000)),
                        (int)(duration * 1000));
                break;

            case R.id.buttonID0VibratorStop:
                XDeviceApi.sendMessage(handleControllerLeft, XDeviceConstants.kMessage_TriggerVibration, -1, 0);
                break;

            case R.id.buttonID0Recenter:
             int ret=   XDeviceApi.sendMessage(handleControllerLeft, XDeviceConstants.kMessage_RecenterSensor, 0, 0);
                Log.e(TAG,"ret:"+ret);
                break;

            case R.id.buttonID1Battery:
               XDeviceApi.updateInputState(handleControllerRight);
                mButtonArray[9].setText("B("+XDeviceApi.getInt(handleControllerRight, XDeviceConstants.kField_BatteryLevelInt, 0)+"%)");
                break;

            case R.id.buttonID1DeviceStatus:
              XDeviceApi.updateInputState(handleControllerRight);
                status = XDeviceApi.getInt(handleControllerRight, XDeviceConstants.kField_ConnectionStateInt, 0);
                switch (status) {
                    case XDeviceConstants.kConnectionState_Disconnected: mButtonArray[8].setText("Disconnect"); break;
                    case XDeviceConstants.kConnectionState_Scanning:      mButtonArray[8].setText("Scanning"); break;
                    case XDeviceConstants.kConnectionState_Connecting:    mButtonArray[8].setText("Connecting"); break;
                    case XDeviceConstants.kConnectionState_Connected:     mButtonArray[8].setText("Connected"); break;
                    case XDeviceConstants.kConnectionState_Error:          mButtonArray[8].setText("Error("+XDeviceApi.getInt(handleControllerRight, XDeviceConstants.kField_ConnectionStateInt, 0)); break;
                    default:break;

                }
                break;
            case R.id.buttonID1VibratorStart:
                int strength1 = 50; //(20~100) %+-
                int frequency1 = 0; //(default 0)
                int duration1 = 0; //ms
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
            default:
                break;
        }

    }


/*   protected Runnable updatelog = new Runnable() {
       @Override
       public void run() {
           XDeviceApi.updateInputState(handleControllerLeft);
           LogFile.writeStr("B("+XDeviceApi.getInt(handleControllerLeft, XDeviceConstants.kField_BatteryLevelInt, 0)+"%)");
       }
   };*/

}
