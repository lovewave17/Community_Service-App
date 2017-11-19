package com.example.hansei.pushapp;

import android.content.SharedPreferences;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.os.Build;
import android.app.NotificationManager;
import android.app.NotificationChannel;
import android.widget.Button;
import android.widget.Toast;
import android.view.View;
import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    Button button;
    private WebView mWebView; //웹뷰
    private WebSettings mWebSettings; //웹뷰세팅
    //    private static final String TAG = "MainActivity";
    String app_server_url = "http://192.168.2.129/token.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWebView = (WebView)findViewById(R.id.webview); //레이어와 연결
        mWebView .setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.FCM_PREF), Context.MODE_PRIVATE);
                final String token = sharedPreferences.getString(getString(R.string.FCM_TOKEN), " ");
                StringRequest stringRequest = new StringRequest(Request.Method.POST, app_server_url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText (getApplicationContext(), " Device Token transferred", Toast.LENGTH_SHORT).show();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText (getApplicationContext(), " Device Token not transferred", Toast.LENGTH_SHORT).show();

                    }
                })
                {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError{
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("fcm_token",token);

                        return params;
                    }
                };
                MySingleton.getmInstance(MainActivity.this).addToRequestque(stringRequest);
            }
        }); // 클릭시 새창 안뜨게

        mWebSettings = mWebView.getSettings(); //세부 세팅 등록
        mWebSettings.setDefaultTextEncodingName("UTF-8"); // 언어설정
        mWebSettings.setJavaScriptEnabled(true); // 자바스크립트 사용 허용
        mWebSettings.setSupportZoom(true);
        mWebSettings.setBuiltInZoomControls(true);// 줌컨트롤 제약
        mWebSettings.setDisplayZoomControls(false);

        mWebView.loadUrl("http://mahndoi.dothome.co.kr/Service_Intro_Page2.html");

//        button = (Button) findViewById(R.id.button);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.FCM_PREF), Context.MODE_PRIVATE);
//                final String token = sharedPreferences.getString(getString(R.string.FCM_TOKEN), " ");
//                StringRequest stringRequest = new StringRequest(Request.Method.POST, app_server_url,
//                        new Response.Listener<String>() {
//                            @Override
//                            public void onResponse(String response) {
//
//                            }
//                        }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                    }
//                })
//                {
//                    @Override
//                    protected Map<String, String> getParams() throws AuthFailureError{
//                        Map<String, String> params = new HashMap<String, String>();
//                        params.put("fcm_token",token);
//
//                        return params;
//                    }
//                };
//                MySingleton.getmInstance(MainActivity.this).addToRequestque(stringRequest);
//            }
//        });
    }
}

