/*
Jean-Marc Boullianne
CSC 296: Assignment08
Fall 2015
 */

package csc296.assignment08.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by JeanMarc on 10/31/15.
 */
public class VideoGamesDatabaseHelper extends SQLiteOpenHelper {

    public VideoGamesDatabaseHelper(Context context) {
        super(context, VideoGameDbSchema.DATABASE_NAME, null, VideoGameDbSchema.VERSION );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + VideoGameDbSchema.VideoGameTable.NAME
                + "(_id integer primary key autoincrement, "
                + VideoGameDbSchema.VideoGameTable.Cols.ID + ", "
                + VideoGameDbSchema.VideoGameTable.Cols.TITLE + ", "
                + VideoGameDbSchema.VideoGameTable.Cols.PUBLISHER + ", "
                + VideoGameDbSchema.VideoGameTable.Cols.YEAR + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}