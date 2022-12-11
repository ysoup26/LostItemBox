package com.example.lostandfound.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.lostandfound.R;

public class MainActivity extends AppCompatActivity {
    final static String TAG = "AndroidAPITest";
    EditText thingShadowURL, getLogsURL;
    //String listThingsURL,thingShadowURL, getLogsURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //listThingsURL = findViewById(R.id.listThingsURL);
        //thingShadowURL = findViewById(R.id.thingShadowURL);
        //getLogsURL = findViewById(R.id.getLogsURL);

        ///////////MKR1010 URI//////////////////
        //listThingsURL ="https://9r3v6uhoqd.execute-api.ap-northeast-2.amazonaws.com/prod/devices";
        //thingShadowURL ="https://9r3v6uhoqd.execute-api.ap-northeast-2.amazonaws.com/prod/devices/MyMKRWiFi1010";
        //getLogsURL ="https://9r3v6uhoqd.execute-api.ap-northeast-2.amazonaws.com/prod/devices/MyMKRWiFi1010/log";

        ////////////////MKR1010_2 URI//////////////////////
        //listThingsURL ="https://db4xmou1l3.execute-api.ap-northeast-2.amazonaws.com/hwag/devices";
        //thingShadowURL ="https://db4xmou1l3.execute-api.ap-northeast-2.amazonaws.com/hwag/devices/MyMKRWiFi1010_2";
        //getLogsURL ="https://db4xmou1l3.execute-api.ap-northeast-2.amazonaws.com/hwag/devices/projectData/log";


        ////////////////////iot통신 //////////////////////////////////////////
        //getLogsURL ="https://r9eh795567.execute-api.ap-northeast-2.amazonaws.com/lostItems";


        //사물 조회 버튼 누르면 ListThingsActivity로 이동
        /*Button listThingsBtn = findViewById(R.id.listThingsBtn);
        listThingsBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //String urlstr = listThingsURL.getText().toString(); // URI 읽어와서
                String urlstr = listThingsURL; // URI 읽어와서
                Log.i(TAG, "listThingsURL=" + urlstr);
                if (urlstr == null || urlstr.equals("")) {
                    Toast.makeText(MainActivity.this, "사물목록 조회 API URI 입력이 필요합니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(MainActivity.this, ListThingsActivity.class);
                //intent.putExtra("listThingsURL", listThingsURL.getText().toString()); //URI정보 파라메타로 전달
                intent.putExtra("listThingsURL", listThingsURL); //URI정보 파라메타로 전달
                startActivity(intent);
                //  new GetThings(MainActivity.this).execute();
                //  new GetThingShadow(MainActivity.this, "MyMKRWiFi1010").execute();

            }
        });*/

//        Button thingShadowBtn = findViewById(R.id.thingShadowBtn);
//        thingShadowBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //String urlstr = thingShadowURL.getText().toString();
//                String urlstr = thingShadowURL;
//                if (urlstr == null || urlstr.equals("")) {
//                    Toast.makeText(MainActivity.this, "사물상태 조회/변경 API URI 입력이 필요합니다.", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                Intent intent = new Intent(MainActivity.this, DeviceActivity.class);
//                intent.putExtra("thingShadowURL", thingShadowURL);
//                startActivity(intent);
//
//            }
//        });

        // 관리자 버튼
        Button listLogsBtn = findViewById(R.id.listLogsBtn);
        listLogsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String urlstr = getLogsURL.getText().toString();
                //String urlstr = getLogsURL;
//                if (urlstr == null || urlstr.equals("")) {
//                    Toast.makeText(MainActivity.this, "사물로그 조회 API URI 입력이 필요합니다.", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                Intent intent = new Intent(MainActivity.this, LogActivity.class);
                //intent.putExtra("https://r9eh795567.execute-api.ap-northeast-2.amazonaws.com/lostItems", getLogsURL);
                //Log.i(TAG,getLogsURL);
                startActivity(intent);
            }
        });

        // 사용자버튼
        Button listUserLogsBtn = findViewById(R.id.listUserLogsBtn);
        listUserLogsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String urlstr = getLogsURL.getText().toString();
                //String urlstr = getLogsURL;
//                if (urlstr == null || urlstr.equals("")) {
//                    Toast.makeText(MainActivity.this, "사물로그 조회 API URI 입력이 필요합니다.", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                Intent intent = new Intent(MainActivity.this, LogActivity.class);
                //intent.putExtra("https://r9eh795567.execute-api.ap-northeast-2.amazonaws.com/lostItems", getLogsURL);
                //Log.i(TAG,getLogsURL);
                startActivity(intent);
            }
        });
    }
}