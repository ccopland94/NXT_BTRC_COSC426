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
    Button cv_btnSingAny;
    SeekBar cv_sbSingHz;

    TextView cv_tvhZ;

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
        cv_btnSingAny = (Button) view.findViewById(R.id.vv_btnSingAny);
        cv_sbSingHz = (SeekBar) view.findViewById(R.id.vv_sbSingHz);
        cv_tvhZ = (TextView) view.findViewById(R.id.vv_tvHz);

        cv_btnSing1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                {
                    ((MainActivity)getActivity()).singNXT(1568);
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP)
                {
                    ((MainActivity)getActivity()).muteNXT();
                }
                return false;
            }
        });

        cv_btnSing2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                {
                    ((MainActivity)getActivity()).singNXT(1175);
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP)
                {
                    ((MainActivity)getActivity()).muteNXT();
                }
                return false;
            }
        });

        cv_btnSing3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                {
                    ((MainActivity)getActivity()).singNXT(880);
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP)
                {
                    ((MainActivity)getActivity()).muteNXT();
                }
                return false;
            }
        });

        cv_btnSing4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                {
                    ((MainActivity)getActivity()).singNXT(784);
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP)
                {
                    ((MainActivity)getActivity()).muteNXT();
                }
                return false;
            }
        });

        cv_btnSingAny.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int hZ = cv_sbSingHz.getProgress();
                hZ = hZ+200; //increase floor of hZ
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                {
                    ((MainActivity)getActivity()).singNXT(hZ);
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP)
                {
                    ((MainActivity)getActivity()).muteNXT();
                }
                return false;
            }
        });

        cv_sbSingHz.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                cv_tvhZ.setText((progress+200)+" hZ");
            }
        });

        return view;
    }

    //enable/disable inputs
    public void enableInputs(boolean value)
    {
        int[] buttons = {R.id.vv_btnSing1, R.id.vv_btnSing2, R.id.vv_btnSing3, R.id.vv_btnSing4, R.id.vv_btnSingAny};
        for(int i=0; i < buttons.length; i++)
        {
            fragView.findViewById(buttons[i]).setEnabled(value);
        }
    }
}
