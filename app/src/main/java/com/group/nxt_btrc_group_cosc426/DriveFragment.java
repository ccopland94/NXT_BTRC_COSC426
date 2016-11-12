package com.group.nxt_btrc_group_cosc426;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;

import java.lang.reflect.Array;


/**
 * A simple {@link Fragment} subclass.
 */
public class DriveFragment extends Fragment {

    View fragView;
    Button cv_btnDrive;
    Button cv_btnLeft;
    Button cv_btnRight;
    Button cv_btnBack;
    SeekBar cv_sbDrivePower;
    int drivePower;

    public DriveFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_drive, container, false);
        fragView = view;

        cv_btnDrive = (Button) view.findViewById(R.id.vv_btnDrive);
        cv_btnLeft = (Button) view.findViewById(R.id.vv_btnLeft);
        cv_btnRight = (Button) view.findViewById(R.id.vv_btnRight);
        cv_btnBack = (Button) view.findViewById(R.id.vv_btnBack);
        cv_sbDrivePower = (SeekBar)view.findViewById(R.id.vv_sbDrivePower);
        cv_sbDrivePower.setProgress(75);
        drivePower = 75;

        cv_btnDrive.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                {
                    ((MainActivity)getActivity()).cf_moveMotor(0, drivePower, 0x20);
                    ((MainActivity)getActivity()).cf_moveMotor(1, drivePower, 0x20);
                    toggleInputs(false, view);
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP)
                {
                    ((MainActivity)getActivity()).cf_moveMotor(0, drivePower, 0x00);
                    ((MainActivity)getActivity()).cf_moveMotor(1, drivePower, 0x00);
                    toggleInputs(true, view);
                }
                return true;
            }
        });

        cv_btnLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    ((MainActivity)getActivity()).cf_moveMotor(0, drivePower, 0x20);
                    ((MainActivity)getActivity()).cf_moveMotor(1, -drivePower, 0x20);
                    toggleInputs(false, view);
                }
                else if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    ((MainActivity)getActivity()).cf_moveMotor(0, drivePower, 0x00);
                    ((MainActivity)getActivity()).cf_moveMotor(1, -drivePower, 0x00);
                    toggleInputs(true, view);
                }
                return true;
            }
        });

        cv_btnRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    ((MainActivity)getActivity()).cf_moveMotor(0, -drivePower, 0x20);
                    ((MainActivity)getActivity()).cf_moveMotor(1, drivePower, 0x20);
                    toggleInputs(false, view);
                }
                else if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    ((MainActivity)getActivity()).cf_moveMotor(0, -drivePower, 0x00);
                    ((MainActivity)getActivity()).cf_moveMotor(1, drivePower, 0x00);
                    toggleInputs(true, view);
                }
                return true;
            }
        });

        cv_btnBack.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    ((MainActivity)getActivity()).cf_moveMotor(0, -drivePower, 0x20);
                    ((MainActivity)getActivity()).cf_moveMotor(1, -drivePower, 0x20);
                    toggleInputs(false, view);
                }
                else if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    ((MainActivity)getActivity()).cf_moveMotor(0, -drivePower, 0x00);
                    ((MainActivity)getActivity()).cf_moveMotor(1, -drivePower, 0x00);
                    toggleInputs(true, view);
                }
                return false;
            }
        });

        cv_sbDrivePower.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                drivePower = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //nothing
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //nothing
            }
        });

        return view;
    }

    private void toggleInputs(boolean value, View callingView)
    {
        int[] buttons = {R.id.vv_btnBack, R.id.vv_btnDrive, R.id.vv_btnLeft, R.id.vv_btnRight};
        for(int i=0; i < buttons.length; i++)
        {
            if(callingView.getId() != buttons[i])
            {
                fragView.findViewById(buttons[i]).setEnabled(value);
            }
        }
        cv_sbDrivePower.setEnabled(value);
    }

}
