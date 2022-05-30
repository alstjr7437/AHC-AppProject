package com.example.aihealthcare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class RoutineList extends AppCompatActivity {

    MainWork mainWork = new MainWork();
    Handler handler = new Handler();
    static String[] Result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine_list);

        dataSearch();

        TextView tvName2 = (TextView) findViewById(R.id.tvRname);
        tvName2.setText(mainWork.rname + " 루틴");

        Button btnBack = (Button) findViewById(R.id.btnBack3);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainWork.class);
                startActivity(intent);
            }
        });
    }

    //조회 메서드
    public void dataSearch(){
        new Thread(){
            public void run(){
                try{
                    URL setURL = new URL("Http://10.0.2.2/RoutineSearch.php/");
                    HttpURLConnection http = (HttpURLConnection) setURL.openConnection();
                    http.setDefaultUseCaches(false);
                    http.setDoInput(true);
                    http.setRequestMethod("POST");
                    http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                    Log.e("",mainWork.rname);
                    StringBuffer buffer = new StringBuffer();
                    buffer.append("name").append("=").append(mainWork.rname);
                    OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(),"UTF-8");
                    outStream.write(buffer.toString());
                    outStream.flush();
                    InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
                    final BufferedReader reader = new BufferedReader(tmp);
                    StringBuilder builder = new StringBuilder();

                    //넘겨온 값 가져오기
                    String str;
                    while((str = reader.readLine()) != null){
                        builder.append(str + "\n");
                    }
                    String resultData = builder.toString();
                    int idx = resultData.indexOf("<");
                    String sRes = resultData.substring(0,idx);
                    final String[] sResult = sRes.split("/");

                    handler.post(new Runnable(){
                        public void run() {

                            ListView listview;
                            ListViewAdapter adapter;

                            //adapter 생성
                            adapter = new ListViewAdapter();

                            //리스트 뷰 들고오기 및 adapter달기
                            listview = (ListView) findViewById(R.id.listview2);
                            listview.setAdapter(adapter);

                            for(int i = 0; i < sResult.length; i++){
                                Log.e("",sResult[i]);
                                String a = Integer.toString(i+1);
                                adapter.addItem(a, sResult[i]);

                            }

                        }
                    });

                } catch(Exception e){
                    Log.e("dataSearch()","지정 에러 발생", e);
                }
            }
        }.start();
    }

}