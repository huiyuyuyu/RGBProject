package com.sunhan.rgbproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.activity.CaptureActivity;
//import com.king.zxing.CaptureActivity;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    TextView tv;
    Button btn;
    Button btn_jump;
    Button btn_jump2;
    Button btn_scanner;

    TextView et_x;
    TextView et_y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        tv = findViewById(R.id.textView);
        btn = findViewById(R.id.button);
        btn_jump = findViewById(R.id.button2);
        btn_jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SecondActivity.class));
            }
        });

        btn_jump2 = findViewById(R.id.button4);
        btn_jump2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CanvasActivity.class));
            }
        });

        btn_scanner = findViewById(R.id.button7);
        btn_scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ScannerActivity.class));
            }
        });

        /**
        et_x = findViewById(R.id.et_x);
        et_y = findViewById(R.id.et_y);*/

        btn.setOnClickListener(new View.OnClickListener() {//匿名内部类
            @Override
            public void onClick(View v) {
                //textOutput();
                /**
                if (et_x.getText().toString().equals("") || et_y.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this,"请重新输入", Toast.LENGTH_SHORT).show();
                } else {
                    int x = Integer.parseInt(et_x.getText().toString());
                    int y = Integer.parseInt(et_y.getText().toString());
                    getPixColor(x, y);
                }*/
                getAllPixColor();
            }
        });
    }

    private void textOutput() {
        int[][] arr = new int[4][4];
        int num = 1;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                arr[i][j] = num;
                num++;
            }
        }

        tv.setText(Arrays.deepToString(arr));
    }

    private void getPixColor(int x, int y) {//读取每个像素的RGB
        Bitmap src = BitmapFactory.decodeResource(getResources(), R.drawable.simple);
        int A, R, G, B;
        int pixelColor;
        Log.i(TAG, "图片的宽：" + src.getWidth() + "图片的高：" + src.getHeight());

        pixelColor = src.getPixel(x, y);
        Log.i(TAG, String.valueOf(pixelColor));
        //A = Color.alpha(pixelColor);
        R = Color.red(pixelColor);
        G = Color.green(pixelColor);
        B = Color.blue(pixelColor);

        tv.setText(String.format("R:%d\nG:%d\nB:%d\n", R, G, B));
    }

    private void getAllPixColor() {
        Bitmap src = BitmapFactory.decodeResource(getResources(), R.drawable.simple);
        int height = src.getWidth();
        int width = src.getHeight();
        int R, G, B;
        int pixelColor;
        StringBuilder result = new StringBuilder();

        int[] arr = new int[]{45, 145, 250, 350};
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                pixelColor = src.getPixel(arr[j] * width / 400, arr[i] * height / 400);
                R = Color.red(pixelColor);
                B = Color.blue(pixelColor);
                if (R < 170) {
                    if (B > 100) {
                        result.append("绿色");
                    } else {
                        result.append("红色");
                    }
                    result.append(",");
                }
            }
            result.append("\n");
        }

        tv.setText(result.toString());
    }
}
