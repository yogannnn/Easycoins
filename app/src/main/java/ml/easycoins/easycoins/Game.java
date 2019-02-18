package ml.easycoins.easycoins;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorAdditionalInfo;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.text.DecimalFormat;
import java.util.Random;


public class Game extends AppCompatActivity implements View.OnClickListener , SensorEventListener {
    TextView textViewChans;
    SeekBar seekBarChans;
    SeekBar seekBarBet;
    TextView editTextBet;
    TextView profit;
    Button betUp;
    Button betDown;
    Button chansUp;
    Button chansDown;
    Button betMin;
    Button betMax;
    Integer tempValue;
    SharedPreferences sPref;
    String sid;
    String bet;
    String chans;
    String answer;
    String uid;
    String success;
    String error;
    String email;
    String username;
    double balance;
    TextView userNameGame;
    TextView userBalanceGame;
    Button btnDown;
    double raschet_bet;
    double raschet_chans;
    double raschet_profit;
    Button btnResult;
    double minRange;
    double maxRange;
    String type;
    String res_profit;
    double new_balance;
    String number;
    String randomString;
    JSONObject objRandom;
    boolean clickable;
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;


    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_game);

        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);

        clickable = true;
        sPref = PreferenceManager.getDefaultSharedPreferences (getApplicationContext ());
        sid = sPref.getString ("sid", "");
        bet = sPref.getString ("bet", "1");
        chans = sPref.getString ("chans", "50");

        if (sid.length () < 32 || sid.length () > 32) {
            SharedPreferences.Editor ed = sPref.edit ();
            ed.putString ("sid", "");
            ed.commit ();
            Intent intent = new Intent (this, Login.class);
            startActivity (intent);
        } else {
            Api api = new Api (getString(R.string.urlSite), this);
            api.createArchive(sid);
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
                userNameGame = (TextView) findViewById (R.id.userNameGame);
                userNameGame.setText (username+":");

                userBalanceGame = (TextView) findViewById (R.id.userBalanceGame);
                userBalanceGame.setText (Double.toString (balance));

            } else {
                Toast.makeText (this, "Произошла ошибка, перелогинтесь", Toast.LENGTH_LONG).show ();
                Intent intent = new Intent (this, Login.class);
                startActivity (intent);
            }


            seekBarChans = (SeekBar) findViewById (R.id.seekBarChans);
            seekBarChans.setProgress (Integer.parseInt (chans));
            seekBarChans.setOnSeekBarChangeListener (seekBarChangeListener);

            seekBarBet = (SeekBar) findViewById (R.id.seekBarBet);
            seekBarBet.setProgress (Integer.parseInt (bet));
            seekBarBet.setOnSeekBarChangeListener (seekBarChangeListener);

            textViewChans = (TextView) findViewById (R.id.textViewChans);
            textViewChans.setText (chans);
            editTextBet = (TextView) findViewById (R.id.TextBet);
            editTextBet.setText (bet);
            betUp = (Button) findViewById (R.id.betUp);
            betUp.setOnClickListener (this);
            betDown = (Button) findViewById (R.id.betDown);
            betDown.setOnClickListener (this);
            chansUp = (Button) findViewById (R.id.chansUp);
            chansUp.setOnClickListener (this);
            chansDown = (Button) findViewById (R.id.chansDown);
            chansDown.setOnClickListener (this);
            betMin = (Button) findViewById (R.id.betMin);
            betMin.setOnClickListener (this);
            betMax = (Button) findViewById (R.id.betMax);
            betMax.setOnClickListener (this);
            btnDown = (Button) findViewById (R.id.btnDown);
            btnDown.setOnClickListener (this);
            profit = (TextView)findViewById (R.id.profit);
            btnResult = (Button)findViewById (R.id.btnResult);
            randomString = getRandomString ();
            try {
                objRandom = new JSONObject (randomString);
            } catch (JSONException e) {
                e.printStackTrace ();
            }

            btnResult.setOnClickListener (this);
            btnResult.setBackgroundColor(Color.parseColor("#333333"));
            try {
                btnResult.setText (objRandom.getString ("title"));
            } catch (JSONException e) {
                e.printStackTrace ();
            }


            updateElements ();

        }
    }


    public SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener () {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            editTextBet.setText (String.valueOf (seekBarBet.getProgress ()));
            textViewChans.setText (String.valueOf (seekBarChans.getProgress ()));
            if (seekBarBet.getProgress () == 0) {
                editTextBet.setText ("1");
                seekBarBet.setProgress (1);
            }
            if (seekBarChans.getProgress () == 0) {
                textViewChans.setText ("1");
                seekBarChans.setProgress (1);
            }
            updateElements ();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            editTextBet.setText (String.valueOf (seekBarBet.getProgress ()));
            textViewChans.setText (String.valueOf (seekBarChans.getProgress ()));
            if (seekBarBet.getProgress () == 0) {
                editTextBet.setText ("1");
                seekBarBet.setProgress (1);
            }
            if (seekBarChans.getProgress () == 0) {
                textViewChans.setText ("1");
                seekBarChans.setProgress (1);
            }
            updateElements ();
        }
    };


    public void updateElements() {
        //Пересчитываем выигрыш

        raschet_bet = Double.parseDouble(String.valueOf (editTextBet.getText()));
        raschet_chans = Double.parseDouble(String.valueOf (textViewChans.getText()));
        raschet_profit = 100/raschet_chans*raschet_bet;
        DecimalFormat f = new DecimalFormat("##.00");
        profit.setText (f.format(raschet_profit));

        minRange=raschet_chans/100*999999;
        maxRange=999999-raschet_chans/100*999999;

        betMin.setText ("0-"+(int)minRange);
        betMax.setText ((int)Math.ceil(maxRange)+"-999999");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId ()) {
            case R.id.betUp:
                tempValue = Integer.parseInt (String.valueOf (editTextBet.getText ()));
                tempValue++;
                if (tempValue > 100) {
                    editTextBet.setText ("100");
                    seekBarBet.setProgress (100);
                } else {
                    seekBarBet.setProgress (tempValue);
                    editTextBet.setText (tempValue.toString ());
                }
                updateElements ();
                break;
            case R.id.betDown:
                tempValue = Integer.parseInt (String.valueOf (editTextBet.getText ()));
                tempValue--;
                if (tempValue < 1) {
                    editTextBet.setText ("1");
                    seekBarBet.setProgress (1);
                } else {
                    seekBarBet.setProgress (tempValue);
                    editTextBet.setText (tempValue.toString ());
                }
                updateElements ();
                break;
            case R.id.chansUp:
                tempValue = Integer.parseInt (String.valueOf (textViewChans.getText ()));
                tempValue++;
                if (tempValue > 95) {
                    textViewChans.setText ("95");
                    seekBarChans.setProgress (95);
                } else {
                    seekBarChans.setProgress (tempValue);
                    textViewChans.setText (tempValue.toString ());
                }
                updateElements ();
                break;
            case R.id.chansDown:
                tempValue = Integer.parseInt (String.valueOf (textViewChans.getText ()));
                tempValue--;
                if (tempValue < 1) {
                    textViewChans.setText ("1");
                    seekBarChans.setProgress (1);
                } else {
                    seekBarChans.setProgress (tempValue);
                    textViewChans.setText (tempValue.toString ());
                }
                updateElements ();
                break;
            case R.id.betMin:
                SharedPreferences.Editor ed2 = sPref.edit ();
                ed2.putString ("chans", String.valueOf (textViewChans.getText ()));
                ed2.putString ("bet", String.valueOf (editTextBet.getText ()));
                ed2.commit ();

                Api apiMin = new Api (getString(R.string.urlSite), this);
                answer = apiMin.betMin (sid, String.valueOf (editTextBet.getText ()), String.valueOf (textViewChans.getText ()));
                JSONObject objMin = null;
                try {
                    objMin = new JSONObject (answer);
                } catch (JSONException e) {
                    e.printStackTrace ();
                }
                try {
                    type = objMin.getString ("type");
                    success = objMin.getString ("success");
                    res_profit = objMin.getString ("profit");
                    balance = objMin.getDouble ("balance");
                    new_balance = objMin.getDouble ("new_balance");
                    //arch = obj.getString ("arch");
                    //number = obj.getString ("number");
                    //check_bet = obj.getString ("check_bet");
                    //error = obj.getString ("error");
                } catch (JSONException e) {
                    e.printStackTrace ();
                }

                try {
                    if (objMin.getString ("success").equals ("success")) {
                        if(objMin.getString ("type").equals ("win")){
                            btnResult.setBackgroundColor(Color.parseColor("#00AA00"));
                            clickable=false;
                        }else{
                            btnResult.setBackgroundColor(Color.parseColor("#FF0000"));
                            clickable=false;
                        }

                        try {
                            btnResult.setText ("Number: "+objMin.getString ("number")+" / Password: "+objMin.getString ("arch"));
                            userBalanceGame.setText (objMin.getString ("new_balance"));
                        } catch (JSONException e) {
                            e.printStackTrace ();
                        }
                    } else {
                        try {
                            Toast.makeText (this, objMin.getString ("error"), Toast.LENGTH_LONG).show ();
                        } catch (JSONException e) {
                            e.printStackTrace ();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace ();
                }


                break;
            case R.id.betMax:
                SharedPreferences.Editor ed3 = sPref.edit ();
                ed3.putString ("chans", String.valueOf (textViewChans.getText ()));
                ed3.putString ("bet", String.valueOf (editTextBet.getText ()));
                ed3.commit ();

                Api apiMax = new Api (getString(R.string.urlSite), this);
                answer = apiMax.betMax (sid, String.valueOf (editTextBet.getText ()), String.valueOf (textViewChans.getText ()));
                JSONObject objMax = null;
                try {
                    objMax = new JSONObject (answer);
                } catch (JSONException e) {
                    e.printStackTrace ();
                }
                try {
                    type = objMax.getString ("type");
                    success = objMax.getString ("success");
                    res_profit = objMax.getString ("profit");
                    balance = objMax.getDouble ("balance");
                    new_balance = objMax.getDouble ("new_balance");
                    //arch = obj.getString ("arch");
                    //number = obj.getString ("number");
                    //check_bet = obj.getString ("check_bet");
                    //error = obj.getString ("error");
                } catch (JSONException e) {
                    e.printStackTrace ();
                }

                try {
                    if (objMax.getString ("success").equals ("success")) {
                        if(objMax.getString ("type").equals ("win")){
                            btnResult.setBackgroundColor(Color.parseColor("#00AA00"));
                            clickable=false;
                        }else{
                            btnResult.setBackgroundColor(Color.parseColor("#FF0000"));
                            clickable=false;
                        }

                        try {
                            btnResult.setText ("Number: "+objMax.getString ("number")+" / Password: "+objMax.getString ("arch"));
                            userBalanceGame.setText (objMax.getString ("new_balance"));
                        } catch (JSONException e) {
                            e.printStackTrace ();
                        }
                    } else {
                        try {
                            Toast.makeText (this, objMax.getString ("error"), Toast.LENGTH_LONG).show ();
                        } catch (JSONException e) {
                            e.printStackTrace ();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace ();
                }

                break;
            case R.id.btnDown:
                long downloadID;

                File direct = new File(Environment.getExternalStorageDirectory() + "/easycoins");

                if (!Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    Log.d("TAG", "SD-карта не доступна: " + Environment.getExternalStorageState());
                }
                Log.d("TAG", String.valueOf (Environment.DIRECTORY_DOWNLOADS));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Log.e("Permission error","You have permission");
                                        }
                }
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                DownloadManager.Request request=new DownloadManager.Request(Uri.parse(getString(R.string.urlSite)+"/api/index.php?act=down&id="+uid))
                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                        .setDestinationInExternalPublicDir (Environment.DIRECTORY_DOWNLOADS,"archive.zip");
                DownloadManager downloadManager= (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                downloadID = downloadManager.enqueue(request);

                Toast.makeText (this, "Файл загружен в папку Download", Toast.LENGTH_LONG).show ();
                break;
            case R.id.btnResult:

                if(clickable){

                    Intent browserIntent = null;
                    try {
                        browserIntent = new Intent (Intent.ACTION_VIEW, Uri.parse(objRandom.getString ("url")));
                    } catch (JSONException e) {
                        e.printStackTrace ();
                    }
                    startActivity(browserIntent);

                }

                break;
            default:
                break;


        }
    }

    static String getRandomString(){
        int r = (int) (Math.random()*14);
        String name = new String [] {"{\"title\":\"Играй на ELF-CSGO.COM\", \"url\":\"http://elf-csgo.com\"}",
                "{\"title\":\"Играй на ELF-CSGO.COM\", \"url\":\"http://elf-csgo.com\"}",
                "{\"title\":\"Играй на ELF-CSGO.COM\", \"url\":\"http://elf-csgo.com\"}",
                "{\"title\":\"Играй на ELF-CSGO.COM\", \"url\":\"http://elf-csgo.com\"}",
                "{\"title\":\"Играй на ELF-CSGO.COM\", \"url\":\"http://elf-csgo.com\"}",
                "{\"title\":\"Играй на ELF-CSGO.COM\", \"url\":\"http://elf-csgo.com\"}",
                "{\"title\":\"Играй на ELF-CSGO.COM\", \"url\":\"http://elf-csgo.com\"}",
                "{\"title\":\"Играй на ELF-CSGO.COM\", \"url\":\"http://elf-csgo.com\"}",
                "{\"title\":\"Тут могла быть ваша реклама\", \"url\":\"http://easycoins.ml\"}",
                "{\"title\":\"Тут могла быть ваша реклама\", \"url\":\"http://easycoins.ml\"}",
                "{\"title\":\"Тут могла быть ваша реклама\", \"url\":\"http://easycoins.ml\"}",
                "{\"title\":\"Тут могла быть ваша реклама\", \"url\":\"http://easycoins.ml\"}",
                "{\"title\":\"Тут могла быть ваша реклама\", \"url\":\"http://easycoins.ml\"}",
                "{\"title\":\"Тут могла быть ваша реклама\", \"url\":\"http://easycoins.ml\"}"}[r];
        return name;
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float speed = Math.abs(x + y + z - last_x - last_y - last_z)/ diffTime * 10000;

                if (speed > SHAKE_THRESHOLD) {


                    Random random = new Random();
                    int num = 1 + random.nextInt(101 - 1);
                    editTextBet.setText (Integer.toString (num));
                    seekBarBet.setProgress (num);

                    Random random2 = new Random();
                    int num2 = 1 + random2.nextInt(96 - 1);
                    textViewChans.setText (Integer.toString (num2));
                    seekBarChans.setProgress (num2);



                    updateElements ();

                }

                last_x = x;
                last_y = y;
                last_z = z;
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    protected void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(this);
    }

    protected void onResume() {
        super.onResume();
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }


}
