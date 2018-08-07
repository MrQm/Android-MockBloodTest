package com.example.linechart;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.view.LineChartView;

import static com.example.linechart.Main3Activity.mmm;
import static lecho.lib.hellocharts.gesture.ZoomType.HORIZONTAL;

/**
 * Created by 秦鸣 on 2018/5/7.
 */

public class Main4Activity extends AppCompatActivity {
    private static String FileName;
    private Tlist<pv> pvValues = new Tlist<>();
    private List<AxisValue> axisValues = new ArrayList<>();
    private List<Line> lines = new ArrayList<>();
    LineChartData data = new LineChartData();
    private List<PointValue> pointValues = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        setContentView(R.layout.activity_main4);
        LineChartView chart = findViewById(R.id.line_chart);
        Intent intent = getIntent();
        FileName = intent.getStringExtra("FileName");
        ObjectInputStream objectInputStream = null;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = openFileInput(FileName);
            objectInputStream = new ObjectInputStream(fileInputStream);
            pvValues = (Tlist<pv>) objectInputStream.readObject();
            Log.d("pointValues==========", String.valueOf(pvValues.size()));
            for (Object object : pvValues) {
                pv pv = (pv) object;
                pointValues.add(new PointValue(pv.getX(), pv.getY()));
                Log.d("x----y", pv.getX() + "   " + pv.getY());
            }
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
        for (int i = 0; i <= pvValues.size(); i++) {
            AxisValue axisValue = new AxisValue(i).setLabel(String.valueOf(i));
            axisValues.add(axisValue);
        }
        Axis axisX = new Axis();
        //axisX.setHasTiltedLabels(true);
        axisX.setTextColor(Color.BLACK);
        axisX.setMaxLabelChars(5);
        axisX.setValues(axisValues);
        axisX.setMaxLabelChars(5);
        axisX.setHasLines(true);
        data.setAxisXBottom(axisX);
        Axis axisY = new Axis();
        axisY.setName("Y");
        axisY.setHasLines(true);//显示y轴网格
        data.setAxisYLeft(axisY);
        //设置行为属性
        chart.setInteractive(true);
        chart.setZoomType(HORIZONTAL);
        chart.setScrollEnabled(true);
        chart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        chart.setVisibility(View.VISIBLE);

        Line line = new Line(pointValues).setColor(Color.BLUE);
        line.setShape(ValueShape.CIRCLE);
        line.setCubic(true);
        line.setFilled(false);
        line.setHasLabels(true);
        line.setHasLines(true);
        lines.add(line);
        data.setLines(lines);
        chart.setLineChartData(data);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //四个参数的含义。1，group的id,2,item的id,3,是否排序，4，将要显示的内容
        menu.add(0, 1, 0, "清楚该记录");
        menu.add(0, 2, 0, "查看统计");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 1) {
            deleteFile(FileName);
            finish();
            mmm.finish();
            Intent intent = new Intent(Main4Activity.this, Main3Activity.class);
            startActivity(intent);
        }
        if (item.getItemId() == 2) {
            MyUtil myUtil = new MyUtil(this);
            float[] resalut = myUtil.oneData(pvValues);
            Toast.makeText(Main4Activity.this, "最低值：" + resalut[0] + ",  " + "最高值：" + resalut[1] + ",  " + "平均：" + resalut[2], Toast.LENGTH_LONG).show();
        }
        return true;
    }
}
