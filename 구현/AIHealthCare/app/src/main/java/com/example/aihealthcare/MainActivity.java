package com.example.aihealthcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends AppCompatActivity {
    EditText etId, etPwd;
    String Id, Pwd;
    Handler handler = new Handler();
    static String name;
    static String cnum;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etId = (EditText) findViewById(R.id.etId);
        etPwd = (EditText) findViewById(R.id.etPwd);

        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        Button btnNew = (Button) findViewById(R.id.btnNew);

        //로그인 버튼 클릭시 데이터 조회 및 이동
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataSearch();
            }
        });
        //회원가입 버튼 클릭시 이동
        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NewCustomer.class);
                startActivity(intent);
            }
        });
    }

    //조회하여 로그인하는 메서드
    public void dataSearch(){
        new Thread(){
            public void run(){
                try{
                    Id = etId.getText().toString();
                    Pwd = etPwd.getText().toString();

                    URL setURL = new URL("Http://10.0.2.2/Login.php/");
                    HttpURLConnection http = (HttpURLConnection) setURL.openConnection();
                    http.setDefaultUseCaches(false);
                    http.setDoInput(true);
                    http.setRequestMethod("POST");
                    http.setRequestProperty("content-type", "application/x-www-form-urlencoded");

                    StringBuffer buffer = new StringBuffer();
                    buffer.append("name").append("=").append(Id).append("/").append(Pwd).append("/");
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
                    final String[] sResult = resultData.split("/");

                    //아이디와 비밀번호 비교해서 로그인하기
                    if(Id.equals(sResult[0]) && Pwd.equals(sResult[1])){
                        handler.post(new Runnable(){
                            public void run() {
                                name = sResult[2];
                                cnum = sResult[3];
                                Intent intent = new Intent(getApplicationContext(), MainWork.class);
                                startActivity(intent);
                            }
                        });
                    }
                    //틀릴 경우 토스트로 뜨게하기
                    else {
                        handler.post(new Runnable(){
                            public void run() {
                                Toast.makeText(MainActivity.this, "아이디 비밀번호를 다시 입력하세요!",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch(Exception e){
                    Log.e("dataSearch()","지정 에러 발생", e);
                }
            }
        }.start();
    }
}