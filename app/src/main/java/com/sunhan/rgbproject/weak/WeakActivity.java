package com.sunhan.rgbproject.weak;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.sunhan.rgbproject.R;

import java.awt.font.TextAttribute;
import java.lang.ref.WeakReference;
import java.util.concurrent.CountDownLatch;

public class WeakActivity extends AppCompatActivity {

    MyHandler myHandler = new MyHandler(this);

    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weak);

        tv = findViewById(R.id.textView3);

        new Thread() {
            @Override
            public void run() {
                myHandler.sendEmptyMessage(2);
            }
        }.start();

        //CountDownLatch
    }

    static class MyHandler extends Handler {
        WeakReference<WeakActivity> weakActivity;//软引用是在内存不足的时候，才会gc
        //而弱引用相对软应用而言，对性能的要求更高
        //java中为什么要使用软引用或者弱引用呢，因为对于一些占较大内存，以及生命周期较长的对象，一般采用软引用和弱引用

        MyHandler(WeakActivity weakActivity) {
            this.weakActivity = new WeakReference<>(weakActivity);
        }
        @Override
        public void handleMessage(@NonNull Message msg) {
            // 对象实例并不持有外部对象
            weakActivity.get().tv.setText("ds");
        }
    }
}
