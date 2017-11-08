package com.myapp.game.wifichat;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;


import android.widget.TextView;
import android.widget.Toast;



import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import CustomClass.CustomCreate;
import CustomClass.CustomJoin;
import CustomClass.HistoryAdapter;
import Database.DataHandler;
import Model.DisplayModel;

public class MainActivity extends AppCompatActivity {


    FloatingActionButton fab, fab2, fab3;
    Animation fabOpen, fabClose, rotateA, rotateC;
   static boolean isUnregistered=false;
    TextView createGroupText, joinGroupText;
    boolean isOpen = false;
   static public ListView historyListView;
    List<String> nameList, messageList, timeList;
    DisplayModel displayModel;
    List<DisplayModel> displayModelList;
   static HistoryAdapter historyAdapter;
    NsdServiceInfo nsdServiceInfo;
  static  NsdManager.RegistrationListener registrationListener;
   static NsdManager nsdManager;
    String mGroupName;
    NsdManager.DiscoveryListener discovaryListener;
    List currentDeviceList;
    HashMap<String, NsdServiceInfo> currentHashMap;
    CustomJoin customJoin;
    int serverPort;
    DataHandler dataHandler= new DataHandler(MainActivity.this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Global.MAINACTIVITY=this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) findViewById(R.id.fab3);

        fabOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fabopen);
        fabClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fabclose);
        rotateC = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotatec);
        rotateA = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotatea);
        historyListView = (ListView) findViewById(R.id.historyList);
        createGroupText = (TextView) findViewById(R.id.createGroupText);
        joinGroupText = (TextView) findViewById(R.id.joinGroupText);
        nameList = new ArrayList<>();
        messageList = new ArrayList<>();
        timeList = new ArrayList<>();
        customJoin = new CustomJoin();





        //

        historyListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                        intent.putExtra("Username", ((DisplayModel) parent.getItemAtPosition(position)).getUserName());
                        Log.v("adslput", parent.getItemIdAtPosition(position) + "");


                        startActivity(intent);


                    }
                }
        );


        displayModelList = new ArrayList<>();

        displayModelList=    dataHandler.getGroupNames();


        historyAdapter = new HistoryAdapter(MainActivity.this, displayModelList);
        historyListView.setAdapter(historyAdapter);
        registerForContextMenu(historyListView);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                floationgAnims();
            }
        });

        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab.startAnimation(rotateA);
                fab2.startAnimation(fabClose);
                fab3.startAnimation(fabClose);
                createGroupText.startAnimation(fabClose);
                joinGroupText.startAnimation(fabClose);
                isOpen = false;
                CustomCreate customCreate = new CustomCreate();
                customCreate.show(getFragmentManager(), "custom Create");


            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab.startAnimation(rotateA);
                fab2.startAnimation(fabClose);
                fab3.startAnimation(fabClose);
                createGroupText.startAnimation(fabClose);
                joinGroupText.startAnimation(fabClose);
                isOpen = false;



                discoverNetworks();
                customJoin.show(getFragmentManager(), "custom Join");


            }
        });


    }

    private void floationgAnims() {
        if (isOpen) {
            fab.startAnimation(rotateA);
            fab2.startAnimation(fabClose);
            fab3.startAnimation(fabClose);
            createGroupText.startAnimation(fabClose);
            joinGroupText.startAnimation(fabClose);

            isOpen = false;


        } else {
            fab.startAnimation(rotateC);
            fab2.startAnimation(fabOpen);
            fab3.startAnimation(fabOpen);
            createGroupText.startAnimation(fabOpen);
            joinGroupText.startAnimation(fabOpen);

            isOpen = true;
        }


    }

    public void unregisterService()
    {

try {


    nsdManager.unregisterService(registrationListener);}catch (IllegalArgumentException e){e.printStackTrace();}
        catch (Exception e){
            e.printStackTrace();

        }
    }


    public void startServiceRegistration(final Context mContext, final String serviceName) throws IOException {
        nsdServiceInfo = new NsdServiceInfo();
        nsdServiceInfo.setServiceName(serviceName);
        nsdServiceInfo.setPort(getAvailablePort());
        nsdServiceInfo.setServiceType("_http._tcp.");
        mGroupName = serviceName;


        registrationListener = new NsdManager.RegistrationListener() {
            @Override
            public void onRegistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
                Log.v("adsl", "registration failed");
            }

            @Override
            public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
                Log.v("adsl", "unregister failed");

            }

            @Override
            public void onServiceRegistered(NsdServiceInfo serviceInfo) {
                Log.v("adsl", "service registered");
                Toast.makeText(mContext, "Service " + serviceInfo.getServiceName() + " is registered.", Toast.LENGTH_LONG).show();

//may be starts para should be here


            }

            @Override
            public void onServiceUnregistered(NsdServiceInfo serviceInfo) {
                Log.v("adsl", "unregistered service");
                isUnregistered=true;

           try{   Toast.makeText(mContext,"Group unregistered.",Toast.LENGTH_LONG).show();}catch (Exception e)
           {



           }

            }
        };

        nsdManager = (NsdManager) mContext.getSystemService(NSD_SERVICE);

        nsdManager.registerService(nsdServiceInfo, NsdManager.PROTOCOL_DNS_SD, registrationListener);

           Server server= new Server();
        server.setPort(serverPort);
              server.start();     ////////////////////////////////////////////

    }

    private int getAvailablePort() throws IOException {


        ServerSocket socket = new ServerSocket(0);
         serverPort = socket.getLocalPort();
        return serverPort;


    }

    public void discoverNetworks() {

        discovaryListener = new NsdManager.DiscoveryListener() {
            @Override
            public void onStartDiscoveryFailed(String serviceType, int errorCode) {


                Log.v("adsl", "start discovery failed");
            }

            @Override
            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                Log.v("adsl", "stop discovery failed");

            }

            @Override
            public void onDiscoveryStarted(String serviceType) {
                Log.v("adsl", "discovery started");

            }

            @Override
            public void onDiscoveryStopped(String serviceType) {
                Log.v("adsl", "discovery stopped");
            }

            @Override
            public void onServiceFound(NsdServiceInfo serviceInfo) {
                Log.v("adsl", "service found");
                customJoin.setList(MainActivity.this,serviceInfo);
            }


            @Override
            public void onServiceLost(NsdServiceInfo serviceInfo) {
                Log.v("adsl", "service lost");
                customJoin.removeFromList(MainActivity.this,serviceInfo);




            }
        };

        NsdManager mNsdManager = (NsdManager) getApplicationContext().getSystemService(NSD_SERVICE);
        mNsdManager.discoverServices("_http._tcp.", NsdManager.PROTOCOL_DNS_SD, discovaryListener);
    }


    public MainActivity() {
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem menuItem= menu.findItem(R.id.action_search);

        SearchView mSearchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText= newText.toLowerCase();

                List<DisplayModel> filteredList= new ArrayList<>();
                for(DisplayModel displayModel:displayModelList )

                {


                    String display=displayModel.getUserName().toLowerCase();

                    if( display.contains(newText)  )
                    {


                        filteredList.add(displayModel);
                    }


                }




                historyAdapter.setFilter(filteredList);


                return true;
            }
        });



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
if(id==R.id.action_wifi_search)
{

    startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK));
   Log.v("hello","connect");
    return true;

}

        if(id==R.id.action_unregister)
        {
           MainActivity mainActivity= new MainActivity();

            mainActivity.unregisterService();

            if(!isUnregistered)
            {
                Toast.makeText(MainActivity.this,"No Group to unregister.",Toast.LENGTH_LONG).show();




            }

return true;



        }




        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {    //note in note
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.contextual_menu_user, menu);





    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.delete:


                AlertDialog.Builder builder= new AlertDialog.Builder(MainActivity.this).setCancelable(true).setIcon(android.R.drawable.ic_menu_delete).
setTitle("Delete").setMessage("This item will be deleted.").setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dataHandler.removeGroup(displayModelList.get(info.position));
                        displayModelList.remove(info.position);

                        historyAdapter.notifyDataSetChanged();

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();

                break;
            case R.id.favourite:


                break;
            case R.id.block:


                break;
        }


        return super.onContextItemSelected(item);
    }



 public void   refreshListViews(final DisplayModel displayModelNew)
    {

        runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {

                        Log.v("notify","here here");

                        displayModelList.add(displayModelNew);
                      historyListView.invalidate();
                        historyAdapter.notifyDataSetChanged();

                    }
                }
        );}


    }




