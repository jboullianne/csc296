/*
Jean-Marc Boullianne
jboullia@u.rochester.edu
Project 03: QuizDown
 */

package csc296.quizdown.model;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class LeaderBoardManager {

    /**
     * An array of sample (dummy) items.
     */
    public static List<ParseUser> ITEMS = new ArrayList<ParseUser>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, ParseUser> ITEM_MAP = new HashMap<String, ParseUser>();

    static {
        addItem(ParseUser.getCurrentUser());
    }

    private static void addItem(ParseUser user) {
        ITEMS.add(user);
        ITEM_MAP.put(user.getObjectId(), user);
    }
}
