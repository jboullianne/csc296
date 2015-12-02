/*
Jean-Marc Boullianne
CSC 296: Fall 2015
Project 02
 */

package project02.csc296.thesocialnetwork.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by JeanMarc on 11/1/15.
 */
public class UsersDatabaseHelper extends SQLiteOpenHelper {

    public UsersDatabaseHelper(Context context) {
        super(context, SocNetDbSchema.DATABASE_NAME, null, SocNetDbSchema.VERSION );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + SocNetDbSchema.UserTable.NAME
                + "(_id integer primary key autoincrement, "
                + SocNetDbSchema.UserTable.Cols.ID + ", "
                + SocNetDbSchema.UserTable.Cols.USERNAME + ", "
                + SocNetDbSchema.UserTable.Cols.PASSWORD + ", "
                + SocNetDbSchema.UserTable.Cols.FULLNAME + ", "
                + SocNetDbSchema.UserTable.Cols.HOMETOWN + ", "
                + SocNetDbSchema.UserTable.Cols.BIRTHDATE + ", "
                + SocNetDbSchema.UserTable.Cols.BIO + ", "
                + SocNetDbSchema.UserTable.Cols.FAVORITES + ", "
                + SocNetDbSchema.UserTable.Cols.PROFILE_PHOTO + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
