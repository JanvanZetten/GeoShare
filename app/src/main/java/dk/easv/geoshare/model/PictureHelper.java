package dk.easv.geoshare.model;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import dk.easv.geoshare.CustomCamera.CustomCameraActivity;

public class PictureHelper {
    private static final String LOGTAG = "Picture helper log";
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private File photoFile;
    private Context context;

    public PictureHelper(Context context) {
        this.context = context;
        askForPermissions(context);
    }


    public File takePicture(int requestCode){
        openCamera(requestCode);
        return photoFile;
    }


    private void askForPermissions(Context context) {
        ArrayList<String> permissions = new ArrayList<String>();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            permissions.add(Manifest.permission.CAMERA);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissions.size() > 0)
            ActivityCompat.requestPermissions((Activity)context, permissions.toArray(new String[]{}), 1);
    }


    private void openCamera(int requstCode) {
        photoFile = getOutputMediaFile(); // create a file to save the image
        if (photoFile == null) {
            Toast.makeText(context, "Could not create file...", Toast.LENGTH_LONG).show();
            return;
        }

        // create Intent to take a picture
        Intent intent = new Intent(context, CustomCameraActivity.class);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(
                context,
                "dk.easv.geoshare.provider", //(use your app signature + ".provider" )
                photoFile));

        if (intent.resolveActivity(context.getPackageManager()) != null) {
            ((Activity)context).startActivityForResult(intent, requstCode);
        } else
            Log.d(LOGTAG, "camera app could NOT be started");
    }


    private static File getOutputMediaFile() {
        String DIRECTORY_NAME = "GeoPicturesLocal";
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), DIRECTORY_NAME);

        if (!checkDirectoryExists(mediaStorageDir)){
            return null;
        }

        // Create a media file name
        //String timeStamp = Calendar.getInstance().toString();
        String postfix = "jpg";
        String prefix = "IMG";

        File mediaFile = new File(mediaStorageDir.getPath() +
                File.separator + prefix +
                UUID.randomUUID().toString()//Unique id
                + "." + postfix);
        return mediaFile;
    }


    /**
     * Check if Directory exists, if not try to make it. If successful return true.
     * @param mediaStorageDir
     * @return
     */
    private static boolean checkDirectoryExists(File mediaStorageDir) {
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return false;
            }
        }
        return true;
    }
}
