package com.example.lostandfound.ui.apicall;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lostandfound.R;
import com.example.lostandfound.httpconnection.GetRequest;
import com.example.lostandfound.ui.GalleryDetailActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

//아이템 하나만 받아옴(임시)
public class GetDBItem extends GetRequest {
    final static String TAG = "AndroidAPITest";
    String urlStr;
    String name;
    public GetDBItem(Activity activity, String urlStr,String name) {
        super(activity);
        this.urlStr = urlStr;
        this.name = name;
    }

    @Override
    protected void onPreExecute() {
        try {
            Log.i(TAG,"urlStr="+urlStr);//+params);
            url = new URL(urlStr);//+params);

        } catch (MalformedURLException e) {
            Toast.makeText(activity,"URL is invalid:"+urlStr, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        Log.i(TAG,"onPreExecute");
        TextView message = activity.findViewById(R.id.message2);
        message.setText("조회중...");
    }

    @Override
    protected void onPostExecute(String jsonString) {
        TextView message = activity.findViewById(R.id.message2);
        if (jsonString == null) {
            message.setText("로그 없음");
            return;
        }
        message.setText("");
        ArrayList<Tag> arrayList = getArrayListFromJSONString(jsonString);

        ////////////////
        //String time = getStringFromJSONString(jsonString);   // 시간받아옴

        Log.i("T","string"+arrayList.toString());
        //[[2022-11-24 23:05:18] Finded: none,Left: 1, Right: 1 , [2022-11-24 23:05:12] Finded: none,Left: 1, Right: 1 , [2022-11-24 23:05:28] Finded: none,Left: 1, Right: 1 , [2022-11-24 23:05:07] Finded: none,Left: 1, Right: 1 , [2022-11-24 23:05:23] Finded: none,Left: 1, Right: 1 ,

        //Log.i("stime","time"+arrayList.get(0)); // time[2022-11-24 23:05:18] Finded: none,Left: 1, Right: 1

        //Log.i("time","time : " +time); // time : none    ,   time : 2022-11-24 23:05:18   성공!!!!!!!!!!!1

        final ArrayAdapter adapter = new ArrayAdapter(activity,
                android.R.layout.simple_list_item_1,
                arrayList.toArray());
        ListView txtList = activity.findViewById(R.id.logList);
        txtList.setAdapter(adapter);
        txtList.setDividerHeight(10);

        // 목록 누르면 상세정보로 이동
        txtList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Tag thing = (Tag)adapterView.getAdapter().getItem(i);
                Log.i("Ting","Thing: "+ thing);   // Thing: [2022-11-24 23:05:12] Finded: none,Left: 1, Right: 1
                //String time = getStringFromJSONString(jsonString);
                Log.i("Tingtime","tingtime: "+ thing.timestamp);  // Tingtime: tingtime: 2022-11-24 23:05:07

                Intent intent = new Intent(activity, GalleryDetailActivity.class);
                //Intent intent = new Intent(activity, DeviceActivity.class );  // 0
                Log.i("GetDB","GetDBname="+name);  // GetDBname=admin
                intent.putExtra("name",name); // 관리자 , 사용자 전달

                // 시간, L CM , R CM 전달
                intent.putExtra("time", thing.timestamp);  // 시간전달
                intent.putExtra("Rcm",thing.right);  // 오른쪽 cm
                intent.putExtra("Lcm",thing.left);  // 왼쪽 cm
                intent.putExtra("find",thing.finded);  // 찾았는지 여부
                activity.startActivity(intent);
            }
        });
    }

    protected ArrayList<Tag> getArrayListFromJSONString(String jsonString) {
        ArrayList<Tag> output = new ArrayList();
        try {
            // 처음 double-quote와 마지막 double-quote 제거
            jsonString = jsonString.substring(1,jsonString.length()-1);
            // \\\" 를 \"로 치환
            jsonString = jsonString.replace("\\\"","\"");

            Log.i("kk", "jsonString!="+jsonString);

            JSONObject root = new JSONObject(jsonString);
            JSONArray jsonArray = root.getJSONArray("data");

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = (JSONObject)jsonArray.get(i);

                //////////////////iot클라우드 ////////////////
                /*
                Tag thing = new Tag(jsonObject.getString("dustsin"),
                                    jsonObject.getString("dustsout"),
                                    jsonObject.getString("fanled"),
                                    jsonObject.getString("fan"),
                                    jsonObject.getString("timestamp"));*/



                Tag thing = new Tag(jsonObject.getString("finded"),
                        jsonObject.getString("left"),
                        jsonObject.getString("right"),
                        jsonObject.getString("timestamp"));

                output.add(thing);
            }

        } catch (JSONException e) {
            //Log.e(TAG, "Exception in processing JSONString.", e);
            e.printStackTrace();
        }
        return output;
    }

    class Tag {

        ////////////////////iot클라우드 ////////////////
        /*String dustsin;
        String dustsout;
        String fanled;
        String fan;
        String timestamp; */

        String finded;
        String left;
        String right;
        String timestamp;

        public Tag(String nfinded,String nleft,String nright,String ntimestamp) {
            //String ndust_s_in,String ndust_s_out,String nfan_led,String nfan,String ntimestamp
            //////////////////iot클라우드 ////////////////
            /*dustsin=ndust_s_in;
            dustsout=ndust_s_out;
            fanled=nfan_led;
            fan=nfan;
            timestamp=ntimestamp;*/

            finded=nfinded;
            left=nleft;
            right=nright;
            timestamp=ntimestamp;

        }

        public String toString() {
            //return String.format("[%s] Temperature1: %s,emperature1: %s, LED: %s", timestamp, temperature1,temperature2, LED);// 타임, 내부농도, 외부농도, 팬 세기

            ////////////////////iot클라우드 ////////////////
            /*return String.format("[%s] Dustsin: %s,Dustsout: %s, Fan: %s, Fanled: %s"
                    , timestamp, dustsin,dustsout,fan,fanled);*/


            return String.format("[%s] Finded: %s,Left: %s, Right: %s "
                    , timestamp, finded,left,right);

        }
    }

    //////////////
    protected String getStringFromJSONString(String jsonString, int i) {
        String output = null;
        try {
            // 처음 double-quote와 마지막 double-quote 제거
            jsonString = jsonString.substring(1,jsonString.length()-1);
            // \\\" 를 \"로 치환
            jsonString = jsonString.replace("\\\"","\"");

            Log.i("kk", "jsonString!="+jsonString);

            JSONObject root = new JSONObject(jsonString);
            JSONArray jsonArray = root.getJSONArray("data");


            JSONObject jsonObject = (JSONObject)jsonArray.get(i);

            String stime = jsonObject.getString("timestamp");
            output = stime;
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }

        return output;
    }
}
