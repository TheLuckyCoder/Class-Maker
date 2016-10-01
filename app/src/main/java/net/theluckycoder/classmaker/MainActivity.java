package net.theluckycoder.classmaker;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;

        if (!checkPermission()) requestPermission();
    }

    public void startActivity(View view) {
        int id = view.getId();
        if (id == R.id.itemClassBtn)
            startActivity(new Intent(MainActivity.this, ItemMaker.class));
        else if (id == R.id.armorItemClassBtn)
            startActivity(new Intent(MainActivity.this, ArmorItemMaker.class));
        else if (id == R.id.blockClassBtn)
            startActivity(new Intent(MainActivity.this, BlockMaker.class));
    }

    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            Toast.makeText(getApplicationContext(), R.string.external_storage_permission_required,Toast.LENGTH_LONG).show();
        else {
            AlertDialog.Builder builder =  new AlertDialog.Builder(this);
            builder.setTitle(R.string.permission_required)
                    .setMessage(R.string.external_storage_permission_required)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                        }
                    }).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length < 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), R.string.external_storage_permission_required, Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }
}
