package csc296.assignment10;

import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.logging.Handler;

public class HandlerTesterActivity extends AppCompatActivity {

    private static String TAG = "HandlerTesterActivity";

    private EditText mLongField;
    private Button mPrimeButton;
    private Button mSquareButton;
    private TextView mResultView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler_tester);


        mLongField = (EditText) findViewById(R.id.edittext_long);
        mPrimeButton = (Button) findViewById(R.id.button_next_prime);
        mSquareButton = (Button) findViewById(R.id.button_square_root);
        mResultView = (TextView) findViewById(R.id.textview_result);

        mPrimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isValidLong(mLongField.getText().toString()))
                    return;

                Log.d(TAG, "Calculating Next Prime");
                Toast.makeText(getApplicationContext(), "Calculating Next Prime", Toast.LENGTH_SHORT).show();


            }
        });

        mSquareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isValidLong(mLongField.getText().toString()))
                    return;

                Log.d(TAG, "Calculating Square Root");
                Toast.makeText(getApplicationContext(), "Calculating Square Root", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean isValidLong(String num){
        long val;
        try{
            val = Long.parseLong(num);
        }catch(NumberFormatException e){
            Toast.makeText(this, "Not a Valid 64bit Integer", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return false;
        }

        if(val < 2){
            Toast.makeText(this, "Number Must Be Greater Than 1", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

}
