package com.example.lostandfound.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lostandfound.R;

public class GalleryDetailActivity extends AppCompatActivity {
    Button btnCamera;
    ImageView imageView;
    TextView TextLCm;
    TextView TextRCm;
    TextView TextTime;
    EditText Check;
    TextView CheckText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_detail);
        // 디자인 정의
        btnCamera = (Button) findViewById(R.id.btnPhoto);
        imageView = (ImageView) findViewById(R.id.imageView);
        TextLCm = (TextView) findViewById(R.id.textLCm);
        TextRCm = (TextView) findViewById(R.id.textRCm);
        TextTime = (TextView) findViewById(R.id.textTime);
        Check = (EditText) findViewById(R.id.editCheck);
        CheckText = (TextView) findViewById(R.id.textCheck);
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        Log.i("detail","detailname="+name);   // detailname=admin

        String time = intent.getStringExtra("time");
        String Rcm = intent.getStringExtra("Rcm");
        String Lcm = intent.getStringExtra("Lcm");
        String find = intent.getStringExtra("find");

        if(name.equals("admin")) { //관리자용 상세정보
            TextTime.setText(time);
            TextRCm.setText(Rcm);
            TextLCm.setText(Lcm);
            //CheckText.setVisibility(View.GONE);
            CheckText.setVisibility(View.INVISIBLE);
            Check.setVisibility(View.VISIBLE);

            if (find.equals("none")) {
                Check.setText("x");
            } else Check.setText("v");
        }
        else if (name.equals("user")) { //사용자용 상세정보
            btnCamera.setVisibility(View.GONE);
            //btnCamera.setEnabled(false); // or btnCamera.setVisibility(View.GONE)
            TextTime.setText(time);
            TextRCm.setText(Rcm);
            TextLCm.setText(Lcm);
            CheckText.setVisibility(View.VISIBLE);
            Check.setVisibility(View.INVISIBLE);

            if (find.equals("none")) { ////이부분 edit -> text로 바꿔야함
                CheckText.setText("x");
            } else CheckText.setText("v");
        }

        //listView =(ListView)findViewById(R.id.listView);
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    // 카메라촬영 클릭 이벤트
                    case R.id.btnPhoto:
                        // 카메라 기능을 Intent
                        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(i, 0);
                        break;
                }
            }
        });   // 카메라 버튼 누르면 , 저장
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)    {  //새로 불리는 엑티비티여기서 이미지 삽입
        super.onActivityResult(requestCode, resultCode, data);

        // 카메라 촬영을 하면 이미지뷰에 사진 삽입
        if(requestCode == 0 && resultCode == RESULT_OK) {

            // Bundle로 데이터를 입력
            Bundle extras = data.getExtras();
            // Bitmap으로 컨버전
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            // 이미지뷰에 Bitmap으로 이미지를 입력 -> 리스트뷰의 imageView에 이미지 입력
            imageView.setImageBitmap(imageBitmap);

        }
    }
}