package Database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.CursorAdapter;

import com.myapp.game.wifichat.Global;
import com.myapp.game.wifichat.MainActivity;

import java.util.ArrayList;
import java.util.List;

import Model.DisplayModel;
import Model.MessageModel;

public class DataHandler extends SQLiteOpenHelper {
    String lastMessage;
    public List<MessageModel> myList = new ArrayList<>();
    public List<DisplayModel> groupList = new ArrayList<>();
    public List<String> nameList = new ArrayList<>();


    public DataHandler(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String statement = " CREATE TABLE " + Constants.TABLE_NAME + " (" + Constants.ID_NAME + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Constants.MESSAGE_TIME + " TEXT, " + Constants.PERSONNAL_NAME + " TEXT, " + Constants.MESSAGE_NAME + " TEXT, " + Constants.ISME_NAME + " TEXT" + ");";
        db.execSQL(statement);

        Log.v("data", "Database created successfully");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String statement = "DROP TABLE IF EXISTS " + Constants.TABLE_NAME;
        db.execSQL(statement);
        onCreate(db);


    }


    public void addMessage(MessageModel messageModel, String personnalName) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.PERSONNAL_NAME, personnalName);
        contentValues.put(Constants.MESSAGE_TIME, messageModel.getTime());
        contentValues.put(Constants.MESSAGE_NAME, messageModel.getMessage());
        contentValues.put(Constants.ISME_NAME, messageModel.isMe());
        Log.v("adsl is me ", messageModel.isMe + "");

        db.insert(Constants.TABLE_NAME, null, contentValues);


        String insertedValues = "Value inserted are " + "Name " + personnalName + " Time " + messageModel.getTime() + " Message " + messageModel.getMessage() + " isme " + messageModel.isMe();
        Log.v("values inserted", insertedValues);
    }


    public List<MessageModel> retriveChat(String personnalName)

    {
        SQLiteDatabase db = this.getReadableDatabase();
        Log.v("temp.", "his name" + personnalName.trim());

        String query = "SELECT * FROM " + Constants.TABLE_NAME + " WHERE " + Constants.PERSONNAL_NAME + " LIKE " + "'" + personnalName.trim() + "'"; //this mark '' is given because without it program did not identify the table name
        //


        Cursor cursor = db.rawQuery(query, null);


        if (cursor.moveToFirst())


            do {

                MessageModel messageModel = new MessageModel();

                messageModel.setMessage(cursor.getString(cursor.getColumnIndex(Constants.MESSAGE_NAME)));
                messageModel.setTime(cursor.getString(cursor.getColumnIndex(Constants.MESSAGE_TIME)));
                messageModel.setPrimary_key(cursor.getInt(cursor.getColumnIndex(Constants.ID_NAME)));
              try{  boolean convertedIsMe = convertStringToBoolean(cursor.getString(cursor.getColumnIndex(Constants.ISME_NAME)));
                messageModel.setMe(convertedIsMe);}catch (Exception e){}


                myList.add(messageModel);

                Log.v("temp", "retrived personnal name" + cursor.getString(cursor.getColumnIndex(Constants.PERSONNAL_NAME)));


            } while (cursor.moveToNext());


        return myList;
    }

    private boolean convertStringToBoolean(String isme) {

     if (isme.matches("1")) {
            return true;

        }
        return false;


    }



    public void  addGroupName(String name)
    {



        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put(Constants.PERSONNAL_NAME,name);
        contentValues.put(Constants.MESSAGE_NAME,"No last message.");
        contentValues.put(Constants.ISME_NAME,"false");


        db.insert(Constants.TABLE_NAME,null,contentValues);





    }


    public void removeMessage(MessageModel messageModel)
    {

        SQLiteDatabase db=getWritableDatabase();
        String statement="DELETE FROM "+ Constants.TABLE_NAME+" WHERE "+Constants.ID_NAME+"="+ messageModel.getPrimary_key();
        db.execSQL(statement);



    }



    public void removeGroup(DisplayModel displayModel)
    {

        SQLiteDatabase db=getWritableDatabase();
String statement="DELETE FROM "+ Constants.TABLE_NAME+" WHERE "+ Constants.PERSONNAL_NAME+"="+ "'"+displayModel.getUserName()+"'";

db.execSQL(statement);


    }

    public List<DisplayModel> getGroupNames() {

        SQLiteDatabase db = this.getReadableDatabase();
        String statement = "Select * FROM " + Constants.TABLE_NAME;

        Cursor cursor = db.rawQuery(statement, null);


        if (cursor.moveToFirst()) {


            do {

                DisplayModel displayModel = new DisplayModel();


                displayModel.setUserName(cursor.getString(cursor.getColumnIndex(Constants.PERSONNAL_NAME)));


                getLastMessage();
                displayModel.setLastMessage(lastMessage);
                displayModel.setLastTime("5:23 PM");
                displayModel.setPrimary_key(cursor.getInt(cursor.getColumnIndex(Constants.ID_NAME)));

                // displayModel.setLastMessage(cursor.getColumnName(cursor.getColumnIndex(Constants.)));
                Log.v("temper", cursor.getColumnName(cursor.getColumnIndex(Constants.PERSONNAL_NAME)));


                if (!nameList.contains(cursor.getString(cursor.getColumnIndex(Constants.PERSONNAL_NAME))))

                {
                    nameList.add(cursor.getString(cursor.getColumnIndex(Constants.PERSONNAL_NAME)));     //name list is created so that we can check if duplicate items are there
                    groupList.add(displayModel);                                                         //by checking through grouplist object, it returned unique object every time and the nameis added
                    lastMessage=  cursor.getString(cursor.getColumnIndex(Constants.MESSAGE_NAME));
                }

            } while (cursor.moveToNext());


        }


        return groupList;
    }

    public void getLastMessage()
    {




    }

}
