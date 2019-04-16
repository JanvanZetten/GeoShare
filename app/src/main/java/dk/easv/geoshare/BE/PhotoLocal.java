package dk.easv.geoshare.BE;

import com.google.android.gms.maps.model.LatLng;

import java.io.File;

public class PhotoLocal {
    private File photofile;
    private LatLng location;

    public PhotoLocal(File photofile, LatLng location) {
        this.photofile = photofile;
        this.location = location;
    }

    public File getPhotofile() {
        return photofile;
    }

    public void setPhotofile(File photofile) {
        this.photofile = photofile;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }
}
