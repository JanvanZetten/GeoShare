package dk.easv.geoshare.CustomCamera;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;

import java.io.File;

import dk.easv.geoshare.R;

public class CustomCameraActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_camera);
        Uri uri = getIntent().getParcelableExtra(MediaStore.EXTRA_OUTPUT);
        File outputFile = new File(uri.getPath());
        if (null == savedInstanceState) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, Camera2BasicFragment.newInstance(outputFile))
                    .commit();
        }
    }
}
