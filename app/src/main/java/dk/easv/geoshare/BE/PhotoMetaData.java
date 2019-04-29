package dk.easv.geoshare.BE;

import com.google.firebase.FirebaseApp;

import java.io.Serializable;
import java.sql.Timestamp;

public class PhotoMetaData implements Serializable {

    private String photoID;
    private Double lat;
    private Double lng;
    private Long timestamp;
    private String photoUrl;

    public PhotoMetaData() {

    }

    public PhotoMetaData(Double lat, Double lng, Long timestamp) {
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

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }

    public Long getTimestamp() {
        return timestamp;
    }



    @Override
    public String toString() {
        return "lat: " + lat + "\nLng: " + lng + "\nTimestamp: " + timestamp + "\nPhotoId: " + photoID + "\nPhotoUrl: " + photoUrl + "\n";
    }
}
