package com.sunhan.rgbproject;

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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 参考链接1：https://blog.csdn.net/weixin_43344890/article/details/95319188
 * 参考链接2:https://www.jianshu.com/p/6c51202c845d
 */
public class TakePhotoActivity extends AppCompatActivity {

    Button btn_photo;
    Button btn_check;
    private Uri imageUri;
    public static final int TAKE_PHOTO = 1;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);

        initView();
    }

    private void initView() {
        imageView = findViewById(R.id.imageView6);
        btn_photo = findViewById(R.id.button8);
        btn_photo.setOnClickListener(new View.OnClickListener() {
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
                    imageUri = FileProvider.getUriForFile(TakePhotoActivity.this, "com.sunhan.rgbproject.fileprovider", outputImage);
                } else {
                    // 将File对象转换为Uri对象，这个Uri标识着output_image.jpg这张图片的本地真实路径
                    imageUri = Uri.fromFile(outputImage);
                }
                // 动态申请权限
                if (ContextCompat.checkSelfPermission(TakePhotoActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) TakePhotoActivity.this, new String[]{Manifest.permission.CAMERA}, 100);
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

        btn_check = findViewById(R.id.button11);
        btn_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();//获取图片之后才能进行检测
            }
        });
    }

    private void check() {//参考SecondActivity的功能
        //1.对图片进行扫描，获取三个角点的坐标


        //2.根据三个角点的坐标，对中间区域进行检测，判断是4、8、16中的哪个通道


        //3.分类，输出结果
    }

    private void takePhoto() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        // 指定图片的输出地址为imageUri
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    // 使用startActivityForResult()方法开启Intent的回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        // 将图片解析成Bitmap对象
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        // 将图片显示出来
                        imageView.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
        }
    }
}
