package Model;

public  class DisplayModel  {

    String userName,lastMessage,lastTime;
    int primary_key;


  public  DisplayModel()
    {

    }

    public DisplayModel(String userName, String lastMessage, String lastTime) {
        this.userName = userName;
        this.lastMessage = lastMessage;
        this.lastTime = lastTime;
    }



    public int getPrimary_key(){return  primary_key;}
    public void setPrimary_key(int primary_key){this.primary_key=primary_key;}
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }
}
