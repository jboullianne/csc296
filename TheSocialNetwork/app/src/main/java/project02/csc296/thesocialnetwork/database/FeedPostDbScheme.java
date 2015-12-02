/*
Jean-Marc Boullianne
CSC 296: Fall 2015
Project 02
 */

package project02.csc296.thesocialnetwork.database;

/**
 * Created by JeanMarc on 11/2/15.
 */
public class FeedPostDbScheme {

    public static final int VERSION = 1;
    public static final String DATABASE_NAME = "social_network_database_feed_post.db";

    public static final class FeedPostTable {
        public static final String NAME = "feedpost";

        public static final class Cols {
            public static final String ID = "id";
            public static final String USER = "user";
            public static final String USER_PICTURE = "user_picture";
            public static final String CONTENT = "content";
            public static final String PICTURE = "picture";
            public static final String TIMESTAMP = "timestamp";
        }
    }
}
