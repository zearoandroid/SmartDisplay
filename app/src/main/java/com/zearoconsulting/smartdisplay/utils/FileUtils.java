package com.zearoconsulting.smartdisplay.utils;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by saravanan on 04-09-2016.
 */
public class FileUtils {

    // Find the SD Card path
    public static  final File filepath = Environment.getExternalStorageDirectory();

    // Create a new folder in SD Card
    public static  final File dir = new File(filepath.getAbsolutePath()+ "/Dhukan Images/");

    public static Bitmap bitmap = null;
    public static OutputStream output = null;

    public static String storeImage(String image, long prodId, Bitmap defaultMap){

        if(!dir.exists())
            dir.mkdirs();

        //set the image name as productid
        String imageName = prodId+".png";

        // Create a name for the saved image
        File file = new File(dir, imageName);

        if(file.exists())
            file.delete();
        else
            file = new File(dir, imageName);

        if(image.equals(""))
            bitmap = defaultMap;
        else
            bitmap = Common.decodeBase64(image);

        try {

            output = new FileOutputStream(file);

            // Compress into png format image from 0% - 100%
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
            output.flush();
            output.close();
        }

        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String getDirectoryPath = file.getAbsolutePath();
        return getDirectoryPath;
    }

    public static String readImage(long prodId){

        return null;
    }
}

