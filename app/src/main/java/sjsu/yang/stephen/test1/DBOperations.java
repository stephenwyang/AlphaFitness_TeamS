package sjsu.yang.stephen.test1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.SystemClock;

import java.util.ArrayList;
import java.util.List;

public class DBOperations {
    //Variables
    private final SQLiteOpenHelper dbhelper;
    private SQLiteDatabase db;


    private static final String[] allUserCol = {
            UserDB.colID,
            UserDB.colName,
            UserDB.colGen,
            UserDB.colWeight
    };

    private static final String[] allDataCol = {
            UserDB.colDist,
            UserDB.colTime,
            UserDB.colCalories,
            UserDB.colDate
    };

    public DBOperations(Context context) {
        dbhelper = new UserDB(context);
    }

    public void open() {
        db = dbhelper.getWritableDatabase();
    }

    public void close() {
        dbhelper.close();
    }

    public User addUser(User u) {
        ContentValues v = new ContentValues();
        v.put(UserDB.colName, u.getuName());
        v.put(UserDB.colGen, u.getuGender());
        v.put(UserDB.colWeight, u.getuWeight());
        long uID = db.insert(UserDB.TB_NAME, null, v);
        u.setuID(uID);
        return u;
    }

    public UserData addUData(UserData ud) {
        ContentValues v = new ContentValues();
        v.put(UserDB.colDist, ud.getwDist());
        v.put(UserDB.colTime, ud.getwTime());
        v.put(UserDB.colCalories, ud.getwCal());
        v.put(UserDB.colDate, ud.getwDate());
        db.insert(UserDB.TB_NAME_2, null, v);
        return ud;
    }

    public User getUser(long id) {
        Cursor c = db.query(UserDB.TB_NAME, allUserCol, UserDB.colID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if(c != null) {
            c.moveToFirst();
        }
        assert c != null;
        User u = new User(Long.parseLong(c.getString(0)), c.getString(1), c.getString(2), Float.parseFloat(c.getString(3)));
        c.close();
        return u;
    }

    public void updateUser(User u) {
        ContentValues v = new ContentValues();
        v.put(UserDB.colName, u.getuName());
        v.put(UserDB.colGen, u.getuGender());
        v.put(UserDB.colWeight, u.getuWeight());
        db.update(UserDB.TB_NAME, v, UserDB.colID + "=?", new String[] {String.valueOf(u.getuID())});
        return;
    }

    public List<UserData> getWeekData() {
        List<UserData> weekUD = new ArrayList<>();
        int week = 1000 * 60 * 60 * 24 * 7;
        Cursor c2 = db.rawQuery("SELECT * FROM workouts WHERE workoutDate >= " + (SystemClock.uptimeMillis() - week), null);
        if(c2.getCount() > 0) {
            while(c2.moveToNext()) {
                UserData udAdd = new UserData();
                udAdd.setwDist(c2.getFloat(c2.getColumnIndex(UserDB.colDist)));
                udAdd.setwTime(c2.getFloat(c2.getColumnIndex(UserDB.colTime)));
                udAdd.setwCal(c2.getFloat(c2.getColumnIndex(UserDB.colCalories)));
                udAdd.setwDate(c2.getLong(c2.getColumnIndex(UserDB.colDate)));
                weekUD.add(udAdd);
            }
        }
        c2.close();
        return weekUD;
    }

    public int getNumWeekWorkout() {
        int week = 1000 * 60 * 60 * 24 * 7;
        Cursor c = db.rawQuery("SELECT Count(*) from " + UserDB.TB_NAME_2 + " where " + UserDB.colDate + " > " + (SystemClock.uptimeMillis() - week), null);
        if(c!= null) {
            c.moveToFirst();
        }
        assert c != null;
        int val = c.getInt(0);
        c.close();
        return val;
    }

    public int getTotalNumWorkout() {
        Cursor c = db.rawQuery("SELECT COUNT(*) FROM workouts", null);
        if(c != null) {
            c.moveToFirst();
        }
        assert c!= null;
        int val = c.getInt(0);
        c.close();
        return val;
    }

    public List<UserData> getAllData() {
        List<UserData> allUD = new ArrayList<>();
        Cursor c = db.query(UserDB.TB_NAME_2, allDataCol, null, null, null, null, null);
        if(c.getCount() > 0) {
            while(c.moveToNext()) {
                UserData udAdd = new UserData();
                udAdd.setwDist(c.getFloat(c.getColumnIndex(UserDB.colDist)));
                udAdd.setwTime(c.getFloat(c.getColumnIndex(UserDB.colTime)));
                udAdd.setwCal(c.getFloat(c.getColumnIndex(UserDB.colCalories)));
                udAdd.setwDate(c.getLong(c.getColumnIndex(UserDB.colDate)));
                allUD.add(udAdd);
            }
        }
        c.close();
        return allUD;
    }

}
