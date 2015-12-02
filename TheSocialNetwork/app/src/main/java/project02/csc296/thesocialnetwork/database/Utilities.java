/*
Jean-Marc Boullianne
CSC 296: Fall 2015
Project 02
 */

package project02.csc296.thesocialnetwork.database;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by JeanMarc on 11/1/15.
 */
public class Utilities {

    public static final String KEY_WHERE = "csc296.project02.WHERE";

    //used to serialize objects so they can be stored in the database
    public static byte[] serialize(Object obj) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(out);
            os.writeObject(obj);
            os.close();
            return out.toByteArray();
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    //used to deserialize objects from the database so they can be used
    public static Object deserialize(byte[] data){
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(data);
            ObjectInputStream is = new ObjectInputStream(in);
            return is.readObject();
        }catch (IOException e){
            e.printStackTrace();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
        return null;
    }
}
