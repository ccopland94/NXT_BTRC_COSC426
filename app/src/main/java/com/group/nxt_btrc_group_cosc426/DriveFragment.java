package com.group.nxt_btrc_group_cosc426;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class DriveFragment extends Fragment {

    View fragView;
    TextView cv_tvdrivePower;
    TextView cv_tvCpower;

    Button cv_btnDrive;
    Button cv_btnLeft;
    Button cv_btnRight;
    Button cv_btnBack;
    Button cv_btnCup;
    Button cv_btnCdown;

    SeekBar cv_sbDrivePower;
    SeekBar cv_sbCpower;

    int drivePower;
    int cPower;

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
        cv_btnCup = (Button) view.findViewById(R.id.vv_btnCup);
        cv_btnCdown = (Button) view.findViewById(R.id.vv_btnCdown);

        cv_sbDrivePower = (SeekBar)view.findViewById(R.id.vv_sbDrivePower);
        cv_sbDrivePower.setProgress(75);

        cv_sbCpower = (SeekBar)view.findViewById(R.id.vv_sbCpower);
        cv_sbCpower.setProgress(75);

        cv_tvdrivePower = (TextView)view.findViewById(R.id.vv_tvdrivePower);
        cv_tvCpower = (TextView)view.findViewById(R.id.vv_tvCpower);

        drivePower = 75;
        cPower = 75;

        enableInputs(false);

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
                return false;
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
                return false;
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
                return false;
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

        cv_btnCup.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    ((MainActivity)getActivity()).cf_moveMotor(2, cPower, 0x20);
                    toggleInputs(false, view);
                }
                else if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    ((MainActivity)getActivity()).cf_moveMotor(2, cPower, 0x00);
                    toggleInputs(true, view);
                }
                return false;
            }
        });

        cv_btnCdown.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    ((MainActivity)getActivity()).cf_moveMotor(2, -cPower, 0x20);
                    toggleInputs(false, view);
                }
                else if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    ((MainActivity)getActivity()).cf_moveMotor(2, -cPower, 0x00);
                    toggleInputs(true, view);
                }
                return false;
            }
        });

        cv_sbDrivePower.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                drivePower = progress;
                cv_tvdrivePower.setText(Integer.toString(progress));
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

        cv_sbCpower.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cPower = progress;
                cv_tvCpower.setText(Integer.toString(progress));
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

    //toggle inputs on or off
    private void toggleInputs(boolean value, View callingView)
    {
        int[] buttons = {R.id.vv_btnBack, R.id.vv_btnDrive, R.id.vv_btnLeft, R.id.vv_btnRight, R.id.vv_btnCup, R.id.vv_btnCdown};
        for(int i=0; i < buttons.length; i++)
        {
            if(callingView.getId() != buttons[i])
            {
                fragView.findViewById(buttons[i]).setEnabled(value);
            }
        }
        cv_sbDrivePower.setEnabled(value);
        cv_sbCpower.setEnabled(value);
    }

    //enable/disable inputs
    public void enableInputs(boolean value)
    {
        int[] buttons = {R.id.vv_btnBack, R.id.vv_btnDrive, R.id.vv_btnLeft, R.id.vv_btnRight, R.id.vv_btnCup, R.id.vv_btnCdown};
        for(int i=0; i < buttons.length; i++)
        {
            fragView.findViewById(buttons[i]).setEnabled(value);
        }
        cv_sbDrivePower.setEnabled(value);
        cv_sbCpower.setEnabled(value);
    }
}
