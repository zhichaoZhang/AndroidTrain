package com.zzc.androidtrain.io;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * 管道
 * <p/>
 * Created by zczhang on 16/9/28.
 */
public class PipedStreamTest {

    //读任务
    private static class ReadTask implements Runnable {
        private PipedInputStream pipedInputStream;

        public ReadTask(PipedInputStream pipedInputStream) {
            this.pipedInputStream = pipedInputStream;
        }

        @Override
        public void run() {
            int c = 0;
            try {
                System.out.println("接受者等待接受消息");
                while ((c = pipedInputStream.read()) != -1) {
                    System.out.println("接受者读取到内容 : " + c);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        PipedOutputStream pipedOutputStream = new PipedOutputStream();

        PipedInputStream pipedInputStream = new PipedInputStream();
        try {
            pipedInputStream.connect(pipedOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Thread read = new Thread(new ReadTask(pipedInputStream));
        read.start();

        int i = 0;

        while (true) {
            try {
                Thread.sleep(2 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                System.out.println("发送者发送消息 : " + i);
                pipedOutputStream.write(i++);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(i == 10) {
                try {
                    pipedOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("发送者停止发送消息");
                break;
            }
        }
    }
}
