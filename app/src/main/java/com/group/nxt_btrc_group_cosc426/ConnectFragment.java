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
    Button cv_btnDrive;
    Button cv_btnLeft;
    Button cv_btnRight;
    Button cv_btnBack;
    boolean cv_moveFlag = false;

    // BT Variables
    private BluetoothSocket cv_socket;
    BluetoothDevice cv_bd;
    final String CV_ROBOTNAME = "NXT";

    // Detect BT connection status
    private BroadcastReceiver cv_btMonitor = null;

    // Data stream to/from NXT bluetooth
    private InputStream cv_is = null;
    private OutputStream cv_os = null;

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
        Button mBTNconnect = (Button) view.findViewById(R.id.BTN_connect);
        cv_btnDisconnect = (Button) view.findViewById(R.id.vv_btnDisconnect);
        cv_btnDrive = (Button) view.findViewById(R.id.vv_btnDrive);
        cv_btnLeft = (Button) view.findViewById(R.id.vv_btnLeft);
        cv_btnRight = (Button) view.findViewById(R.id.vv_btnRight);
        cv_btnBack = (Button) view.findViewById(R.id.vv_btnBack);

        cf_setupBTMonitor();
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
                cf_disconnectNXT();
            }
        });

        cv_btnDrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cv_moveFlag) {
                    cf_moveMotor(0, 75, 0x20);
                    cf_moveMotor(1, 75, 0x20);
                }
                else {
                    cf_moveMotor(0, 75, 0x00);
                    cf_moveMotor(1, 75, 0x00);
                }
                cv_moveFlag = !cv_moveFlag;
            }
        });

        cv_btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cv_moveFlag) {
                    cf_moveMotor(0, 75, 0x20);
                    cf_moveMotor(1, -75, 0x20);
                }
                else {
                    cf_moveMotor(0, 75, 0x00);
                    cf_moveMotor(1, -75, 0x00);
                }
                cv_moveFlag = !cv_moveFlag;
            }
        });

        cv_btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cv_moveFlag) {
                    cf_moveMotor(0, -75, 0x20);
                    cf_moveMotor(1, 75, 0x20);
                }
                else {
                    cf_moveMotor(0, -75, 0x00);
                    cf_moveMotor(1, 75, 0x00);
                }
                cv_moveFlag = !cv_moveFlag;
            }
        });

        cv_btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cv_moveFlag) {
                    cf_moveMotor(0, -75, 0x20);
                    cf_moveMotor(1, -75, 0x20);
                }
                else {
                    cf_moveMotor(0, -75, 0x00);
                    cf_moveMotor(1, -75, 0x00);
                }
                cv_moveFlag = !cv_moveFlag;
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(cv_btMonitor,new IntentFilter("android.bluetooth.device.action.ACL_CONNECTED"));
        getActivity().registerReceiver(cv_btMonitor,new IntentFilter("android.bluetooth.device.action.ACL_DISCONNECTED"));
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(cv_btMonitor);
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
                        // this toast shows selection is working
                        cv_bd = mBluetoothList.get(i);
                        Toast.makeText(getContext(),cv_bd.getAddress(),Toast.LENGTH_SHORT).show();
                        cf_connectNXT();
                        // Here code should be called to create socket to select device
                        // Perhaps automatically change viewpager to Drive page if socket successful?
                    }
                });
        return builder.create();
    }

    private void cf_connectNXT() {
        try	{
            if (cv_bd.getName().equalsIgnoreCase(CV_ROBOTNAME)) {
                try {
                    cv_socket = cv_bd.createRfcommSocketToServiceRecord(
                            java.util.UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
                    cv_socket.connect();
                }
                catch (Exception e) {
                    cv_connectStatus.setText("Error interacting with remote device [" +
                            e.getMessage() + "]");
                }
            }
        }
        catch (Exception e) {
           // cv_tvHello.setText("Failed in findRobot() " + e.getMessage());
        }
    }
    private void cf_disconnectNXT() {
        try {
            cv_socket.close();
            cv_is.close();
            cv_os.close();
            cv_connectStatus.setText(cv_bd.getName() + " is disconnect " );
        } catch (Exception e) {
            cv_connectStatus.setText("Error in disconnect -> " + e.getMessage());
        }
    }

    private void cf_setupBTMonitor() {
        cv_btMonitor = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(
                        "android.bluetooth.device.action.ACL_CONNECTED")) {
                    try {
                        cv_is = cv_socket.getInputStream();
                        cv_os = cv_socket.getOutputStream();
                        cv_connectStatus.setText("Connect to " + cv_bd.getName() + " at " + cv_bd.getAddress());
                    } catch (Exception e) {
                        cf_disconnectNXT();
                        cv_is = null;
                        cv_os = null;
                    }
                }
                if (intent.getAction().equals(
                        "android.bluetooth.device.action.ACL_DISCONNECTED")) {
                    cv_connectStatus.setText("Connection is broken");
                }
            }
        };
    }

    private void cf_moveMotor(int motor,int speed, int state) {
        try {
            byte[] buffer = new byte[15];

            buffer[0] = (byte) (15-2);			//length lsb
            buffer[1] = 0;						// length msb
            buffer[2] =  0;						// direct command (with response)
            buffer[3] = 0x04;					// set output state
            buffer[4] = (byte) motor;			// output 1 (motor B)
            buffer[5] = (byte) speed;			// power
            buffer[6] = 1 + 2;					// motor on + brake between PWM
            buffer[7] = 0;						// regulation
            buffer[8] = 0;						// turn ration??
            buffer[9] = (byte) state; //0x20;					// run state
            buffer[10] = 0;
            buffer[11] = 0;
            buffer[12] = 0;
            buffer[13] = 0;
            buffer[14] = 0;

            cv_os.write(buffer);
            cv_os.flush();
        }
        catch (Exception e) {
            cv_connectStatus.setText("Error in MoveForward(" + e.getMessage() + ")");
        }
    }
}