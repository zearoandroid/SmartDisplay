package com.zearoconsulting.smartdisplay.data;

/*******************************************************************************
 * Copyright (c) 2017 ZEARO CONSULTING SERVICES, QATAR.
 * All rights reserved.
 *
 * Contributors:
 *     Zearo Consulting Services - Implementation
 *
 * Created by saravanan
 *
 * Version: 1.0
 * Release Date: 12-04-2017
 *******************************************************************************/
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.annotation.SuppressLint;
import android.os.Environment;

public class AppLog {
    // local variable
    private static AppLog instance = null;

    // Constructor
    protected AppLog() {
        // Exists only to defeat instantiation.
    }

    // Creating Singleton Instance of SGFileManager
    public static AppLog getInstance() {
        if (instance == null) {
            instance = new AppLog();
        }
        return instance;
    }

    /*
     * appendLog - append the log with the msg and TAG
     *
     * @param TAG
     * @param msg
     */
    @SuppressLint("SimpleDateFormat")
    private static void appendLog(String TAG, String msg) {

        File dir = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/SmartDisplayLog/");

        if (!dir.exists()) {
            dir.mkdirs();
        }

        File logFile = new File(dir, "logger" + ".txt");

        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

            // BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile,
                    true));
            buf.append(TAG+" "+df.format(new Date())+"  " + msg );
            buf.newLine();
            buf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * write the every log with TAG and msg
     *
     * @param TAG
     * @param msg
     */
    public static void e(String TAG, String msg) {
        appendLog(TAG, msg);
    }

}
