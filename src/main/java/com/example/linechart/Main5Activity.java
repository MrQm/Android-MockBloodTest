package com.example.linechart;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.view.LineChartView;

import static lecho.lib.hellocharts.gesture.ZoomType.HORIZONTAL;

/**
 * Created by 秦鸣 on 2018/5/7.
 * 统计报表
 */

public class Main5Activity extends Activity {
    private ArrayList<PointValue> pointMin = new ArrayList<>();
    private ArrayList<PointValue> pointMax = new ArrayList<>();
    private ArrayList<PointValue> pointSem = new ArrayList<>();
    private ArrayList<AxisValue> axisValues = new ArrayList<>();
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    LineChartData data = new LineChartData();
    private LineChartView chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏

        setContentView(R.layout.activity_main5);
        chart = findViewById(R.id.line_chart);
        Intent intent = getIntent();
        int type = intent.getIntExtra("type", 0);
        if (type == 1) {
            ArrayList<String> files = intent.getStringArrayListExtra("files");
            toDay(files);
        } else if (type == 2) {
            ArrayList<pv> tlist = (ArrayList<pv>) getIntent().getSerializableExtra("dayData");
            toWeek(tlist);
        }
    }

    public void toDay(ArrayList<String> files) {
        Toast.makeText(Main5Activity.this, "今天的血压报告", Toast.LENGTH_SHORT).show();
        initChart();
        MyUtil myUtil = new MyUtil(this);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (files.size() != 0) {
            if (files.size() <= 1) {
                Toast.makeText(Main5Activity.this, "请保证今天有两次以上的测量记录", Toast.LENGTH_SHORT).show();

            }
            float[] arr;
            int a = 1;
            for (String fileName : files) {
                Date date = null;
                try {
                    date = simpleDateFormat.parse(fileName);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long Time = date.getTime();

                arr = myUtil.oneData(myUtil.Select(fileName));
                axisValues.add(new AxisValue(a).setLabel(fileName.split(" ")[1]));
                pointMin.add(new PointValue(a, arr[0]));
                pointMax.add(new PointValue(a, arr[1]));
                pointSem.add(new PointValue(a, arr[2]));
                a++;
            }
            ArrayList lines = new ArrayList();
            Line line = new Line(pointMin).setColor(Color.BLUE);
            Line line2 = new Line(pointMax).setColor(Color.RED);
            Line line3 = new Line(pointSem).setColor(Color.GREEN);
            line.setShape(ValueShape.CIRCLE);
            line2.setShape(ValueShape.CIRCLE);
            line3.setShape(ValueShape.CIRCLE);
            line.setCubic(false);
            line.setFilled(false);
            line.setHasLabels(true);
            line.setHasLines(true);
            line2.setCubic(false);
            line2.setFilled(false);
            line2.setHasLabels(true);
            line2.setHasLines(true);
            line3.setCubic(false);
            line3.setFilled(false);
            line3.setHasLabels(true);
            line3.setHasLines(true);
            lines.add(line);
            lines.add(line2);
            lines.add(line3);
            data.setLines(lines);
            chart.setLineChartData(data);
            textView1 = findViewById(R.id.Max);
            textView2 = findViewById(R.id.Sem);
            textView3 = findViewById(R.id.Min);
            textView1.setTextColor(Color.RED);
            textView2.setTextColor(Color.GREEN);
            textView3.setTextColor(Color.BLUE);
            textView1.setText("绿色：平均血压变化");
            textView2.setText("蓝色：低压变化");
            textView3.setText("红色：高压变化");
            System.gc();

        }
    }

    public void toWeek(ArrayList<pv> pvTlist) {
        initChart();
        Toast.makeText(Main5Activity.this, "最近七天的血压报告", Toast.LENGTH_SHORT).show();
        if (pvTlist.size() <= 2) {
            Toast.makeText(Main5Activity.this, "最近七天最少要有两天测量哦", Toast.LENGTH_SHORT).show();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd");
        if (pvTlist.size() != 0) {

            for (Object pv : pvTlist) {
                Date date = null;
                pv pv1 = (pv) pv;
                axisValues.add(new AxisValue(pv1.getX()).setLabel(simpleDateFormat.format(pv1.getX())));
                pointSem.add(new PointValue(pv1.getX(), pv1.getY()));
            }
            ArrayList lines = new ArrayList();
            Line line = new Line(pointSem).setColor(Color.GREEN);
            line.setShape(ValueShape.CIRCLE);
            line.setCubic(false);
            line.setFilled(false);
            line.setHasLabels(true);
            line.setHasLines(true);
            lines.add(line);
            data.setLines(lines);
            chart.setLineChartData(data);
            System.gc();
        }
    }

    public void initChart() {
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
//        chart.setInteractive(true);
        chart.setZoomType(HORIZONTAL);
        chart.setScrollEnabled(true);
        chart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        chart.setVisibility(View.VISIBLE);
        textView2 = findViewById(R.id.Sem);
        textView2.setText("近七天平均血压变化");
        textView2.setTextColor(Color.BLACK);
    }
}
