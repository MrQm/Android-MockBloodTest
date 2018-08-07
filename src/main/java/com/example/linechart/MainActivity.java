package com.example.linechart;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
/**
 * Created by 秦鸣 on 2018/5/7.
 */

//这个是主活动
public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button1 = findViewById(R.id.button_1);
        Button button2 = findViewById(R.id.button_2);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Time2Activity.class);
                Toast.makeText(MainActivity.this,"准备开始测量，请连接硬件",Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(MainActivity.this,Main3Activity.class);
                Toast.makeText(MainActivity.this,"查看记录",Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
    }
}
