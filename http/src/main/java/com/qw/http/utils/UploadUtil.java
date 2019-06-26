package com.qw.http.utils;


import com.qw.http.core.FileEntity;
import com.qw.http.core.OnProgressUpdateListener;
import com.qw.http.exception.HttpException;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * @author stay
 */
public class UploadUtil {

    /**
     * 单文件上传
     *
     * @param out      输出流
     * @param filePath 文件路径
     * @throws HttpException 异常信息
     */
    public static void uploadFile(OutputStream out, String filePath) throws HttpException {
        try {
            File file = new File(filePath);
            byte[] buffer = new byte[1024];
            FileInputStream fis = new FileInputStream(file);
            while (fis.read(buffer, 0, 1024) != -1) {
                out.write(buffer, 0, buffer.length);
            }
        } catch (IOException e) {
            throw new HttpException(HttpException.ErrorType.UPLOAD, e.getMessage());
        }
    }

    /**
     * 单文件上传
     *
     * @param out      输出流
     * @param filePath 文件路径
     * @throws HttpException 异常信息
     */
    public static void upload(OutputStream out, String filePath) throws HttpException {
        // 数据分隔线
        String BOUNDARY = "7d4a6d158c9";
        DataOutputStream outStream = new DataOutputStream(out);
        try {
            outStream.writeBytes("--" + BOUNDARY + "\r\n");
            outStream.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\""
                    + filePath.substring(filePath.lastIndexOf("/") + 1) + "\"" + "\r\n");
            outStream.writeBytes("\r\n");
            byte[] buffer = new byte[1024];
            FileInputStream fis = new FileInputStream(filePath);
            while (fis.read(buffer, 0, 1024) != -1) {
                outStream.write(buffer, 0, buffer.length);
            }
            fis.close();
            outStream.write("\r\n".getBytes());
            // 数据结束标志
            byte[] end_data = ("--" + BOUNDARY + "--\r\n").getBytes();
            outStream.write(end_data);
            outStream.flush();
        } catch (Exception e) {
            throw new HttpException(HttpException.ErrorType.UPLOAD, e.getMessage());
        }
    }

    /**
     * 多文件上传
     *
     * @param out         文件输出流
     * @param postContent 提交数据
     * @param entities    文件列表
     * @throws HttpException 异常信息
     */
    public static void upload(OutputStream out, String postContent, ArrayList<FileEntity> entities) throws HttpException {
        // 数据分隔线
        String BOUNDARY = "7d4a6d158c9";
        String PREFIX = "--", LINEND = "\r\n";
        String CHARSET = "UTF-8";
        DataOutputStream outStream = new DataOutputStream(out);
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(PREFIX);
            sb.append(BOUNDARY);
            sb.append(LINEND);
            sb.append("Content-Disposition: form-data; name=\"" + "data" + "\"" + LINEND);
            sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
            sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
            sb.append(LINEND);
            sb.append(postContent);
            sb.append(LINEND);
            outStream.write(sb.toString().getBytes());
            int i = 0;
            for (FileEntity entity : entities) {
                StringBuilder sb1 = new StringBuilder();
                sb1.append(PREFIX);
                sb1.append(BOUNDARY);
                sb1.append(LINEND);
                sb1.append("Content-Disposition: form-data; name=\"file" + (i++) + "\"; filename=\"" + entity.getFileName() + "\""
                        + LINEND);
                sb1.append("Content-Type: " + entity.getFileType() + LINEND);
                sb1.append(LINEND);
                outStream.write(sb1.toString().getBytes());

                InputStream is = new FileInputStream(entity.getFilePath());
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = is.read(buffer)) != -1) {

                    outStream.write(buffer, 0, len);
                }

                is.close();
                outStream.write(LINEND.getBytes());
            }
            byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
            outStream.write(end_data);
            outStream.flush();
        } catch (Exception e) {
            throw new HttpException(HttpException.ErrorType.UPLOAD, e.getMessage());
        }
    }

    /**
     * 多文件上传
     *
     * @param out         文件输出流
     * @param postContent 提交数据
     * @param entities    文件列表
     * @param listener    进度监听
     * @throws HttpException 异常信息
     */
    public static void upload(OutputStream out, String postContent, ArrayList<FileEntity> entities, OnProgressUpdateListener listener) throws HttpException {
        // 数据分隔线
        String BOUNDARY = "7d4a6d158c9";
        String PREFIX = "--", LINEND = "\r\n";
        String CHARSET = "UTF-8";
        DataOutputStream outStream = new DataOutputStream(out);
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(PREFIX);
            sb.append(BOUNDARY);
            sb.append(LINEND);
            sb.append("Content-Disposition: form-data; name=\"" + "data" + "\"" + LINEND);
            sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
            sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
            sb.append(LINEND);
            sb.append(postContent);
            sb.append(LINEND);
            outStream.write(sb.toString().getBytes());
            int i = 0;
            int totalLen = 0;
            int percent = 0;
            if (listener != null) {
//				 compute total file length
                File file = null;
                for (FileEntity entity : entities) {
                    file = new File(entity.getFilePath());
                    if (file.length() > 0) {
                        totalLen += file.length();
                    }
                }
            }
            for (FileEntity entity : entities) {
                StringBuilder sb1 = new StringBuilder();
                sb1.append(PREFIX);
                sb1.append(BOUNDARY);
                sb1.append(LINEND);
                sb1.append("Content-Disposition: form-data; name=\"file" + (i++) + "\"; filename=\"" + entity.getFileName() + "\""
                        + LINEND);
                sb1.append("Content-Type: " + entity.getFileType() + LINEND);
                sb1.append(LINEND);
                outStream.write(sb1.toString().getBytes());

                InputStream is = new FileInputStream(entity.getFilePath());
                byte[] buffer = new byte[1024];
                int len = 0;
                int curLen = 0;
                while ((len = is.read(buffer)) != -1) {
                    outStream.write(buffer, 0, len);
                    if (listener != null) {
                        curLen += len;
                        if (curLen * 100l / totalLen > percent) {
                            listener.onProgressUpdate(curLen, totalLen);
                            percent = (int) (curLen * 100l / totalLen);
                        }
                    }
                }

                is.close();
                outStream.write(LINEND.getBytes());
            }
            byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
            outStream.write(end_data);
            outStream.flush();
        } catch (Exception e) {
            throw new HttpException(HttpException.ErrorType.UPLOAD, e.getMessage());
        }
    }
}
