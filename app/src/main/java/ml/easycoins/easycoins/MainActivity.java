package ml.easycoins.easycoins;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    Button btnGame;
    Button btnLast;
    Button btnBonus;
    Button btnAuto;
    Button btnPopoln;
    Button btnVivod;
    Button btnSet;
    Button btnUser;
    Button btnLogin;
    private static SharedPreferences sPref;
    String sid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

        setElements();

    }

    protected void onResume() {
        super.onResume ();
        setElements();
    }

    protected void onRestart() {
        super.onRestart ();
        setElements();
    }



    private void setElements(){

        sPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sid = sPref.getString ("sid", "");
        btnGame = (Button) findViewById (R.id.btnGame);
        btnGame.setOnClickListener (this);
        if (sid.length () < 32) {
            btnGame.setEnabled (false);
            btnGame.setTextColor (Color.GRAY);
        }

        btnLast = (Button) findViewById (R.id.btnLast);
        btnLast.setOnClickListener (this);

        btnBonus = (Button) findViewById (R.id.btnBonus);
        btnBonus.setOnClickListener (this);
        if (sid.length () < 32) {
            btnBonus.setEnabled (false);
            btnBonus.setTextColor (Color.GRAY);
        }

        btnAuto = (Button) findViewById (R.id.btnAuto);
        btnAuto.setOnClickListener (this);
        if (sid.length () < 32) {
            btnAuto.setEnabled (false);
            btnAuto.setTextColor (Color.GRAY);
        }

        btnPopoln = (Button) findViewById (R.id.btnPopoln);
        btnPopoln.setOnClickListener (this);
        if (sid.length () < 32) {
            btnPopoln.setEnabled (false);
            btnPopoln.setTextColor (Color.GRAY);
        }

        btnVivod = (Button) findViewById (R.id.btnVivod);
        btnVivod.setOnClickListener (this);
        if (sid.length () < 32) {
            btnVivod.setEnabled (false);
            btnVivod.setTextColor (Color.GRAY);
        }

        btnSet = (Button) findViewById (R.id.btnSet);
        btnSet.setOnClickListener (this);

        btnUser = (Button) findViewById (R.id.btnUser);
        btnUser.setOnClickListener (this);
        if (sid.length () < 32) {
            btnUser.setEnabled (false);
            btnUser.setTextColor (Color.GRAY);
        }

        btnLogin = (Button) findViewById (R.id.btnLogin);
        btnLogin.setOnClickListener (this);
        if (sid.length () < 32) {
            btnLogin.setText ("\uf2f6");
        } else {
            btnLogin.setText ("\uf2f5");
        }

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId ()) {
            case R.id.btnGame:
                intent = new Intent (this, Game.class);
                startActivity (intent);
                break;
            case R.id.btnLast:
                intent = new Intent (this, Last.class);
                startActivity (intent);
                break;
            case R.id.btnBonus:
                intent = new Intent (this, Bonus.class);
                startActivity (intent);
                break;
            case R.id.btnAuto:
                intent = new Intent (this, Auto.class);
                startActivity (intent);
                break;
            case R.id.btnPopoln:
                intent = new Intent (this, Popoln.class);
                startActivity (intent);
                break;
            case R.id.btnVivod:
                intent = new Intent (this, Vivod.class);
                startActivity (intent);
                break;
            case R.id.btnSet:
                intent = new Intent (this, Set.class);
                startActivity (intent);
                break;
            case R.id.btnUser:
                intent = new Intent (this, User.class);
                startActivity (intent);
                break;
            case R.id.btnLogin:
                intent = new Intent (this, Login.class);
                startActivity (intent);
                break;
            default:
                break;
        }

    }
}
