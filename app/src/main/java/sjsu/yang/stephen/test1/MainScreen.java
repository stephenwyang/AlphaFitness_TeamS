package sjsu.yang.stephen.test1;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

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
    long workoutID = 0;

    //Sensors
    SensorManager sManage;
    Sensor stepSense;


    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean mPermissionDenied = false;
    private final int REQUEST_CODE = 0;
    private boolean isFirstLaunch = true;

    @Nullable
    LocationManager mLocationManager;
    Context mContext;
    private ArrayList<LatLng> mLocationList;

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
        DBop.open();

        //Sensor for steps
        sManage = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepSense = sManage.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        sManage.registerListener(this, stepSense, SensorManager.SENSOR_DELAY_FASTEST);


        //Switch to detail if you change the orientation
        /*
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Intent intent = new Intent(this, detailActivity.class);
            startActivity(this);
        }
         */

        //Initialize the others
        h = new Handler();

        //Location stuff

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE);

        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            SupportMapFragment mFrag = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
            mFrag.getMapAsync(this);
        }
        mContext = this;
        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        assert mLocationManager != null;
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 10, locationListenerGPS);
        mLocationList = new ArrayList<>();


    }

    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE);

        if ((ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            gMap.setMyLocationEnabled(true);
        }

        LocationManager lManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Location l = lManager != null ? lManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) : null;
        if (l == null) {
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            String provider = lManager != null ? lManager.getBestProvider(criteria, true) : null;
            assert lManager != null;
            l = lManager.getLastKnownLocation(provider);

            if (isFirstLaunch) {
                isFirstLaunch = false;

                if(l == null){

                    l = new Location("");
                    l.setLatitude(-122.084d);
                    l.setLongitude(37.4220d);
                    l.setAltitude(0.0d);
                }

                googleMap.addMarker(new MarkerOptions().position(
                        new LatLng(l.getLatitude(), l.getLongitude())).title("Current location"));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(l.getLatitude(), l.getLongitude()), 16));
            } else {
                isFirstLaunch = false;
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(
                        new LatLng(l.getLatitude(), l.getLongitude())));
            }

        } else {

            if (isFirstLaunch) {
                isFirstLaunch = false;
                googleMap.addMarker(new MarkerOptions().position(
                        new LatLng(l.getLatitude(), l.getLongitude())).title("Current location"));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(l.getLatitude(), l.getLongitude()), 16));
            } else {
                isFirstLaunch = false;
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(
                        new LatLng(l.getLatitude(), l.getLongitude())));
            }
        }

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

            //Create the userData and add it into
            ud.setwID(100);
            ud.setwDist(0);
            ud.setwCal(0);
            ud.setwTime(0);
            ud.setwDate(startTime);
            UserData throwaway = DBop.addUData(ud);
            ud.setwID(throwaway.getwID());

            h.postDelayed(updateDisTime, 0);
            h.postDelayed(updateDB, 0);
            h.postDelayed(locationChangedRunnable, 10);


        }
        else {
            //Set button back to normal
            workoutB.setText("START WORKOUT");
            workoutB.setBackgroundColor(Color.WHITE);
            workoutB.setTextColor(Color.BLACK);

            isWorkOut = false;

            //Put the workout inside the DB, etc.
            h.removeCallbacks(updateDisTime);
            h.removeCallbacks(updateDB);
            h.removeCallbacks(locationChangedRunnable);
            steps = 0;

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
            distanceView.setText(String.format(java.util.Locale.US, "%.2f", getDistance()));
            h.postDelayed(this, 0 );
        }
    };

    //Runnable method to add data into the system
    private Runnable updateDB = new Runnable() {
        public void run() {
            float calBurned = getCalories();
            float workoutTime = SystemClock.uptimeMillis() - startTime;
            float distance = getDistance();
            ud.setwDist(distance);
            ud.setwTime(workoutTime);
            ud.setwCal(calBurned);

            DBop.updateUData(ud);

            h.postDelayed(this, 0);
            //Temp method: Just for the video
            //Should be working otherwise
            steps += 1;
        }
    };

    //General calories burned per 2k steps is like .55 of your weight
    public float getCalories() {
        float cal = 0;
        float weight;
        try {
            User u = DBop.getUser(1);
            weight = u.getuWeight();
        }
        catch (Exception e) {
            weight = 120;
        }
        cal = (float)(.55 * weight); //This is per 2k steps
        cal = cal * steps / 2000; //This is total cal
        return cal;
    }

    //Method for finding distance
    public float getDistance() {
        return (float) (steps * 2.2) / 5280;
    }


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

    @NonNull
    LocationListener locationListenerGPS = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull android.location.Location location) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            mLocationList.add(new LatLng(latitude, longitude));

            if(isWorkOut) {
                PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
                for (int z = 0; z < mLocationList.size(); z++) {
                    LatLng p = mLocationList.get(z);
                    options.add(p);
                }
                gMap.addPolyline(options);
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    @NonNull
    @SuppressWarnings("unused")
    private Runnable locationChangedRunnable = new Runnable() {
        public void run() {
            LocationListener locationListenerGPS = new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull android.location.Location location) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                    mLocationList.add(new LatLng(latitude, longitude));

                    PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
                    for (int z = 0; z < mLocationList.size(); z++) {
                        LatLng point = mLocationList.get(z);
                        options.add(point);
                    }
                    gMap.addPolyline(options);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };

            h.postDelayed(this, 0);
        }
    };

}
