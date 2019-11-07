package com.tencent.qcloud.tim.demo.login;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressLint("NewApi")
public class Util {
    private final static String sroot = "/test";
    private final static String projectDir = sroot;
    private final static String cacheDir = projectDir + "/Cache/";// 图片目录
    private final static String downloadDir = cacheDir + "/DownLoad/";// 保存下载地址
    private final static String downMusicDir = downloadDir + "/Music/";// 保存音乐下载地址
    private final static String downVedioDir = downloadDir + "/Video/";// 保存下载视频地址
    private final static String backDir = projectDir + "/Back/";// 备份目录;
    private final static String caogaoDir = backDir + "/caogao/";// 备份目录;
    private final static String transferDir = backDir + "/transfer/";// 备份目录;
    public static String head = "test_";

    public static File getProjectDir(Context context) {
        // 存放目录
        File file = new File(FileUtil.getSDPath(context), projectDir);
        // 判断文件目录是否存在
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static File getCacheDir(Context context) {
        // 存放目录
        File file = new File(FileUtil.getSDPath(context), cacheDir);
        // 判断文件目录是否存在
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static File getDownloadDir(Context context) {
        // 存放目录
        File file = new File(FileUtil.getSDPath(context), downloadDir);
        // 判断文件目录是否存在
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static int[] text2Bitmap(String text) {
        try {
            int width = 640;
            int height = 64;
            Bitmap bitmap = Bitmap
                    .createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStrokeWidth(3);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTextSize(21f);
            paint.setColor(Color.WHITE);
            Paint.FontMetrics fontMetrics = paint.getFontMetrics();
            int baseline = (int) ((height - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top);
            canvas.drawText(text, width / 2, baseline, paint);
            canvas.save();
            canvas.restore();
            int[] pixels = new int[width * height];
            bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

            // String path = Environment.getExternalStorageDirectory()
            // + "/image.png";
            // File file = new File(path);
            // if (file.exists()) {
            // file.delete();
            // }
            // file.createNewFile();
            // FileOutputStream os = new FileOutputStream(file);
            // bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
            // os.flush();
            // os.reNet();
            return pixels;
        } catch (Exception e) {
            // TODO Auto-generated catch block
        }
        return null;
    }

    public static Bitmap view2Bitmap(View v) {
        Bitmap b = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);
        return b;
    }

    /**
     * 图片存文件
     * Author @WuBo
     * Create at 2016/9/27 17:03
     */
    public static boolean bitmap2File(Bitmap bitmap, String path) {
        if (bitmap == null) return false;
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
            int bitmapWidth = bitmap.getWidth();
            int bitmapHeight = bitmap.getHeight();
            // 缩放图片的尺寸
            Matrix matrix = new Matrix();
            matrix.postScale(0.5f, 0.5f);
            // 产生缩放后的Bitmap对象
            Bitmap resizeBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth, bitmapHeight, matrix, false);
            // save file
            FileOutputStream os = new FileOutputStream(file);
            resizeBitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
            if (!bitmap.isRecycled()) {
                bitmap.recycle();//记得释放资源，否则会内存溢出
            }
            if (!resizeBitmap.isRecycled()) {
                resizeBitmap.recycle();
            }
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 图片存文件
     * Author @WuBo
     * Create at 2016/9/27 17:03
     */
    public static String bitmap2File(Context context, File dir, Bitmap bitmap) {
        if (bitmap == null) return "";
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String name = Util.head + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".jpg";
        File file = new File(dir, name);
        return bitmap2File(context, bitmap, file.getAbsolutePath());
    }

    /**
     * 图片存文件
     * Author @WuBo
     * Create at 2016/9/27 17:03
     */
    public static String bitmap2File(Context context, Bitmap bitmap, String savePath) {
        if (bitmap == null) return "";
        File file = new File(savePath);
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
            FileOutputStream os = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
            final Intent i = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file));
            context.sendBroadcast(i);
            return file.getAbsolutePath();
        } catch (Exception e) {
        }
        return "";
    }

    /**
     * @param context
     * @param dpValue
     * @return
     * @Describe dip2px
     * @author wubo
     * @time 2013-6-18
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int getCurrTime() {
        int time = (int) System.currentTimeMillis();
        return time;
    }


    /**
     * 获取自身类名
     *
     * @author wubo
     * @createtime 2012-7-9
     */
    public static String getClassName() {
        return new Throwable().getStackTrace()[1].getClassName();
    }


    /**
     * 验证手机号
     *
     * @param str
     * @return
     * @author wubo
     * @createtime 2012-9-13
     */
    public static boolean isCellphone(String str) {
        Pattern pattern = Pattern.compile("1[0-9]{10}");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    /**
     * 为空或者无值
     * Author @WuBo
     * Create at 2016/5/17 18:05
     */
    public static boolean isBlank(String x) {
        if (x == null) {
            return true;
        } else return x.trim().equals("");
    }

    /**
     * @param val
     * @return
     */
    public static int charWidth(String val) {
        int len = 0;
        String chinese = "[\u0391-\uFFE5]";
        for (int i = 0; i < val.length(); i++) {
            String temp = val.substring(i, i + 1);
            if (temp.matches(chinese)) {
                len += 2;
            } else {
                len += 1;
            }
        }
        return len;
    }

    /**
     * 不为空并且有值
     * Author @WuBo
     * Create at 2016/5/17 18:09
     */
    public static boolean isNotBlank(String x) {
        return !isBlank(x);
    }

    /**
     * 为空或者无值
     * Author @WuBo
     * Create at 2016/5/17 18:05
     */
    public static boolean isBlank(Object x) {
        return x == null;
    }

    /**
     * 不为空并且有值
     * Author @WuBo
     * Create at 2016/5/17 18:09
     */
    public static boolean isNotBlank(Object x) {
        return !isBlank(x);
    }


    /**
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return int 返回类型
     * @throws
     * @author wubo
     * @Title: DistanceOfTwoPoints
     * @Description: 根据两点间经纬度坐标（double值），计算两点间距离，单位为米
     */
    public static long distanceOfTwoPoints(double lat1, double lng1,
                                           double lat2, double lng2) {

        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);

        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * 6378.137f;
        return Integer.parseInt(Math.round(s * 1000) + "");
    }

    /**
     * @param d
     * @return double 返回类型
     * @throws
     * @author wubo
     * @Title: rad
     * @Description: 求某个经纬度的值的角度值
     */
    private static double rad(double d) {
        return d * Math.PI / 180;
    }

    public static String monthConvert(long date) {
        String monthstr = "";
        SimpleDateFormat sdf = new SimpleDateFormat("MM");
        String dates = sdf.format(date);
        switch (Integer.parseInt(dates)) {
            case 1:
                monthstr = "一";
                break;
            case 2:
                monthstr = "二";
                break;
            case 3:
                monthstr = "三";
                break;
            case 4:
                monthstr = "四";
                break;
            case 5:
                monthstr = "五";
                break;
            case 6:
                monthstr = "六";
                break;
            case 7:
                monthstr = "七";
                break;
            case 8:
                monthstr = "八";
                break;
            case 9:
                monthstr = "九";
                break;
            case 10:
                monthstr = "十";
                break;
            case 11:
                monthstr = "十一";
                break;
            case 12:
                monthstr = "十二";
                break;
            default:
                monthstr = "一";
                break;
        }
        monthstr += "月";
        return monthstr;
    }

    public static String dayConvert(long date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        return sdf.format(date);
    }

    /**
     * @return
     * @Describe 文件复制
     * @author wubo
     * @time 2013-9-16
     */
    public static String fileCopy(String oldFilePath, String newFilePath) {
        File file = new File(oldFilePath);
        if (file == null || !file.exists()) {
            return "";
        }

        File newFile = new File(newFilePath);
        if (newFile.exists()) {
            newFile.delete();
        }
        try {
            newFile.createNewFile();
        } catch (IOException e1) {
            return "";
        }
        FileInputStream input = null;
        FileOutputStream output = null;
        try {
            input = new FileInputStream(file);
            output = new FileOutputStream(newFile);
            byte[] buffer = new byte[4096];
            int n = 0;
            while (-1 != (n = input.read(buffer))) {
                output.write(buffer, 0, n);
            }
            return newFile.getPath();
        } catch (Exception ioe) {
            return "";
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                }
            }
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                }
            }
        }
    }


    /**
     * @param src
     * @return
     * @throws IOException
     * @Describe 复制list
     * @author wubo
     * @time 2013-12-20
     */
    public static <T> List<T> deepCopy(List<T> src) throws IOException,
            ClassNotFoundException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(src);

        ByteArrayInputStream byteIn = new ByteArrayInputStream(
                byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        @SuppressWarnings("unchecked")
        List<T> dest = (List<T>) in.readObject();
        return dest;
    }

    /**
     * @param date
     * @return
     * @Describe 比较时间是否大于等于今天
     * @author wubo
     * @time 2014-1-16
     */
    public static boolean datevs(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
            Date strDate = sdf.parse(date);
            Date todayDate = new Date();
            return strDate.getTime() <= todayDate.getTime();
        } catch (ParseException e) {
        }
        return false;
    }

    /**
     * 获取string资源
     * Author @WuBo
     * Create at 2016/5/17 18:13
     */
    public static String getString(Context context, int id) {
        return context.getResources().getString(id);
    }

    public static String getArrayString(Context context, int id, int index) {
        return context.getResources().getStringArray(id)[index];
    }


    /**
     * 当前是否为wifi状态
     * Author @WuBo
     * Create at 2016/5/17 18:14
     */
    public static boolean isWifi(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }


    /**
     * 获取屏幕宽
     * Author @WuBo
     * Create at 2016/5/17 18:16
     */
    public static int[] getDisplayMetrics(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        // 获取屏幕信息
        context.getWindowManager().getDefaultDisplay()
                .getMetrics(dm);
        int screenWidth = dm.widthPixels;
        int screenHeigh = dm.heightPixels;
        return new int[]{screenWidth, screenHeigh};
    }

    /**
     * 获取屏幕宽
     * Author @WuBo
     * Create at 2016/5/17 18:16
     */
    public static int[] getDisplayMetrics(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int screenHeigh = dm.heightPixels;
        return new int[]{screenWidth, screenHeigh};
    }


    /**
     * 隐藏虚拟机
     * Author @WuBo
     * Create at 2016/5/17 18:19
     */
    public static void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
        }
    }

    public static BitmapDrawable getBitmapFormVideo(Context context,
                                                    String path, long time, int progress) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(path);
        Bitmap bm = retriever.getFrameAtTime(time);
        if (bm == null)
            return null;
        Bitmap newBitmap1;
        if (progress > 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(progress, bm.getWidth() / 2, bm.getHeight() / 2);
            newBitmap1 = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
                    bm.getHeight(), matrix, true);
        } else {
            newBitmap1 = bm;
        }
        return new BitmapDrawable(context.getResources(), newBitmap1);
    }

    /**
     * 检查网络
     * Author @WuBo
     * Create at 2016/5/20 18:39
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        return info != null && info.isAvailable() && info.isConnected();
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri,
                                       String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri
                .getAuthority());
    }

    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }


    /**
     * 是否在主线程
     * Author @WuBo
     * Create at 2016/7/6 16:18
     */
    public static boolean inMainProcess(Context context) {
        String packageName = context.getPackageName();
        String processName = getProcessName(context);
        return packageName.equals(processName);
    }


    /**
     * 获取当前进程名
     *
     * @param context
     * @return 进程名
     */
    public static final String getProcessName(Context context) {
        String processName = null;

        // ActivityManager
        ActivityManager am = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE));

        while (true) {
            for (ActivityManager.RunningAppProcessInfo info : am.getRunningAppProcesses()) {
                if (info.pid == android.os.Process.myPid()) {
                    processName = info.processName;
                    break;
                }
            }

            // go home
            if (!TextUtils.isEmpty(processName)) {
                return processName;
            }

            // take a rest and again
            try {
                Thread.sleep(100L);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }


    public static int getColor(Context context, int id) {
        return context.getResources().getColor(id);
    }

    public static View getLayoutById(Context mContext, ViewGroup group, int find_topview) {
        return LayoutInflater.from(mContext).inflate(find_topview, group, false);
    }


    public static String getChannel(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            ApplicationInfo info = manager.getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            if (info != null) {
                String meta = info.metaData.getString("UMENG_CHANNEL");
                if (meta != null && meta.length() > 0) {
                    return meta;
                }
            }
        } catch (Exception e) {
        }
        return "xiudan";
    }

    public static int getVerCode(Context context) {
        PackageInfo packageInfo = getPackageInfo(context);
        return packageInfo != null ? packageInfo.versionCode : 100;
    }

    public static String getVerName(Context context) {
        PackageInfo packageInfo = getPackageInfo(context);
        return packageInfo != null ? packageInfo.versionName : "1.0";
    }

    public static String getIMEI(Context context) {
        return "";

    }

    public static String getDeviceId(Context context) {
        String deviceid = uuid(context);
        if (deviceid.length() > 0) {
            return deviceid;
        }
        return Build.SERIAL;
    }

    private synchronized static String uuid(Context context) {
        File file = new File(context.getFilesDir(), "INSTALLATION");
        try {
            if (!file.exists()) {
                String deviceid = Settings.Secure.getString(context.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
                if (deviceid != null && deviceid.length() > 0) {
                    return deviceid;
                }
                writeId2File(file);
            }
            return readId(file);
        } catch (IOException e) {
        }
        return "";
    }

    private static String readId(File file) throws IOException {
        RandomAccessFile f = new RandomAccessFile(file, "r");
        byte[] bytes = new byte[(int) f.length()];
        f.readFully(bytes);
        f.close();
        return new String(bytes);
    }

    private static void writeId2File(File file) throws IOException {
        FileOutputStream out = new FileOutputStream(file);
        String id = UUID.randomUUID().toString();
        out.write(id.getBytes());
        out.close();
    }

    private static PackageInfo getPackageInfo(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            return manager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
        }
        return null;
    }

    // ---------------------------------------------------------------------------------------//

    // ---------------------------------------------------------------------------------------//

    public static String getCurrTimeName() {
        return getTimeName(System.currentTimeMillis());
    }

    @SuppressLint("SimpleDateFormat")
    public static String getTimeName(long time) {
        return new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")
                .format(new Date(time));
    }

    public static String construct_covername(int uid, long time) {
        return uid + "_" + time + ".jpeg";
    }


    public static String construct_videoname(int uid, long time) {
        return "_" + uid + randomName() + time + ".mp4";
    }

    public static String construct_videoname_temp(String filename) {
        int index = filename.lastIndexOf(".");
        String filepath1 = filename.substring(0, index) + "1"
                + filename.substring(index);
        return filepath1;
    }

    public static String randomName() {
        Random random = new Random();
        String alp = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        char[] buf = new char[6];
        for (int k = 0; k < buf.length; k++) {
            buf[k] = alp.charAt(random.nextInt(alp.length()));
        }
        return new String(buf);
    }

    public static void clearCache(Context ctx) {
        deleteDir(ctx.getCacheDir());
        deleteDir(FileUtil.getExternalCacheDir(ctx));
        //   clear(getRecord(ctx));
    }

    //    private static File getRecord(Context ctx) {
//        File cache = Util.getBackDir();
//        return new File(cache, ShotEnv.srecord);
//    }
    public static void clear(File dir) {
        if (!dir.exists()) {
            return;
        }
        File[] fs = dir.listFiles();
        if (fs != null && fs.length > 0) {
            for (File f : fs) {
                if (f.isFile())
                    f.delete();
                else
                    clear(f);
            }
        }
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     *
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     * If a deletion fails, the method stops attempting to
     * delete and returns "false".
     */
    private static boolean deleteDir(File dir) {
        if (!dir.exists()) {
            return false;
        }
        if (dir.isDirectory()) {
            File[] children = dir.listFiles();
            //递归删除目录中的子目录下
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(children[i]);
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    public static String filesize(File file) {
        StringBuffer sb = new StringBuffer();
        convert2(sb, file.length());
        return sb.toString();
    }

    public static String getCacheSize(Context context) {
        long size = calculate4(context.getCacheDir());
        long extSize = calculate4(FileUtil.getExternalCacheDir(context));
        StringBuffer sb = new StringBuffer();
        convert2(sb, size + extSize);
        return sb.toString();
    }

    private static long calculate4(File dir) {
        File[] fs = dir.listFiles();

        if (fs == null || fs.length == 0)
            return 0;
        //
        long size = 0;
        for (File f : fs) {
            if (f.isFile())
                size += f.length();
            else
                size += calculate4(f);
        }
        return size;
    }

    private static void convert2(StringBuffer sb, long size) {
        int K = 1024;
        int million = 2 << 19;
        if (size > million) {
            long m = size / million;
            sb.append(m).append('.');
            size -= m * million;
            size /= K;
            // 只显示2位小数
            while (size > 99) {
                size /= 10;
            }
            if (size < 10) {
                sb.append('0');
            }
            sb.append(size).append("M");
        } else if (size > 1024) {
            size /= 1024;
            sb.append(size).append("K");
        } else if (size > 0) {
            sb.append(size).append("B");
        } else {
            sb.append("0B");
        }
    }


    public static String getAbsolutePath(File dir, String url, boolean flag) {
        if (Util.isBlank(dir) || Util.isBlank(url)) {
            return "";
        }
        File myfile = new File(dir, flag ? FileUtil.getFilePathMp4(url) : FileUtil.getFilePath(url));
        return myfile.getAbsolutePath();
    }

    public static void cleanOldCache(final Context context) {
        new Thread() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                File dir = new File(FileUtil.getSDPath(context),
                        Util.downloadDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                    return;
                }
                File[] files = dir.listFiles();
                if (files == null) {
                    return;
                }
                for (int i = 0; i < files.length; i++) {
                    File file = files[i];
                    long sumDate = file.lastModified() + 1000 * 60 * 60 * 24
                            * 5;
                    if (sumDate < System.currentTimeMillis()) {
                        files[i].delete();
                    }
                }
            }
        }.start();
    }


    public static String getFileName(String videoPath) {
        File file = new File(videoPath);
        if (file.exists()) {
            return file.getName();
        } else {
            return "";
        }
    }

    /**
     * 创建消息id
     *
     * @return
     * @Describe
     * @author wubo
     * @time 2014-5-8
     */
    public static int createMsgId() {
        return (int) (new Date().getTime() % 10000000);
    }

    public static boolean isHttp(String str) {
        if (isBlank(str)) {
            return false;
        }
        String rule = "([hH][tT][tT][pP][sS]{0,1})://[a-zA-Z0-9\\.\\-]+\\.([a-zA-Z]{2,4})[^\u4e00-\u9fa5\\s]*";
        Pattern p = Pattern.compile(rule);
        Matcher m = p.matcher(str);
        return m.find();
    }

    public static String getHttp(String str) {
        if (isBlank(str)) {
            return "";
        }
        String rule = "([hH][tT][tT][pP][sS]{0,1})://[a-zA-Z0-9\\.\\-]+\\.([a-zA-Z]{2,4})[^\u4e00-\u9fa5\\s]*";
        Pattern p = Pattern.compile(rule);
        Matcher m = p.matcher(str);
        if (m.find()) {
            return m.group();
        }
        return "";
    }

    public static void CopyAssets(Context context, String assetDir, String dir) {
        String[] files;
        try {
            files = context.getResources().getAssets().list(assetDir);
        } catch (IOException e1) {
            return;
        }
        File mWorkingPath = new File(dir);
        // if this directory does not exists, make one.
        if (!mWorkingPath.mkdirs()) {

        }
        for (int i = 0; i < files.length; i++) {
            try {
                String fileName = files[i];
                // we make sure file name not contains '.' to be a folder.
                if (!fileName.contains(".")) {
                    if (0 == assetDir.length()) {
                        CopyAssets(context, fileName, dir + fileName + "/");
                    } else {
                        CopyAssets(context, assetDir + "/" + fileName, dir + "/" + fileName + "/");
                    }
                    continue;
                }
                File outFile = new File(mWorkingPath, fileName);
                if (outFile.exists())
                    outFile.delete();
                InputStream in = null;
                if (0 != assetDir.length())
                    in = context.getAssets().open(assetDir + "/" + fileName);
                else
                    in = context.getAssets().open(fileName);
                OutputStream out = new FileOutputStream(outFile);

                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }

                in.close();
                out.close();
            } catch (FileNotFoundException e) {
            } catch (IOException e) {
            }
        }
    }

    /**
     * 打开系统设置
     *
     * @param activity
     */
    public static void openSetting(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_SETTINGS);
        activity.startActivity(intent);
    }


    public static String base64encode(String msg) {
        return Base64.encodeToString(msg.getBytes(Charset.forName("UTF-8")), Base64.NO_WRAP | Base64.URL_SAFE);
    }

    public static boolean isViewCovered(final View view) {
        View currentView = view;

        Rect currentViewRect = new Rect();
        boolean partVisible = currentView.getGlobalVisibleRect(currentViewRect);
        boolean totalHeightVisible = (currentViewRect.bottom - currentViewRect.top) >= view.getMeasuredHeight();
        boolean totalWidthVisible = (currentViewRect.right - currentViewRect.left) >= view.getMeasuredWidth();
        boolean totalViewVisible = partVisible && totalHeightVisible && totalWidthVisible;
        // if any part of the view is clipped by any of its parents,return true
        if (!totalViewVisible)
            return true;

        while (currentView.getParent() instanceof ViewGroup) {
            ViewGroup currentParent = (ViewGroup) currentView.getParent();
            // if the parent of view is not visible,return true
            if (currentParent.getVisibility() != View.VISIBLE)
                return true;

            int start = indexOfViewInParent(currentView, currentParent);
            for (int i = start + 1; i < currentParent.getChildCount(); i++) {
                Rect viewRect = new Rect();
                view.getGlobalVisibleRect(viewRect);
                View otherView = currentParent.getChildAt(i);
                Rect otherViewRect = new Rect();
                otherView.getGlobalVisibleRect(otherViewRect);
                // if view intersects its older brother(covered),return true
                if (Rect.intersects(viewRect, otherViewRect))
                    return true;
            }
            currentView = currentParent;
        }
        return false;
    }

    public static void showInputMethod(EditText et) {
        if (!et.hasFocus()) et.setSelection(et.getText().length());
        et.requestFocus();
        et.setCursorVisible(true);
        InputMethodManager imm = (InputMethodManager) et.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(et, 0);
    }

    public static void hideInputMethod(EditText et) {
        InputMethodManager imm = (InputMethodManager) et.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
        et.setCursorVisible(false);
        et.clearFocus();
    }

    private static int indexOfViewInParent(View view, ViewGroup parent) {
        int index;
        for (index = 0; index < parent.getChildCount(); index++) {
            if (parent.getChildAt(index) == view)
                break;
        }
        return index;
    }


    public static double[] getRgb(int color) {
        return new double[]{
                (color & 0xff0000) >> 16,
                (color & 0x00ff00) >> 8,
                (color & 0x0000ff)
        };
    }

    /**
     * 对rgb色彩加入透明度
     *
     * @param alpha     透明度，取值范围 0.0f -- 1.0f.
     * @param baseColor
     * @return a color with alpha made from base color
     */
    public static int getColorWithAlpha(float alpha, int baseColor) {
        int a = Math.min(255, Math.max(0, (int) (alpha * 255))) << 24;
        int rgb = 0x00ffffff & baseColor;
        return a + rgb;
    }


}
