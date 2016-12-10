package net.theluckycoder.classmaker;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

class Util {

    static final int PERMISSION_REQUEST_CODE = 1;
    static final String folderPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ClassMaker/";

    static void save(File file, String[] data) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        finally {
            try {
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static boolean checkPermission(Context context){
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    static void requestPermission(final Activity activity){
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            Toast.makeText(activity, R.string.external_storage_permission_required,Toast.LENGTH_LONG).show();
        else {
            AlertDialog.Builder builder =  new AlertDialog.Builder(activity);
            builder.setTitle(R.string.permission_required)
                    .setMessage(R.string.external_storage_permission_required)
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                        }
                    }).show();
        }
    }
}
