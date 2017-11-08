package com.myapp.game.wifichat;

import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import Model.MessageModel;

/**
 * Created by Shailendra on 2/12/2017.
 */
public class Server extends Thread {
    private ArrayList<Socket> sockets=new ArrayList<>();
    public int port;
    ServerSocket serverSocket;
    Socket connection;
    ObjectOutputStream outputStream;
    ObjectInputStream inputStream;
    String message;

    public Server() {

    }

    @Override
    public void run() {
        Global.SERVER = this;

        startServer();


    }


    public void startServer() {

        try {

            serverSocket = new ServerSocket(10001);


            while (true)
            {
                waitingConnection();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.v("adsl", "exception");

        }

    }


    private void waitingConnection() {

        Log.v("adsl", "waiting for connection");

        try {
            connection = serverSocket.accept();
            Log.v("adsl", "connected to " + connection.getInetAddress().getHostName());
            ServerOrClient.sor=true; ////////////////////////////////////////////////////////////////////////
            sockets.add(connection);
Log.v("adsl","Starting thread");
            new MessageWaitThread(connection).start();
            Log.v("adsl","Starting thread portion 2");

        } catch (IOException e) {
            e.printStackTrace();
            Log.v("adsl","exception my");


        }



    }







    private int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void sendOut(final String message) {
        Global.SERVER = this;


        Thread t= new Thread(new Runnable() {
            @Override
            public void run() {
                try {


                    MessageModel model = new MessageModel();
                    model.setMessage(message);



                    for(Socket socket: sockets) {


                       outputStream = new ObjectOutputStream(connection.getOutputStream());


                      Socket connections=socket;

                      outputStream = new ObjectOutputStream(connections.getOutputStream());

                       outputStream.writeObject(model);
                 }
                    Log.v("adsl hehehe sent is","sent message"+message);



                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        t.start();


    }


    public void closeConnection() {

        try {


            outputStream.close();
            inputStream.close();
            connection.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  class MessageWaitThread extends Thread{
        private Socket socket;

        public MessageWaitThread(Socket socket){
            this.socket = socket;
        }

        @Override
        public void run() {
            ObjectInputStream objectInputStream = null;
            try {
                Log.v("adsl","server waiting for message");

                objectInputStream=new ObjectInputStream(socket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
                Log.v("adsl","exception my2");

            }
            while(true){
                MessageModel input;
                try {

                    input= (MessageModel) objectInputStream.readObject(); //wait here
                    //processhere




                   // if(!input.getMessage().trim().toLowerCase().matches("end"))

                   // {
                        ChatActivity chatActivity = (ChatActivity) Global.CHAT_ACTIVITY;
                        chatActivity.updateSendingMessage(input.getMessage(), false);

                    //}else{closeConnection();   }

                    Log.v("adsl server aftupdate", input.getMessage());

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    Log.v("adsl","exception my 3");

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.v("adsl","exception my 4");

                }
            }
        }
    }
}
