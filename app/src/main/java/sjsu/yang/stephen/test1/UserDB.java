package sjsu.yang.stephen.test1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDB extends SQLiteOpenHelper {

    //DB_Name
    private static final String DB_NAME = "userDb";
    private static final int DB_VER = 1;

    //DB components
    private static final String TB_NAME = "users";
    private static final String colID = "uID";
    private static final String colName = "uName";
    private static final String colGen = "gender";
    private static final String colWeight = "weight";

    private static final String TB_NAME_2 = "workouts";
    private static final String colDist = "workDist";
    private static final String colTime = "workTime";
    private static final String colCalories = "workCalories";
    private static final String colDate = "workoutDate";

    private static final String USER_TABLE = " CREATE TABLE " + TB_NAME + " " +
            "(" + colID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + colName + " TEXT, "
            + colGen + " TEXT, "
            + colWeight  + " NUMERIC" + ")";
    private static final String DATA_TABLE = " CREATE TABLE " + TB_NAME_2 + " " +
            "(" + colDist + " NUMERIC, "
            + colTime + " NUMERIC, "
            + colCalories + " NUMERIC, "
            + colDate + " NUMERIC" + ")";

    UserDB(Context c) {
        super(c,DB_NAME, null, DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(USER_TABLE);
        db.execSQL(DATA_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS "+ TB_NAME);
        db.execSQL(" DROP TABLE IF EXISTS " + TB_NAME_2);
        db.execSQL(USER_TABLE);
        db.execSQL(DATA_TABLE);
    }
}

