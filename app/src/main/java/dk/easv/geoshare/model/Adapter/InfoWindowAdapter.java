package dk.easv.geoshare.model.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import dk.easv.geoshare.R;

public class InfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private LayoutInflater inflater;
    private Context context;

    public InfoWindowAdapter(Context context) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
       return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        // Getting view from the layout file
        View v = inflater.inflate(R.layout.information_window, null);

        URL url = null;

        try {
            url = new URL(marker.getSnippet());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        ImageView iv = (ImageView) v.findViewById(R.id.markerImage);

        Log.d("Fire", "getInfoWindow: " + url);

        try {
            InputStream content = (InputStream) url.getContent();
            Drawable d = Drawable.createFromStream(content, "src");
            iv.setImageDrawable(d);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return v;
    }
}

