package com.group.nxt_btrc_group_cosc426;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
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
        public View getView(int position, View view, ViewGroup viewGroup) {
            if (view == null) {
                LayoutInflater mInflater = (LayoutInflater) context.getSystemService(((MainActivity)getActivity()).LAYOUT_INFLATER_SERVICE);
                view = mInflater.inflate(R.layout.row_polling_list, null);
            }
            TextView item_num = (TextView) view.findViewById(R.id.tv_num);
            item_num.setText(Integer.toString(position+1));
            ImageView icon = (ImageView) view.findViewById(R.id.iv_sensor_icon);
            icon.setImageResource(array.get(position).getDraw_icon());
            if (position < 4) {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // INTENT TO SELECT SENSOR
                    }
                });
            }
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

}
