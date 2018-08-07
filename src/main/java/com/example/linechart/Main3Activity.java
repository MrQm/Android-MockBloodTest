package com.example.linechart;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by 秦鸣 on 2018/5/7.
 * 历史记录查看
 */

public class Main3Activity extends AppCompatActivity {
    public static Activity mmm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        final String[] files = fileList();
        mmm = this;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                Main3Activity.this, android.R.layout.simple_list_item_1, files);
        final ListView listView = findViewById(R.id.list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String ss = (String) listView.getAdapter().getItem(position);
                Toast.makeText(Main3Activity.this, ss, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Main3Activity.this, Main4Activity.class);
                intent.putExtra("FileName", ss);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //四个参数的含义。1，group的id,2,item的id,3,是否排序，4，将要显示的内容
        menu.add(0, 1, 0, "清空所有记录");
        menu.add(0, 2, 0, "显示最近的测量");
        menu.add(0, 3, 0, "显示今天的统计");
        menu.add(0, 4, 0, "显示近一周的统计");


        return true;
    }
    @Override
    public void setTitle (CharSequence title){

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                for (String fileName : fileList()) {
                    deleteFile(fileName);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        Main3Activity.this, android.R.layout.simple_list_item_1, fileList());
                ((ListView) findViewById(R.id.list)).setAdapter(adapter);
                Toast.makeText(Main3Activity.this, "清空所有记录", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                String[] files = fileList();
                adapter = new ArrayAdapter<String>(
                        Main3Activity.this, android.R.layout.simple_list_item_1, revers(files));
                ((ListView) findViewById(R.id.list)).setAdapter(adapter);
                Toast.makeText(Main3Activity.this, "按时间逆序排列", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                String[] files2 = fileList();
//                yyyy-MM-dd HH:mm:ss
                String TM = "yyyy-MM-dd";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(TM);
                String currentTime = simpleDateFormat.format(System.currentTimeMillis());
                ArrayList<String> toDays = new ArrayList<>();
                if (files2.length != 0) {
                    for (String fileName : files2) {
                        if (fileName.split(" ")[0].equals(currentTime)) {
                            toDays.add(fileName);
                        }
                    }
                }
                Intent intent = new Intent(Main3Activity.this, Main5Activity.class);
                intent.putExtra("type", 1);
                intent.putExtra("files", toDays);
                startActivity(intent);
                break;
            case 4:
                String Tm = "yyyy-MM-dd";
                MyUtil myUtil = new MyUtil(this);
                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat(Tm);
                Calendar calendar = Calendar.getInstance();
                Tlist<pv> pvTlist = new Tlist<>();
                String[] files3 = fileList();
                for (int i = 1; i <= 7; i++) {
                    if (i != 1) {
                        calendar.add(Calendar.DATE, -1);
                    }
                    String day = simpleDateFormat1.format(calendar.getTime());
                    ArrayList<String> oneDay = new ArrayList<>();
                    if (files3.length != 0) {
                        for (String fileName : files3) {
                            if (fileName.contains(day)) {
                                oneDay.add(fileName);
                            }
                        }
                    }
                    pvTlist.add(new pv(calendar.getTimeInMillis(), myUtil.ToDay(oneDay)[2]));
                }
                Intent intent1 = new Intent(Main3Activity.this, Main5Activity.class);
                intent1.putExtra("type", 2);
                intent1.putExtra("dayData", pvTlist);
                startActivity(intent1);
                break;
        }
        return true;
    }

    public String[] revers(String[] files) {
        for (int s = 0, e = files.length - 1; s < e; s++, e--) {
            String tmp = files[s];
            files[s] = files[e];
            files[e] = tmp;
        }
        return files;
    }
}
