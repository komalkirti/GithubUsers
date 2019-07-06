package com.example.komal.networking;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    Button btn;
    EditText etvar;
  //  TextView etvar2;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
     //  etvar2=findViewById(R.id.etvar2);
       etvar=findViewById(R.id.etvar);
        btn=findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
             updateText();
            }
        });
    }
    public static URL concetnatestring(URL baseUrl, String extraPath) throws URISyntaxException, MalformedURLException {
        URI uri = baseUrl.toURI();
        String newPath = uri.getPath()  + extraPath;
        URI newUri = uri.resolve(newPath);
        return newUri.toURL();

    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    void updateText()  {


          String s=etvar.getText().toString();
       String U="https://api.github.com/search/users?q=";
        U=U+s;
      //  etvar2.setText(U);
       callnetwork(U);
    }

   @RequiresApi(api = Build.VERSION_CODES.KITKAT)
   void callnetwork(String url){
       OkHttpClient client = new OkHttpClient();
       Request request = new Request.Builder()
                   .url(url)
                   .build();
    client.newCall(request).enqueue(new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {

        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String result=response.body().string();
            ArrayList<Githubusers> users=parsejson(result);
            final GithubUserAdapter githubUserAdapter=new GithubUserAdapter(users);
           MainActivity.this.runOnUiThread(new Runnable() {
               @Override
               public void run() {
                   RecyclerView recyclerView=findViewById(R.id.rvUsers);
                   recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                   recyclerView.setAdapter(githubUserAdapter);

               }
           });


        }
    });
       }



    class NetworkTask extends AsyncTask<String, Void , String>{

        @Override
        protected String doInBackground(   String  ... strings) {
            String stringUrl=strings[0];
            try {
                URL url=new URL(stringUrl);

                HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
                InputStream inputStream= httpURLConnection.getInputStream();
                Scanner scanner=new Scanner(inputStream);

                scanner.useDelimiter("\\A");

                if(scanner.hasNext()){
                    String s=scanner.next();
                    return s;
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return "Failed to load";

        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        protected void onPostExecute(String s){
            super.onPostExecute(s);
            ArrayList<Githubusers> users=parsejson(s);
           GithubUserAdapter githubUserAdapter=new GithubUserAdapter(users);
            RecyclerView recyclerView=findViewById(R.id.rvUsers);
            recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
            recyclerView.setAdapter(githubUserAdapter);

        }


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    ArrayList<Githubusers> parsejson(String s){
        ArrayList<Githubusers> users=new ArrayList<Githubusers>();

        try {
            JSONObject root= new JSONObject(s);
            JSONArray items= root.getJSONArray("items");
            for (int i=0;i<items.length();i++){
                JSONObject object=items.getJSONObject(i);
                String login= object.getString("login");
                int id=object.getInt("id");
                String html_url=object.getString("html_url");
                Double score=object.getDouble("score");
                String avatar_url=object.getString("avatar_url");

                Githubusers gt=new Githubusers(login,id,html_url,score,avatar_url);
                users.add(gt);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return users;
    }
}
