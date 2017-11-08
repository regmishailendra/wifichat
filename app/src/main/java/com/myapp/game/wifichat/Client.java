package com.myapp.game.wifichat;

import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.Connection;

import Model.MessageModel;


public class Client extends Thread {

    int port;
    InetAddress host;
    Socket connection;
    static ObjectOutputStream output;
    static ObjectInputStream input;

    String message;


    @Override
    public void run() {
        Global.CLIENT = this;

        startClient();

    }

    private void startClient() {


        serverConnect();

    }

    private void serverConnect() {
        try {
            // connection=new Socket(getHost(),getPort());   // use this later

            connection = new Socket(getHost(), 10001);
            ServerOrClient.sor=false; ////////////////////////////////////////////////////////////////////////////
            Log.v("adsl client", "client is connected");
            new MessageWaitThread(connection).start();

        } catch (IOException e) {
            e.printStackTrace();
            Log.v("adsl", "exp 1");

        }


    }


    public static class MessageWaitThread extends Thread {
        private Socket socket;

        public MessageWaitThread(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {


            Log.v("adsl", "client 2nd thread started");
            ObjectInputStream objectInputStream = null;
            try {
                objectInputStream = new ObjectInputStream(socket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

            while (true) {
                MessageModel input;
                try {
                    input = (MessageModel) objectInputStream.readObject(); //wait here
                    //processhere

                    ChatActivity chatActivity = (ChatActivity) Global.CHAT_ACTIVITY;
                    chatActivity.updateSendingMessage(input.getMessage(), false);

                    Log.v("adsl hehehe received is", input.getMessage());


                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    public void closeConnection()
    {

        try {


            output.close();
        input.close();
            connection.close();




        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public void sendOut(final String message) {
        Global.CLIENT = this;


       Thread t= new Thread(new Runnable() {
           @Override
           public void run() {
               try {


                   MessageModel model = new MessageModel();
                   model.setMessage(message);


                //   Socket connection = new Socket(host,port);
                   output =     new ObjectOutputStream(connection.getOutputStream());

                   output.writeObject(model);
                   Log.v("adsl hehehe sent is","sent message"+message);



               } catch (IOException e) {
                   e.printStackTrace();
               }

           }
       });
       t.start();

    }


    private int getPort() {


        return this.port;
    }

    public void setPort(int port) {
        this.port = port;


    }

    public void setHost(InetAddress connectedHost) {
        host = connectedHost;
    }


    public InetAddress getHost() {
        return host;


    }
}
