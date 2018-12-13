package sjsu.yang.stephen.test1;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;


public class UserProfile extends FragmentActivity {

    //Define all the variables
    String uName;
    TextView name;
    Spinner gSpin;
    TextView uWeight;

    TextView weekDist;
    TextView weekTime;
    TextView weekWorkout;
    TextView weekCal;

    TextView allDist;
    TextView allTime;
    TextView totalWorkout;
    TextView totalCal;
    boolean start = true;

    User user;
    int cID = 1;
    DBOperations DBop;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        DBop = new DBOperations(this);
        DBop.open();
        user = new User();
        name = (TextView) findViewById(R.id.uName);
        gSpin = (Spinner) findViewById(R.id.genderSpinner);
        uWeight = (TextView) findViewById(R.id.userWeight);

        weekDist = (TextView) findViewById(R.id.userDistanceAvg);
        weekTime = (TextView) findViewById(R.id.timeAvg);
        weekWorkout = (TextView) findViewById(R.id.workoutCountAvg);
        weekCal = (TextView) findViewById(R.id.caloriesAvg);

        allDist = (TextView) findViewById(R.id.userDistanceAll);
        allTime = (TextView) findViewById(R.id.timeAll);
        totalWorkout = (TextView) findViewById(R.id.workoutCountAll);
        totalCal = (TextView) findViewById(R.id.caloriesAll);

        uName = name.getText().toString();
        getUserInfo();
        getWeekInfo();
        getAllInfo();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DBop.close();
    }

    @Override
    public void onBackPressed() {
        saveInfo();
        finish();
    }

    private void getUserInfo() {
        User cUser = null;
        try {
            cUser = DBop.getUser(cID);
        } catch (Exception e) {
            cUser = new User();
            cUser.setuID(1);
            cUser.setuName("John Doe");
            cUser.setuGender("male");
            cUser.setuWeight(150);
            User finished = DBop.addUser(cUser);
            cID = (int)finished.getuID();
        }
        finally {
            name.setText(cUser.getuName());
            String gen = cUser.getuGender();
            if(gen.equalsIgnoreCase("male")) {
                gSpin.setSelection(0);
            }
            else {
                gSpin.setSelection(1);
            }
            uWeight.setText(String.valueOf(cUser.getuWeight()));
            start = false;
        }
    }

    private void saveInfo() {
        if(!name.getText().toString().isEmpty()) {
            user.setuName(name.getText().toString());
        }
        user.setuGender(gSpin.getSelectedItem().toString());
        user.setuID(cID);
        if(!uWeight.getText().toString().isEmpty()) {
            user.setuWeight(Float.parseFloat(uWeight.getText().toString()));
        }
        DBop.updateUser(user);
    }

    public void getWeekInfo() {
        List<UserData> weekData = DBop.getWeekData();
        float weekADist = 0;
        float weekATime = 0;
        int weekAWorkout = DBop.getNumWeekWorkout();
        float weekACal = 0;
        boolean temp = false;

        for(UserData d: weekData) {
            weekADist += d.getwDist();
            weekATime += d.getwTime();
            weekACal += d.getwCal();
        }
        if(weekAWorkout == 0) {
            weekAWorkout = 1;
            temp = true;
        }
        weekADist = weekADist / weekAWorkout;
        weekATime = weekATime / weekAWorkout;
        weekACal = weekACal / weekAWorkout;
        if(temp) {
            weekAWorkout = 0;
        }

        String time = convertTime((int) weekATime);

        String distance = String.format(java.util.Locale.US,"%.2f",weekADist) + " miles";
        String workouts = String.valueOf(weekAWorkout) + " times";
        String calories = String.format(java.util.Locale.US,"%.2f",weekACal) + " calories";

        weekDist.setText(distance);
        weekTime.setText(time);
        weekWorkout.setText(workouts);
        weekCal.setText(calories);

    }

    public void getAllInfo() {
        List<UserData> allData = DBop.getAllData();
        float totalDist = 0;
        float totalTime = 0;
        int totalWorkouts = DBop.getTotalNumWorkout();
        float totalCals = 0;

        for(UserData d: allData) {
            totalDist += d.getwDist();
            totalTime += d.getwTime();
            totalCals += d.getwCal();
        }
        String time = convertTime((int) totalTime);
        String distance = String.format(java.util.Locale.US,"%.2f", totalDist) + " miles";
        String workouts = String.valueOf(totalWorkouts) + " times";
        String calories = String.format(java.util.Locale.US,"%.2f",totalCals) + " calories";

        allDist.setText(distance);
        allTime.setText(time);
        totalWorkout.setText(workouts);
        totalCal.setText(calories);

    }

    private String convertTime(int time) {
       int sTime = time / 1000;
       int min = (sTime % 3600) / 60;
       int hour = sTime / (60 * 60);
       int day = sTime / (60 * 60 * 24);
       sTime = (sTime % 3600) % 60;
       return day + " day(s) " + hour + " hr(s) " + min + " min(s) " + sTime + " sec ";
    }
}
