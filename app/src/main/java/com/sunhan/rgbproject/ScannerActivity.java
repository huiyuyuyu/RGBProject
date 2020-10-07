package com.sunhan.rgbproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.activity.CaptureActivity;

/**
 * 参考链接：https://codeload.github.com/open-android/Zxing
 */
public class ScannerActivity extends AppCompatActivity {

    private final static int REQ_CODE = 1028;
    Button btn_scan;
    Button btn_takephoto;
    ImageView mImageCallback;
    TextView mTvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        initView();
    }

    private void initView() {
        btn_scan = findViewById(R.id.button9);
        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScannerActivity.this, CaptureActivity.class);
                startActivityForResult(intent, REQ_CODE);
            }
        });

        btn_takephoto = findViewById(R.id.button10);
        btn_takephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScannerActivity.this, TakePhotoActivity.class);
                startActivity(intent);
            }
        });

        mImageCallback = findViewById(R.id.imageView3);
        mTvResult = findViewById(R.id.textView2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE) {
            //mImage.setVisibility(View.GONE);
            //mTvResult.setVisibility(View.VISIBLE);
            mImageCallback.setVisibility(View.VISIBLE);

            if (data == null) {
                return;
            }

            String result = data.getStringExtra(CaptureActivity.SCAN_QRCODE_RESULT);
            Bitmap bitmap = data.getParcelableExtra(CaptureActivity.SCAN_QRCODE_BITMAP);

            mTvResult.setText("扫码结果："+result);
            showToast("扫码结果：" + result);
            if(bitmap != null){
                mImageCallback.setImageBitmap(bitmap);//现实扫码图片
            }
        }
    }

    private void showToast(String msg) {
        Toast.makeText(ScannerActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
    }
}
