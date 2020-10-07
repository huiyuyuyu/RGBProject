package com.sunhan.rgbproject;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class CanvasActivity extends AppCompatActivity {

    Button btn;
    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas);

        initView();
    }

    private void initView() {
        btn = findViewById(R.id.button5);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test).copy(Bitmap.Config.ARGB_8888, true);
                Canvas canvas = new Canvas(bitmap);
                String text = "666";
                Rect rect = new Rect();
                Paint paint = new Paint();
                paint.setColor(Color.RED);
                paint.setTextSize(30);
                paint.getTextBounds(text, 0, text.length(), rect);
                canvas.drawText(text, (bitmap.getWidth() - rect.width())>>1, (bitmap.getHeight() + rect.height())>>1, paint);

                paint.setAntiAlias(true);//用于防止边缘的锯齿
                paint.setColor(Color.BLUE);//设置颜色
                paint.setStyle(Paint.Style.STROKE);//设置样式为空心矩形
                paint.setStrokeWidth(2.5f);//设置空心矩形边框的宽度
                paint.setAlpha(1000);//设置透明度

                canvas.drawRect(20,20, bitmap.getWidth() >> 1, bitmap.getHeight() >> 1,paint);

                canvas.save();
                iv.setImageBitmap(bitmap);
            }
        });

        iv = findViewById(R.id.imageView2);
    }
}
