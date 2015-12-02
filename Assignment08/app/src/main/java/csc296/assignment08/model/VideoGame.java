/*
Jean-Marc Boullianne
CSC 296: Assignment08
Fall 2015
 */

package csc296.assignment08.model;

import java.util.UUID;

/**
 * Created by JeanMarc on 10/31/15.
 */
public class VideoGame {

    private String title;
    private String publisher;
    private int year;
    private UUID id;

    public VideoGame(String title, String publisher, int year){
        this.title = title;
        this.publisher = publisher;
        this.year = year;
        this.id = UUID.randomUUID();
    }

    public String getTitle() {
        return title;
    }

    public String getPublisher() {
        return publisher;
    }

    public int getYear() {
        return year;
    }

    public UUID getId() {
        return id;
    }
}
