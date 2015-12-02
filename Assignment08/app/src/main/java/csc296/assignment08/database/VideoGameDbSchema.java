/*
Jean-Marc Boullianne
CSC 296: Assignment08
Fall 2015
 */

package csc296.assignment08.database;

/**
 * Created by JeanMarc on 10/31/15.
 */
public class VideoGameDbSchema {

    public static final int VERSION = 1;
    public static final String DATABASE_NAME = "video_games_database.db";

    public static final class VideoGameTable {
        public static final String NAME = "video_games";

        public static final class Cols {
            public static final String ID = "id";
            public static final String TITLE = "title";
            public static final String PUBLISHER = "publisher";
            public static final String YEAR = "year";
        }
    }
}
