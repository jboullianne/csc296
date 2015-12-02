package csc296.assignment08;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import java.io.File;
import java.util.UUID;

public class CameraActivity extends AppCompatActivity {

    private static final int RC_ADD_IMAGE = 2;
    private static final String TAG = "CAMERA_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Camera");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CameraFragment cameraFragment = new CameraFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().add(R.id.recycler_view_grid_frame, cameraFragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_camera_recycler, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.ic_new_image){
            Log.d(TAG, "New Image Icon Pressed");
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

                String filename = "IMG_" + UUID.randomUUID().toString() + ".jpg";
                File picturesDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                File mImage = new File(picturesDir, filename);

                Uri photoUri = Uri.fromFile(mImage);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

                Log.d(TAG, getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString());

                startActivityForResult(takePictureIntent, RC_ADD_IMAGE);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_ADD_IMAGE && resultCode == RESULT_OK) {
            Log.d(TAG, "New Image Added");
        }
    }

}
