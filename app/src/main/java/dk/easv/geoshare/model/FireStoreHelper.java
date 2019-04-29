package dk.easv.geoshare.model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import dk.easv.geoshare.BE.PhotoMetaData;


public class FireStoreHelper {

    private String TAG = "FireStoreHelper";

    FirebaseFirestore db = FirebaseFirestore.getInstance();

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

                Log.d(TAG, snapshots.size() + "");

            }
        });

    }

    public void addPhotoMeta(PhotoMetaData meta) {

        final CollectionReference colRef = db.collection("photoMetaData");

        Map<String, Object> metaData = new HashMap<>();

        metaData.put("photoId", meta.getPhotoID());
        metaData.put("lat", meta.getLat());
        metaData.put("lng", meta.getLng());
        metaData.put("timestamp", meta.getTimestamp());


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


}
