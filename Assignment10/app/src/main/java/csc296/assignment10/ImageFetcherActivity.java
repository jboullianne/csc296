package csc296.assignment10;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageFetcherActivity extends AppCompatActivity {

    private static String TAG = "ImageFetcherActivity";

    private ImageView mFetchedImage;
    private EditText mImageAddress;
    private Button mFetchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_fetcher);

        mFetchedImage = (ImageView) findViewById(R.id.imageview_fetched_image);
        mImageAddress = (EditText) findViewById(R.id.edittext_image_address);
        mFetchButton = (Button) findViewById(R.id.button_fetch_image);

        mFetchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Fetching Image...");
                mFetchButton.setEnabled(false);

                String stringUrl = mImageAddress.getText().toString();
                ConnectivityManager connectivityManager = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    Toast.makeText(getApplicationContext(), "Fetching Image...", Toast.LENGTH_SHORT).show();
                    new FetchImageTask().execute(stringUrl);
                } else {
                    Log.d(TAG, "No Network Connection");
                    Toast.makeText(getApplicationContext(), "No Network Connection", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    private class FetchImageTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {

            Bitmap image = null;
            // params comes from the execute() call: params[0] is the url.
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return image;
            }
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(Bitmap result) {
            ImageView imageView = (ImageView) findViewById(R.id.imageview_fetched_image);

            mFetchButton.setEnabled(true); //Re-Enable the Button So more images can be fetched

            if(result == null){
                Toast.makeText(getApplicationContext(), "No Image Found At URL", Toast.LENGTH_SHORT).show();
                return;
            }


            imageView.setImageBitmap(result);

            // get the center for the clipping circle
            int cx = imageView.getWidth() / 2;
            int cy = imageView.getHeight() / 2;

            // get the final radius for the clipping circle
            int finalRadius = Math.max(imageView.getWidth(), imageView.getHeight());

            // create the animator for this view (the start radius is zero)
            Animator anim =
                    ViewAnimationUtils.createCircularReveal(imageView, cx, cy, 0, finalRadius);

            // make the view visible and start the animation
            imageView.setVisibility(View.VISIBLE);
            anim.start();


        }

        // Given a URL, establishes an HttpUrlConnection and retrieves
        // the web page content as a InputStream, which it returns as
        // a string.
        private Bitmap downloadUrl(String myurl) throws IOException {
            Bitmap image = null;
            InputStream is = null;

            try {
                URL url = new URL(myurl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();
                int response = conn.getResponseCode();
                Log.d(TAG, "The response is: " + response);
                is = conn.getInputStream();

                image = BitmapFactory.decodeStream(is);

                // Makes sure that the InputStream is closed after the app is
                // finished using it.
            } finally {
                if (is != null) {
                    is.close();
                }
            }
            return image;
        }


    }
}
