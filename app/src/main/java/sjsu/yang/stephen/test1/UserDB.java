package sjsu.yang.stephen.test1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDB extends SQLiteOpenHelper {

    //DB_Name
    private static final String DB_NAME = "userDb";
    private static final int DB_VER = 1;

    //DB components
    static final String TB_NAME = "users";
    static final String colID = "uID";
    static final String colName = "uName";
    static final String colGen = "gender";
    static final String colWeight = "weight";

    static final String TB_NAME_2 = "workouts";
    static final String colDist = "workDist";
    static final String colTime = "workTime";
    static final String colCalories = "workCalories";
    static final String colDate = "workoutDate";
    static final String colwID = "wID";

    private static final String USER_TABLE = " CREATE TABLE " + TB_NAME + " " +
            "(" + colID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + colName + " TEXT, "
            + colGen + " TEXT, "
            + colWeight  + " NUMERIC" + ")";
    private static final String DATA_TABLE = " CREATE TABLE " + TB_NAME_2 + " " +
            "(" + colwID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            colDist + " NUMERIC, "
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

