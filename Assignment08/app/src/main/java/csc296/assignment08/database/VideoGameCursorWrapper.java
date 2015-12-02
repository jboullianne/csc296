/*
Jean-Marc Boullianne
CSC 296: Assignment08
Fall 2015
 */

package csc296.assignment08.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import csc296.assignment08.model.VideoGame;
import csc296.assignment08.database.VideoGameDbSchema.VideoGameTable;

/**
 * Created by JeanMarc on 10/31/15.
 */
public class VideoGameCursorWrapper extends CursorWrapper {

    public VideoGameCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public VideoGame getVideoGame() {
        String title = getString(getColumnIndex(VideoGameTable.Cols.TITLE));
        String publisher = getString(getColumnIndex(VideoGameTable.Cols.PUBLISHER));
        int year = getInt(getColumnIndex(VideoGameTable.Cols.YEAR));

        VideoGame game = new VideoGame(title, publisher, year);
        return game;
    }
}
