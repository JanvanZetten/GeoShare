package dk.easv.geoshare.model;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

import dk.easv.geoshare.BE.PhotoMetaData;

public class FireStorageHelper {

    final String TAG = "FireStorageHelper";
    final FireStoreHelper fireStoreHelper = new FireStoreHelper();

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    public void UploadPhoto(File file_, final PhotoMetaData metaData) {

        Uri file = Uri.fromFile(file_);
        StorageReference storageReference = storageRef.child("images/" + file.getLastPathSegment());
        UploadTask uploadTask = storageReference.putFile(file);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                String name = taskSnapshot.getMetadata().getName();
                Log.d(TAG, name);
                String photoID = taskSnapshot.getMetadata().getName().substring(0, name.length() - 4);
                metaData.setPhotoID(photoID);
                fireStoreHelper.addPhotoMeta(metaData);
            }
        });
    }

    public void getDownLoadUrl(String photoId) {

        StorageReference storageReference = storageRef.child("images/" + photoId);
        Log.d(TAG, "getDownLoadUrl: " + storageReference.getName());

    }

}
