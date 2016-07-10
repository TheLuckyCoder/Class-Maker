package com.smartdev.classmaker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_EXTERNAL_STORAGE_RESULT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkForPermissions();
    }

    public void makeItemClass(View view) {
        startActivity(new Intent(MainActivity.this, ItemMaker.class));
    }

    public void makeArmorItemClass(View view) {
        startActivity(new Intent(MainActivity.this, ArmorItemMaker.class));
    }

    public void makeBlockClass(View view) {
        startActivity(new Intent(MainActivity.this, BlockMaker.class));
    }

    public void checkForPermissions() {
        if(android.os.Build.VERSION.SDK_INT >= 23) {
            if(ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if(shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, R.string.error_external_storage_permission, Toast.LENGTH_SHORT).show();
                }
                requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE_RESULT);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(requestCode == REQUEST_EXTERNAL_STORAGE_RESULT) {
            if(grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, R.string.error_external_storage_permission, Toast.LENGTH_LONG).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void startSettings(View view) {
        startActivity(new Intent(MainActivity.this, SettingsActivity.class));
    }
}
