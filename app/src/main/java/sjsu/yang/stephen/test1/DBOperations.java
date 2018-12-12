package sjsu.yang.stephen.test1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

}
