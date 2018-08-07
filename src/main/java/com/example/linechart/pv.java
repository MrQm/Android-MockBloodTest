package com.example.linechart;

import java.io.Serializable;

/**
 * Created by 秦鸣 on 2018/5/7.
 * 数据持久化所储存的对象
 */

public class pv  implements Serializable {
    private float x;
    private float y;

    public pv(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public pv() {
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }
}
