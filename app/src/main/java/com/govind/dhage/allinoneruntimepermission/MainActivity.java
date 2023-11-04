package com.govind.dhage.allinoneruntimepermission;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_SMS;

import android.content.Intent;
import android.net.wifi.EasyConnectStatusCallback;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.govind.dhage.allinoneruntimepermission.databinding.ActivityMainBinding;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks, EasyPermissions.RationaleCallbacks {

    //private static final String LOCATION_AND_CONTACTS = ;
    public ActivityMainBinding mainBinding;
    //private static final String[] LOCATION_AND_CONTACTS =
    //     new String[]{Manifest.ACCESS_FINE_LOCATION, permission.READ_CONTACTS};

    public static final String[] LOCATIN_AND_CONTACTS = new String[]{ACCESS_FINE_LOCATION, READ_CONTACTS};

    private static final int RC_CAMERA_PERM = 123;
    private static final int RC_LOCATION_CONTACTS_PERM = 124;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = mainBinding.getRoot();
        setContentView(view);

        mainBinding.btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraTask();
            }
        });
        mainBinding.btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationAndContactsTask();
            }
        });

    }
    @AfterPermissionGranted(RC_LOCATION_CONTACTS_PERM)
    private void locationAndContactsTask() {

            if (hasLocationAndContactsPermissions()) {
                // Have permissions, do the thing!
                Toast.makeText(this, "TODO: Location and Contacts things", Toast.LENGTH_LONG).show();
            } else {
                // Ask for both permissions
                EasyPermissions.requestPermissions(
                        this,
                        getString(R.string.rationale_location_contacts),
                        RC_LOCATION_CONTACTS_PERM,
                        LOCATIN_AND_CONTACTS);
            }
        }


    public void cameraTask() {
        if (hasCameraPermission()) {
            // Have permission, do the thing!
            //Toast.makeText(this, "TODO: Camera things", Toast.LENGTH_LONG).show();
            Toast.makeText(this, "Permission Already Granted", Toast.LENGTH_LONG).show();
            Intent intent = new Intent();
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivity(intent);
        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.rationale_camera),
                    RC_CAMERA_PERM,
                    CAMERA);
        }
    }


    private boolean hasCameraPermission() {
        return EasyPermissions.hasPermissions(this, CAMERA);
    }

    private boolean hasLocationAndContactsPermissions() {
        return EasyPermissions.hasPermissions(this, LOCATIN_AND_CONTACTS);
    }
    private boolean hasSmsPermission() {
        return EasyPermissions.hasPermissions(this, READ_SMS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            String yes = "Yes";
            String no = "No";

            // Do something after user returned from app settings screen, like showing a Toast.
            Toast.makeText(
                            this,
                            getString(R.string.returned_from_app_settings_to_activity,
                                    hasCameraPermission() ? yes : no,
                                    hasLocationAndContactsPermissions() ? yes : no,
                                    hasSmsPermission() ? yes : no),
                            Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Log.d("onPermissionsGranted", String.valueOf(requestCode) + "," + perms.size());


    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Log.d("onPermissionsDenied", String.valueOf(requestCode) + "," + perms.size());
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    public void onRationaleAccepted(int requestCode) {
        Log.d("onRationaleAccepted", String.valueOf(requestCode));

    }

    @Override
    public void onRationaleDenied(int requestCode) {
        Log.d("onRationaleDenied", String.valueOf(requestCode));
    }


}