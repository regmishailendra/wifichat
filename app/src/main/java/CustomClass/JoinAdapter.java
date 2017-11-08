package CustomClass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.myapp.game.wifichat.R;

import java.util.List;

/**
 * Created by Shailendra on 2/16/2017.
 */
public class JoinAdapter extends ArrayAdapter {

    Context context;
    List<String> deviceList;


    public JoinAdapter(Context context, List deviceList) {
        super(context, R.layout.joinlist, deviceList);
        this.context=context;
        this.deviceList=deviceList;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater= LayoutInflater.from(context);
     View v=   inflater.inflate(R.layout.joinlist,parent,false);

        TextView tv=(TextView)v.findViewById(R.id.deviceName);
        tv.setText(deviceList.get(position));
        return v;





    }


    @Override
    public int getCount() {
        return deviceList.size();
    }

    @Override
    public Object getItem(int position) {
     return    deviceList.get(position);
    }

    @Override
    public int getPosition(Object item) {
        return super.getPosition(item);
    }
}








