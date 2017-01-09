package com.zzc.androidtrain.algorithm;

import com.tencent.tinker.bsdiff.BSPatch;
import com.tencent.tinker.bsdiff.BSUtil;
import com.zzc.androidtrain.util.IoUtil;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.zip.GZIPInputStream;

/**
 * BsPatch算法实现
 *
 * Created by zczhang on 17/1/4.
 */

public class BsPatch {
    /**
     * 文件合并成功
     */
    public static final int RETURN_SUCCESS = 1;

    /**
     * 差分文件不存在
     */
    public static final int RETURN_DIFF_FILE_ERR = 2;

    /**
     * 原始文件不存在
     */
    public static final int RETURN_OLD_FILE_ERR = 3;

    /**
     * 合并后的文件不存在
     */
    public static final int RETURN_NEW_FILE_ERR = 4;


    /**
     * 速度慢，低内存
     * @param oldFile
     * @param newFile
     * @param diffFile
     * @param extLen
     * @return
     * @throws IOException
     */
    public static int patchLessMemory(RandomAccessFile oldFile, File newFile, File diffFile, int extLen) throws IOException {
        if (oldFile == null || oldFile.length() <= 0) {
            return RETURN_OLD_FILE_ERR;
        }
        if (newFile == null) {
            return RETURN_NEW_FILE_ERR;
        }
        if (diffFile == null || diffFile.length() <= 0) {
            return RETURN_DIFF_FILE_ERR;
        }

        byte[] diffBytes = new byte[(int) diffFile.length()];
        InputStream diffInputStream = new FileInputStream(diffFile);
        try {
            IoUtil.readBytesFromStream(diffInputStream, diffBytes, 0, diffBytes.length);
        } finally {
            diffInputStream.close();
        }
        return patchLessMemory(oldFile, (int) oldFile.length(), diffBytes, diffBytes.length, newFile, extLen);
    }

    public static int patchLessMemory(RandomAccessFile oldFile, int oldsize, byte[] diffBuf, int diffSize, File newFile, int extLen) throws IOException {

        if (oldFile == null || oldsize <= 0) {
            return RETURN_OLD_FILE_ERR;
        }
        if (newFile == null) {
            return RETURN_NEW_FILE_ERR;
        }
        if (diffBuf == null || diffSize <= 0) {
            return RETURN_DIFF_FILE_ERR;
        }

        int commentLenPos = oldsize - extLen - 2;
        if (commentLenPos <= 2) {
            return RETURN_OLD_FILE_ERR;
        }

        DataInputStream diffIn = new DataInputStream(new ByteArrayInputStream(diffBuf, 0, diffSize));

        diffIn.skip(8); // skip headerMagic at header offset 0 (length 8 bytes)
        long ctrlBlockLen = diffIn.readLong(); // ctrlBlockLen after bzip2 compression at heater offset 8 (length 8 bytes)
        long diffBlockLen = diffIn.readLong(); // diffBlockLen after bzip2 compression at header offset 16 (length 8 bytes)
        int newsize = (int) diffIn.readLong(); // size of new file at header offset 24 (length 8 bytes)

        diffIn.close();

        InputStream in = new ByteArrayInputStream(diffBuf, 0, diffSize);
        in.skip(BSUtil.HEADER_SIZE);
        DataInputStream ctrlBlockIn = new DataInputStream(new GZIPInputStream(in));

        in = new ByteArrayInputStream(diffBuf, 0, diffSize);
        in.skip(ctrlBlockLen + BSUtil.HEADER_SIZE);
        InputStream diffBlockIn = new GZIPInputStream(in);

        in = new ByteArrayInputStream(diffBuf, 0, diffSize);
        in.skip(diffBlockLen + ctrlBlockLen + BSUtil.HEADER_SIZE);
        InputStream extraBlockIn = new GZIPInputStream(in);

        OutputStream outStream = new FileOutputStream(newFile);
        try {
            int oldpos = 0;
            int newpos = 0;
            int[] ctrl = new int[3];

            // int nbytes;
            while (newpos < newsize) {

                for (int i = 0; i <= 2; i++) {
                    ctrl[i] = ctrlBlockIn.readInt();
                }

                if (newpos + ctrl[0] > newsize) {
                    outStream.close();
                    return RETURN_DIFF_FILE_ERR;
                }

                // Read ctrl[0] bytes from diffBlock stream
                byte[] buffer = new byte[ctrl[0]];
                if (!BSUtil.readFromStream(diffBlockIn, buffer, 0, ctrl[0])) {
                    outStream.close();
                    return RETURN_DIFF_FILE_ERR;
                }

                byte[] oldBuffer = new byte[ctrl[0]];
                if (oldFile.read(oldBuffer, 0, ctrl[0]) < ctrl[0]) {
                    outStream.close();
                    return RETURN_DIFF_FILE_ERR;
                }
                for (int i = 0; i < ctrl[0]; i++) {
                    if (oldpos + i == commentLenPos) {
                        oldBuffer[i] = 0;
                        oldBuffer[i + 1] = 0;
                    }

                    if ((oldpos + i >= 0) && (oldpos + i < oldsize)) {
                        buffer[i] += oldBuffer[i];
                    }
                }
                outStream.write(buffer);

//            System.out.println(""+ctrl[0]+ ", " + ctrl[1]+ ", " + ctrl[2]);

                newpos += ctrl[0];
                oldpos += ctrl[0];

                if (newpos + ctrl[1] > newsize) {
                    outStream.close();
                    return RETURN_DIFF_FILE_ERR;
                }

                buffer = new byte[ctrl[1]];
                if (!BSUtil.readFromStream(extraBlockIn, buffer, 0, ctrl[1])) {
                    outStream.close();
                    return RETURN_DIFF_FILE_ERR;
                }
                outStream.write(buffer);
                outStream.flush();

                newpos += ctrl[1];
                oldpos += ctrl[2];
                oldFile.seek(oldpos);
            }
            ctrlBlockIn.close();
            diffBlockIn.close();
            extraBlockIn.close();
        } finally {
            oldFile.close();
            outStream.close();
        }
        return RETURN_SUCCESS;
    }

    /**
     * 速度快，高内存
     * @param oldFile
     * @param newFile
     * @param diffFile
     * @param extLen
     * @return
     * @throws IOException
     */
    public static int patchFast(File oldFile, File newFile, File diffFile, int extLen) throws IOException {
        if (oldFile == null || oldFile.length() <= 0) {
            return RETURN_OLD_FILE_ERR;
        }
        if (newFile == null) {
            return RETURN_NEW_FILE_ERR;
        }
        if (diffFile == null || diffFile.length() <= 0) {
            return RETURN_DIFF_FILE_ERR;
        }

        InputStream oldInputStream = new BufferedInputStream(new FileInputStream(oldFile));
        byte[] diffBytes = new byte[(int) diffFile.length()];
        InputStream diffInputStream = new FileInputStream(diffFile);
        try {
            BSUtil.readFromStream(diffInputStream, diffBytes, 0, diffBytes.length);
        } finally {
            diffInputStream.close();
        }

        byte[] newBytes = patchFast(oldInputStream, (int) oldFile.length(), diffBytes, extLen);

        OutputStream newOutputStream = new FileOutputStream(newFile);
        try {
            newOutputStream.write(newBytes);
        } finally {
            newOutputStream.close();
        }
        return RETURN_SUCCESS;
    }

    public static int patchFast(InputStream oldInputStream, InputStream diffInputStream, File newFile) throws IOException {
        if (oldInputStream == null) {
            return RETURN_OLD_FILE_ERR;
        }
        if (newFile == null) {
            return RETURN_NEW_FILE_ERR;
        }
        if (diffInputStream == null) {
            return RETURN_DIFF_FILE_ERR;
        }

        byte[] oldBytes = BSUtil.inputStreamToByte(oldInputStream);
        byte[] diffBytes = BSUtil.inputStreamToByte(diffInputStream);

        byte[] newBytes = patchFast(oldBytes, oldBytes.length, diffBytes, diffBytes.length, 0);

        OutputStream newOutputStream = new FileOutputStream(newFile);
        try {
            newOutputStream.write(newBytes);
        } finally {
            newOutputStream.close();
        }
        return RETURN_SUCCESS;
    }

    public static byte[] patchFast(InputStream oldInputStream, InputStream diffInputStream) throws IOException {
        if (oldInputStream == null) {
            return null;
        }

        if (diffInputStream == null) {
            return null;
        }

        byte[] oldBytes = BSUtil.inputStreamToByte(oldInputStream);
        byte[] diffBytes = BSUtil.inputStreamToByte(diffInputStream);

        byte[] newBytes = patchFast(oldBytes, oldBytes.length, diffBytes, diffBytes.length, 0);
        return newBytes;
    }

    public static byte[] patchFast(InputStream oldInputStream, int oldsize, byte[] diffBytes, int extLen) throws IOException {
        // Read in old file (file to be patched) to oldBuf
        byte[] oldBuf = new byte[oldsize];
        BSUtil.readFromStream(oldInputStream, oldBuf, 0, oldsize);
        oldInputStream.close();

        return BSPatch.patchFast(oldBuf, oldsize, diffBytes, diffBytes.length, extLen);
    }

    public static byte[] patchFast(byte[] oldBuf, int oldsize, byte[] diffBuf, int diffSize, int extLen) throws IOException {
        DataInputStream diffIn = new DataInputStream(new ByteArrayInputStream(diffBuf, 0, diffSize));

        diffIn.skip(8); // skip headerMagic at header offset 0 (length 8 bytes)
        long ctrlBlockLen = diffIn.readLong(); // ctrlBlockLen after bzip2 compression at heater offset 8 (length 8 bytes)
        long diffBlockLen = diffIn.readLong(); // diffBlockLen after bzip2 compression at header offset 16 (length 8 bytes)
        int newsize = (int) diffIn.readLong(); // size of new file at header offset 24 (length 8 bytes)

        diffIn.close();

        InputStream in = new ByteArrayInputStream(diffBuf, 0, diffSize);
        in.skip(BSUtil.HEADER_SIZE);
        DataInputStream ctrlBlockIn = new DataInputStream(new GZIPInputStream(in));

        in = new ByteArrayInputStream(diffBuf, 0, diffSize);
        in.skip(ctrlBlockLen + BSUtil.HEADER_SIZE);
        InputStream diffBlockIn = new GZIPInputStream(in);

        in = new ByteArrayInputStream(diffBuf, 0, diffSize);
        in.skip(diffBlockLen + ctrlBlockLen + BSUtil.HEADER_SIZE);
        InputStream extraBlockIn = new GZIPInputStream(in);

        // byte[] newBuf = new byte[newsize + 1];
        byte[] newBuf = new byte[newsize];

        int oldpos = 0;
        int newpos = 0;
        int[] ctrl = new int[3];

        // int nbytes;
        while (newpos < newsize) {

            for (int i = 0; i <= 2; i++) {
                ctrl[i] = ctrlBlockIn.readInt();
            }

            if (newpos + ctrl[0] > newsize) {
                throw new IOException("Corrupt by wrong patch file.");
            }

            // Read ctrl[0] bytes from diffBlock stream
            if (!BSUtil.readFromStream(diffBlockIn, newBuf, newpos, ctrl[0])) {
                throw new IOException("Corrupt by wrong patch file.");
            }

            for (int i = 0; i < ctrl[0]; i++) {
                if ((oldpos + i >= 0) && (oldpos + i < oldsize)) {
                    newBuf[newpos + i] += oldBuf[oldpos + i];
                }
            }

            newpos += ctrl[0];
            oldpos += ctrl[0];

            if (newpos + ctrl[1] > newsize) {
                throw new IOException("Corrupt by wrong patch file.");
            }

            if (!BSUtil.readFromStream(extraBlockIn, newBuf, newpos, ctrl[1])) {
                throw new IOException("Corrupt by wrong patch file.");
            }

            newpos += ctrl[1];
            oldpos += ctrl[2];
        }
        ctrlBlockIn.close();
        diffBlockIn.close();
        extraBlockIn.close();

        return newBuf;
    }
}
