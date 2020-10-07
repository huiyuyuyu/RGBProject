package com.sunhan.rgbproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sunhan.rgbproject.util.ColorUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.WeakReference;

public class SecondActivity extends AppCompatActivity {

    public static final String TAG = "SecondActivity";

    Button button_photo;
    Button button;
    Button button_rotate;
    Button button_frame;
    ImageView iv;
    TextView tv;
    Handler handler;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    int image_src = R.drawable.test_new1;

    int x_red;
    int y_red;
    int x_black;
    int y_black;
    int x_purple;
    int y_purple;

    private Uri imageUri;
    public static final int TAKE_PHOTO = 1;

    private static class FHandler extends Handler {//匿名类和非静态内部类，都会默认持有外部引用
        private WeakReference<SecondActivity> reference;

        private FHandler(SecondActivity activity) {
            reference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            if (reference.get() != null) {
                reference.get().tv.setText((CharSequence) msg.obj);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        initView();

        handler = new FHandler(this);
    }

    private void initView() {
        button_photo = findViewById(R.id.button12);
        button_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建一个File对象，用于保存摄像头拍下的图片，这里把图片命名为output_image.jpg
                // 并将它存放在手机SD卡的应用关联缓存目录下
                File outputImage = new File(getExternalCacheDir(), "output_image.jpg");

                // 对照片的更换设置
                try {
                    // 如果上一次的照片存在，就删除
                    if (outputImage.exists()) {
                        outputImage.delete();
                    }
                    // 创建一个新的文件
                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // 如果Android版本大于等于7.0
                if (Build.VERSION.SDK_INT >= 24) {
                    // 将File对象转换成一个封装过的Uri对象
                    imageUri = FileProvider.getUriForFile(SecondActivity.this, "com.sunhan.rgbproject.fileprovider", outputImage);
                } else {
                    // 将File对象转换为Uri对象，这个Uri标识着output_image.jpg这张图片的本地真实路径
                    imageUri = Uri.fromFile(outputImage);
                }
                // 动态申请权限
                if (ContextCompat.checkSelfPermission(SecondActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) SecondActivity.this, new String[]{Manifest.permission.CAMERA}, 100);
                } else {
                    takePhoto();
                    // 启动相机程序
                    /**
                     Toast.makeText(TakePhotoActivity.this, "开始拍照", Toast.LENGTH_SHORT).show();
                     Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                     startActivity(intent);*/
                }
            }
        });
        button = findViewById(R.id.button);
        button_rotate = findViewById(R.id.button3);
        iv = findViewById(R.id.imageView4);
        iv.setImageResource(image_src);
        button.setOnClickListener(new View.OnClickListener() {//点击监测
            @Override
            public void onClick(View v) {
                //找到红色中心坐标
                new Thread() {
                    @Override
                    public void run() {
                        Message message = handler.obtainMessage();
                        message.what = 1;
                        Bitmap src = BitmapFactory.decodeResource(getResources(), image_src);
                        if (bitmap != null) {
                            src = bitmap;
                        }
                        String corner_color = String.format("%s\n%s\n%s", ColorUtil.find("black", src),
                                ColorUtil.find("purple", src), ColorUtil.find("yellow", src));
                        String middleColotArray = cal_coord(src);
                        message.obj = corner_color + "\n" + middleColotArray;
                        handler.sendMessage(message);
                    }
                }.start();
            }
        });
        button_rotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rotate();
            }
        });
        tv = findViewById(R.id.textView);

        button_frame = findViewById(R.id.button6);
        button_frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_frame();
            }
        });
    }

    private String cal_coord(Bitmap src) {
        int x1 = ColorUtil.x_black;
        int y1 = ColorUtil.y_black;
        int x2 = ColorUtil.x_purple;
        int y2 = ColorUtil.y_purple;
        int x3 = ColorUtil.x_yellow;
        int y3 = ColorUtil.y_yellow;

        int x4 = x3 + x2 - x1;
        int y4 = y3 + y2 - y1;
        int x5 = (x1 + x2) >> 1;
        int y5 = (y1 + y2) >> 1;
        int x6 = (x3 + x4) >> 1;
        int y6 = (y3 + y4) >> 1;
        int x7 = (x1 + x3) >> 1;
        int y7 = (y1 + y3) >> 1;
        int x8 = (x2 + x4) >> 1;
        int y8 = (y2 + y4) >> 1;

        int x10 = x5 + (x6 - x5) * 172 / 800;
        int y10 = y5 + (y6 - y5) * 172 / 800;
        int x11 = x7 + (x8 - x7) * 168 / 800;
        int y11 = y7 + (y8 - y7) * 168 / 800;
        int x12 = x6 + (x5 - x6) * 140 / 800;
        int y12 = y6 + (y5 - y6) * 140 / 800;
        int x13 = x8 + (x7 - x8) * 168 / 800;
        int y13 = y8 + (y7 - y8) * 168 / 800;
        //获取坐标对应的rgb值
        StringBuilder sb = new StringBuilder();
        int pixelColor = src.getPixel(x10, y10);
        getColor(sb, pixelColor);
        sb.append("x10=" + x10 + ",y10=" + y10 + "\n");

        pixelColor = src.getPixel(x11, y11);
        getColor(sb, pixelColor);
        sb.append("x11=" + x11 + ",y11=" + y11 + "\n");

        pixelColor = src.getPixel(x12, y12);
        getColor(sb, pixelColor);
        sb.append("x12=" + x12 + ",y12=" + y12 + "\n");

        pixelColor = src.getPixel(x13, y13);
        getColor(sb, pixelColor);
        sb.append("x13=" + x13 + ",y13=" + y13 + "\n");
        return sb.toString();
    }

    private void getColor(StringBuilder sb, int pixelColor) {
        if (Color.green(pixelColor) > 100) {
            sb.append("绿色：");
        } else {
            sb.append("红色：");
        }
    }

    private void add_frame1() {
        Canvas canvas = new Canvas();
        Paint paint = new Paint();
        paint.setAntiAlias(true);//用于防止边缘的锯齿
        paint.setColor(Color.BLUE);//设置颜色
        paint.setStyle(Paint.Style.STROKE);//设置样式为空心矩形
        paint.setStrokeWidth(6.5f);//设置空心矩形边框的宽度
        paint.setAlpha(1000);//设置透明度

        canvas.drawRect(20,20, 300, 300, paint);
        canvas.save();
    }

    private void takePhoto() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        // 指定图片的输出地址为imageUri
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    private void add_frame3() {
        try {
            iv.setImageBitmap(getBitmap());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 参考链接：https://www.jianshu.com/p/9a3ff10e076a
     * @return 截图
     */
    private Bitmap getBitmap() {

        View screenView = getWindow().getDecorView();
        screenView.setDrawingCacheEnabled(true);
        screenView.buildDrawingCache();

        //获取屏幕整张图片
        Bitmap bitmap = screenView.getDrawingCache();

        if (bitmap != null) {

            //获取需要截图部分的在屏幕上的坐标(view的左上角坐标）
            int[] viewLocationArray = new int[2];

            //从屏幕整张图片中截取指定区域
            bitmap = Bitmap.createBitmap(bitmap, 200, 500, 800, 1100);
            Log.i(TAG, "执行了一次");
        }

        return bitmap;
    }

    private void add_frame() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), image_src).copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(bitmap);

        //String text = "666";
        //Rect rect = new Rect();
        Paint paint = new Paint();
        /**
        paint.setColor(Color.RED);
        paint.setTextSize(30);
        paint.getTextBounds(text, 0, text.length(), rect);
        canvas.drawText(text, (bitmap.getWidth() - rect.width())>>1, (bitmap.getHeight() + rect.height())>>1, paint);*/

        paint.setAntiAlias(true);//用于防止边缘的锯齿
        paint.setColor(Color.BLUE);//设置颜色
        paint.setStyle(Paint.Style.STROKE);//设置样式为空心矩形
        paint.setStrokeWidth(6.5f);//设置空心矩形边框的宽度
        paint.setAlpha(1000);//设置透明度

        //canvas.drawRect(20,20, bitmap.getWidth() >> 1, bitmap.getHeight() >> 1,paint);


        if (x_red == 0) {
            Toast.makeText(this, "先进行检测，方能加边框", Toast.LENGTH_SHORT).show();
            return;
        }
        canvas.drawLine(x_red, y_red, x_purple, y_purple, paint);
        canvas.drawLine(x_red, y_red, x_black, y_black, paint);
        canvas.drawLine(x_purple - (x_red - x_black), y_purple - (y_red - y_black), x_black, y_black, paint);
        canvas.drawLine(x_purple - (x_red - x_black), y_purple - (y_red - y_black), x_purple, y_purple, paint);

        canvas.save();
        iv.setImageBitmap(bitmap);
    }

    private String find(String color, Bitmap src) {

        int pixelColor;
        Log.i(TAG, "图片的宽：" + src.getWidth() + "图片的高：" + src.getHeight());

        //确定x的坐标
        int x_start = 0, x_end = 0;
        boolean flag;
        for (int x = 0; x < src.getWidth(); x+=10) {
            flag = false;
            for (int y = 0; y < src.getHeight(); y+=10) {
                pixelColor = src.getPixel(x, y);
                if (rgbRange(color, pixelColor) && Color.green(pixelColor) < 100) {//出现红色
                    if (x_start == 0) {
                        x_start = x;
                    }
                    flag = true;
                    break;
                }
            }
            if (x_start != 0 && !flag) {//如果确定起点和终点
                x_end = x;
                break;
            }
        }

        //确定y的坐标
        int y_start = 0, y_end = 0;
        for (int y = 0; y < src.getHeight(); y+=10) {
            flag = false;
            for (int x = 0; x < src.getWidth(); x+=10) {
                pixelColor = src.getPixel(x, y);
                if (rgbRange(color, pixelColor) && Color.green(pixelColor) < 100) {
                    if (y_start == 0) {
                        y_start = y;
                    }
                    flag = true;
                    break;
                }
            }
            if (y_start != 0 && !flag) {//如果确定起点和终点
                y_end = y;
                break;
            }
        }
        switch (color) {
            case "red":
                x_red = (x_start + x_end) / 2;
                y_red = (y_start + y_end) / 2;
                break;
            case "black":
                x_black = (x_start + x_end) / 2;
                y_black = (y_start + y_end) / 2;
                break;
            case "purple":
                x_purple = (x_start + x_end) / 2;
                y_purple = (y_start + y_end) / 2;
                break;
        }
        String result = color + "方框的坐标x=" + (x_start + x_end) / 2+
                ",y=" + (y_start + y_end) / 2;
        Log.i(TAG, result);
        return result;
    }

    private boolean rgbRange(String color, int pixelColor) {
        switch (color) {
            case "red":
                return Color.red(pixelColor) > 200;
            case "black":
                return Color.red(pixelColor) < 50;
            case "purple":
                return Color.blue(pixelColor) > 100;
            default:return true;
        }
    }

    private void rotate() {//让图片旋转
        iv.setPivotX(iv.getWidth() >> 1);
        iv.setPivotY(iv.getHeight() >> 1);
        tv.setText("宽=" + iv.getWidth() + "，高=" + iv.getHeight());
        //计算角度
        if (x_red == 0) {
            Toast.makeText(this, "先进行检测，方能进行旋转", Toast.LENGTH_SHORT).show();
            return;
        }
        int derX = x_red - x_purple;//630
        int derY = y_red - y_purple;//-920
        Log.i(TAG, "derX=" + derX);
        Log.i(TAG, "derY=" + derY);
        double degree;
        degree = 270 - Math.atan2(derY, derX) / (Math.PI / 180);
        /**
        if (derX >= 0) {
            degree = -90 - Math.atan2(derY, derX) / (Math.PI / 180);//逆时针旋转
            Log.i(TAG, "逆时针旋转的度数=" + (-degree));
        } else {
            degree = 270 - Math.atan2(derY, derX) / (Math.PI / 180);//顺时针旋转
            Log.i(TAG, "顺时针旋转的度数=" + degree);
        }*/
        Log.i(TAG, "测试度数=" + Math.atan2(1, 1.7) / (Math.PI / 180));
        iv.setRotation((float) degree);
    }

    Bitmap bitmap;

    // 使用startActivityForResult()方法开启Intent的回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        // 将图片解析成Bitmap对象
                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        // 将图片显示出来
                        iv.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
        }
    }
}