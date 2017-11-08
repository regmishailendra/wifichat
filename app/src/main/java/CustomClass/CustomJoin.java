package CustomClass;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.myapp.game.wifichat.Client;
import com.myapp.game.wifichat.MyServiceResolver;
import com.myapp.game.wifichat.R;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CustomJoin extends DialogFragment {

    ListView listView;
    public static List<String> deviceList = new ArrayList<>();
    LayoutInflater inflater;
    View v;
    TextView connectText;
    int countDot = 1;
    Handler timerHandler;
    Runnable r;
    NsdServiceInfo mServiceInfo;
    static HashMap<String, NsdServiceInfo> hashMap = new HashMap<>();
    static JoinAdapter mAdapter;
    Context mContext;
    NsdManager.ResolveListener resolveListener;
    NsdServiceInfo clickedServiceInfo;
    int connectedPort;
    InetAddress connectedHost;
    String connectedName;


    public CustomJoin() {

    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        inflater = getActivity().getLayoutInflater();
        v = inflater.inflate(R.layout.customjoin, null);
        listView = (ListView) v.findViewById(R.id.deviceListView);

        connectText = (TextView) v.findViewById(R.id.connectText);


        mAdapter = new JoinAdapter(getMyContext(), deviceList);



        mAdapter.notifyDataSetChanged();


        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //join the network now use hashmap to find the service info

                        clickedServiceInfo = hashMap.get(parent.getItemAtPosition(position));



                        NsdManager connectManager = (NsdManager) getMyContext().getSystemService(Context.NSD_SERVICE);
                        connectManager.resolveService(clickedServiceInfo, new MyServiceResolver(clickedServiceInfo.getPort(), clickedServiceInfo.getHost(), clickedServiceInfo.getServiceName(),getMyContext()));
                        dismiss();


                    }
                }
        );



        listView.setAdapter(mAdapter);

        timerHandler = new Handler();
        r = new Runnable() {
            @Override
            public void run() {

                switch (countDot) {
                    case (0):
                        connectText.setText("Connect to a device");
                        countDot = 1;
                        break;


                    case (1):
                        connectText.setText("Connect to a device.");
                        countDot = 2;
                        break;

                    case (2):
                        connectText.setText("Connect to a device..");
                        countDot = 3;
                        break;

                    case (3):
                        connectText.setText("Connect to a device...");
                        countDot = 0;
                        break;

                }
                timerHandler.postDelayed(this, 450);
            }
        };

        timerHandler.postDelayed(r, 0);


        AlertDialog.Builder builder = new AlertDialog.Builder(getMyContext());
        builder.setView(v);
        return builder.create();

    }


    @Override
    public void onCancel(DialogInterface dialog) {
        timerHandler.removeCallbacks(r);

    }


    private Context getMyContext() {
        return getActivity();
    }


    public void setList(Context applicationContext, final NsdServiceInfo serviceInfo) {
        if (!deviceList.contains(serviceInfo.getServiceName())) {
            deviceList.add(serviceInfo.getServiceName());
            hashMap.put(serviceInfo.getServiceName(), serviceInfo);

        }
       notifyChange(applicationContext);

    }




    public void removeFromList(Context applicationContext,final NsdServiceInfo serviceInfo)
    {


        if(deviceList.contains(serviceInfo.getServiceName()))
        {
            deviceList.remove(serviceInfo.getServiceName());
            notifyChange(applicationContext);
            hashMap.remove(serviceInfo.getServiceName());





        }




    }
    private void notifyChange(Context applicationContext) {

        ((Activity) applicationContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
                listView.invalidate();
            }
        });
    }


}
