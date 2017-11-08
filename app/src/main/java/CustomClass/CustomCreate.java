package CustomClass;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.myapp.game.wifichat.MainActivity;
import com.myapp.game.wifichat.R;

import java.io.IOException;

/**
 * Created by Shailendra on 2/9/2017.
 */
public class CustomCreate extends DialogFragment {
    EditText groupName;
    Button createGroup;
    LayoutInflater inflater;
    String previousName;
    String mPreferenceName="GROUP_NAME";
    String groupNameString;
    MainActivity mainActivity;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.customcreate, null);
        groupName = (EditText) v.findViewById(R.id.groupName);
        createGroup = (Button) v.findViewById(R.id.createGroup);

        SharedPreferences prefs= getMyContext().getSharedPreferences(mPreferenceName,Context.MODE_PRIVATE);
        previousName=prefs.getString(mPreferenceName,null);
        if(previousName!=null)
        {
            groupName.setText(previousName);
        }
        else{  previousName="My Chat Group";
        }


        createGroup.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                     //check null

                      groupNameString = groupName.getText().toString();   //see if edit text is null


                        SharedPreferences.Editor editor= getMyContext().getSharedPreferences(mPreferenceName,Context.MODE_PRIVATE).edit();
                        editor.putString(mPreferenceName,groupNameString);
                        editor.commit();

//check


                 MainActivity mainActivity= new MainActivity();
                        try {
                            mainActivity.startServiceRegistration(getMyContext(),groupNameString);
                        } catch (IOException e) {
                            e.printStackTrace();

                            Toast.makeText(getMyContext(),"Error starting service registration.",Toast.LENGTH_LONG).show();

                        }


                        dismiss();






                    }
                }
        );


        AlertDialog.Builder builder = new AlertDialog.Builder(getMyContext());

        builder.setView(v);


        return builder.create();
    }

    private Context getMyContext() {
        return getActivity();
    }

}
