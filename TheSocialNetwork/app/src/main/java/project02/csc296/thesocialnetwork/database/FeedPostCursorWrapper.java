/*
Jean-Marc Boullianne
CSC 296: Fall 2015
Project 02
 */
package project02.csc296.thesocialnetwork.database;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.net.Uri;
import java.util.UUID;

import project02.csc296.thesocialnetwork.model.FeedPost;
import project02.csc296.thesocialnetwork.model.User;

/**
 * Created by JeanMarc on 11/2/15.
 */
public class FeedPostCursorWrapper extends CursorWrapper {

    public FeedPostCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public FeedPost getFeedPost() {
        String id =  getString(getColumnIndex(FeedPostDbScheme.FeedPostTable.Cols.ID));
        User author = (User) Utilities.deserialize(getBlob(getColumnIndex(FeedPostDbScheme.FeedPostTable.Cols.USER)));
        String authorPicture = getString(getColumnIndex(FeedPostDbScheme.FeedPostTable.Cols.USER_PICTURE));
        String content = getString(getColumnIndex(FeedPostDbScheme.FeedPostTable.Cols.CONTENT));
        String picture = getString(getColumnIndex(FeedPostDbScheme.FeedPostTable.Cols.PICTURE));
        String timeStamp = getString(getColumnIndex(FeedPostDbScheme.FeedPostTable.Cols.TIMESTAMP));
        FeedPost feedPost = new FeedPost(id, author,authorPicture, content, picture, timeStamp);
        return feedPost;
    }
}
