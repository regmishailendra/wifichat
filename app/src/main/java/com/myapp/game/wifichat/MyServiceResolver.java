package com.myapp.game.wifichat;

import android.app.Activity;
import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;
import android.widget.Toast;

import java.net.InetAddress;

import CustomClass.HistoryAdapter;
import Database.DataHandler;
import Model.DisplayModel;


public class MyServiceResolver implements NsdManager.ResolveListener {

    private  Context context;
    InetAddress host;
    int port;
    String name;
    Client client;
    DataHandler dataHandler;


    public MyServiceResolver(int port, InetAddress host, String name, Context context) {
        this.host = host;
        this.port = port;
        this.name = name;
        this.context=context;

    }

    @Override
    public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {

    }

    @Override
    public void onServiceResolved(NsdServiceInfo serviceInfo) {
        Log.v("adsl", "resolved service " + serviceInfo.getServiceName());
        port = serviceInfo.getPort();
        host = serviceInfo.getHost();
        name = serviceInfo.getServiceName();
        Log.v("adsl ", "later" + port + "");
//                              Toast.makeText(getActivity().getApplicationContext(),"Connection to "+connectedName+" created.",Toast.LENGTH_LONG).show();

        client = new Client();
        client.setPort(port);
        client.setHost(host);
        Toast.makeText(context,"Connected to "+serviceInfo.getServiceName(),Toast.LENGTH_LONG).show();




        client.start();
        Global.CLIENT=client;
      dataHandler= new DataHandler(context);


        dataHandler.addGroupName(serviceInfo.getServiceName());




        MainActivity mainActivity= Global.MAINACTIVITY;

        DisplayModel displayModel=new DisplayModel();
        displayModel.setUserName(serviceInfo.getServiceName());
        displayModel.setLastMessage("No last message");
        displayModel.setLastTime("00:00");
        mainActivity.refreshListViews(displayModel);   ////////////////////////////////////////////why is not this workinf



    }


}
