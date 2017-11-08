package com.myapp.game.wifichat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class UserDetail extends AppCompatActivity {

    TextView userNameTextView;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
   Bundle bundle=getIntent().getExtras();
        userName=bundle.getString("Username","No value");




        userNameTextView =(TextView)findViewById(R.id.userName);
            userNameTextView.setText(userName);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.user_detail_menu,menu);


        return true;



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int item_id=item.getItemId();
        switch(item_id)
        {

            case R.id.action_delete_convo:
                Toast.makeText(UserDetail.this,"Clicked delete",Toast.LENGTH_LONG).show();
                break;

            case R.id.action_mark_favourite:
                Toast.makeText(UserDetail.this,"Clicked favourite",Toast.LENGTH_LONG).show();
                break;

            case android.R.id.home:
                this.finish();


        }


        return true;
    }
}
