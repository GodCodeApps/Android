package com.tencent.qcloud.tim.demo.login;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileUtil {

    /**
     * 移动文件
     *
     * @param source 源文件
     * @param target 目标文件
     */
    public static void moveFile(File source, File target) {
        copyFile(source, target);
        delfile(source.getPath());
    }

    public static void delfile(String fromFileName) {
        if (Util.isBlank(fromFileName)) return;
        File fromFile = new File(fromFileName);
        if (!fromFile.exists()) return;
        if (fromFile.exists()) fromFile.delete();
    }

    /**
     * 文件的复制操作方法
     *
     * @param rewrite 是否重新创建文件
     */
    public static void copyfile(String fromFileName, String toFileName,
                                Boolean rewrite) {

        File fromFile = new File(fromFileName);
        File toFile = new File(toFileName);
        if (!fromFile.exists()) {
            return;
        }

        if (!fromFile.isFile()) {
            return;
        }
        if (!fromFile.canRead()) {
            return;
        }
        if (!toFile.getParentFile().exists()) {
            toFile.getParentFile().mkdirs();
        }
        if (toFile.exists() && rewrite) {
            toFile.delete();
        }

        try {
            FileInputStream fosfrom = new FileInputStream(fromFile);
            FileOutputStream fosto = new FileOutputStream(toFile);

            byte[] bt = new byte[1024];
            int c;
            while ((c = fosfrom.read(bt)) > 0) {
                fosto.write(bt, 0, c);
            }
            // 关闭输入、输出流
            fosfrom.close();
            fosto.close();

        } catch (Exception e) {

        }

    }

    /**
     * 复制整个文件夹内容
     *
     * @param oldPath String 原文件路径 如：c:/fqf
     * @param newPath String 复制后路径 如：f:/fqf/ff
     * @return boolean
     */
    public static void copyFolder(String oldPath, String newPath) {

        try {
            (new File(newPath)).mkdirs(); // 如果文件夹不存在 则建立新文件夹
            File a = new File(oldPath);
            String[] file = a.list();
            File temp = null;
            for (int i = 0; i < file.length; i++) {
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + file[i]);
                } else {
                    temp = new File(oldPath + File.separator + file[i]);
                }

                if (temp.isFile()) {
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(newPath
                            + "/" + (temp.getName()));
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                if (temp.isDirectory()) {// 如果是子文件夹
                    copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
                }
            }
        } catch (Exception e) {
            System.out.println("复制整个文件夹内容操作出错");

        }

    }

    /**
     * 拷贝文件
     *
     * @param src 源文件
     * @param dst 目标文件
     */
    public static void copyFile(File src, File dst) {
        try {
            if (dst.exists()) {
                dst.delete();
            }
            FileInputStream in = new FileInputStream(src);
            FileOutputStream out = new FileOutputStream(dst);
            int len = 0;
            byte[] buffer = new byte[2048];
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
            in.close();
            out.close();
        } catch (Exception e) {
        }
    }

    /**
     * 判定 路径列表是否全部存在
     *
     * @param paths 路径列表
     * @return true, false
     */
    public static boolean isFilesExist(String... paths) {
        if (null != paths) {
            for (String path : paths) {
                if (!TextUtils.isEmpty(path)) {
                    File file = new File(path);
                    if (!file.exists()) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * 标记目录媒体扫描器忽略
     *
     * @param paths
     * @return
     */
    public static boolean markNoMedia(String... paths) {
        for (String path : paths) {
            if (prepareDirs(path)) {
                try {
                    return new File(path + File.separator + ".nomedia").createNewFile();
                } catch (IOException e) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * 创建目录
     *
     * @param paths 目录s
     * @return 全部创建成功返回true
     */
    public static boolean prepareDirs(String... paths) {
        for (String path : paths) {
            if (!isFilesExist(path)) {
                if (TextUtils.isEmpty(path) || !new File(path).mkdirs()) {
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean isExist(File dir, String url, boolean flag) {
        if (Util.isBlank(dir) || Util.isBlank(url)) {
            return false;
        }
        File myfile = new File(dir, flag ? getFilePathMp4(url) : getFilePath(url));
        return myfile.exists();
    }


    public static String createFile(File dir, String url, boolean flag) {
        if (Util.isBlank(dir) || Util.isBlank(url)) {
            return "";
        }
        try {
            File myfile = new File(dir, flag ? getFilePathMp4(url) : getFilePath(url));
            if (myfile.exists()) {
                myfile.delete();
            }
            myfile.createNewFile();
            return myfile.getName();
        } catch (IOException e) {
            return "";
        }
    }

    public static File createFile(String dir, String name) {
        if (Util.isBlank(dir) || Util.isBlank(name)) {
            return null;
        }
        try {
            File myfile = new File(dir, name);
            myfile.createNewFile();
            return myfile;
        } catch (IOException e) {
            return null;
        }
    }

    public static String getFilePathMp4(String path) {
        if (path == null || path.length() < 1) {
            return "";
        }
        String regEx = "[`~!@#$%^&*()-+=|{}':;',//[//].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(path);
        String filePath = m.replaceAll("a").trim();
        return filePath + ".mp4";
    }

    /**
     * @param path
     * @return
     * @Describe 地址转换
     * @author wubo
     * @time 2013-9-2
     */
    public static String getFilePath(String path) {
        if (path == null || path.length() < 1) {
            return "";
        }
        String regEx = "[`~!@#$%^&*()-+=|{}':;',//[//].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(path);
        String filePath = m.replaceAll("a").trim();
        return filePath + ".wb";
    }

    public static String getFileNameFromURL(String url) {
        if (url == null) {
            return "";
        }
        try {
            URL resource = new URL(url);
            String host = resource.getHost();
            if (host.length() > 0 && url.endsWith(host)) {
                // handle ...example.com
                return "";
            }
        } catch (MalformedURLException e) {
            return "";
        }

        int startIndex = url.lastIndexOf('/') + 1;
        int length = url.length();

        // find end index for ?
        int lastQMPos = url.lastIndexOf('?');
        if (lastQMPos == -1) {
            lastQMPos = length;
        }

        // find end index for #
        int lastHashPos = url.lastIndexOf('#');
        if (lastHashPos == -1) {
            lastHashPos = length;
        }

        // calculate the end index
        int endIndex = Math.min(lastQMPos, lastHashPos);
        return url.substring(startIndex, endIndex);
    }

    public static void createFile(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * 取SD卡路径
     **/
    public static String getSDPath(Context context) {
        if (context.getFilesDir().exists())
            return context.getFilesDir().getAbsolutePath();
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory(); // 获取根目录
        }
        if (sdDir != null) {
            return sdDir.toString();
        } else {
            return "";
        }
    }

    public static String getDiskCacheDir(Context context) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }



    /**
     * 缓存目录
     */
    public static File getExternalCacheDir(Context context) {
        File cache = context.getExternalCacheDir();
        if (null != cache) {
            prepareDirs(cache.getAbsolutePath());
        } else {
            cache = Environment.getDataDirectory();
        }
        FileUtil.markNoMedia(cache.getAbsolutePath());
        return cache;
    }

    /**
     * 从assets目录中复制文件到本地
     *
     * @param context Context
     * @param oldPath String  原文件路径
     * @param newPath String  复制后路径
     */
    public static void copyFilesFassets(Context context, String oldPath, String newPath) {
        try {
            String[] fileNames = context.getAssets().list(oldPath);
            if (fileNames.length > 0) {
                File file = new File(newPath);
                file.mkdirs();
                for (String fileName : fileNames) {
                    copyFilesFassets(context, oldPath + "/" + fileName, newPath + "/" + fileName);
                }
            } else {
                InputStream is = context.getAssets().open(oldPath);
                File ff = new File(newPath);
                if (!ff.exists()) {
                    FileOutputStream fos = new FileOutputStream(ff);
                    byte[] buffer = new byte[1024];
                    int byteCount = 0;
                    while ((byteCount = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, byteCount);
                    }
                    fos.flush();
                    is.close();
                    fos.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte[] buffer = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return bytesToHexString(digest.digest());
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static boolean isFileValible(Context context, String remd5, String url) {
        String dir = Util.getCacheDir(context).getAbsolutePath();
        String name = FileUtil.getFilePath(url);
        if (!url.startsWith("http:")) return true;
        final File file = new File(dir, name);
        //本地已经有数据,不需要下载
        return FileUtil.isFileValible(remd5, file);

    }

    public static boolean isFileValible(String remd5, File file) {
        try {
            if (file != null && file.exists()) {
                String md5 = FileUtil.getFileMD5(file);
                if (Util.isBlank(remd5) || md5.equals(remd5)) {
                    return true;
                } else {
                    file.delete();
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public interface DownStatuListener {
        void progress(int progress);

        void downSucc(File file);

        void downFail();
    }
}
