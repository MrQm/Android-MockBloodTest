package com.example.linechart;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;

import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

import static lecho.lib.hellocharts.gesture.ZoomType.*;

/**
 * Created by 秦鸣 on 2018/5/7.
 * 这个是测量Activity
 */


public class Time2Activity extends AppCompatActivity implements SensorEventListener {

    private LineChartView lineChartView;
    private TextView lightIntensityTExtView;
    private ArrayList<PointValue> pointValues = new ArrayList<>();
    private List<AxisValue> axisValues = new ArrayList<>();
    private List<Line> lines = new ArrayList<>();
    LineChartData data = new LineChartData();
    private SensorManager sensorManager;
    private Sensor lightSensor;
    private Tlist<pv> tlist = new Tlist<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //这个东西要一开始就赋值，要不报空指针
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lineChartView = findViewById(R.id.line_chart);


        Button button1 = findViewById(R.id.start);
        button1.setBackgroundColor(Color.GREEN);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start();
            }
        });

        Button button2 = findViewById(R.id.stop);
        button2.setBackgroundColor(Color.RED);
        button2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
//                ...停止初始化还怎么存东西
//                initLineChart();
                stop();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //四个参数的含义。1，group的id,2,item的id,3,是否排序，4，将要显示的内容
        menu.add(0, 1, 0, "查看分析结果");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        MyUtil myUtil = new MyUtil(this);
        float[] resalut = myUtil.oneData(tlist);
        Toast.makeText(Time2Activity.this, "最低值：" + resalut[0] + ",  " + "最高值：" + resalut[1] + ",  " + "平均：" + resalut[2], Toast.LENGTH_LONG).show();
        return true;
    }


    protected void start() {


        lightIntensityTExtView = findViewById(R.id.light_intensity_text);

        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorManager.registerListener(this, lightSensor, sensorManager.SENSOR_DELAY_NORMAL);
        initLineChart();
    }

    private void stop() {
        saveDate();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        lightIntensityTExtView.setText("血压：" + event.values[0]);
        drawNewPoint(event.values[0]);
//        Log.d("point size:",Integer.toString(pointValues.size()));
//        Log.d("axis size:",Integer.toString(axisValues.size()));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private void initLineChart() {
        //坐标轴

        Axis axisX = new Axis();
        //axisX.setHasTiltedLabels(true);
        axisX.setTextColor(Color.BLACK);
        axisX.setMaxLabelChars(5);
        axisX.setValues(axisValues);
        axisX.setMaxLabelChars(5);
        axisX.setHasLines(true);
        data.setAxisXBottom(axisX);
        //清除之前的数据，彻底初始化。
        axisValues.clear();
        pointValues.clear();

        Axis axisY = new Axis();
        axisY.setHasLines(true);
        axisY.setName("Y");
        data.setAxisYLeft(axisY);

        //设置行为属性
        lineChartView.setInteractive(true);
        lineChartView.setZoomType(HORIZONTAL_AND_VERTICAL);
        lineChartView.setScrollEnabled(true);
        lineChartView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChartView.setVisibility(View.VISIBLE);

    }

    private void drawNewPoint(float value) {

//        Log.d("pointValues=========", String.valueOf(pointValues.size()));

        AxisValue av = new AxisValue(axisValues.size() + 1).setLabel(Integer.toString(axisValues.size() + 1));
        axisValues.add(av);
        PointValue pointValue = new PointValue(pointValues.size() + 1, (int) value);
        pointValues.add(pointValue);

        Line line = new Line(pointValues).setColor(Color.BLUE);
        line.setShape(ValueShape.CIRCLE);
        line.setCubic(true);
        line.setFilled(false);
        line.setHasLabels(true);
        line.setHasLines(true);
//        由于超出显示范围的线段太多导致显示卡顿，将超出范围的线段移除；
        lines.add(line);
        if (lines.size() >= 20) {
            lines.remove(0);
        }
        data.setLines(lines);
        lineChartView.setLineChartData(data);
        lineChartView.setZoomType(VERTICAL);
        int x = axisValues.size();
//        Log.d("x-------->",Integer.toString(x));
        if (x > 20) {
            lineChartView.setCurrentViewport(refreshViewport(x - 20, x, lineChartView.getMaximumViewport().height(), 0));
        }
//        不用的对象设为空值，调用垃圾回收器；
        av = null;
        pointValue = null;
        System.gc();
    }

    private Viewport refreshViewport(float left, float right, float top, float bottom) {
        Viewport viewport = new Viewport();
        viewport.left = left;
        viewport.right = right;
        viewport.top = top;
        viewport.bottom = bottom;
        return viewport;
    }

    //    数据储存方案
    private void saveDate() {
        String T = "yyyy-MM-dd HH:mm:ss";


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(T);

        String fileName = simpleDateFormat.format(System.currentTimeMillis());
//        Log.d("Time=====", fileName);
        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
//            Log.d("pointValues=========", String.valueOf(pointValues.size()));
            if (pointValues.size() != 0) {
                fileOutputStream = openFileOutput(fileName, Context.MODE_APPEND);
                objectOutputStream = new ObjectOutputStream(fileOutputStream);
                for (PointValue pointValue : pointValues) {
                    pv pv = new pv(pointValue.getX(), pointValue.getY());
                    tlist.add(pv);
//                    Log.d("pvpvpv:",pv.getX()+"====="+pv.getY());
                }
//                Log.d("Tlist:", "======"+String.valueOf(tlist.size()));

                objectOutputStream.writeObject(tlist);
                Toast.makeText(Time2Activity.this, "储存成功", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (objectOutputStream != null) {
                try {
                    objectOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

