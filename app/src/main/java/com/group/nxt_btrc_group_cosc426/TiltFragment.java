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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

import static android.content.Context.SENSOR_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class TiltFragment extends Fragment implements SensorEventListener {

    View fragView;

    Button cv_btnActivate;

    ImageView cv_ivArrow;

    ProgressBar cv_pbTiltPower;

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
        cv_btnActivate = (Button) view.findViewById(R.id.vv_btnActivate);
        cv_ivArrow = (ImageView) view.findViewById(R.id.vv_imgArrow);
        cv_pbTiltPower = (ProgressBar) view.findViewById(R.id.vv_pbTiltPower);

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
                    ((MainActivity)getActivity()).driveNXT(0, 0, 0x00, 1, 0, 0x00);
                    cv_ivArrow.setImageResource(R.drawable.circle);
                    cv_pbTiltPower.setProgress(0);
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
                startVals[0] = 0;
                startVals[1] = 0;
                startVals[2] = 0;
            }

            try {
                if (startMovement == false) return;
                int motorPower = 0;
                // process this sensor data
                if (event.values[1] < (startVals[1] - ySensitivity)) {
                    motorPower = (int)Math.floor(Math.abs(event.values[1]-startVals[1]));
                    if(motorPower > 10) motorPower = 10;
                    motorPower = motorPower *10;
                    cv_pbTiltPower.setProgress(motorPower);
                    ((MainActivity)getActivity()).driveNXT(0, motorPower, 0x20, 1, motorPower, 0x20);
                    cv_ivArrow.setImageResource(R.drawable.arrow_right);
                    cv_ivArrow.setRotation(-90);
                } else if (event.values[1] > (startVals[1] + ySensitivity)) {
                    motorPower = (int)Math.floor(Math.abs(event.values[1]-startVals[1]));
                    if(motorPower > 10) motorPower = 10;
                    motorPower = motorPower *10;
                    cv_pbTiltPower.setProgress(motorPower);
                    ((MainActivity)getActivity()).driveNXT(0, -motorPower, 0x20, 1, -motorPower, 0x20);
                    cv_ivArrow.setImageResource(R.drawable.arrow_right);
                    cv_ivArrow.setRotation(90);
                } else if (event.values[0] >(startVals[0] + xSensitivity)) {
                    motorPower = (int)Math.floor(Math.abs(event.values[0]-startVals[0]));
                    if(motorPower > 10) motorPower = 10;
                    motorPower = motorPower *10;
                    cv_pbTiltPower.setProgress(motorPower);
                    ((MainActivity)getActivity()).driveNXT(0, motorPower, 0x20, 1, -motorPower, 0x20);
                    cv_ivArrow.setImageResource(R.drawable.arrow_right);
                    cv_ivArrow.setRotation(-180);
                } else if (event.values[0] < (startVals[0] - xSensitivity)) {
                    motorPower = (int)Math.floor(Math.abs(event.values[0]-startVals[0]));
                    if(motorPower > 10) motorPower = 10;
                    motorPower = motorPower *10;
                    cv_pbTiltPower.setProgress(motorPower);
                    ((MainActivity)getActivity()).driveNXT(0, -motorPower, 0x20, 1, motorPower, 0x20);
                    cv_ivArrow.setImageResource(R.drawable.arrow_right);
                    cv_ivArrow.setRotation(0);
                } else {
                    cv_pbTiltPower.setProgress(0);
                    ((MainActivity)getActivity()).driveNXT(0, 0, 0x00, 1, 0, 0x00);
                    cv_ivArrow.setImageResource(R.drawable.circle);
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
