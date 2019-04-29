package dk.easv.geoshare.BE;

import com.google.firebase.FirebaseApp;

import java.io.Serializable;
import java.sql.Timestamp;

public class PhotoMetaData implements Serializable {

    private String photoID;
<<<<<<< HEAD
    private Double lat;
    private Double lng;
=======
    private double lat;
    private double lng;
>>>>>>> 2b2d3605549764510037b878a2ad1e6f3a361e93
    private Long timestamp;
    private String photoUrl;

    public PhotoMetaData() {

    }

<<<<<<< HEAD
    public PhotoMetaData(Double lat, Double lng, Long timestamp) {
=======
    public PhotoMetaData(double lat, double lng, Long timestamp) {
>>>>>>> 2b2d3605549764510037b878a2ad1e6f3a361e93
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

<<<<<<< HEAD
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
=======
    public double getLat() {
        return lat;
    }

    public double getLng() {
>>>>>>> 2b2d3605549764510037b878a2ad1e6f3a361e93
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
