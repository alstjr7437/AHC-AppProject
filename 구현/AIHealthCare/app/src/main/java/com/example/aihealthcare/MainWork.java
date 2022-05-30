package com.example.aihealthcare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainWork extends AppCompatActivity {

    MainActivity main = new MainActivity();
    Handler handler = new Handler();
    static String rname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_work);

        dataSearch();       //시작할때 루틴 받아와서 넣도록

        TextView tvName2 = (TextView) findViewById(R.id.tvName2);
        tvName2.setText(main.name);     //맨위에 회원 이름 나오도록


        Button btn1 = (Button) findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), WorkList.class);
                startActivity(intent);
            }
        });

        Button btn7 = (Button) findViewById(R.id.btn7);
        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        Button btn8 = (Button) findViewById(R.id.btn8);
        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Information.class);
                startActivity(intent);
            }
        });
    }

    //조회하여 루틴 보여주는 메서드
    public void dataSearch(){
        new Thread(){
            public void run(){
                try{
                    URL setURL = new URL("Http://10.0.2.2/MainSearch.php/");
                    HttpURLConnection http = (HttpURLConnection) setURL.openConnection();
                    http.setDefaultUseCaches(false);
                    http.setDoInput(true);
                    http.setRequestMethod("POST");
                    http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                    StringBuffer buffer = new StringBuffer();
                    buffer.append("name").append("=").append(main.name);
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

                    //핸들러로 안에 어댑터붙여넣고 하기
                    handler.post(new Runnable(){
                        public void run() {

                            ListView listview;
                            ListViewAdapter adapter;

                            //adapter 생성
                            adapter = new ListViewAdapter();

                            //리스트 뷰 들고오기 및 adapter달기
                            listview = (ListView) findViewById(R.id.listview);
                            listview.setAdapter(adapter);
                            
                            //반복문을 이용한 list 붙이기
                            for(int i = 0; i < sResult.length; i++){
                                String a = Integer.toString(i+1);
                                adapter.addItem(a, sResult[i]);

                            }
                            //list 클릭시
                            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView parent, View v, int position, long id) {
                                    ListViewItem item = (ListViewItem) parent.getItemAtPosition(position);
                                    String title = item.getTitle();
                                    rname = title;

                                    Intent intent = new Intent(getApplicationContext(), RoutineList.class);
                                    startActivity(intent);
                                }
                            });


                        }
                    });

                } catch(Exception e){
                    Log.e("dataSearch()","지정 에러 발생", e);
                }
            }
        }.start();
    }
}