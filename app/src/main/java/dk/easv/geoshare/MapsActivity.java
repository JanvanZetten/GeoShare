package dk.easv.geoshare;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.util.ArrayList;

import dk.easv.geoshare.BE.PhotoLocal;
import dk.easv.geoshare.BE.PhotoMetaData;
import dk.easv.geoshare.model.FireStoreHelper;
import dk.easv.geoshare.model.MyLocationListener;
import dk.easv.geoshare.model.PictureHelper;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private File photofile;
    private final static int PHOTO_REQUEST_CODE = 101;
    private FireStoreHelper fireStoreHelper = new FireStoreHelper();
    private ArrayList<PhotoMetaData> photoMetaDataArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        askForPermissions();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fireStoreHelper = new FireStoreHelper();

        photoMetaDataArrayList = fireStoreHelper.getPhotoMeta();

        Log.d("FireStor", "onCreate: " + photoMetaDataArrayList);

        final PictureHelper pictureHelper = new PictureHelper(this);
        findViewById(R.id.btnPicture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!askForPermissions()) return;
                photofile = pictureHelper.takePicture(PHOTO_REQUEST_CODE);
            }
        });
    }

    /**
     * Asks for the required permissions needed to use the application.
     * If one or more permissions are not granted, this method returns false.
     * If all permissions are granted, this method returns true.
     * @return boolean, false if permissions are missing, true if all are given.
     */
    private boolean askForPermissions() {
        ArrayList<String> permissions = new ArrayList<String>();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            permissions.add(Manifest.permission.CAMERA);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissions.size() > 0){
            Toast.makeText(this, "To use this application, you must accept the following permissions.", Toast.LENGTH_LONG);
            ActivityCompat.requestPermissions((Activity) this, permissions.toArray(new String[]{}), 1);
            return false;
        }
        return true;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PHOTO_REQUEST_CODE && resultCode == RESULT_OK) {
            new PhotoLocal(photofile, null);

            LatLng coordinates = null;
            try {
                coordinates = getCurrentLocation();
            } catch (Exception e) {
                e.printStackTrace();
            }
            double latitude = coordinates.latitude;
            double longitude = coordinates.longitude;

            fireStoreHelper.UploadPhoto(photofile, new PhotoMetaData(latitude, longitude, photofile.lastModified()));
            // TODO take photofile and the current location and upload it to firebase and put Photo on map
        }
    }

    /**
     * Gets the devices current coordinates as lat lng.
     * Creates a locationManager, gets
     * @return LatLng
     * @throws Exception
     */
    private LatLng getCurrentLocation() throws Exception {
        // TODO: Multithread this method, as it is too heavy for UI thread
        // Set location manager
        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);

        // Create a location listener
        LocationListener listener = new MyLocationListener();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            askForPermissions(); // This should NEVER be reachable as
                                 // permissions must be granted earlier
        }

        String provider; // Which type of provider to use
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            provider = LocationManager.GPS_PROVIDER;
        } else if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            provider = LocationManager.NETWORK_PROVIDER;
        } else {
            throw new Exception("No provider enabled");
        }
        // Request single coordinate update, and save the location to curLocation.
        locationManager.requestSingleUpdate(provider, listener, null);
        Location curLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        locationManager.removeUpdates(listener); // Remove update to ensure it's actually stopped.
        return new LatLng(curLocation.getLatitude(), curLocation.getLongitude());
    }
}
