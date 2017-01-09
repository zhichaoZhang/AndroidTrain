package com.zzc.androidtrain.util;

import java.io.IOException;
import java.io.InputStream;

/**
 * IO工具类
 * <p>
 * Created by zczhang on 17/1/4.
 */

public class IoUtil {

    /**
     * 从输入流中读取指定数据到字节数组中
     *
     * @param inputStream 源输入流
     * @param byteBuf     目的字节数组
     * @param start       读取起始位置
     * @param length      读取长度
     * @return 是否读取成功
     */
    public static boolean readBytesFromStream(InputStream inputStream, byte[] byteBuf, int start, int length) throws IOException {
        int totalBytesRead = 0;
        // TODO: 17/1/4 why use while
        while(totalBytesRead < length) {
            int byteRead = inputStream.read(byteBuf, start + totalBytesRead, length - totalBytesRead);
            if(byteRead < 0) {
                return false;
            }
            totalBytesRead += byteRead;
        }
        return true;
    }
}
