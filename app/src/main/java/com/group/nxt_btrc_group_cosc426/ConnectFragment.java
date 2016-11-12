package com.group.nxt_btrc_group_cosc426;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class ConnectFragment extends Fragment {

    private BluetoothAdapter mBluetoothAdapter;
    private Set<BluetoothDevice> mPairedDevices;
    private ArrayList<BluetoothDevice> mBluetoothList;
    TextView cv_connectStatus;
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
        mBTNconnect = (Button) view.findViewById(R.id.BTN_connect);
        cv_btnDisconnect = (Button) view.findViewById(R.id.vv_btnDisconnect);

        mBTNconnect.setEnabled(true);
        cv_btnDisconnect.setEnabled(false);

        //cf_setupBTMonitor();
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
                ((MainActivity)getActivity()).cf_disconnectNXT();

                //enable connect, disable disconnect
                mBTNconnect.setEnabled(true);
                cv_btnDisconnect.setEnabled(false);
            }
        });

        return view;
    }

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

                        //Toast.makeText(getContext(),cv_bd.getAddress(),Toast.LENGTH_SHORT).show();

                        //connect the NXT (bind it to the bluetooth connection)
                        ((MainActivity)getActivity()).cf_connectNXT();

                        //disable connect, enable disconnect
                        mBTNconnect.setEnabled(false);
                        cv_btnDisconnect.setEnabled(true);
                    }
                });
        return builder.create();
    }
}