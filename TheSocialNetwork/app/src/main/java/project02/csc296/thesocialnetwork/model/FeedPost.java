/*
Jean-Marc Boullianne
CSC 296: Fall 2015
Project 02
 */

package project02.csc296.thesocialnetwork.model;

import java.util.Calendar;
import java.util.UUID;

/**
 * Created by JeanMarc on 11/2/15.
 */
public class FeedPost {

    private User author;
    private String authorPicture;
    private String content;
    private String picture;
    private String id;
    private String timeStamp;

    public FeedPost(String id, User author,String authorPicture, String content, String picture, String timeStamp){
        this.author = author;
        this.authorPicture = authorPicture;
        this.content = content;
        this.picture = picture;
        if(id == null)
            this.id = UUID.randomUUID().toString();
        else
            this.id = id;
        if(timeStamp == null){
            Calendar current = Calendar.getInstance();
            this.timeStamp = current.getTime().toString();
        }else{
            this.timeStamp = timeStamp;
        }


    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public void setAuthorPicture(String authorPicture){
        this.authorPicture = authorPicture;
    }

    public String getAuthorPicture(){
        return this.authorPicture;
    }

    public String getContent() {
        return content;
    }

    public String getId(){
        return this.id;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
