package com.example.linechart;


import android.content.ContextWrapper;

import android.util.Log;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by 秦鸣 on 2018/5/9.
 * 这是数据分析工具类。
 */

public class MyUtil {
//    成员变量
    private ContextWrapper contextWrapper;
//    构造
    public MyUtil(ContextWrapper contextWrapper){
        this.contextWrapper =contextWrapper;
    }


    //    这是一次测量数据的分析工具
    public float[] oneData(Tlist<pv> tlist) {
        float[] arr = {0, 0, 0};//分别为最小值，最大值，平均值
        ArrayList<Float> arrayList = new ArrayList<>();
        float sum = 0;
        if (tlist.size()!=0){
            for (Object pv : tlist) {
                pv pv1 = (pv) pv;
                arrayList.add(pv1.getY());
                sum += pv1.getY();
            }
            Collections.sort(arrayList);
            arr[0] = arrayList.get(0);
            arr[1] = arrayList.get(arrayList.size() - 1);
            arr[2] = sum / arrayList.size();
        }
        return arr;
    }

    //    这是IO工具
    public Tlist<pv> Select(String fileName) {
        Log.d("meizhaodao","++++++++++++++++++++++"+fileName);
        Tlist<pv> tlist = new Tlist<>();
        FileInputStream fileInputStream = null;
        ObjectInputStream objectInputStream = null;

        try {
            fileInputStream = contextWrapper.openFileInput(fileName);
            objectInputStream = new ObjectInputStream(fileInputStream);
            tlist = (Tlist<pv>) objectInputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (objectInputStream != null) {
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return tlist;
    }

    //    这是今天一天测量记录的分析工具
    public float[] ToDay(ArrayList<String> files) {
        float[] arr = {0, 0, 0};//该天中平均血压最小的一次测量的平均值，最大的，和这几次测量的平均值
        ArrayList<Float> arrayList = new ArrayList<>();
        float sum = 0;
        if (files.size()!=0){
            for (String fileNmae : files) {
                arrayList.add(this.oneData(this.Select(fileNmae))[2]);
                sum += this.oneData(this.Select(fileNmae))[2];
            }
            Collections.sort(arrayList);
            arr[0] = arrayList.get(0);
            arr[1] = arrayList.get(arrayList.size() - 1);
            arr[2] = sum / arrayList.size();
        }
        return arr;
    }
}

