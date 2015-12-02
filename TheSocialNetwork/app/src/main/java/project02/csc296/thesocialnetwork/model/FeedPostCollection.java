/*
Jean-Marc Boullianne
CSC 296: Fall 2015
Project 02
 */

package project02.csc296.thesocialnetwork.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import project02.csc296.thesocialnetwork.database.FeedPostCursorWrapper;
import project02.csc296.thesocialnetwork.database.FeedPostDatabaseHelper;
import project02.csc296.thesocialnetwork.database.FeedPostDbScheme;
import project02.csc296.thesocialnetwork.database.Utilities;

/**
 * Created by JeanMarc on 11/2/15.
 */
public class FeedPostCollection {

    private static final String TAG = "FEED_POST_COLLECTION";

    private static FeedPostCollection sCollection;
    private final Context mContext;
    private final SQLiteDatabase mDatabase;
    private final LinkedList<FeedPost> mFeedPosts;
    private final Map<String, FeedPost> mFeedPostsMap;

    private FeedPostCollection(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new FeedPostDatabaseHelper(mContext).getWritableDatabase();
        mFeedPosts = new LinkedList<>();
        mFeedPostsMap = new HashMap<>();
    }

    //Returns instance of Collection
    public static synchronized FeedPostCollection get(Context context) {
        if (sCollection == null) {
            sCollection = new FeedPostCollection(context);
        }
        return sCollection;
    }

    //Returns the specialized list, in accordance with the "where" string
    public LinkedList<FeedPost> getFeedPosts(String where, String[] args) {
        mFeedPosts.clear();
        mFeedPostsMap.clear();
        LinkedList<FeedPost> temp = new LinkedList<>();
        //Get all certain feed posts according to the "where" string
        FeedPostCursorWrapper wrapper = queryFeedPosts(where, args);

        try {
            wrapper.moveToFirst();
            while (!wrapper.isAfterLast()) {
                FeedPost feedPost = wrapper.getFeedPost();
                temp.add(feedPost);
                mFeedPostsMap.put(feedPost.getId(), feedPost);
                wrapper.moveToNext();
            }
        } finally {
            wrapper.close();
        }

        //I know this is inefficient. Sorry.
        for(int i=0; i<temp.size(); i++){
            mFeedPosts.add(0, temp.get(i));
        }

        return mFeedPosts;
    }

    //Inserts a new FeedPost into the database
    public void addfeedPost(FeedPost feedPost) {
        ContentValues values = getContentValues(feedPost);
        mDatabase.insert(FeedPostDbScheme.FeedPostTable.NAME, null, values);
    }


    //Translates feedPost Values so they can be inserted into the database
    private static ContentValues getContentValues(FeedPost feedPost) {
        ContentValues values = new ContentValues();

        values.put(FeedPostDbScheme.FeedPostTable.Cols.ID, feedPost.getId());
        values.put(FeedPostDbScheme.FeedPostTable.Cols.USER, Utilities.serialize(feedPost.getAuthor()));
        values.put(FeedPostDbScheme.FeedPostTable.Cols.USER_PICTURE, feedPost.getAuthorPicture());
        values.put(FeedPostDbScheme.FeedPostTable.Cols.CONTENT, feedPost.getContent());
        values.put(FeedPostDbScheme.FeedPostTable.Cols.PICTURE, feedPost.getPicture());
        values.put(FeedPostDbScheme.FeedPostTable.Cols.TIMESTAMP, feedPost.getTimeStamp());

        return values;
    }

    //Gets References to all Video Games in the Database
    private FeedPostCursorWrapper queryFeedPosts(String where, String[] args) {
        Cursor cursor = mDatabase.query(FeedPostDbScheme.FeedPostTable.NAME, null, where, args, null, null, null);
        return new FeedPostCursorWrapper(cursor);
    }

    //Gets all FeedPosts by an User's Favorites
    public List<FeedPost> getFavoritesPosts(User user){
        List<FeedPost> posts = new LinkedList<>();
        getFeedPosts(null, null);

        User current = UserCollection.getCurrentUser();

        for(int i=0; i<mFeedPosts.size(); i++){
            if(current.getFavorites().contains(mFeedPosts.get(i).getAuthor().getId())){
                posts.add(mFeedPosts.get(i));
            }
        }

        return posts;
    }

    //Used to update all Feed Posts when their author's picture has changed
    public void updateAuthorPictures(String oldPath, String newPath){

        Cursor cursor = mDatabase.rawQuery("UPDATE " + FeedPostDbScheme.FeedPostTable.NAME
                                        + " SET " + FeedPostDbScheme.FeedPostTable.Cols.USER_PICTURE + "=\"" + newPath
                                        + "\" WHERE " + FeedPostDbScheme.FeedPostTable.Cols.USER_PICTURE + "=\"" +oldPath + "\""
                , null);
        Log.d(TAG, "Rows Updated: " + String.valueOf(cursor.getCount()));

    }
}
