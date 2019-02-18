package ml.easycoins.easycoins;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class User extends AppCompatActivity {

    private static SharedPreferences sPref;
    String sid;
    String answer;
    String uid;
    String success;
    String error;
    double balance;
    String email;
    String username;
    TextView userNameOnDisplay;
    TextView userMailOnDisplay;
    TextView userBalanceOnDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        setElements();

    }

    //protected void onResume() {
    //    super.onResume ();
    //    setElements();
    //}

    //protected void onRestart() {
      //  super.onRestart ();
        //setElements();
    //}

    private void setElements(){

        sPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sid = sPref.getString ("sid", "");
        //userNameOnDisplay = (TextView)findViewById (R.id.userName);

        if(sid.length ()<32 || sid.length ()>32){
            SharedPreferences.Editor ed = sPref.edit ();
            ed.putString ("sid", "");
            ed.commit ();
            Intent intent = new Intent (this, Login.class);
            startActivity (intent);
        }else
        {
            Api api = new Api (getString (R.string.urlSite), this);
            answer = api.userInfo (sid);

            JSONObject obj = null;
            try {
                obj = new JSONObject (answer);
            } catch (JSONException e) {
                e.printStackTrace ();
            }

            try {
                sid = obj.getString ("sid");
                uid = obj.getString ("uid");
                success = obj.getString ("success");
                error = obj.getString ("error");
                email = obj.getString ("email");
                username = obj.getString ("username");
                balance = obj.getDouble ("balance");
            } catch (JSONException e) {
                e.printStackTrace ();
            }

            if (success.equals ("success")) {
            userNameOnDisplay = (TextView)findViewById (R.id.userNameOnDisplay);
            userNameOnDisplay.setText (username);
            userMailOnDisplay = (TextView)findViewById (R.id.userMailOnDisplay);
            userMailOnDisplay.setText (email);
            userBalanceOnDisplay = (TextView)findViewById (R.id.userBalanceOnDisplay);
            userBalanceOnDisplay.setText (Double.toString (balance));

            }else{
                Toast.makeText (this, "Произошла ошибка, перелогинтесь", Toast.LENGTH_LONG).show ();
                Intent intent = new Intent (this, Login.class);
                startActivity (intent);
            }

            Log.d("TAG",answer);
        }



    }


}
