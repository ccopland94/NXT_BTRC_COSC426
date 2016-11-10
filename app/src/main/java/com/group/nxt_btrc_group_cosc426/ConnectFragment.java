package com.group.nxt_btrc_group_cosc426;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import java.util.Set;

public class ConnectFragment extends Fragment {

    private BluetoothAdapter mBluetoothAdapter;
    private Set<BluetoothDevice> mPairedDevices;

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

        Button mBTNconnect = (Button) view.findViewById(R.id.BTN_connect);
        mBTNconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // run buildDialog() (private method below) and show that Dialog
                buildDialog().show();
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

        // add each device name and address to adapter
        for(BluetoothDevice device : mPairedDevices) {
            mPopupAdapter.add(device.getName() + "\n" + device.getAddress());
        }

        builder.setTitle("Paired Bluetooth Devices")
                .setAdapter(mPopupAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // this toast shows selection is working
                        Toast.makeText(getContext(),mPopupAdapter.getItem(i),Toast.LENGTH_SHORT).show();

                        // Here code should be called to create socket to select device
                        // Perhaps automatically change viewpager to Drive page if socket successful?
                    }
                });
        return builder.create();
    }
}