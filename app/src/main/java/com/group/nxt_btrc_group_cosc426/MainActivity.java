package com.group.nxt_btrc_group_cosc426;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Timer;
import java.util.TimerTask;


/**
 * 11/14/16
 * Group Project NXT Submission - Part 2
 * COSC 426
 *
 * Cameron Copland
 * Ryan Speck
 * Connor Krupa
 * Joshua Kreider
 */
public class MainActivity extends AppCompatActivity {

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
    static DriveFragment cv_driveView;
    static SingFragment cv_singView;
    static TiltFragment cv_tiltView;

    byte battery1;
    byte battery2;

    RobotBattery battery;
    RobotConnect asyncConnect;
    RobotDisconnect asyncDisconnect;
    RobotSing asyncSing;
    RobotMute asyncMute;
    RobotDrive asyncDrive;

    //creates activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMyFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), this);
        mViewPager = (ViewPager) findViewById(R.id.VP_viewpager);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(mMyFragmentPagerAdapter);
        mViewPager.setCurrentItem(0); // Connect page
        cf_setupBTMonitor();
    }

    //pager adapter
    private static class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        Context mContext;

        private static int NUM_ITEMS = 4;

        public MyFragmentPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            mContext = context;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0: return "Connect";
                case 1: return "Drive";
                case 2: return "Sing";
                case 3: return "Tilt Control";
                default: return null;
            }
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 1:
                    cv_driveView = new DriveFragment();
                    return cv_driveView;
                case 0:
                    cv_connectView = new ConnectFragment();
                    return cv_connectView;
                case 2:
                    cv_singView = new SingFragment();
                    return cv_singView;
                case 3:
                    cv_tiltView = new TiltFragment();
                    return cv_tiltView;
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

    //bluetooth monitor
    private void cf_setupBTMonitor() {
        cv_btMonitor = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(
                        "android.bluetooth.device.action.ACL_CONNECTED")) {
                    try {
                        cv_is = cv_socket.getInputStream();
                        cv_os = cv_socket.getOutputStream();
                        battery = new RobotBattery();
                        battery.execute();
                    } catch (Exception e) {
                        cf_disconnectNXT();
                        cv_is = null;
                        cv_os = null;
                    }
                }
                if (intent.getAction().equals(
                        "android.bluetooth.device.action.ACL_DISCONNECTED")) {
                    cf_disconnectNXT();
                    cv_is = null;
                    cv_os = null;
                }
            }
        };
    }
    //sets the device
    public void setDevice(BluetoothDevice device)
    {
        this.cv_bd = device;
    }

    //connects the NXT
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
                    cv_connectView.changeBattery("Battery Info", false);
                    cv_driveView.enableInputs(false);
                    cv_tiltView.enableInputs(false);
                    cv_singView.enableInputs(false);
                }
            }
        }
        catch (Exception e) {
        }
    }

    //disconnect NXT
    public void cf_disconnectNXT() {
        try {
            cv_socket.close();
            cv_is.close();
            cv_os.close();
            battery.cancel(true);
            cv_connectView.changeText("Device", false);
            cv_connectView.changeBattery("Battery Info", false);
            cv_driveView.enableInputs(false);
            cv_tiltView.enableInputs(false);
            cv_singView.enableInputs(false);

        } catch (Exception e) {
        }
    }

    //move the NXT motor
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
            buffer[8] = 0;						// turn ratio??
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

    //get input from robot
    public void getInput() {
        byte[] buffer = new byte[1024];
        int bytes = 0;
        if(cv_is != null) {
            while (bytes < 7) {
                try {
                    bytes = cv_is.read(buffer);
                } catch (IOException e) {
                    break;
                }
            }
            battery1 = buffer[5];
            battery2 = buffer[6];
        }
        //Log.d("Battery bit 1", Byte.toString(battery1));
        //Log.d("Battery bit 2", Byte.toString(battery2));
    }

    //send battery command
    public void readBattery() {
        try {
            byte[] buffer = new byte[4];

            buffer[0] = (byte) (4-2);			//length lsb
            buffer[1] = 0;						// length msb
            buffer[2] = 0;						// direct command (with response)
            buffer[3] = 0x0B;					// set output state

            cv_os.write(buffer);
            cv_os.flush();
        }
        catch (Exception e) {

        }
    }

    public void changeBattery(int batteryLvl)
    {
        cv_connectView.changeText(cv_bd.getName(), true);
        cv_connectView.changeBattery(Integer.toString(batteryLvl), true);
        cv_driveView.enableInputs(true);
        cv_tiltView.enableInputs(true);
        cv_singView.enableInputs(true);
    }

    public void playTone(int hZ)
    {
        byte hZ1 = (byte)(hZ & 0xff);
        hZ = hZ >> 8;
        byte hZ2 = (byte)(hZ & 0xff);
        int length = 100000;
        byte l1 = (byte)(length & 0xff);
        length = length >> 8;
        byte l2 = (byte)(length & 0xff);
        try {
            byte[] buffer = new byte[8];

            buffer[0] = (byte) (8-2);			//length lsb
            buffer[1] = 0;						// length msb
            buffer[2] =  (byte)0x80;			// direct command (with response)
            buffer[3] = 0x03;					// command
            buffer[4] = hZ1;	        // uword pt 1
            buffer[5] = hZ2;		// uword pt 2
            buffer[6] = l1;				// uword pt 1
            buffer[7] = l2;              // uword pt 2

            Log.d("Byte String: ", Byte.toString(buffer[4]));
            cv_os.write(buffer);
            cv_os.flush();
        }
        catch (Exception e) {
            //cv_connectStatus.setText("Error in MoveForward(" + e.getMessage() + ")");
        }
    }

    public void stopTone()
    {
        try {
            byte[] buffer = new byte[4];

            buffer[0] = (byte) (4-2);			//length lsb
            buffer[1] = 0;						// length msb
            buffer[2] =  (byte)0x80;			// direct command (with no response)
            buffer[3] = 0x0C;					// command

            cv_os.write(buffer);
            cv_os.flush();
        }
        catch (Exception e) {
            //cv_connectStatus.setText("Error in MoveForward(" + e.getMessage() + ")");
        }
    }

    public void connectNXT()
    {
        asyncConnect = new RobotConnect();
        asyncConnect.execute();
    }

    public void disconnectNXT()
    {
        asyncDisconnect = new RobotDisconnect();
        asyncDisconnect.execute();
    }
    public void singNXT(int hz)
    {
        asyncSing = new RobotSing();
        asyncSing.execute(hz);
    }

    public void muteNXT()
    {
        asyncMute = new RobotMute();
        asyncMute.execute();
    }
    public void driveNXT(int motor1, int speed1, int state1, int motor2, int speed2, int state2)
    {
        asyncDrive = new RobotDrive();
        asyncDrive.execute(motor1, speed1, state1, motor2, speed2, state2);
    }
    public void driveNXTOneMotor(int motor1, int speed1, int state1)
    {
        asyncDrive = new RobotDrive();
        asyncDrive.execute(motor1, speed1, state1);
    }
    public class RobotBattery extends AsyncTask<Object, Object, Object>
    {
        Timer timer = new Timer();
        int var = 0;
        @Override
        protected Object doInBackground(Object... objects) {
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    readBattery();
                    getInput();
                    var = ((battery1 & 0xff) | ((battery2 & 0xff)<<8));
                    publishProgress();
                }
            }, 0, 60000);
            //checks battery every 1 minute

            return null;
        }

        @Override
        protected void onProgressUpdate(Object... objects)
        {
            changeBattery(var);
        }

        @Override
        protected void onCancelled()
        {
            timer.cancel();
        }
    }

    public class RobotConnect extends AsyncTask<Object, Object, Object>
    {
        @Override
        protected Object doInBackground(Object... objects) {
            cf_connectNXT();
            return null;
        }
    }

    public class RobotDisconnect extends AsyncTask<Object, Object, Object>
    {
        @Override
        protected Object doInBackground(Object... objects) {
            cf_disconnectNXT();
            return null;
        }
    }

    public class RobotSing extends AsyncTask <Integer, Object, Object>
    {
        @Override
        protected Object doInBackground(Integer... integers) {
            if(integers[0] != null)
            {
                playTone(integers[0]);
            }
            return null;
        }
    }

    public class RobotMute extends AsyncTask <Integer, Object, Object>
    {
        @Override
        protected Object doInBackground(Integer... integers) {
            stopTone();
            return null;
        }
    }

    public class RobotDrive extends AsyncTask<Integer, Object, Object>
    {
        @Override
        protected Object doInBackground(Integer... integers) {
            if(integers.length > 3) {
                cf_moveMotor(integers[0], integers[1], integers[2]);
                cf_moveMotor(integers[3], integers[4], integers[5]);
            } else if (integers.length > 0 && integers.length <= 3)
            {
                cf_moveMotor(integers[0], integers[1], integers[2]);
            }
            return null;
        }
    }
}
