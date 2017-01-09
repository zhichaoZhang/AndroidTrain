package com.zzc.androidtrain.algorithm;

import com.zzc.androidtrain.util.IoUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Bsdiff算法实现
 * <p>
 * 后缀排序
 * Created by zczhang on 17/1/4.
 */

public class BsDiff {
    private static final String TAG = "BsDiff";
    /**
     * Length of the diff file header.
     */
    public static final int HEADER_SIZE = 32;
    public static final int BUFFER_SIZE = 8192;

    /**
     * 文件差分
     *
     * @param oldFile   旧文件
     * @param newFile   新文件
     * @param patchFile 差分文件
     * @return 操作结果 return 0 if success, otherwise return -1.
     * @throws IOException
     */
    public static int bsDiff(File oldFile, File newFile, File patchFile) throws IOException {
        if (!oldFile.exists() || !newFile.exists() || !patchFile.exists()) {
            return -1;
        }
        int result = 0;
        InputStream oldInputStream = new BufferedInputStream(new FileInputStream(oldFile));
        InputStream newInputStream = new BufferedInputStream(new FileInputStream(newFile));
        OutputStream diffOutputStream = new BufferedOutputStream(new FileOutputStream(patchFile));
        try {
            byte[] diffBytes = bsDiff(oldInputStream, (int) oldFile.length(), newInputStream, (int) newFile.length());
            diffOutputStream.write(diffBytes);
        } catch (Exception e) {
            e.printStackTrace();
            result = -1;
        } finally {
            diffOutputStream.close();
        }
        return result;

    }

    public static byte[] bsDiff(InputStream oldInputStream, int oldSize, InputStream newInputStream, int newSize) throws IOException {
        byte[] oldBuf = new byte[oldSize];
        byte[] newBuf = new byte[newSize];
        IoUtil.readBytesFromStream(oldInputStream, oldBuf, 0, oldSize);
        oldInputStream.close();
        IoUtil.readBytesFromStream(newInputStream, newBuf, 0, newSize);
        newInputStream.close();
        return bsDiff(oldBuf, oldSize, newBuf, newSize);
    }

    private static final byte[] MAGIC_BYTES = new byte[]{0x4D, 0x69, 0x63,
            0x72, 0x6F, 0x4D, 0x73, 0x67};

    public static byte[] bsDiff(byte[] oldBuf, int oldSize, byte[] newBuf, int newSize) throws IOException {
        int[] arrayI = new int[oldSize + 1];
        qsufSort(arrayI, new int[oldSize + 1], oldBuf, oldSize);

        //diff block
        int diffBlockLen = 0;
        byte[] diffBlock = new byte[newSize];

        //extra block
        int extraBlockLen = 0;
        byte[] extraBlock = new byte[newSize];

        /**
         * 差分文件组成：
         *
         * 1. 文件头数据（32字节）
         *  0~7字节，文件魔数（MAGIC_BYTES）
         *  8~15字节，控制块压缩长度
         *  16~23字节，差异块压缩长度
         *  24~31字节，合成后文件长度
         *
         * 2. 文件数据
         * 32 控制块数据 ,假设长度为ctrlBlockLen (bzip2)
         * 32 + ctrlBlockLen 差异块数据，假设长度为diffBlockLen (bzip2)
         * 32 + ctrlBlockLen + diffBlockLen 新增块 (bzip2)
         *
         * 控制块由一个记录集合构成，每个记录12个字节
         * 一个记录有 3 x 32位整数组成，控制块没有被压缩
         *
         */
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        DataOutputStream diffOut = new DataOutputStream(byteOut);

        //先向补丁文件中写入已知的头部。控制块和差异块的长度将在后面填充
        diffOut.write(MAGIC_BYTES);
        diffOut.write(-1);//控制块长度占位
        diffOut.write(-1);//差异块长度占位
        diffOut.write(newSize);//合成后的文件长度
        diffOut.flush();

        GZIPOutputStream bzip2Out = new GZIPOutputStream(diffOut);
        DataOutputStream dataOut = new DataOutputStream(bzip2Out);

        int oldsore, scsc;
        int overlap, ss, lens;
        int i;
        int scan = 0;
        int matchLen = 0;
        int lastscan = 0;
        int lastpos = 0;
        int lastoffset = 0;

        IntByRef pos = new IntByRef();
        while (scan < newSize) {
            oldsore = 0;


            for (scsc = scan += matchLen; scan < newSize; scan++) {
                //寻找oldBuf[0...oldsize] 和 newBuf[scan...newsize]最大匹配长度，最大匹配长度的起始位置记录在pos.value中
                matchLen = search(arrayI, oldBuf, oldSize, newBuf, newSize, scan, 0, oldSize, pos);

                for (; scsc < scan + matchLen; scsc++) {
                    if ((scsc + lastoffset < oldSize) && (oldBuf[scsc + lastoffset] == newBuf[scsc])) {
                        oldsore++;
                    }
                }

                if (((matchLen == oldsore) && (oldBuf[scan + lastoffset] == newBuf[scan]))) {
                    oldsore--;
                }
            }

            if ((matchLen != oldsore) || (scan == newSize)) {
                int equalNum = 0;
                int sf = 0;
                int lenFromOld = 0;
                for (i = 0; (lastscan + i < scan) && (lastpos + i < oldSize);) {
                    if(oldBuf[lastpos + i] == newBuf[lastscan + i]) {
                        equalNum++;
                    }
                    i++;
                    if(equalNum * 2 - i > sf * 2 - lenFromOld) {
                        sf = equalNum;
                        lenFromOld = i;
                    }
                }

                int lenb = 0;
                if(scan < newSize) {
                    equalNum = 0;
                    int sb = 0;
                    for(i = 1; (scan >= lastscan + i) && (pos.value >= i); i++) {
                        if(oldBuf[pos.value - i] == newBuf[scan - i]) {
                            equalNum++;
                        }

                        if(equalNum * 2 -i > sb * 2 -lenb) {
                            sb = equalNum;
                            lenb = i;
                        }
                    }
                }

                if (lastscan + lenFromOld > scan - lenb) {
                    overlap = (lastscan + lenFromOld) - (scan - lenb);
                    equalNum = 0;
                    ss = 0;
                    lens = 0;
                    for (i = 0; i < overlap; i++) {
                        if (newBuf[lastscan + lenFromOld - overlap + i] == oldBuf[lastpos + lenFromOld - overlap + i]) {
                            equalNum++;
                        }
                        if (newBuf[scan - lenb + i] == oldBuf[pos.value - lenb + i]) {
                            equalNum--;
                        }
                        if (equalNum > ss) {
                            ss = equalNum;
                            lens = i + 1;
                        }
                    }

                    lenFromOld += lens - overlap;
                    lenb -= lens;
                }

                for(i = 0; i < lenFromOld; i++) {
                    diffBlock[diffBlockLen + i] = (byte) (newBuf[lastscan + i] - oldBuf[lastpos + i]);
                }

                for(i =0; i < (scan - lenb) - (lastscan + lenFromOld); i++) {
                    extraBlock[extraBlockLen + i] = newBuf[lastscan + lenFromOld + i];
                }

                diffBlockLen += lenFromOld;
                extraBlockLen += (scan - lenb) - (lastscan + lenFromOld);

                //写入控制块
                dataOut.writeInt(lenFromOld);
                dataOut.writeInt((scan - lenb) - (lastscan + lenFromOld));
                dataOut.writeInt((pos.value - lenb) - (lastpos + lenFromOld));

                lastscan = scan - lenb;
                lastpos = pos.value - lenb;
                lastoffset = pos.value - scan;
            }
        }

        dataOut.flush();
        bzip2Out.finish();

        //压缩后的控制块长度
        int ctrlBlockLen = diffOut.size() - HEADER_SIZE;

        //写入差异块
        bzip2Out = new GZIPOutputStream(dataOut);
        bzip2Out.write(diffBlock, 0, diffBlockLen);
        bzip2Out.finish();
        bzip2Out.flush();
        //差异块长度
        diffBlockLen = diffOut.size() - ctrlBlockLen - HEADER_SIZE;

        //写入新增块
        bzip2Out = new GZIPOutputStream(diffOut);
        bzip2Out.write(extraBlock, 0 ,extraBlockLen);
        bzip2Out.finish();
        bzip2Out.flush();

        diffOut.close();

        //完整的头信息
        ByteArrayOutputStream byteHeaderOut = new ByteArrayOutputStream(HEADER_SIZE);
        DataOutputStream headerOut = new DataOutputStream(byteHeaderOut);
        headerOut.write(MAGIC_BYTES);
        headerOut.writeLong(ctrlBlockLen);
        headerOut.writeLong(diffBlockLen);
        headerOut.writeLong(newSize);
        headerOut.close();

        //拷贝文件头信息到差分文件中
        byte[] diffBytes = byteOut.toByteArray();
        byte[] headerBytes = byteHeaderOut.toByteArray();
        System.arraycopy(headerBytes, 0, diffBytes, 0, headerBytes.length);

        return diffBytes;
    }

    /**
     * 快速后缀排序
     *
     * @param arrayI
     * @param arrayV
     * @param oldBuf
     * @param oldSize
     */
    private static void qsufSort(int[] arrayI, int[] arrayV, byte[] oldBuf, int oldSize) {
        int[] buckets = new int[256];//1111 1111 大小的数组

        //遍历旧字节数组，每个字节与0xff做与操作，算出在存放在buckets中的位置，并将buckets中此位置的值加1
        //十六进制0xff转换成二进制1111 1111
        //最终buckets中存放的是每个字节重复出现的次数
        for (int i = 0; i < oldSize; i++) {
            int oneOldByte = oldBuf[i];
            int bucketPos = oneOldByte & 0xff;
            buckets[bucketPos]++;
        }

        //将buckets[i] 设置为buckets[i] + buckets[i-1] + ... + buckets[1]
        //即buckets[i] 中存储的是（二进制）i 以及比 i 小的字节出现的次数
        for (int i = 1; i < 256; i++) {
            buckets[i] += buckets[i - 1];
        }

        //0-255中的值整体向高位位移一位 0位置不变，相当于去除255（1111 1111）字节出现的次数
        for (int i = 255; i > 0; i--) {
            buckets[i] = buckets[i - 1];
        }

        //0位置 赋值为 0， 相当于去除0000 0000 字节出现的次数
        //注 0000 0000 代表NUL 空字符
        buckets[0] = 0;

        //128~255扩展字符
        //0~127常用字符

        for (int i = 0; i < oldSize; i++) {
            int oldByte = oldBuf[i];
            int bucketPos = oldByte & 0xff;
            int valueForBucketPos = buckets[bucketPos];
            ++valueForBucketPos;
            arrayI[valueForBucketPos] = i;
        }

        arrayI[oldSize] = 0;

        for (int i = 1; i < 256; i++) {
            if (buckets[i] == buckets[i - 1] + 1) {
                arrayI[buckets[i]] = -1;
            }
        }

        arrayI[0] = -1;

        for (int h = 1; arrayI[0] != -(oldSize + 1); h += h) {
            int len = 0;
            int i = 0;
            for (i = 0; i < oldSize + 1; ) {
                if (arrayI[i] < 0) {
                    len -= arrayI[i];
                    i -= arrayI[i];
                } else {
                    if (len != 0) {
                        arrayI[i - len] = -len;
                    }
                    len = arrayV[arrayI[i]] + 1 - i;
                    split(arrayI, arrayV, i, len, h);
                    i += len;
                    len = 0;
                }
            }

            if (len != 0) {
                arrayI[i - len] = -len;
            }
        }

        for (int i = 0; i < oldSize + 1; i++) {
            arrayI[arrayV[i]] = i;
        }
    }

    private static void split(int[] arrayI, int[] arrayV, int start, int len, int h) {
        int i, j, k, x, tmp, jj, kk;
        if (len < 16) {
            for (k = start; k < start + len; i++) {
                j = 1;
                x = arrayV[arrayI[k] + h];
                for (i = 1; k + 1 < start + len; i++) {
                    if (arrayV[arrayI[k + i] + h] < x) {
                        x = arrayV[arrayI[k + i] + h];
                        j = 0;
                    }

                    if (arrayV[arrayI[k + i] + h] == x) {
                        tmp = arrayI[k + j];
                        arrayI[k + j] = arrayI[k + i];
                        arrayI[k + i] = tmp;
                        j++;
                    }
                }

                for (i = 0; i < j; i++) {
                    arrayV[arrayI[k + i]] = k + j - 1;
                }
                if (j == 1) {
                    arrayI[k] = -1;
                }
            }

            return;
        }

        x = arrayV[arrayI[start + len / 2] + h];
        jj = 0;
        kk = 0;
        for (i = start; i < start + len; i++) {
            if (arrayV[arrayI[i] + h] < x) {
                jj++;
            }
            if (arrayV[arrayI[i] + h] == x) {
                kk++;
            }
        }

        jj += start;
        kk += jj;

        i = start;
        j = 0;
        k = 0;
        while (i < jj) {
            if (arrayV[arrayI[i] + h] < x) {
                i++;
            } else if (arrayV[arrayI[i] + h] == x) {
                //交换
                tmp = arrayI[i];
                arrayI[i] = arrayI[jj + j];
                arrayI[jj + j] = tmp;
                j++;
            } else {
                //交换
                tmp = arrayI[i];
                arrayI[i] = arrayI[kk + k];
                arrayI[kk + k] = tmp;
                k++;
            }
        }

        while (jj + j < kk) {
            if (arrayV[arrayI[jj + j] + h] == x) {
                j++;
            } else {
                tmp = arrayI[jj + j];
                arrayI[jj + j] = arrayI[kk + k];
                arrayI[kk + k] = tmp;
                k++;
            }
        }

        if (jj > start) {
            split(arrayI, arrayV, start, jj - start, h);
        }

        for (i = 0; i < kk - jj; i++) {
            arrayV[arrayI[jj + i]] = kk - 1;
        }

        if (jj == kk - 1) {
            arrayI[jj] = -1;
        }

        if (start + len > kk) {
            split(arrayI, arrayV, kk, start + len - kk, h);
        }
    }

    /**
     * 分别将 oldBufd[start..oldSize] 和 oldBufd[end..oldSize] 与  newBuf[newBufOffset...newSize] 进行匹配，
     * 返回他们中的最长匹配长度，并且将最长匹配的开始位置记录到pos.value中。
     */
    private static int search(int[] arrayI, byte[] oldBuf, int oldSize, byte[] newBuf, int newSize, int newBufOffset, int start, int end, IntByRef
            pos) {

        if (end - start < 2) {
            int x = matchlen(oldBuf, oldSize, arrayI[start], newBuf, newSize, newBufOffset);
            int y = matchlen(oldBuf, oldSize, arrayI[end], newBuf, newSize, newBufOffset);

            if (x > y) {
                pos.value = arrayI[start];
                return x;
            } else {
                pos.value = arrayI[end];
                return y;
            }
        }

        // binary search
        int x = start + (end - start) / 2;
        if (memcmp(oldBuf, oldSize, arrayI[x], newBuf, newSize, newBufOffset) < 0) {
            return search(arrayI, oldBuf, oldSize, newBuf, newSize, newBufOffset, x, end, pos);  // Calls itself recursively
        } else {
            return search(arrayI, oldBuf, oldSize, newBuf, newSize, newBufOffset, start, x, pos);
        }
    }

    /**
     * Count the number of bytes that match in oldBuf[oldOffset...oldSize] and newBuf[newOffset...newSize]
     */
    private static int matchlen(byte[] oldBuf, int oldSize, int oldOffset, byte[] newBuf, int newSize, int newOffset) {

        int end = Math.min(oldSize - oldOffset, newSize - newOffset);
        for (int i = 0; i < end; i++) {
            if (oldBuf[oldOffset + i] != newBuf[newOffset + i]) {
                return i;
            }
        }
        return end;
    }

    /**
     * Compare two byte array segments to see if they are equal
     * <p>
     * return 1 if s1[s1offset...s1Size] is bigger than s2[s2offset...s2Size] otherwise return -1
     */
    private static int memcmp(byte[] s1, int s1Size, int s1offset, byte[] s2, int s2Size, int s2offset) {

        int n = s1Size - s1offset;

        if (n > (s2Size - s2offset)) {
            n = s2Size - s2offset;
        }

        for (int i = 0; i < n; i++) {

            if (s1[i + s1offset] != s2[i + s2offset]) {
                return s1[i + s1offset] < s2[i + s2offset] ? -1 : 1;
            }
        }
        return 0;
    }

    private static class IntByRef {
        private int value;
    }

    public static void main(String[] args) {
        String oldStr = "abdc";
        byte[] oldBuf = oldStr.getBytes();
        int oldSize = oldBuf.length;
        int[] arrayI = new int[oldSize + 1];
        int[] arrayV = new int[oldSize + 1];
        qsufSort(arrayI, arrayV, oldBuf, oldSize);
    }


}
