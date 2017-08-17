package com.fan.framework.utils;

import android.os.Environment;

import com.fan.framework.config.FFConfig;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class FFLoger {
    public static final SimpleDateFormat DATA_FORMAT_TIME = new SimpleDateFormat("MM-dd HH:mm:ss.SSS", Locale.CHINA);

    static {
        init();
    }

    private static Logger logger;

    private static void init() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return;
        }
        DATA_FORMAT_TIME.setTimeZone(TimeZone.getDefault());
        logger = Logger.getLogger(FFLoger.class.getName());
        logger.setLevel(Level.INFO);
        FileHandler fileHandler = null;
        try {
            String logPath = getLogFilePath(true);
            fileHandler = new FileHandler(logPath, true);
            fileHandler.setLevel(Level.INFO);
            fileHandler.setFormatter(new MyLogFormatter());
        } catch (Exception e) {
            e.printStackTrace();
            FFConfig.LOG2SD_ENABLE = false;
            return;
        }
        logger.addHandler(fileHandler);
    }

    private static class MyLogFormatter extends Formatter {
        @Override
        public String format(LogRecord record) {
            return DATA_FORMAT_TIME.format(new Date()) + "| " + record.getMessage() + "\n";
        }
    }

    public static void i(String message) {
        if (logger == null) {
            init();
        }
        if (logger == null) {
            return;
        }
        logger.info(message);
    }

    @Override
    protected void finalize() throws Throwable {
        if (logger != null) {
            Handler[] hs = logger.getHandlers();
            if (hs != null) {
                for (Handler h : hs) {
                    h.close();
                }
            }
        }
        super.finalize();
    }

    public static void clean() {
        String logPath = getLogFilePath(false);
        if (logPath == null || "".equals(logPath.trim())) {
            return;
        }
        File f = new File(logPath);
        if (f.exists()) {
            try {
                FileWriter wf = new FileWriter(f);
                wf.write("");
                wf.flush();
                wf.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String getLogFilePath(boolean creatIfNotExist) {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return null;
        }
        String cacheDir = Environment.getExternalStorageDirectory() + "/" + FFConfig.LOG_DIR + "/";
        File logDir = new File(cacheDir);
        if (!logDir.exists()) {
            if (creatIfNotExist) {
                logDir.mkdirs();
            } else {
                return null;
            }
        }
        File appFile = new File(cacheDir + "/" + FFConfig.LOG_FILE + ".txt");
        if (!appFile.exists()) {
            try {
                if (creatIfNotExist) {
                    appFile.createNewFile();
                    return appFile.getAbsolutePath();
                } else {
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return appFile.getAbsolutePath();
        }
    }
}
