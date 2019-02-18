package ml.easycoins.easycoins;

import android.content.Context;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import android.util.Log;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Api {
    public static final MediaType JSON = MediaType.parse("application/json;; charset=utf-8");



    private String url;
    private Context context;
    String status;
    Bundle bundle;
    static String body;
    String code;


    public Api(String url, Context context) { // Конструктор класса при инициализации устанавливаем переменные
        this.url = url;
        this.context = context;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public void setContext(Context context) {
        this.context = context;
    }

    public String login(String login, String password) {
        bundle = new Bundle ();
        //int flag = 0;ьтипоегпрт итпоге итпреог имтао нер

        //bundle.putString ("body", "");
        String result = null;


                String postUrl= url+"/api/index.php";
                String postBody="{\n" +
                        "    \"type\": \"login\",\n" +
                        "    \"login\": \""+login+"\",\n" +
                        "    \"password\": \""+password+"\"\n" +
                        "}";
                try {
                    postRequest(postUrl,postBody);
                } catch (IOException e) {
                    e.printStackTrace();
                }

        body = extractResult ();
                //Log.d("TAG","Забрали из бандла " + bundle.getString ("code", ""));
                 bundle.putString ("body", "");
                 bundle.putString ("code", "");
                 result = body;




        return result;
    }


    public String betMax(String sid, String betSize, String betPercent) {
        bundle = new Bundle ();
        String result = null;


        String postUrl= url+"/api/index.php";
        String postBody="{\n" +
                "    \"type\": \"betMax\",\n" +
                "    \"sid\": \""+sid+"\",\n" +
                "    \"betSize\": \""+betSize+"\",\n" +
                "    \"betPercent\": \""+betPercent+"\"\n" +
                "}";
        try {
            postRequest(postUrl,postBody);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //body = bundle.getString ("key", "");
        body = extractResult();
        //Log.d("TAG","Забрали из бандла " + body);
        bundle.putString ("body", "");
        result = body;
        return result;
    }

    public String betMin(String sid, String betSize, String betPercent) {
        bundle = new Bundle ();
        int flag = 0;
        //bundle.putString ("body", "");
        String result = null;


        String postUrl= url+"/api/index.php";
        String postBody="{\n" +
                "    \"type\": \"betMin\",\n" +
                "    \"sid\": \""+sid+"\",\n" +
                "    \"betSize\": \""+betSize+"\",\n" +
                "    \"betPercent\": \""+betPercent+"\"\n" +
                "}";
        try {
            postRequest(postUrl,postBody);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //body = bundle.getString ("key", "");
        body = extractResult();
        //Log.d("TAG","Забрали из бандла " + body);
        bundle.putString ("body", "");
        result = body;
        return result;
    }




    public String userInfo(String sid) {

        String result = null;
        int flag = 0;
        bundle = new Bundle ();

        String postUrl= url+"/api/index.php";
        String postBody="{\n" +
                "    \"type\": \"userinfo\",\n" +
                "    \"sid\": \""+sid+"\"\n" +
                "}";
        try {
            postRequest(postUrl,postBody);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //body = bundle.getString ("key", "");
        body = extractResult();
        //Log.d("TAG","Забрали из бандла " + body);
        bundle.putString ("body", "");
        result = body;


      return result;
    }


    public void createArchive(String sid) {

        String result = null;
        int flag = 0;
        bundle = new Bundle ();

        String postUrl= url+"/api/index.php";
        String postBody="{\n" +
                "    \"type\": \"create\",\n" +
                "    \"sid\": \""+sid+"\"\n" +
                "}";

        try {
            postRequest(postUrl,postBody);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //body = bundle.getString ("key", "");
        while (flag==0){
            body = bundle.getString ("body", "");
            if (body.length ()>0){flag=1;}
        }
        //Log.d("TAG","Забрали из бандла " + body);
        bundle.putString ("body", "");
        result = body;


        //return result;


    }

    public String extractResult(){
        String res = null;
        String cod;
        int flag = 0;

        int resTime = (int) System.currentTimeMillis();

        while (flag==0){
            res = bundle.getString ("body", "");
            cod = bundle.getString ("code", "");
            switch (cod){
                case "200": case "201": case "202": case "203": case "204": case "205": case "206": case "207": case "208": case "226":
                    if (res.length ()>0){flag=1;}
                    break;
                case "300": case "301": case "302": case "303": case "304": case "305": case "306": case "307": case "308":
                    res="{\"sid\":\"\", \"uid\":\"\", \"success\":\"error\", \"error\":\"Ошибка 3XX, обратитесь к владельцу сайта\", \"balance\":\"\"}";
                    flag = 1;
                    break;
                case "400": case "401": case "402": case "403": case "404": case "405": case "406": case "407": case "408":
                case "409": case "410": case "411": case "412": case "413": case "414": case "415": case "416": case "417":
                case "418": case "419": case "421": case "422": case "423": case "424": case "425": case "426": case "427":
                    res="{\"sid\":\"\", \"uid\":\"\", \"success\":\"error\", \"error\":\"Ошибка 404, обратитесь к владельцу сайта\", \"balance\":\"\"}";
                    flag = 1;
                    break;
                case "500": case "501": case "502": case "503": case "504": case "505": case "506": case "507": case "508":
                case "509": case "510": case "511": case "512": case "513": case "514": case "515": case "516": case "517":
                case "518": case "519": case "521": case "522": case "523": case "524": case "525": case "526": case "527":
                    res="{\"sid\":\"\", \"uid\":\"\", \"success\":\"error\", \"error\":\"Ошибка 5XX, обратитесь к владельцу сайта\", \"balance\":\"\"}";
                    flag = 1;
                    break;
                default:
                    if((int) System.currentTimeMillis() - resTime > 3000){
                        res="{\"sid\":\"\", \"uid\":\"\", \"success\":\"error\", \"error\":\"Сервер не отвечает. Проверьте подключение к интернету\", \"balance\":\"\"}";
                        flag = 1;
                    }
                    break;


            }

        }
        bundle.putString ("body", "");
        bundle.putString ("code", "");
        return res;
    }





    void postRequest(String postUrl,String postBody) throws IOException {

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, postBody);

        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback () {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                bundle.putString ("body", response.body ().string ());
                bundle.putString ("code", String.valueOf (response.code ()));
                //Log.d("TAG","Вставили в бандл " + response.body ().string ());
            }
        });
    }



}
