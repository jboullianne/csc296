/*
Jean-Marc Boullianne
CSC 296: Fall 2015
Project 02
 */

package project02.csc296.thesocialnetwork.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.UUID;

/**
 * Created by JeanMarc on 11/1/15.
 */
public class User implements Serializable{

    private String id;
    private String username;
    private String password;
    private String fullName;
    private String birthDate;
    private String homeTown;
    private String bio;
    private LinkedList<String> favorites;
    private String profilePhoto;

    public User(String id, String username, String password, String fullName, String birthDate, String homeTown, String bio, LinkedList<String> favorites, String profilePhoto){
        if(id == null)
            this.id = UUID.randomUUID().toString();
        else
            this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.homeTown = homeTown;
        this.bio = bio;
        if(favorites == null) {
            this.favorites = new LinkedList<>();
        }
        else{
            this.favorites = favorites;
        }
        if(profilePhoto == null)
            this.profilePhoto = null;
        else
            this.profilePhoto = profilePhoto;

    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFullName() {
        return fullName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getHomeTown() {
        return homeTown;
    }

    public String getBio() {
        return bio;
    }

    public LinkedList<String> getFavorites() {
        return favorites;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public void setHomeTown(String homeTown) {
        this.homeTown = homeTown;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setFavorites(LinkedList<String> favorites) {
        this.favorites = favorites;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

}
