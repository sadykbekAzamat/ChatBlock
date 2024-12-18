package kz.beta.chatblock;

import java.util.List;

public class User {
    private String userID;
    private int id;

    public User() {
        // Default constructor required for Firebase
    }

    public User(String userID, int id) {
        this.userID = userID;
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
