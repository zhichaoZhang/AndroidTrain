package com.zzc.androidtrain.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

/**
 * 文件读写测试
 * Created by zczhang on 16/9/29.
 */
public class ReadAndWriteFileTest {

    public void readFileAndPrint(File file) throws IOException {
        if (!file.exists()) {
            System.out.println("文件未找到!");
            return;
        }
        FileInputStream fileInputStream = new FileInputStream(file);
        FileChannel fileChannel = fileInputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

//        PrintStream printStream = System.out;


//        int readBytes = fileChannel.read(byteBuffer);
        while(fileChannel.read(byteBuffer) != -1) {

            System.out.println("缓冲区大小--->" + byteBuffer.position());

            String content = new String(byteBuffer.array(), 0, byteBuffer.position());
            System.out.println(content);
            byteBuffer.clear();
        }
    }

    public static void main(String[] args) {
        ReadAndWriteFileTest readAndWriteFileTest = new ReadAndWriteFileTest();
        try {
            File file = new File("/Users/joye/develop/android/workspace/AndroidTrain/AndroidTrain/app/src/main/java/com/zzc/androidtrain/io/maindexlist.txt");
            readAndWriteFileTest.readFileAndPrint(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
