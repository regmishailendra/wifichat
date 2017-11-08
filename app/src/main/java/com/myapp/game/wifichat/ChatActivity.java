package com.myapp.game.wifichat;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import CustomClass.MessageAdapter;
import Database.DataHandler;
import Model.MessageModel;

public class ChatActivity extends AppCompatActivity {

    ImageButton sendButton;
    EditText messageBox;
    String userName;
    public ListView messageListView;
    List<MessageModel> messageList = new ArrayList();
    MessageAdapter adapter;
    DataHandler dataHandler= new DataHandler(ChatActivity.this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Global.CHAT_ACTIVITY = this;
        setContentView(R.layout.activity_chat);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle = getIntent().getExtras();
        userName = bundle.getString("Username");
        messageListView = (ListView) findViewById(R.id.messageListView);
        messageListView.setDivider(null);
        //  messageList = new ArrayList();
        setTitle(userName);
        //remove these later



        //

  messageList= dataHandler.retriveChat(userName.trim());









        sendButton = (ImageButton) findViewById(R.id.sendButton);
        messageBox = (EditText) findViewById(R.id.messageBox);

        sendButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String message = messageBox.getText().toString();
                        updateSendingMessage(message, true);
                        messageBox.setText("");


                        if(ServerOrClient.sor==true)
                        {
                            Server server=Global.SERVER;
                          try{  server.sendOut(message);}catch (NullPointerException e){e.printStackTrace();
                      Toast.makeText(ChatActivity.this,"No connection!",Toast.LENGTH_LONG).show();
                          }






                        }
                        else if (ServerOrClient.sor==false)
                        { Client client = Global.CLIENT;


try{                        client.sendOut(message);}catch (NullPointerException e){e.printStackTrace();
   Toast.makeText(ChatActivity.this,"No connection!",Toast.LENGTH_LONG).show();
}
                        }

                    }
                }
        );
        registerForContextMenu(messageListView);


        adapter = new MessageAdapter(ChatActivity.this, messageList);
        messageListView.setAdapter(adapter);


    }

    public void updateSendingMessage(String message, boolean isMe) {


        //store your messages in database too and send the messages too

        // if (!messageBox.getText().toString().trim().matches(""))


        Log.v("adsl", "came here");

        final MessageModel sendingMessage = new MessageModel();
        sendingMessage.setMessage(message);
        sendingMessage.setTime("8:57Pm"); //set current time in form of database format
        sendingMessage.setMe(isMe);

        if (isMe == true) {


            if (!messageBox.getText().toString().trim().matches(""))

            {
                doScreenUpdate(sendingMessage);
            }


        } else

        {

            doScreenUpdate(sendingMessage);


        }


    }

    private void doScreenUpdate(final MessageModel sendingMessage) {

        dataHandler.addMessage(sendingMessage, userName.trim());





        runOnUiThread(new Runnable() {
                          @Override
                          public void run() {

                              Log.v("adsl", "updating chat window");
                              messageList.add(sendingMessage);
                              adapter.notifyDataSetChanged();


                          }
                      }


        );

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.userinfo_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.user_info:


                Toast.makeText(ChatActivity.this, "Clicked info of " + userName, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ChatActivity.this, UserDetail.class);
                intent.putExtra("Username", userName);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_left, R.anim.activity_right);


                return true;
            case android.R.id.home:
                this.finish();
        }
        return true;

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.contextual_menu_message, menu);


    }

    ChatActivity() {
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int id = item.getItemId();

        switch (id) {

            case R.id.delete_message:
                AlertDialog.Builder builder= new AlertDialog.Builder(ChatActivity.this).setCancelable(true).setIcon(android.R.drawable.ic_menu_delete).
                        setTitle("Delete").setMessage("This message will be deleted.").setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dataHandler.removeMessage(messageList.get(info.position));
                        messageList.remove(info.position);
                        adapter.notifyDataSetChanged();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();



                break;
            case R.id.copytext_message:

                String message = ((MessageModel) (messageListView.getItemAtPosition(info.position))).getMessage();


                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("coppied text", message);
                clipboardManager.setPrimaryClip(clip);
                Toast.makeText(ChatActivity.this, "Text Copied.", Toast.LENGTH_LONG).show();
                break;
            case R.id.details_message:
                break;
        }
        return super.onContextItemSelected(item);
    }
}
