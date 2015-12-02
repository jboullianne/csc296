/*
Jean-Marc Boullianne
CSC 296: Fall 2015
Project 02
 */

package project02.csc296.thesocialnetwork.database;

/**
 * Created by JeanMarc on 11/1/15.
 */
public class SocNetDbSchema {

    public static final int VERSION = 1;
    public static final String DATABASE_NAME = "social_network_database_users.db";

    public static final class UserTable {
        public static final String NAME = "users";

        public static final class Cols {
            public static final String ID = "id";
            public static final String USERNAME = "username";
            public static final String PASSWORD = "password";
            public static final String FULLNAME = "fullname";
            public static final String BIRTHDATE = "birthdate";
            public static final String HOMETOWN = "hometown";
            public static final String BIO = "bio";
            public static final String FAVORITES = "favorites";
            public static final String PROFILE_PHOTO = "profile_photo";

        }
    }

}
