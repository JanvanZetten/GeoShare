package dk.easv.geoshare.model;

import java.util.ArrayList;

import dk.easv.geoshare.BE.PhotoMetaData;

public interface StoreListener {

    public void onDataLoaded(ArrayList<PhotoMetaData> photoMetaDataArrayList);

}
