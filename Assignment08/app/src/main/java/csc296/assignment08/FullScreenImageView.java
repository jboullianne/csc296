/*
Jean-Marc Boullianne
CSC 296: Assignment08
Fall 2015
 */

package csc296.assignment08;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

public class FullScreenImageView extends AppCompatActivity {


    private ImageView fullImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fullImage = (ImageView) findViewById(R.id.image_view_full_screen);
        Uri uri = getIntent().getParcelableExtra(CameraAdapter.KEY_FULL_IMAGE);
        Bitmap bitmap = BitmapFactory.decodeFile(uri.getPath());
        fullImage.setImageBitmap(bitmap);
    }



}
