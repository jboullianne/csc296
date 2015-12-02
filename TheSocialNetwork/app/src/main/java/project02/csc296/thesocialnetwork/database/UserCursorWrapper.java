/*
Jean-Marc Boullianne
CSC 296: Fall 2015
Project 02
 */

package project02.csc296.thesocialnetwork.database;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.net.Uri;
import android.util.Log;

import java.util.LinkedList;
import java.util.UUID;

import project02.csc296.thesocialnetwork.model.User;

/**
 * Created by JeanMarc on 11/1/15.
 */
public class UserCursorWrapper extends CursorWrapper {

    public UserCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public User getUser() {
        String id =  getString(getColumnIndex(SocNetDbSchema.UserTable.Cols.ID));
        String username = getString(getColumnIndex(SocNetDbSchema.UserTable.Cols.USERNAME));
        String password = getString(getColumnIndex(SocNetDbSchema.UserTable.Cols.PASSWORD));
        String fullName = getString(getColumnIndex(SocNetDbSchema.UserTable.Cols.FULLNAME));
        String homeTown = getString(getColumnIndex(SocNetDbSchema.UserTable.Cols.HOMETOWN));
        String birthDate = getString(getColumnIndex(SocNetDbSchema.UserTable.Cols.BIRTHDATE));
        String bio = getString(getColumnIndex(SocNetDbSchema.UserTable.Cols.BIO));
        LinkedList<String> favorites = (LinkedList<String>) Utilities.deserialize(getBlob(getColumnIndex(SocNetDbSchema.UserTable.Cols.FAVORITES)));
        String profilePhoto = getString(getColumnIndex(SocNetDbSchema.UserTable.Cols.PROFILE_PHOTO));

        User user = new User(id, username, password, fullName, birthDate, homeTown, bio, favorites, profilePhoto);
        return user;
    }
}
