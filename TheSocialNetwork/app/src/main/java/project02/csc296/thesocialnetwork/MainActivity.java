/*
Jean-Marc Boullianne
CSC 296: Fall 2015
Project 02
 */

package project02.csc296.thesocialnetwork;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import project02.csc296.thesocialnetwork.database.Utilities;
import project02.csc296.thesocialnetwork.model.User;
import project02.csc296.thesocialnetwork.model.UserCollection;

public class MainActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener, SignUpFragment.OnFragmentInteractionListener, HomeFragment.OnFragmentInteractionListener, WritePostFragment.OnChildFragmentInteractionListener{


    private static final String TAG = "MAIN_ACTIVITY";
    public static final int FRAGMENT_LOGIN = 0;
    public static final int FRAGMENT_SIGN_UP = 1;
    public static final int FRAGMENT_HOME = 2;

    private static final int MS_LOGIN = 0;
    private static final int MS_SIGNUP = 1;
    public static final int MS_HOME = 2;
    public static final int MS_USER_HOME = 3;
    private static final int MS_FAVORITES_FEED = 4;
    private static final int MS_ALL_USERS = 5;
    private static int sMENU_STATE = MS_LOGIN;


    private MenuItem mHome;
    private MenuItem mFavoritesFeed;
    private MenuItem mAllUsers;
    private MenuItem mLogout;


    public static int sSTATE = FRAGMENT_LOGIN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //First Startup of App
        if(savedInstanceState == null) {
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.current_fragment_frame, new LoginFragment(), null).commit();
            sSTATE = FRAGMENT_LOGIN;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mHome = menu.findItem(R.id.menu_home);
        mFavoritesFeed =  menu.findItem(R.id.menu_favorites_feed);
        mAllUsers =  menu.findItem(R.id.menu_all_users);
        mLogout =  menu.findItem(R.id.menu_logout);
        setMenuState(sMENU_STATE);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        FragmentManager manager = getSupportFragmentManager();
        Bundle arguments = new Bundle();
        arguments.putSerializable(SignUpFragment.KEY_USER, UserCollection.getCurrentUser());

        //Starts new fragment exchange, depending on which menu item was clicked
        switch(id){
            case R.id.menu_home:
                sSTATE = FRAGMENT_HOME;
                onFragmentInteraction(FRAGMENT_HOME, arguments);
                getSupportActionBar().setTitle(R.string.title_home);
                setMenuState(MS_HOME);
                break;
            case R.id.menu_favorites_feed:
                setMenuState(MS_FAVORITES_FEED);
                FeedRecyclerFragment favoritesRecyclerFragment = new FeedRecyclerFragment();
                arguments.putString(Utilities.KEY_WHERE, "favorites_feed");
                favoritesRecyclerFragment.setArguments(arguments);
                manager.beginTransaction().setCustomAnimations(R.anim.fragment_move_right_in, R.anim.fragment_move_left_out)
                        .replace(R.id.current_fragment_frame, favoritesRecyclerFragment, "Favorites Feed")
                        .addToBackStack("Favorites Feed")
                        .commit();
                getSupportActionBar().setTitle(R.string.title_favorites_feed);
                break;
            case R.id.menu_all_users:
                setMenuState(MS_ALL_USERS);
                FeedRecyclerFragment allUsersdRecyclerFragment = new FeedRecyclerFragment();
                arguments.putString(Utilities.KEY_WHERE, "all_users");
                allUsersdRecyclerFragment.setArguments(arguments);
                manager.beginTransaction().setCustomAnimations(R.anim.fragment_move_right_in, R.anim.fragment_move_left_out)
                        .replace(R.id.current_fragment_frame, allUsersdRecyclerFragment, "All Users")
                        .addToBackStack("All Users")
                        .commit();

                break;
            case R.id.menu_logout:
                sSTATE = FRAGMENT_LOGIN;
                onFragmentInteraction(FRAGMENT_LOGIN, null);
                UserCollection.setCurrentUser(null);
                setMenuState(MS_LOGIN);
                getSupportActionBar().setTitle(R.string.app_name);
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    public void onFragmentInteraction(int fragment, Bundle args){

        //Starts new fragment exchange based on fragment interaction
        FragmentManager manager = getSupportFragmentManager();
        switch (fragment){
            case FRAGMENT_LOGIN:
                while(manager.popBackStackImmediate()){
                    Log.d(TAG, "popped fragment from bckstack");
                }
                manager.beginTransaction().setCustomAnimations(R.anim.fragment_move_right_in, R.anim.fragment_move_left_out)
                        .replace(R.id.current_fragment_frame, new LoginFragment(), "Login")
                        .commit();

                sSTATE = FRAGMENT_LOGIN;
                setMenuState(MS_LOGIN);
                getSupportActionBar().setTitle(R.string.app_name);

                break;
            case FRAGMENT_SIGN_UP:
                manager.beginTransaction().setCustomAnimations(R.anim.fragment_move_left_in, R.anim.fragment_move_right_out)
                        .replace(R.id.current_fragment_frame, new SignUpFragment(), "Sign Up")
                        .commit();
                sSTATE = FRAGMENT_SIGN_UP;
                setMenuState(MS_SIGNUP);
                getSupportActionBar().setTitle(R.string.title_sign_up);
                break;
            case FRAGMENT_HOME:
                HomeFragment homeFragment = new HomeFragment();
                homeFragment.setArguments(args);
                manager.beginTransaction().setCustomAnimations(R.anim.fragment_move_right_in, R.anim.fragment_move_left_out)
                        .replace(R.id.current_fragment_frame, homeFragment, "Home")
                        .addToBackStack("User Page")
                        .commit();
                sSTATE = FRAGMENT_HOME;
                User tmp = (User) args.getSerializable(SignUpFragment.KEY_USER);
                if(tmp.getUsername().equals(UserCollection.getCurrentUser())){
                    setMenuState(MS_HOME);
                }else{
                    setMenuState(MS_USER_HOME);
                }
                getSupportActionBar().setTitle(R.string.title_home);
                break;
        }
    }

    public void onChildFragmentInteraction(int fragment, Bundle args){
        Log.d(TAG, "Fragment: " + fragment);
        HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("Home");
        homeFragment.onChildFragmentInteraction(fragment, args);
    }

    //Processes Camera Photo Results
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == WritePostFragment.RC_ATTACH_PHOTO && resultCode == RESULT_OK){
            HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("Home");
            homeFragment.writePostResult(requestCode,resultCode,data);
        }else if (requestCode == SignUpFragment.RC_NEW_PROFILE_PHOTO && resultCode == RESULT_OK) {
            Log.d(TAG, "Sign Up Returned Photo");
            SignUpFragment signUpFragment = (SignUpFragment) getSupportFragmentManager().findFragmentByTag("Sign Up");
            signUpFragment.onActivityResult(requestCode,resultCode, data);
        }else if(requestCode == HomeFragment.RC_CHANGE_PROFILE_PHOTO && resultCode == RESULT_OK){
            Log.d(TAG, "Changing Profile Photo");
            HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag(("Home"));
            homeFragment.onActivityResult(requestCode,resultCode,data);
        }

    }

    //Sets Action Bar and Menu Items across the Activity
    public void setMenuState(int state){

        switch (state){
            case MS_LOGIN:
                mHome.setVisible(false);
                mFavoritesFeed.setVisible(false);
                mAllUsers.setVisible(false);
                mLogout.setVisible(false);
                sMENU_STATE = MS_LOGIN;
                getSupportActionBar().setTitle(R.string.app_name);
                break;
            case MS_SIGNUP:
                mHome.setVisible(false);
                mFavoritesFeed.setVisible(false);
                mAllUsers.setVisible(false);
                mLogout.setVisible(false);
                sMENU_STATE = MS_SIGNUP;
                getSupportActionBar().setTitle(R.string.title_sign_up);
                break;
            case MS_HOME:
                mHome.setVisible(false);
                mFavoritesFeed.setVisible(true);
                mAllUsers.setVisible(true);
                mLogout.setVisible(true);
                sMENU_STATE = MS_HOME;
                getSupportActionBar().setTitle(R.string.title_home);
                break;
            case MS_FAVORITES_FEED:
                mHome.setVisible(true);
                mFavoritesFeed.setVisible(false);
                mAllUsers.setVisible(true);
                mLogout.setVisible(true);
                sMENU_STATE = MS_FAVORITES_FEED;
                getSupportActionBar().setTitle(R.string.title_favorites_feed);
                break;
            case MS_ALL_USERS:
                mHome.setVisible(true);
                mFavoritesFeed.setVisible(true);
                mAllUsers.setVisible(false);
                mLogout.setVisible(true);
                sMENU_STATE = MS_ALL_USERS;
                getSupportActionBar().setTitle(R.string.title_all_users);
                break;
            case MS_USER_HOME:
                mHome.setVisible(true);
                mFavoritesFeed.setVisible(true);
                mAllUsers.setVisible(true);
                mLogout.setVisible(true);
                sMENU_STATE = MS_USER_HOME;
                break;
        }

    }

}
