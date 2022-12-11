package com.example.lostandfound.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
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

        Intent intent = getIntent();
        String time = intent.getStringExtra("time");
        String Rcm = intent.getStringExtra("Rcm");
        String Lcm = intent.getStringExtra("Lcm");
        String find = intent.getStringExtra("find");
        TextTime.setText(time);
        TextRCm.setText(Rcm);
        TextLCm.setText(Lcm);

        //////// 이부분 동작 잘 안됨//////////
        if(find == "none"){
            Check.setText("x");
        }
        else Check.setText("v");
        ////////////////////////////////////

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

        //imageView = (ImageView) findViewById(R.id.imageView);

        //Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //startActivityForResult(i, 0);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)    {  //새로 불리는 엑티비티여기서 이미지 삽입
        super.onActivityResult(requestCode, resultCode, data);
        //listView =(ListView)findViewById(R.id.listView);

        // 카메라 촬영을 하면 이미지뷰에 사진 삽입
        if(requestCode == 0 && resultCode == RESULT_OK) {
            //        // 데이터 원본 준비 : 7개의 사진 리스트
            // listView =(ListView)findViewById(R.id.listView);
            // ArrayList<MyItem> data = new ArrayList<MyItem>();
            //
            // data.add(new MyItem(imageBitmap, "x"));
            //


            //ArrayList<MyItem> Item = new ArrayList<MyItem>();
            // Bundle로 데이터를 입력
            Bundle extras = data.getExtras();

            // Bitmap으로 컨버전
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            //int count;
            //count = adapter.getCount();

            // 이미지뷰에 Bitmap으로 이미지를 입력 -> 리스트뷰의 imageView에 이미지 입력
            imageView.setImageBitmap(imageBitmap);
            //TextCm.setText("");  // 로그목록에서 intent받아서
            //TextTime.setText("");  // 로그목록에서 intent받아서


            //Item.add(new MyItem(imageBitmap, "x","5cm" ,"12:30"));

            //adapter= new MyAdapter(this, R.layout.item, Item);
            //listView.setAdapter(adapter);
        }
    }
}