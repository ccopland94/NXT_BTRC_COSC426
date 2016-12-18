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

    Button cv_btnSing1;
    Button cv_btnSing2;
    Button cv_btnSing3;
    Button cv_btnSing4;

    public SingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sing, container, false);
        fragView = view;

        cv_btnSing1 = (Button) view.findViewById(R.id.vv_btnSing1);
        cv_btnSing2 = (Button) view.findViewById(R.id.vv_btnSing2);
        cv_btnSing3 = (Button) view.findViewById(R.id.vv_btnSing3);
        cv_btnSing4 = (Button) view.findViewById(R.id.vv_btnSing4);

        cv_btnSing1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                {
                    ((MainActivity)getActivity()).singNXT(1568);
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP)
                {
                }
                return false;
            }
        });

        cv_btnSing2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                {
                    ((MainActivity)getActivity()).singNXT(1000);
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP)
                {
                }
                return false;
            }
        });

        cv_btnSing3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                {
                    ((MainActivity)getActivity()).singNXT(750);
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP)
                {
                }
                return false;
            }
        });

        cv_btnSing4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                {
                    ((MainActivity)getActivity()).singNXT(500);
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP)
                {
                }
                return false;
            }
        });

        return view;
    }

    //enable/disable inputs
    public void enableInputs(boolean value)
    {
        int[] buttons = {R.id.vv_btnSing1, R.id.vv_btnSing2, R.id.vv_btnSing3, R.id.vv_btnSing4};
        for(int i=0; i < buttons.length; i++)
        {
            fragView.findViewById(buttons[i]).setEnabled(value);
        }
    }
}
