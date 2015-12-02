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
 * Created by JeanMarc on 11/2/15.
 */
public class FeedPostDatabaseHelper extends SQLiteOpenHelper {

    public FeedPostDatabaseHelper(Context context) {
        super(context, FeedPostDbScheme.DATABASE_NAME, null, FeedPostDbScheme.VERSION );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + FeedPostDbScheme.FeedPostTable.NAME
                + "(_id integer primary key autoincrement, "
                + FeedPostDbScheme.FeedPostTable.Cols.ID + ", "
                + FeedPostDbScheme.FeedPostTable.Cols.USER + ", "
                + FeedPostDbScheme.FeedPostTable.Cols.USER_PICTURE + ", "
                + FeedPostDbScheme.FeedPostTable.Cols.CONTENT + ", "
                + FeedPostDbScheme.FeedPostTable.Cols.PICTURE + ", "
                + FeedPostDbScheme.FeedPostTable.Cols.TIMESTAMP + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Never Used in this project
    }
}

