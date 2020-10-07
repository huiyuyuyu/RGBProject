package com.sunhan.rgbproject.util;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

/**
 * Created by 孙汉 on 2020-07-26/12/45
 */
public class ColorUtil {
    private static final String TAG = "ColorUtil";

    public static int x_black;
    public static int y_black;
    public static int x_purple;
    public static int y_purple;
    public static int x_yellow;
    public static int y_yellow;

    public static String find(String color, Bitmap src) {
        int pixelColor;
        Log.i(TAG, "图片的宽：" + src.getWidth() + "图片的高：" + src.getHeight());

        //确定x的坐标
        int x_start = 0, x_end = 0;
        boolean flag;
        for (int x = 0; x < src.getWidth(); x+=10) {
            flag = false;
            for (int y = 0; y < src.getHeight(); y+=10) {
                pixelColor = src.getPixel(x, y);
                if (rgbRange(color, pixelColor)) {//出现黑色
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
                if (rgbRange(color, pixelColor)) {
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
            case "black":
                x_black = (x_start + x_end) / 2;
                y_black = (y_start + y_end) / 2;
                break;
            case "purple":
                x_purple = (x_start + x_end) / 2;
                y_purple = (y_start + y_end) / 2;
                break;
            case "yellow":
                x_yellow = (x_start + x_end) / 2;
                y_yellow = (y_start + y_end) / 2;
                break;
        }
        String result = color + "方框的坐标x=" + (x_start + x_end) / 2+
                ",y=" + (y_start + y_end) / 2;
        Log.i(TAG, result);
        return result;
    }

    private static boolean rgbRange(String color, int pixelColor) {
        switch (color) {
            case "black":
                return Color.red(pixelColor) < 40 && Color.green(pixelColor) < 40 && Color.blue(pixelColor) < 40;
            case "purple":
                return Color.red(pixelColor) > 150 && Color.green(pixelColor) < 100 && Color.blue(pixelColor) > 100;
            case "yellow":
                return Color.red(pixelColor) > 200 && Color.green(pixelColor) > 100 && Color.blue(pixelColor) < 100;
            default:return true;
        }
    }
}
