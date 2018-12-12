package sjsu.yang.stephen.test1;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

public class MainScreen extends AppCompatActivity implements OnMapReadyCallback, SensorEventListener {

    private GoogleMap gMap;

    //Storing user information and such

    private boolean isWorkOut = false;
    //UI elements
    private Button workoutB;
    private TextView timeView;
    private TextView distanceView;

    Handler h;

    //Other variables
    int s, m, ms;
    long msTime, startTime, timeBuff, updateTime = 0L;
    private long steps;

    //Database Stuff
    DBOperations DBop;
    private UserData ud;

    //Sensors
    SensorManager sManage;
    Sensor stepSense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize the UI elements
        workoutB = (Button) findViewById(R.id.workoutButton);
        timeView = (TextView) findViewById(R.id.timeView);
        distanceView = (TextView) findViewById(R.id.distanceView);

        //Database stuff

        ud = new UserData();
        DBop = new DBOperations(this);


        //Switch to detail if you change the orientation
        /*
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Intent intent = new Intent(this, detailActivity.class);
            startActivity(this);
        }
         */

        //Initialize the others
        h = new Handler();

    }

    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;

    }

    //Button click methods
    public void onUserButtonClicked(View view) {
        Intent intent = new Intent(this, UserProfile.class);
        startActivity(intent);
    }

    public void workoutButtonClicked(View view) {
        if(!isWorkOut) {

            //Change the text
            workoutB.setText("STOP WORKOUT");
            workoutB.setBackgroundColor(Color.RED);
            workoutB.setTextColor(Color.WHITE);

            isWorkOut = true;

            //Reset the data
            msTime = 0L;
            startTime = 0L;
            timeBuff = 0L;
            updateTime = 0L;
            s = 0;
            m = 0;
            ms = 0;

            //Get the beginning of the clock
            startTime = SystemClock.uptimeMillis();

            h.postDelayed(updateDisTime, 0);


        }
        else {
            //Set button back to normal
            workoutB.setText("START WORKOUT");
            workoutB.setBackgroundColor(Color.WHITE);
            workoutB.setTextColor(Color.BLACK);

            isWorkOut = false;

            //Put the workout inside the DB, etc.
            h.removeCallbacks(updateDisTime);
        }

    }

    //Runnable method to track a timer
    public Runnable updateDisTime = new Runnable() {
        public void run() {
            msTime = SystemClock.uptimeMillis();
            updateTime = timeBuff + msTime - startTime;

            s = (int) (updateTime / 1000);
            m = s / 60;
            s = s % 60;
            ms = (int) (updateTime % 1000);
            timeView.setText("" + m + ":" + String.format("%02d", s) + ":" + String.format("%03d", ms));
            //distanceView.setText(String.format(java.util.Locale.US, "%.2f", getDistance()));
            h.postDelayed(this, 0 );
        }
    };

    @Override
    public void onSensorChanged(SensorEvent e) {
        Sensor sensor = e.sensor;
        float[] values = e.values;
        int value = -1;

        if (values.length > 0) {
            value = (int) values[0];
        }


        if (sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            steps+=1;
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //We don't touch accuracy, no need to mess with it
    }

}
