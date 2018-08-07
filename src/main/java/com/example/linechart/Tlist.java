package com.example.linechart;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by 秦鸣 on 2018/5/7.
 * 用于储存数据对象的序列化
 */

public class Tlist<P> extends ArrayList implements Serializable{
//    序列化arraylist
    public Tlist(int initialCapacity) {
        super(initialCapacity);
    }

    public Tlist() {
    }

    public Tlist(@NonNull Collection c) {
        super(c);
    }
}
