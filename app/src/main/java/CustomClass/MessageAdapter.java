package CustomClass;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.myapp.game.wifichat.ChatActivity;
import com.myapp.game.wifichat.R;

import java.util.List;

import Model.MessageModel;

/**
 * Created by Shailendra on 2/10/2017.
 */
public class MessageAdapter extends ArrayAdapter {


    List<MessageModel> messageList;
    Context context;
    TextView message,timeText;
    boolean isMe;
    LinearLayout linearLayout,mainLayout;
    ImageView userPicFriend,userPicMe;
    boolean isTextViewVisible=false;








    public MessageAdapter(Context context,List<MessageModel> messageList) {
        super(context,R.layout.message_layout, messageList);
        this.messageList=messageList;
        this.context=context;
    }




    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        LayoutInflater inflater= LayoutInflater.from(context);
      View v=  inflater.inflate(R.layout.message_layout,null);
        message=(TextView)v.findViewById(R.id.message);
        timeText=(TextView)v.findViewById(R.id.timeText);
        linearLayout=(LinearLayout)v.findViewById(R.id.linearLayout);
        userPicFriend=(ImageView)v.findViewById(R.id.userPicFriend);
        userPicMe=(ImageView)v.findViewById(R.id.userPicMe);
    mainLayout=(LinearLayout)v.findViewById(R.id.mainLayout);
linearLayout.setOnClickListener(
        new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   showTime();                can we do time showing in click

            }
        }
);

        isMe=messageList.get(position).isMe();
        if(isMe)
        {
           setComponents(position);
            userPicFriend.setVisibility(View.GONE);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)linearLayout.getLayoutParams();
            params.setMargins(adderDp(),0,  0, 0);
            linearLayout.setLayoutParams(params);



        }

        else{


            makeLayoutChange();
            setComponents(position);

        }












return v;
    }

    private void showTime() {


      if(isTextViewVisible)
      {timeText.setVisibility(View.GONE);
          isTextViewVisible=false;

      }



        else{
          {timeText.setVisibility(View.VISIBLE);
              isTextViewVisible=true;





          }
    }}


    private void makeLayoutChange() {
        linearLayout.setBackgroundColor(Color.parseColor("#FFECE7E7"));
        message.setTextColor(Color.BLACK);
        message.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL );
        timeText.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL );
        userPicMe.setVisibility(View.GONE);


        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)linearLayout.getLayoutParams();
        params.setMargins(0, 0, adderDp(), 0);
        linearLayout.setLayoutParams(params);
    }

    private int adderDp() {
        Resources r= context.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,50,r.getDisplayMetrics());

    }

    private void setComponents(int position) {
        message.setText(messageList.get(position).getMessage());
        timeText.setText(messageList.get(position).getTime());
    }


}
