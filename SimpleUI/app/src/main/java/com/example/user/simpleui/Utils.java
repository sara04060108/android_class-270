package com.example.user.simpleui;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by user on 2016/7/18.
 */
public class Utils {

    public  static  void  writeFile(Context context,String fileName,String content){
        //要寫file要寫fileoutputstream執行路徑
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(fileName,Context.MODE_APPEND);

            fileOutputStream.write(content.getBytes());//檔案資了以byte儲存
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static  String readFile(Context context,String fileName){
        //讀哪個檔案
        //讀進來再轉成string
        try {
            FileInputStream fileInputStream = context.openFileInput(fileName);
            byte[] buffer = new byte[2048];
            fileInputStream.read(buffer, 0, buffer.length);
            fileInputStream.close();
            return  new String(buffer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";//發生錯誤回傳空字串
    }
}
