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

    User user;
    DBOperations DBop;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
    }

}
