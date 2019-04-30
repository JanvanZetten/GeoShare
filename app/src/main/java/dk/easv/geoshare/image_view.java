package dk.easv.geoshare;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class image_view extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        ImageView iv = findViewById(R.id.i_v);

        URL url = (URL) getIntent().getSerializableExtra("url");

        try {
            InputStream content = (InputStream) url.getContent();
            Drawable d = Drawable.createFromStream(content, "src");
            iv.setImageDrawable(d);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
