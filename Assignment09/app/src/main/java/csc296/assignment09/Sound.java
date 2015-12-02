package csc296.assignment09;

/**
 * Created by JeanMarc on 11/19/15.
 */
public class Sound {
    private final String mPath;
    private final String mName;
    private final String mAlbum;
    private final String mArtist;
    private Integer mId;

    public Sound(String path, String name, String album, String artist) {
        mPath = path;
        mName = name;
        mAlbum = album;
        mArtist = artist;
    }

    public String getPath() {
        return mPath;
    }

    public String getName() {
        return mName;
    }

    public String getAlbum() {
        return mAlbum;
    }

    public String getArtist() {
        return mArtist;
    }

    public Integer getId() {
        return mId;
    }

    public void setId(Integer id) {
        mId = id;
    }
}
