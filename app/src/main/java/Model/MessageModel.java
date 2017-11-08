package Model;

import java.io.Serializable;

/**
 * Created by Shailendra on 2/10/2017.
 */
public class MessageModel implements Serializable{


    public String message;
    public String time;
    public boolean isMe;
    public int primary_key;


    public int getPrimary_key(){return  primary_key;}
    public void setPrimary_key(int primary_key){this.primary_key=primary_key;}

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isMe() {
        return isMe;
    }

    public void setMe(boolean me) {
        isMe = me;
    }
}
