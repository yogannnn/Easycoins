package ml.easycoins.easycoins;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;


public class Login extends AppCompatActivity implements OnClickListener {

    Button btnLogin;
    TextView login;
    TextView pass;
    String sid;
    String uid;
    String success;
    String error;
    String answer;
    double balance;
    private static SharedPreferences sPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_login);

        sPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sid = sPref.getString ("sid", "");

        if (sid.length ()==32){
            SharedPreferences.Editor ed = sPref.edit ();
            ed.putString ("sid", "");
            ed.commit ();
            Intent intent = new Intent (this, MainActivity.class);
            startActivity (intent);
        }


        btnLogin = (Button) findViewById (R.id.btnLogin);
        btnLogin.setOnClickListener (this);
        login = (TextView) findViewById (R.id.InputLogin);
        pass = (TextView) findViewById (R.id.InputPassword);
        //mImageView = (ImageView) findViewById (R.id.Ima)


    }

    @Override
    public void onClick(View v) {

        switch (v.getId ()) {

            case R.id.btnLogin:
                Api api = new Api (getString(R.string.urlSite), this);
                answer = api.login (login.getText ().toString (), pass.getText ().toString ());
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
                    balance = obj.getDouble ("balance");
                } catch (JSONException e) {
                    e.printStackTrace ();
                }
                if (success.equals ("success")) {
                    SharedPreferences.Editor ed = sPref.edit ();
                    ed.putString ("sid", sid);
                    ed.commit ();
                    Intent intent = new Intent (this, MainActivity.class);
                    startActivity (intent);
                }else{
                    Toast.makeText (this, error, Toast.LENGTH_LONG).show ();
                }

                break;
        }
    }


}

