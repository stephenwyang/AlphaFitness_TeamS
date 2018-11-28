package sjsu.yang.stephen.alphafitness;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;


public class MainScreen extends AppCompatActivity implements OnMapReadyCallback{

    private GoogleMap gMap;
    private boolean isWorkOut = false;
    private Button workout;
    private TextView timeView;
    int s, m, ms;
    long msTime, startTime, timeBuff, updateTime = 0L;
    private long steps;

    private TextView distanceView;

    SensorManager sensManager;
    Sensor stepSense;
    Handler h;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        //Setup Button/View
        workout = (Button) findViewById(R.id.workoutButton);
        timeView = (TextView) findViewById(R.id.timeView);
        distanceView =  (TextView) findViewById(R.id.distanceView);


    }

    public void onMapReady(@NonNull GoogleMap googleMap) {

    }

    //Click on the button, go to the profile page
    public void goToUserProf(View view) {
        //Intent intent = new Intent(this, profile.class)
        //startActivity(intent)
    }

    public void workoutButtonClicked(View view) {
        if(!isWorkOut) {
            //Reset all the data if you aren't working out
            msTime = 0L;
            startTime = 0L;
            timeBuff = 0L;
            updateTime = 0L;
            s = 0;
            m = 0;
            ms = 0;
            steps = 0;

            //Change the text
            workout.setText("STOP WORKOUT");
            workout.setBackgroundColor(Color.RED);
            workout.setTextColor(Color.WHITE);

            isWorkOut = true;

            //Set the start time for the clock
            startTime = SystemClock.uptimeMillis();
            //Other things
            h.postDelayed(updateStuffRun, 0);

        }
        else {
            //Set button back to normal
            workout.setText("START WORKOUT");
            workout.setBackgroundColor(Color.WHITE);
            workout.setTextColor(Color.BLACK);

            isWorkOut = false;

            //Put the workout inside the DB, etc.
        }

    }

    public Runnable updateStuffRun = new Runnable() {
        public void run() {
            msTime = SystemClock.uptimeMillis();
            updateTime = timeBuff + msTime;
            s = (int) (updateTime / 1000);
            m = s / 60;
            s = s % 60;
            ms = (int) (updateTime % 1000);
            timeView.setText(m + ":" + String.format("%02d", s) + ":" + String.format("%03d", ms));
            distanceView.setText(String.format(java.util.Locale.US, "%.2f", getDistance()));
            h.postDelayed(this, 0 );
        }
        };

    //Do this later, need to calculate distance walken
    public Object getDistance() {
        return 0;
    }
}



