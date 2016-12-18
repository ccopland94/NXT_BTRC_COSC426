package com.group.nxt_btrc_group_cosc426;

/**
 * Created by sp3ck on 12/17/16.
 */

public class Sensor {
    private int draw_icon;
    private int sensor_type; // 0 - light, 1 - sound, 2 - touch, 3 - distance, 4 - Motor

    Sensor(int sensor_type_num) {
        this.sensor_type = sensor_type_num;
        if (sensor_type == 0) {
            draw_icon = R.drawable.nxt_light_120;
        } else if (sensor_type == 1) {
            draw_icon = R.drawable.nxt_sound_120;
        } else if (sensor_type == 2) {
            draw_icon = R.drawable.nxt_touch_120;
        } else if (sensor_type == 3) {
            draw_icon = R.drawable.nxt_distance_120;
        } else if (sensor_type == 4) {
            draw_icon = R.drawable.nxt_servo_120;
        } else {
            // INVALID
        }
    }

    public int getDraw_icon() {
        return draw_icon;
    }

    public void setDraw_icon(int draw_icon) {
        this.draw_icon = draw_icon;
    }
}
