package cn.ljuns.logcollector;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ljuns on 2018/8/15
 * I am just a developer.
 * 缓存文件
 */
public class CacheFile {

    private static final String DEFAULT_FORMAT = "yyyyMMdd_HHmmss_SSS";

    /**
     * 创建缓存文件
     * @param context
     */
    public static File createCacheFile(Context context, boolean cleanCache) {
        // 缓存文件
        DateFormat format = new SimpleDateFormat(DEFAULT_FORMAT, Locale.getDefault());
        String fileName = format.format(new Date(System.currentTimeMillis())) + ".html";

        String path = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            path = context.getApplicationContext().getExternalCacheDir() + "/log";
        } else {
            path = context.getApplicationContext().getCacheDir() + "/log";
        }

        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // 计算缓存日志大小
        computeSize(folder);

        // 初始化
        return initCacheFile(fileName, folder, cleanCache);
    }

    /**
     * 初始化缓存文件
     *
     * @param fileName 缓存文件名
     * @param folder   文件夹
     * @param cleanCache   是否清除缓存文件
     */
    private static File initCacheFile(String fileName, File folder, boolean cleanCache) {
        // 是否删除缓存日志文件
        if (cleanCache) {
            cleanCache(folder);
        }

        File file = new File(folder, fileName);
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 删除缓存文件
     *
     * @param folder 文件夹
     */
    private static void cleanCache(File folder) {
        for (File file : folder.listFiles()) {
            file.delete();
        }
    }

    /**
     * 获取缓存大小
     *
     * @param folder log 文件夹
     */
    private static void computeSize(File folder) {
        long length = 0L;
        if (folder.exists()) {
            for (File file1 : folder.listFiles()) {
                length += file1.length();
            }
        }

        //限定大小 10M
        if (length / 1024 / 1024 >= 10) {
            cleanCache(folder);
        }
    }
}