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
public class SingFragment extends Fragment {

    View fragView;

    Button cv_btnSing;

    public SingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sing, container, false);
        fragView = view;

        cv_btnSing = (Button) view.findViewById(R.id.vv_btnSing);

        cv_btnSing.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                {
                    ((MainActivity)getActivity()).playTone(1568);
                    //toggleInputs(false, view);
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP)
                {
                    //((MainActivity)getActivity()).cf_moveMotor(0, drivePower, 0x00);
                    //((MainActivity)getActivity()).cf_moveMotor(1, drivePower, 0x00);
                    //toggleInputs(true, view);
                }
                return false;
            }
        });

        return view;
    }

    //toggle inputs on or off
    /*private void toggleInputs(boolean value, View callingView)
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
    }*/
}
