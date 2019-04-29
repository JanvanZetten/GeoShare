package dk.easv.geoshare.BE;

import java.io.Serializable;

public class PhotoMetaData implements Serializable {

    private String photoID;

    private double lat;
    private double lng;
    private Long timestamp;
    private String photoUrl;

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

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
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


    @Override
    public String toString() {
        return "lat: " + lat + "\nLng: " + lng + "\nTimestamp: " + timestamp + "\nPhotoId: " + photoID + "\nPhotoUrl: " + photoUrl + "\n";
    }
}
