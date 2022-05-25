package com.example.aihealthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class NewCustomer extends AppCompatActivity {

    EditText etName, etCall, etId2, etPwd2, etPwd3, etHeight, etWeight;
    String name, call, id, pwd, height, weight, ability;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_customer);

        //아이디 가져오기
        etName = (EditText) findViewById(R.id.etName);
        etCall = (EditText) findViewById(R.id.etCall);
        etId2 = (EditText) findViewById(R.id.etId2);
        etPwd2 = (EditText) findViewById(R.id.etPwd2);
        etPwd3 = (EditText) findViewById(R.id.etPwd3);
        etHeight = (EditText) findViewById(R.id.etHeight);
        etWeight = (EditText) findViewById(R.id.etWeight);

        //운동능력 스피너 가져오기
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.work,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                ability = parent.getItemAtPosition(pos).toString();
            }
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        //날짜 가져오기(실패)
//        CalendarView calendarView = findViewById(R.id.calendarView);
//        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
//            @Override
//            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
//                date = year + "/" + (month+1) + "/" + day;
//            }
//        });
        
        //저장 버튼 클릭시 이동 및 데이터 저장
        Button btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = etName.getText().toString();
                call = etCall.getText().toString();
                id = etId2.getText().toString();
                pwd = etPwd2.getText().toString();
                height = etHeight.getText().toString();
                weight = etWeight.getText().toString();
                dataInsert(name, call, id, pwd, height, weight, ability);

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

    }

    //데이터 저장하는 메서드
    public void dataInsert(String name, String call, String id, String pwd, String height, String weight, String ability){
        new Thread(){
            public void run(){
                try{
                    URL setURL = new URL("Http://10.0.2.2/insert2.php/");
                    HttpURLConnection http = (HttpURLConnection) setURL.openConnection();
                    http.setDefaultUseCaches(false);
                    http.setDoInput(true);
                    http.setRequestMethod("POST");
                    http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                    StringBuffer buffer = new StringBuffer();
                    buffer.append("name").append("=").append(name).append("/").append(call).append("/").append(id).append("/").append(pwd).append("/")
                            .append(height).append("/").append(weight).append("/").append(ability).append("/");
                    OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(),"UTF-8");
                    outStream.write(buffer.toString());
                    outStream.flush();
                    InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");

                    String str;
                    str = buffer.toString();
                    Log.e("",str);
                    final BufferedReader reader = new BufferedReader(tmp);
                    while(reader.readLine() != null){
                        System.out.println(reader.readLine());
                    }
                } catch(Exception e){
                    Log.e("dataInsert()","지정 에러 발생", e);
                }
            }
        }.start();
    }
}