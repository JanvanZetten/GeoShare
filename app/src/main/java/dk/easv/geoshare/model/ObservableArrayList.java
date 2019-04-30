package dk.easv.geoshare.model;

import java.util.ArrayList;

import dk.easv.geoshare.BE.PhotoMetaData;
import dk.easv.geoshare.model.Interface.StoreListener;

public class ObservableArrayList {

    private static ArrayList<PhotoMetaData> photoMetaDataArrayList  = new ArrayList<>();
    private static ArrayList<StoreListener> listenerArrayList = new ArrayList<>();

    public static void setPhotoMetaDataArrayList(ArrayList<PhotoMetaData> photoMetaDataArrayList_) {
        photoMetaDataArrayList = photoMetaDataArrayList_;
        for (StoreListener listener : listenerArrayList) {
            listener.onDataLoaded(photoMetaDataArrayList);
        }
    }

    public static void addOnDataLoadedListener(StoreListener listener) {
        listenerArrayList.add(listener);
    }

    public static void removeListener(StoreListener listener) {
        listenerArrayList.remove(listener);
    }
}
