package com.zzc.androidtrain.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 字节流到字符流的转换
 * <p>
 * Created by zczhang on 16/9/29.
 */
public class InputStreamReaderTest {
    public static void main(String[] args) {
        System.out.println("请输入一个数字");
        String inputStr = null;
        InputStreamReader inputStreamReader = new InputStreamReader(System.in);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        try {
            inputStr = bufferedReader.readLine();
            System.out.println("输入数据为 : " + Integer.parseInt(inputStr));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("输入的不是数字");
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
