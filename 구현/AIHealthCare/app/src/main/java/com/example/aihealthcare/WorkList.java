package com.example.aihealthcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class WorkList extends AppCompatActivity {

    EditText etSearch;
    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_list);

        etSearch = (EditText) findViewById(R.id.etSearch);
        dataSearch();       //시작할때 루틴 받아와서 넣도록

        Button btnBack = (Button) findViewById(R.id.btnBack4);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainWork.class);
                startActivity(intent);
            }
        });
        Button btnSearch = (Button) findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                workSearch();
            }
        });

    }

    //조회 메서드
    public void dataSearch(){
        new Thread(){
            public void run(){
                try{
                    URL setURL = new URL("Http://10.0.2.2/WorkSearch.php/");
                    HttpURLConnection http = (HttpURLConnection) setURL.openConnection();
                    http.setDefaultUseCaches(false);
                    http.setDoInput(true);
                    http.setRequestMethod("POST");
                    http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                    OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(),"UTF-8");
                    outStream.flush();

                    InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuilder builder = new StringBuilder();

                    //넘겨온 값 가져오기
                    String str;
                    while((str = reader.readLine()) != null){
                        builder.append(str + "\n");
                    }
                    String resultData = builder.toString();
                    //< 이후를 끊어서 뒷부분 버리기
                    int idx = resultData.indexOf("<");
                    String sRes = resultData.substring(0,idx);
                    final String[] sResult = sRes.split("/");

                    handler.post(new Runnable(){
                        public void run() {

                            ListView listview;
                            ListViewAdapter2 adapter;

                            //adapter 생성
                            adapter = new ListViewAdapter2();

                            //리스트 뷰 들고오기 및 adapter달기
                            listview = (ListView) findViewById(R.id.listview2);
                            listview.setAdapter(adapter);

                            for(int i = 0; i < sResult.length; i = i+4){
                                Log.e("",sResult[i]);
                                String a = Integer.toString(i+1);
                                adapter.addItem(sResult[i], sResult[i+1], sResult[i+2], sResult[i+3]);
                            }
                        }
                    });

                } catch(Exception e){
                    Log.e("dataSearch()","지정 에러 발생", e);
                }
            }
        }.start();
    }

    //조회 메서드
    public void workSearch(){
        new Thread(){
            public void run(){
                try{
                    String search = etSearch.getText().toString();
                    System.out.println(search);
                    URL setURL = new URL("Http://10.0.2.2/WorkSearch2.php/");
                    HttpURLConnection http = (HttpURLConnection) setURL.openConnection();
                    http.setDefaultUseCaches(false);
                    http.setDoInput(true);
                    http.setRequestMethod("POST");
                    http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                    Log.e("",search);
                    StringBuffer buffer = new StringBuffer();
                    buffer.append("name").append("=").append(search);
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
                    //< 이후를 끊어서 뒷부분 버리기
                    int idx = resultData.indexOf("<");
                    String sRes = resultData.substring(0,idx);
                    final String[] sResult = sRes.split("/");

                    System.out.println(resultData);
                    handler.post(new Runnable(){
                        public void run() {

                            ListView listview;
                            ListViewAdapter2 adapter;

                            //adapter 생성
                            adapter = new ListViewAdapter2();

                            //리스트 뷰 들고오기 및 adapter달기
                            listview = (ListView) findViewById(R.id.listview2);
                            listview.setAdapter(adapter);

                            for(int i = 0; i < sResult.length; i = i+4){
                                Log.e("",sResult[i]);
                                String a = Integer.toString(i+1);
                                adapter.addItem(sResult[i], sResult[i+1], sResult[i+2], sResult[i+3]);
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