/*
Jean-Marc Boullianne
CSC 296: Assignment08
Fall 2015
 */

package csc296.assignment08.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import csc296.assignment08.database.VideoGameCursorWrapper;
import csc296.assignment08.database.VideoGameDbSchema;
import csc296.assignment08.database.VideoGamesDatabaseHelper;

/**
 * Created by JeanMarc on 10/31/15.
 */
public class Collection {

    private static final String TAG = "COLLECTION";

    private static Collection sCollection;

    private final Context mContext;
    private final SQLiteDatabase mDatabase;
    private final List<VideoGame> mVideoGames;
    private final Map<UUID, VideoGame> mVideoGamesMap;

    private Collection(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new VideoGamesDatabaseHelper(mContext).getWritableDatabase();
        mVideoGames = new LinkedList<>();
        mVideoGamesMap = new HashMap<>();
    }

    //Returns instance of Collection
    public static synchronized Collection get(Context context) {
        if (sCollection == null) {
            sCollection = new Collection(context);
        }
        return sCollection;
    }

    //Gets the Full Model List
    public List<VideoGame> getVideoGames() {
        mVideoGames.clear();
        mVideoGamesMap.clear();
        VideoGameCursorWrapper wrapper = queryVideoGames(null, null);

        try {
            wrapper.moveToFirst();
            while (wrapper.isAfterLast() == false) {
                VideoGame videoGame = wrapper.getVideoGame();
                mVideoGames.add(videoGame);
                mVideoGamesMap.put(videoGame.getId(), videoGame);
                wrapper.moveToNext();
            }
        } finally {
            wrapper.close();
        }

        for (int i=0; i<mVideoGames.size(); i++){
            Log.d(TAG, mVideoGames.get(i).toString());
        }

        return mVideoGames;
    }

    //Inserts a new video game into the database
    public void addVideoGame(VideoGame videoGame) {
        ContentValues values = getContentValues(videoGame);
        mDatabase.insert(VideoGameDbSchema.VideoGameTable.NAME, null, values);
    }

    public void updateVideoGame(VideoGame videoGame) {
        String id = videoGame.getId().toString();
        ContentValues values = getContentValues(videoGame);
        mDatabase.update(VideoGameDbSchema.VideoGameTable.NAME,
                values,
                VideoGameDbSchema.VideoGameTable.Cols.ID + "=?",
                new String[]{id});
    }

    public VideoGame getVideoGame(UUID id) {
        return mVideoGamesMap.get(id);
    }

    //Makes contentValues for inserting into the database
    private static ContentValues getContentValues(VideoGame videoGame) {
        ContentValues values = new ContentValues();

        values.put(VideoGameDbSchema.VideoGameTable.Cols.ID, videoGame.getId().toString());
        values.put(VideoGameDbSchema.VideoGameTable.Cols.TITLE, videoGame.getTitle());
        values.put(VideoGameDbSchema.VideoGameTable.Cols.PUBLISHER, videoGame.getPublisher());
        values.put(VideoGameDbSchema.VideoGameTable.Cols.YEAR, videoGame.getYear());

        return values;
    }

    //Gets References to all Video Games in the Database
    private VideoGameCursorWrapper queryVideoGames(String where, String[] args) {
        Cursor cursor = mDatabase.query(
                VideoGameDbSchema.VideoGameTable.NAME, // table name
                null,                          // which columns; null for all
                where,                         // where clause, e.g. id=?
                args,                          // where arguments
                null,                          // group by
                null,                          // having
                null                           // order by
        );

        return new VideoGameCursorWrapper(cursor);
    }

}
