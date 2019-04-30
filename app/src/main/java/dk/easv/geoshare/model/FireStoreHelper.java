package dk.easv.geoshare.model;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import dk.easv.geoshare.BE.PhotoMetaData;

public class FireStoreHelper {

    private String TAG = "FireStoreHelper";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();

    final ArrayList<PhotoMetaData> photoMetaDataArrayList = new ArrayList<>();

    public void getPhotoMeta() {
        final CollectionReference colRef = db.collection("photoMetaData");
        colRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }
                photoMetaDataArrayList.clear();

                for (QueryDocumentSnapshot snapShot : snapshots) {
                    Map<String, Object> data = snapShot.getData();
                    PhotoMetaData photoMetaData = new PhotoMetaData(
                            (Double) data.get("lat")
                            , (Double) data.get("lng")
                            , (long) data.get("timestamp"));
                    photoMetaData.setPhotoID(data.get("photoId").toString());
                    photoMetaData.setPhotoUrl(data.get("photoUrl").toString());
                    photoMetaDataArrayList.add(photoMetaData);
                }

                ObservableArrayList.setPhotoMetaDataArrayList(photoMetaDataArrayList);
            }
        });
    }


    //Adding to firebase

    /**
     * Adds the metadata to a firebase collection
     *
     * @param meta
     */
    public void addPhotoMeta(PhotoMetaData meta) {

        final CollectionReference colRef = db.collection("photoMetaData");

        Map<String, Object> metaData = new HashMap<>();

        metaData.put("photoId", meta.getPhotoID());
        metaData.put("lat", meta.getLat());
        metaData.put("lng", meta.getLng());
        metaData.put("timestamp", meta.getTimestamp());
        metaData.put("photoUrl", meta.getPhotoUrl());

        colRef.add(metaData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }


    /**
     * Uploads a file to firebase and adds the id of the photo to the metadata
     * then gets the download url and adds that to the metadata
     * then uploads the metadata with the method above
     *
     * @param file_
     * @param metaData
     */
    public void UploadPhoto(File file_, final PhotoMetaData metaData) {

        Uri file = Uri.fromFile(file_);
        final StorageReference storageReference = storageRef.child("images/" + file.getLastPathSegment());
        UploadTask uploadTask = storageReference.putFile(file);

        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                String name = task.getResult().getMetadata().getName();
                Log.d(TAG, name);
                String photoID = task.getResult().getMetadata().getName().substring(0, name.length() - 4);
                metaData.setPhotoID(photoID);

                return storageReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    metaData.setPhotoUrl(task.getResult().toString());

                    Log.d(TAG, "UploadPhoto: " + metaData.toString());
                    addPhotoMeta(metaData);
                } else {

                }
            }
        });
    }
}
