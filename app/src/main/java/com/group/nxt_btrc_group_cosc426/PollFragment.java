package com.group.nxt_btrc_group_cosc426;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class PollFragment extends Fragment {

    private ListView mSensorList;
    private MyArrayAdapter mAdapter;
    private int[] imgStringArray = {R.drawable.nxt_distance_120,R.drawable.nxt_light_120,R.drawable.nxt_servo_120,
            R.drawable.nxt_sound_120,R.drawable.nxt_touch_120};
    private ArrayList<Sensor> mSensors;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_poll, container, false);

        mSensorList = (ListView) view.findViewById(R.id.vv_sensor_list);
        Button mBTNreset = (Button) view.findViewById(R.id.btn_resetSensors);

        mSensors = new ArrayList<>();
        mSensors.add(new Sensor(0));
        mSensors.add(new Sensor(1));
        mSensors.add(new Sensor(2));
        mSensors.add(new Sensor(3));
        mSensors.add(new Sensor(4));
        mSensors.add(new Sensor(4));
        mSensors.add(new Sensor(4));

        mAdapter = new MyArrayAdapter(getActivity(),mSensors);
        mSensorList.setAdapter(mAdapter);

        mBTNreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSensors.clear();
                mSensors.add(new Sensor(0));
                mSensors.add(new Sensor(1));
                mSensors.add(new Sensor(2));
                mSensors.add(new Sensor(3));
                mSensors.add(new Sensor(4));
                mSensors.add(new Sensor(4));
                mSensors.add(new Sensor(4));
                mAdapter.notifyDataSetChanged();
            }
        });

        Log.d("onCreateView","List created and adapter set");
        return view;
    }

    private class MyArrayAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<Sensor> array;

        public MyArrayAdapter(Context context, ArrayList<Sensor> passedArray) {
            this.context = context;
            this.array = passedArray;
        }

        @Override
        public View getView(final int position, View view, ViewGroup viewGroup) {
            if (view == null) {
                LayoutInflater mInflater = (LayoutInflater) context.getSystemService(((MainActivity)getActivity()).LAYOUT_INFLATER_SERVICE);
                view = mInflater.inflate(R.layout.row_polling_list, null);
            }
            TextView item_num = (TextView) view.findViewById(R.id.tv_num);
            if (position == 4) {
                item_num.setText("A");
            } else if (position == 5) {
                item_num.setText("B");
            } else if (position == 6) {
                item_num.setText("C");
            } else {
                item_num.setText(Integer.toString(position+1));
            }
            ImageView icon = (ImageView) view.findViewById(R.id.iv_sensor_icon);
            icon.setImageResource(array.get(position).getDraw_icon());
            icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (position < 4) {
                        buildDialog(position).show();
                    }
                }
            });
            return view;
        }

        @Override
        public int getCount() {
            return array.size();
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public Object getItem(int i) {
            return array.get(i);
        }
    }

    //popup dialog
    private Dialog buildDialog(final int clicked) {

        final int[] sensorImgs = {R.drawable.nxt_light_120, R.drawable.nxt_sound_120, R.drawable.nxt_touch_120, R.drawable.nxt_distance_120};
        final String[] sensorTitles = {"Light Sensor", "Sound Sensor", "Touch Sensor", "Distance Sensor"};
        class tmpObj {
            private int image;
            private String title;
            tmpObj(int img,String title) {
                this.image = img;
                this.title = title;
            }
        }
        ArrayList<tmpObj> tmpObjArray = new ArrayList<tmpObj>();
        for(int i=0;i<4;i++) {
            tmpObjArray.add(new tmpObj(sensorImgs[i],sensorTitles[i]));
        }
        final ArrayAdapter<tmpObj> mPopupAdapter;
        mPopupAdapter = new ArrayAdapter<tmpObj>(getActivity(),R.layout.row_builddialog_sensor, tmpObjArray) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {

                    LayoutInflater mInflater = (LayoutInflater) getActivity()
                            .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                    convertView = mInflater.inflate(R.layout.row_builddialog_sensor, null);
                }

                final ImageView img = (ImageView) convertView.findViewById(R.id.iv_builddialog_icon);
                final TextView title = (TextView) convertView.findViewById(R.id.tv_builddialog_text);
                img.setImageResource(sensorImgs[position]);
                title.setText(sensorTitles[position]);
                return convertView;
            }

            @Override
            public int getCount() {
                return 4;
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Select NXT Sensor Type")
                .setAdapter(mPopupAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Sensor sensor = (Sensor) mAdapter.getItem(clicked);
                        sensor.setDraw_icon(sensorImgs[i]);
                        mAdapter.notifyDataSetChanged();
                    }
                });
        return builder.create();
    }
}
