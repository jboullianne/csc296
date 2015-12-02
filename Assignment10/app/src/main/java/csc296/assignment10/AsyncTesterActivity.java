package csc296.assignment10;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AsyncTesterActivity extends AppCompatActivity {

    private static String TAG = "AsyncTesterActivity";

    private EditText mLongField;
    private Button mPrimeButton;
    private Button mSquareButton;
    private TextView mResultView;

    private NextPrimeTask nextPrimeTask;
    private SquareRootTask squareRootTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async_tester);

        mLongField = (EditText) findViewById(R.id.edittext_long);
        mPrimeButton = (Button) findViewById(R.id.button_next_prime);
        mSquareButton = (Button) findViewById(R.id.button_square_root);
        mResultView = (TextView) findViewById(R.id.textview_result);

        mPrimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isValidLong(mLongField.getText().toString()))
                    return;

                Log.d(TAG, "Calculating Next Prime");
                Toast.makeText(getApplicationContext(), "Calculating Next Prime", Toast.LENGTH_SHORT).show();
                nextPrimeTask =  new NextPrimeTask();
                nextPrimeTask.execute(mLongField.getText().toString());
                mPrimeButton.setEnabled(false);
                mResultView.setText("Result: ... (Calculating) ...");
            }
        });

        mSquareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isValidLong(mLongField.getText().toString()))
                    return;

                Log.d(TAG, "Calculating Square Root");
                Toast.makeText(getApplicationContext(), "Calculating Square Root", Toast.LENGTH_SHORT).show();
                squareRootTask = new SquareRootTask();
                squareRootTask.execute(mLongField.getText().toString());
                mPrimeButton.setEnabled(false);
                mResultView.setText("Result: ... (Calculating) ...");
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

    @Override
    public void onStop(){
        nextPrimeTask.cancel(true);
        squareRootTask.cancel(true);
        super.onStop();
    }

    @Override
    public void onDestroy(){
        nextPrimeTask.cancel(true);
        squareRootTask.cancel(true);
        super.onDestroy();
    }


    private class NextPrimeTask extends AsyncTask<String, Void, Long> {

        @Override
        protected Long doInBackground(String... num) {

            Long start = new Long(Long.parseLong(num[0]));
            Long val = new Long(start + 1);

            while(!isPrime(val)){
                val++;
            }
            return val;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override

        protected void onPostExecute(Long result) {
            mResultView.setText("Result: " + result.toString());
            mPrimeButton.setEnabled(true);
        }

        public boolean isPrime(long n) {
            for(int i=2;i<n;i++) {
                if(n%i==0)
                    return false;
            }
            return true;
        }

    }

    private class SquareRootTask extends AsyncTask<String, Void, Long> {

        @Override
        protected Long doInBackground(String... num) {

            Long start = new Long(Long.parseLong(num[0]));
            return (long) Math.sqrt(start);

        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(Long result) {
            if(result == -1){
                mResultView.setText("Result: No Integer Root Found");
            }else{
                mResultView.setText("Result: " + result.toString());
            }
            mPrimeButton.setEnabled(true);
        }

    }
}
