package com.group.nxt_btrc_group_cosc426;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Set;

public class ConnectFragment extends Fragment {

    private BluetoothAdapter mBluetoothAdapter;
    private Set<BluetoothDevice> mPairedDevices;
    private ArrayList<BluetoothDevice> mBluetoothList;
    TextView cv_connectStatus;
    TextView cv_cStatus;
    TextView cv_battery;
    ImageView cv_btImage;
    ProgressBar cv_pbbattery;
    Button cv_btnDisconnect;
    Button mBTNconnect;

    public ConnectFragment() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            // Bluetooth should be enabled already so this does not concern us
        }
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth but this does not concern us
            // NOTE:  android emulator does not support Bluetooth it will crash.
            // Must test on real device!
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_connect, container, false);

        cv_connectStatus = (TextView) view.findViewById(R.id.vv_connectStatus);
        cv_cStatus = (TextView)view.findViewById(R.id.vv_cStatus);
        cv_battery = (TextView)view.findViewById(R.id.vv_tvBatteryVolt);

        mBTNconnect = (Button) view.findViewById(R.id.BTN_connect);
        cv_btnDisconnect = (Button) view.findViewById(R.id.vv_btnDisconnect);
        cv_btImage = (ImageView) view.findViewById(R.id.vv_imgConnect);

        cv_pbbattery = (ProgressBar) view.findViewById(R.id.vv_progBar);
        mBTNconnect.setEnabled(true);
        cv_btnDisconnect.setEnabled(false);

        mBTNconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // run buildDialog() (private method below) and show that Dialog
                buildDialog().show();
            }
        });

        cv_btnDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //disconnect robot from device
                ((MainActivity)getActivity()).disconnectNXT();

            }
        });

        return view;
    }

    //popup dialog
    private Dialog buildDialog() {

        final ArrayAdapter<String> mPopupAdapter;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // get Set of bonded devices
        mPairedDevices = mBluetoothAdapter.getBondedDevices();

        // create adapter
        mPopupAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.select_dialog_item);
        mBluetoothList = new ArrayList<BluetoothDevice>();

        //TESTING COMMENT
        // add each device name and address to adapter
        for(BluetoothDevice device : mPairedDevices) {
            mPopupAdapter.add(device.getName() + "\n" + device.getAddress());
            mBluetoothList.add(device);
        }

        builder.setTitle("Paired Bluetooth Devices")
                .setAdapter(mPopupAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Set the bluetooth device in main activity to robot
                        ((MainActivity)getActivity()).setDevice(mBluetoothList.get(i));
                        //connect the NXT (bind it to the bluetooth connection)
                        ((MainActivity)getActivity()).connectNXT();
                    }
                });
        return builder.create();
    }

    //change text
    public void changeText(String name, boolean connected)
    {
        cv_connectStatus.setText(name);
        if(connected) {
            cv_cStatus.setText("Connected");
            cv_connectStatus.setTextColor(getResources().getColor(R.color.orange));
            cv_cStatus.setTextColor(getResources().getColor(android.R.color.black));
            cv_btImage.setImageResource(R.drawable.bluetoothon);
            mBTNconnect.setEnabled(false);
            cv_btnDisconnect.setEnabled(true);
        } else {
            cv_cStatus.setText("Not Connected");
            cv_connectStatus.setTextColor(getResources().getColor(android.R.color.darker_gray));
            cv_cStatus.setTextColor(getResources().getColor(android.R.color.darker_gray));
            cv_btImage.setImageResource(R.drawable.bluetoothoff);
            mBTNconnect.setEnabled(true);
            cv_btnDisconnect.setEnabled(false);
        }
    }

    //change battery information
    public void changeBattery(String info, boolean connected)
    {
        if(connected) {
            cv_battery.setText(info+" mV");
            cv_pbbattery.setProgress(Integer.parseInt(info));
        } else {
            cv_battery.setText(info);
            cv_pbbattery.setProgress(3000);
        }
    }
}