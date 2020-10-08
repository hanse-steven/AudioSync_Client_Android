package be.isservers.audiosync.authorization;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class AutorizationRequest {
    static public void requestStoragePermission(AppCompatActivity parent) {
        Dexter.withActivity(parent)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Log.i("AutorizationRequest","All permissions are granted!");
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog(parent);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(error -> Log.i("AutorizationRequest","Error occurred! "))
                .onSameThread()
                .check();
    }

    static private void showSettingsDialog(AppCompatActivity parent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(parent);
        builder.setTitle("Permissions nécessaires");
        builder.setMessage("Cette application a besoin de permissions pour utiliser cette fonctionnalité. Vous pouvez les accorder dans les paramètres d’applications.");
        builder.setPositiveButton("Vers les paramètres", (dialog, which) -> {
            dialog.cancel();
            openSettings(parent);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();

    }

    static private void openSettings(AppCompatActivity parent) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", parent.getPackageName(), null);
        intent.setData(uri);
        parent.startActivityForResult(intent, 101);
    }
}
