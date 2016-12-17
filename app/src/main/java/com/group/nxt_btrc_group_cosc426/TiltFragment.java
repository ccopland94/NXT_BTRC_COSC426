package com.group.nxt_btrc_group_cosc426;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import static android.content.Context.SENSOR_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class TiltFragment extends Fragment implements SensorEventListener {

    View fragView;

    Button cv_btnActivate;

    TextView cv_tvTest;
    TextView cv_tvReadings;

    float[] startVals = {-9999, -9999, -9999};

    // device sensor manager
    private SensorManager mSensorManager;

    public TiltFragment() {
        // Required empty public constructor
    }

    Boolean startMovement = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tilt, container, false);
        fragView = view;

        //cv_tvTest = (TextView) view.findViewById(R.id.vv_test);
        cv_tvReadings = (TextView) view.findViewById(R.id.vv_tvReadings);
        cv_btnActivate = (Button) view.findViewById(R.id.vv_btnActivate);

        cv_btnActivate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                {
                    startMovement = true;
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP)
                {
                    startMovement = false;
                    cv_tvReadings.setText("STOPPED");
                    ((MainActivity)getActivity()).cf_moveMotor(0, 25, 0x00);
                    ((MainActivity)getActivity()).cf_moveMotor(1, 25, 0x00);
                }
                return false;
            }
        });

        mSensorManager = (SensorManager) (getActivity()).getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);

        return view;
    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x, y, z;
        float xSensitivity = 5;
        float ySensitivity = 3;
        if (event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
            x=event.values[0];
            y=event.values[1];
            z=event.values[2];
            //set initial values if not set
            if(startVals[0] == -9999)
            {
                startVals[0] = x;
                startVals[1] = y;
                startVals[2] = z;
            }
            //cv_tvTest.setText("Start: x:"+startVals[0]+", y:"+startVals[1]);
            //cv_tvReadings.setText("Current: x:"+x+", y:"+y);

            try {
                if (startMovement == false) return;
                StringBuilder sb = new StringBuilder();
                sb.append("[" + event.values[0] + "]");
                sb.append("[" + event.values[1] + "]");
                sb.append("[" + event.values[2] + "]");
                int motorPower = 0;
                //cv_tvReadings.setText(sb.toString());
                // process this sensor data
                if (event.values[1] < (startVals[1] - ySensitivity)) {
                    motorPower = (int)Math.floor(Math.abs(event.values[1]-startVals[1]));
                    if(motorPower > 10) motorPower = 10;
                    motorPower = motorPower *10;
                    ((MainActivity)getActivity()).cf_moveMotor(0, motorPower, 0x20);
                    ((MainActivity)getActivity()).cf_moveMotor(1, motorPower, 0x20);
                    cv_tvReadings.setText("MOVE FORWARD");
                } else if (event.values[1] > (startVals[1] + ySensitivity)) {
                    motorPower = (int)Math.floor(Math.abs(event.values[1]-startVals[1]));
                    if(motorPower > 10) motorPower = 10;
                    motorPower = motorPower *10;
                    ((MainActivity)getActivity()).cf_moveMotor(0, -motorPower, 0x20);
                    ((MainActivity)getActivity()).cf_moveMotor(1, -motorPower, 0x20);
                    cv_tvReadings.setText("MOVE BACK");
                } else if (event.values[0] >(startVals[0] + xSensitivity)) {
                    motorPower = (int)Math.floor(Math.abs(event.values[0]-startVals[0]));
                    if(motorPower > 20) motorPower = 20;
                    motorPower = motorPower *5;
                    ((MainActivity)getActivity()).cf_moveMotor(0, motorPower, 0x20);
                    ((MainActivity)getActivity()).cf_moveMotor(1, -motorPower, 0x20);
                    cv_tvReadings.setText("MOVE LEFT");
                } else if (event.values[0] < (startVals[0] - xSensitivity)) {
                    motorPower = (int)Math.floor(Math.abs(event.values[0]-startVals[0]));
                    if(motorPower > 20) motorPower = 20;
                    motorPower = motorPower *5;
                    ((MainActivity)getActivity()).cf_moveMotor(0, -motorPower, 0x20);
                    ((MainActivity)getActivity()).cf_moveMotor(1, motorPower, 0x20);
                    cv_tvReadings.setText("MOVE RIGHT");
                } else {
                    cv_tvReadings.setText("STOPPED");
                    ((MainActivity)getActivity()).cf_moveMotor(0, 0, 0x00);
                    ((MainActivity)getActivity()).cf_moveMotor(1, 0, 0x00);
                }
            } catch (Exception e) {
                Log.e("ERROR:","onSensorChanged Error::" + e.getMessage());
            }
        }
    }

    //enable/disable inputs
    public void enableInputs(boolean value)
    {
        int[] buttons = {R.id.vv_btnActivate};
        for(int i=0; i < buttons.length; i++) {
            fragView.findViewById(buttons[i]).setEnabled(value);
        }
    }
}
