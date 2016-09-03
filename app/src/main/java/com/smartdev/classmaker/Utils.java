package com.smartdev.classmaker;

import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Utils {

    public static String folderPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ClassMaker/";

	public void setFolderPath(String path) {
		this.folderPath = path;
	}
	
	public String getFolderPath() {
		return folderPath;
	}
	
    public static void Save(File file, String[] data) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
        }
        catch (FileNotFoundException e) {e.printStackTrace();}
        try {
            try {
                for (int i = 0; i<data.length; i++) {
                    if (fos != null)
                        fos.write(data[i].getBytes());
                    if (i < data.length-1) {
                        if (fos != null)
                            fos.write("\n".getBytes());
                    }
                }
            }
            catch (IOException e) {e.printStackTrace();}
        }
        finally {
            try {
                if (fos != null)
                    fos.close();
            }
            catch (IOException e) {e.printStackTrace();}
        }
    }
}
