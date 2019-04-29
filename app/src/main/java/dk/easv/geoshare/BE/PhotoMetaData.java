package dk.easv.geoshare.BE;

import com.google.firebase.FirebaseApp;

import java.io.Serializable;
import java.sql.Timestamp;

public class PhotoMetaData implements Serializable {

    private String photoID;
    private double lat;
    private double lng;
    private Long timestamp;

    public PhotoMetaData() {

    }

    public PhotoMetaData(double lat, double lng, Long timestamp) {
        this.lat = lat;
        this.lng = lng;
        this.timestamp = timestamp;
    }

    public void setPhotoID(String photoID) {
        this.photoID = photoID;
    }

    public String getPhotoID() {
        return photoID;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public Long getTimestamp() {
        return timestamp;
    }
}
