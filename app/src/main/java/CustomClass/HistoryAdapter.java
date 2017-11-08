package CustomClass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myapp.game.wifichat.R;

import java.util.ArrayList;
import java.util.List;

import Model.DisplayModel;

/**
 * Created by Shailendra on 2/9/2017.
 */
public class HistoryAdapter extends ArrayAdapter {
    public Context context;
    ImageView userPic;
    TextView userName,imageText;
    TextView lastMessage;
    TextView time;
    LinearLayout linearClick;
    List<DisplayModel> displayModelList;





    public HistoryAdapter(Context context, List displayModelList) {
        super(context, R.layout.chatlist,displayModelList);
        this.context=context;
        this.displayModelList=displayModelList;


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater= LayoutInflater.from(context);
        View customView=inflater.inflate(R.layout.chatlist,parent,false);
        userPic=(ImageView)customView.findViewById(R.id.userPicMe);
        userName=(TextView)customView.findViewById(R.id.userName);
        lastMessage=(TextView)customView.findViewById(R.id.lastMessage);
        time=(TextView)customView.findViewById(R.id.time);
        imageText=(TextView)customView.findViewById(R.id.imageText);


        userName.setText(displayModelList.get(position).getUserName());
        char alphabet= displayModelList.get(position).getUserName().toUpperCase().charAt(0);   //put this after username is completely found
        imageText.setText(alphabet+"");
        lastMessage.setText(displayModelList.get(position).getLastMessage());
        time.setText(displayModelList.get(position).getLastTime());


    return customView;
    }


    @Override
    public int getCount() {
        return displayModelList.size();
    }


    @Override
    public DisplayModel getItem(int position) {
        return displayModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getPosition(Object item) {
        return super.getPosition(item);
    }







    public void setFilter(List<DisplayModel> filteredList)
    {


        displayModelList= new ArrayList<>();
        displayModelList.addAll(filteredList);
        notifyDataSetChanged();
    }


}
