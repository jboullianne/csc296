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

import project02.csc296.thesocialnetwork.database.SocNetDbSchema;
import project02.csc296.thesocialnetwork.database.UserCursorWrapper;
import project02.csc296.thesocialnetwork.database.UsersDatabaseHelper;
import project02.csc296.thesocialnetwork.database.Utilities;

/**
 * Created by JeanMarc on 11/1/15.
 */
public class UserCollection {

    private static final String TAG = "USER_COLLECTION";

    private static UserCollection sCollection;
    private final Context mContext;
    private final SQLiteDatabase mDatabase;
    private final List<User> mUsers;
    private final Map<String, User> mUsersIDMap; // id, User Pair.
    private final Map<String, User> mUsersUsernameMap; // id, User Pair.

    public static User mCurrentUser;

    private UserCollection(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new UsersDatabaseHelper(mContext).getWritableDatabase();
        mUsers = new LinkedList<>();
        mUsersIDMap = new HashMap<>();
        mUsersUsernameMap = new HashMap<>();
        getUsers();
    }

    //Returns instance of UserCollection
    public static synchronized UserCollection get(Context context) {
        if (sCollection == null) {
            sCollection = new UserCollection(context);
        }
        return sCollection;
    }

    //Gets the Full User List
    public List<User> getUsers() {
        mUsers.clear();
        mUsersIDMap.clear();
        mUsersUsernameMap.clear();
        UserCursorWrapper wrapper = queryUsers(null, null);

        try {
            wrapper.moveToFirst();
            while (wrapper.isAfterLast() == false) {
                User user = wrapper.getUser();
                mUsers.add(user);
                mUsersIDMap.put(user.getId(), user);
                mUsersUsernameMap.put(user.getUsername(), user);
                wrapper.moveToNext();
            }
        } finally {
            wrapper.close();
        }

        for (int i=0; i< mUsers.size(); i++){
            Log.d(TAG, String.valueOf(mUsers.get(i).getUsername()));
            Log.d(TAG, String.valueOf(mUsers.get(i).getPassword()));
        }

        return mUsers;
    }

    //Inserts a new User into the database
    public void addUser(User user) {
        ContentValues values = getContentValues(user);
        mDatabase.insert(SocNetDbSchema.UserTable.NAME, null, values);
    }

    public void updateUser(User user) {
        String id = user.getId();
        ContentValues values = getContentValues(user);
        Log.d(TAG, user.getFavorites().toString());
        int rowsUpdated = mDatabase.update(SocNetDbSchema.UserTable.NAME,
                values,
                SocNetDbSchema.UserTable.Cols.ID + "=?",
                new String[]{id});
        Log.d(TAG, "ROWS UPDATED: " + String.valueOf(rowsUpdated));
    }

    public User getUser(String id) {
        return mUsersIDMap.get(id);
    }

    public User getUserByUsername(String username) {
        return mUsersUsernameMap.get(username);
    }


    //Translates user into ContentValues so it can be stored in the database
    private static ContentValues getContentValues(User user) {
        ContentValues values = new ContentValues();

        values.put(SocNetDbSchema.UserTable.Cols.ID, user.getId());
        values.put(SocNetDbSchema.UserTable.Cols.USERNAME, user.getUsername());
        values.put(SocNetDbSchema.UserTable.Cols.PASSWORD, user.getPassword());
        values.put(SocNetDbSchema.UserTable.Cols.FULLNAME, user.getFullName());
        values.put(SocNetDbSchema.UserTable.Cols.HOMETOWN, user.getHomeTown());
        values.put(SocNetDbSchema.UserTable.Cols.BIRTHDATE, user.getBirthDate());
        values.put(SocNetDbSchema.UserTable.Cols.BIO, user.getBio());
        values.put(SocNetDbSchema.UserTable.Cols.FAVORITES, Utilities.serialize(user.getFavorites()));
        values.put(SocNetDbSchema.UserTable.Cols.PROFILE_PHOTO, user.getProfilePhoto());

        return values;
    }

    //Gets References to certain Users in the database
    private UserCursorWrapper queryUsers(String where, String[] args) {
        Cursor cursor = mDatabase.query(SocNetDbSchema.UserTable.NAME, null, where, args, null, null, null);
        return new UserCursorWrapper(cursor);
    }

    //Searchs Username map to see if such user exists. If it does, it checks to see if passwords match
    public boolean requestLogin(String username, String password){
        if(mUsersUsernameMap.get(username) != null){
            Log.d(TAG, "Username matchs a user in database");
            Log.d(TAG, mUsersUsernameMap.get(username).getPassword());
            if(mUsersUsernameMap.get(username).getPassword().equals(password)){
                Log.d(TAG, "User provided valid credentials");
                return true;
            }
            Log.d(TAG, "But username doesn't match:" +password+":"+mUsersUsernameMap.get(username).getPassword());
        }
        return false;
    }


    //Returns an User's Favorites in List Form
    public List<User> getFavorites(User user){
        List<User> favorites = new LinkedList<>();
        for(int i=0; i<user.getFavorites().size(); i++){
            favorites.add(getUser(user.getFavorites().get(i)));
        }
        return favorites;
    }

    public static User getCurrentUser(){
        return mCurrentUser;
    }

    public static void setCurrentUser(User user){
        mCurrentUser = user;
    }

}