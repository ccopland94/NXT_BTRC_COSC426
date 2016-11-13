package com.group.nxt_btrc_group_cosc426;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    // THIS IS JUST A TEST COMMENT FOR LEARNING GITHUB
    // SECOND TEST COMMENT

    private MyFragmentPagerAdapter mMyFragmentPagerAdapter;
    private ViewPager mViewPager;

    // Detect BT connection status
    private BroadcastReceiver cv_btMonitor = null;

    // Data stream to/from NXT bluetooth
    private InputStream cv_is = null;
    private OutputStream cv_os = null;

    // BT Variables
    private BluetoothSocket cv_socket;
    BluetoothDevice cv_bd;
    final String CV_ROBOTNAME = "NXT";
    static ConnectFragment cv_connectView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMyFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), this);
        mViewPager = (ViewPager) findViewById(R.id.VP_viewpager);
        mViewPager.setAdapter(mMyFragmentPagerAdapter);
        mViewPager.setCurrentItem(1); // Connect page
        cf_setupBTMonitor();
    }

    private static class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        Context mContext;

        private static int NUM_ITEMS = 2;

        public MyFragmentPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            mContext = context;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0: return "Drive";
                case 1: return "Connect";
                default: return null;
            }
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: return new DriveFragment();
                case 1:
                    cv_connectView = new ConnectFragment();
                    return cv_connectView;
                default: return null;
            }
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(cv_btMonitor,new IntentFilter("android.bluetooth.device.action.ACL_CONNECTED"));
        registerReceiver(cv_btMonitor,new IntentFilter("android.bluetooth.device.action.ACL_DISCONNECTED"));
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(cv_btMonitor);
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
                        cv_connectView.changeText(cv_bd.getName(), true);
                        //cv_connectStatus.setText("Connect to " + cv_bd.getName() + " at " + cv_bd.getAddress());
                    } catch (Exception e) {
                        cf_disconnectNXT();
                        cv_connectView.changeText("Device", false);
                        cv_is = null;
                        cv_os = null;
                    }
                }
                if (intent.getAction().equals(
                        "android.bluetooth.device.action.ACL_DISCONNECTED")) {
                    //cv_connectStatus.setText("Connection is broken");
                }
            }
        };
    }
    public void setDevice(BluetoothDevice device)
    {
        this.cv_bd = device;
    }
    public void cf_connectNXT() {
        try	{
            if (cv_bd.getName().equalsIgnoreCase(CV_ROBOTNAME)) {
                try {
                    cv_socket = cv_bd.createRfcommSocketToServiceRecord(
                            java.util.UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
                    cv_socket.connect();
                    cv_connectView.changeText(cv_bd.getName(), true);
                }
                catch (Exception e) {
                    cv_connectView.changeText("Device", false);
                }
            }
        }
        catch (Exception e) {
            // cv_tvHello.setText("Failed in findRobot() " + e.getMessage());
        }
    }
    public void cf_disconnectNXT() {
        try {
            cv_socket.close();
            cv_is.close();
            cv_os.close();
            cv_connectView.changeText("Device", false);
        } catch (Exception e) {
            //cv_connectStatus.setText("Error in disconnect -> " + e.getMessage());
        }
    }

    public void cf_moveMotor(int motor,int speed, int state) {
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
            //cv_connectStatus.setText("Error in MoveForward(" + e.getMessage() + ")");
        }
    }
}
