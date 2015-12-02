package csc296.assignment10;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "MainActivity";

    private Button mAsyncButton;
    private Button mHandlerButton;
    private Button mFetcherButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Thread Playground");

        mAsyncButton = (Button) findViewById(R.id.button_async_tester);
        mHandlerButton = (Button) findViewById(R.id.button_handler_tester);
        mFetcherButton = (Button) findViewById(R.id.button_image_fetcher);

        mAsyncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "AsyncTask Activity Starting");

                Intent intent = new Intent(getApplicationContext(), AsyncTesterActivity.class);
                startActivity(intent);
            }
        });

        mHandlerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Handler Activity Starting");

                Intent intent = new Intent(getApplicationContext(), HandlerTesterActivity.class);
                startActivity(intent);
            }
        });

        mFetcherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "ImageFetcher Activity Starting");

                Intent intent = new Intent(getApplicationContext(), ImageFetcherActivity.class);
                startActivity(intent);
            }
        });
    }
}
