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

public class Information extends AppCompatActivity {

    Handler handler = new Handler();
    MainActivity main = new MainActivity();
    EditText etName, etCall, etId, etPwd, etHeight, etWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        etName = (EditText) findViewById(R.id.etName2);
        etCall = (EditText) findViewById(R.id.etCall2);
        etId = (EditText) findViewById(R.id.etId3);
        etPwd = (EditText) findViewById(R.id.etPwd3);
        etHeight = (EditText) findViewById(R.id.etHeight2);
        etWeight = (EditText) findViewById(R.id.etWeight2);

        dataSearch();

        Button btnSave = (Button) findViewById(R.id.btnSave2);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataUpdate();
            }
        });

        Button btnDelete = (Button) findViewById(R.id.btnDelete2);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataDelete();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        Button btnBack = (Button) findViewById(R.id.btnBack2);
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
                    URL setURL = new URL("Http://10.0.2.2/Information.php/");
                    HttpURLConnection http = (HttpURLConnection) setURL.openConnection();
                    http.setDefaultUseCaches(false);
                    http.setDoInput(true);
                    http.setRequestMethod("POST");
                    http.setRequestProperty("content-type", "application/x-www-form-urlencoded");

                    StringBuffer buffer = new StringBuffer();
                    buffer.append("name").append("=").append(main.cnum);
                    OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(),"UTF-8");
                    outStream.write(buffer.toString());
                    outStream.flush();
                    InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
                    final BufferedReader reader = new BufferedReader(tmp);
                    StringBuilder builder = new StringBuilder();
                    String str;

                    while((str = reader.readLine()) != null){
                        builder.append(str + "\n");
                    }
                    String resultData = builder.toString();
                    final String[] sResult = resultData.split("/");
                    handler.post(new Runnable(){
                        public void run() {

                            etName.setText(sResult[0]);
                            etCall.setText(sResult[1]);
                            etId.setText(sResult[2]);
                            etPwd.setText(sResult[3]);
                            etHeight.setText(sResult[4]);
                            etWeight.setText(sResult[5]);
                        }
                    });
                } catch(Exception e){
                    Log.e("dataSearch()","지정 에러 발생", e);
                }
            }
        }.start();
    }

    //수정 메서드
    public void dataUpdate(){
        new Thread(){
            public void run(){
                try{

                    String name = etName.getText().toString();
                    String call = etCall.getText().toString();
                    String id = etId.getText().toString();
                    String pwd = etPwd.getText().toString();
                    String height = etHeight.getText().toString();
                    String weight = etWeight.getText().toString();

                    URL setURL = new URL("Http://10.0.2.2/Cupdate.php/");
                    HttpURLConnection http = (HttpURLConnection) setURL.openConnection();
                    http.setDefaultUseCaches(false);
                    http.setDoInput(true);
                    http.setRequestMethod("POST");
                    http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                    StringBuffer buffer = new StringBuffer();
                    buffer.append("name").append("=").append(main.cnum).append("/").append(name).append("/").append(call).append("/").append(id).append("/")
                            .append(pwd).append("/").append(height).append("/").append(weight).append("/");
                    OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(),"UTF-8");
                    outStream.write(buffer.toString());
                    outStream.flush();
                    InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
                    final BufferedReader reader = new BufferedReader(tmp);
                    StringBuilder builder = new StringBuilder();
                    String str, result;

                    while((str = reader.readLine()) != null){
                        builder.append(str + "\n");
                    }
                    result = builder.toString();
                    String[] sResult = result.split("/");

                    handler.post(new Runnable(){
                        public void run() {
                            Toast.makeText(Information.this, name + "의 정보 수정",Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch(Exception e){
                    Log.e("dataUpdate()","지정 에러 발생", e);
                }
            }
        }.start();
    }

    //삭제 메서드
    public void dataDelete(){
        new Thread(){
            public void run(){
                try{
                    String name = etName.getText().toString();

                    URL setURL = new URL("Http://10.0.2.2/Cdelete.php/");
                    HttpURLConnection http = (HttpURLConnection) setURL.openConnection();
                    http.setDefaultUseCaches(false);
                    http.setDoInput(true);
                    http.setRequestMethod("POST");
                    http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                    StringBuffer buffer = new StringBuffer();
                    buffer.append("name").append("=").append(main.cnum);
                    OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(),"UTF-8");
                    outStream.write(buffer.toString());
                    outStream.flush();
                    InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
                    final BufferedReader reader = new BufferedReader(tmp);
                    StringBuilder builder = new StringBuilder();
                    String result = builder.toString();
                    final String[] sResult = result.split("/");

                    handler.post(new Runnable(){
                        public void run() {
                            Toast.makeText(Information.this, name + "의 정보 삭제",Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch(Exception e){
                    Log.e("dataDelete()","지정 에러 발생", e);
                }
            }
        }.start();
    }

}