package com.example.myapplication.Activity;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FreeActivity extends AppCompatActivity {
    private static final int REQUEST_LOCATION_PERMISSION = 1001;
    private static final int UPDATE_INTERVAL = 1000; // 업데이트 간격 (1초)

    TextView dateView;
    TextView cityView;
    TextView weatherView;
    TextView tempView;
    TextView pView;

    static RequestQueue requestQueue;
    String url;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable updateDataRunnable = new Runnable() {
        @Override
        public void run() {
            CurrentCall();
            handler.postDelayed(this, UPDATE_INTERVAL); // 일정 간격으로 Runnable 반복 실행
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free);

        dateView = findViewById(R.id.dateView);
        cityView = findViewById(R.id.cityView);
        weatherView = findViewById(R.id.weatherView);
        tempView = findViewById(R.id.tempView);
        pView = findViewById(R.id.pView);

        ImageButton button = findViewById(R.id.imageButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 버튼 클릭시 한번 데이터를 업데이트
                CurrentCall();
            }
        });

        ImageButton button2 = findViewById(R.id.freeCall);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 전화 앱을 열기 위한 Intent 생성
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:")); // 전화번호를 넣을 수도 있음
                startActivity(intent);
            }
        });

        ImageButton button3 = findViewById(R.id.freeCam);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 전화 앱을 열기 위한 Intent 생성
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivity(intent);
            }
        });

        // Volley를 쓸 때 큐가 비어있으면 새로운 큐 생성하기
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }


    }

    private void CurrentCall() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        } else {
            LocationListener locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    double cur_lat = location.getLatitude(); // 위도
                    double cur_lon = location.getLongitude(); // 경도
                    pView.setText(cur_lat + ", " + cur_lon);
                    Log.d("asdasd", cur_lat + ", " + cur_lon);
                    System.out.println(String.valueOf(cur_lat) + ", " + String.valueOf(cur_lon));
                    url = "https://api.openweathermap.org/data/2.5/weather?lat=" + cur_lat + "&lon=" + cur_lon + "&appid=d5adc8ce05e9e4cd506e8886305da11e";

                    // API 호출
                    fetchWeatherData(url);

                    // 위치 정보를 받은 후 리스너 제거
                    locationManager.removeUpdates(this);
                }

                //... (다른 메서드들은 동일)
            };

            // 최소 시간 간격이나 최소 거리 등을 설정하여 위치 업데이트 요청
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // GPS_PROVIDER와 NETWORK_PROVIDER 모두를 사용하여 위치 업데이트 요청
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            }
        }
    }

    private void fetchWeatherData(String url) {
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    // 시간 데이터 가져오기
                    long now = System.currentTimeMillis();
                    Date date = new Date(now);
                    SimpleDateFormat simpleDateFormatDay = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat simpleDateFormatTime = new SimpleDateFormat("HH:mm:ss");
                    String getDay = simpleDateFormatDay.format(date);
                    String getTime = simpleDateFormatTime.format(date);
                    String getDate = getDay + "\n" + getTime;
                    dateView.setText(getDate);

                    // API로 받은 JSON 파일을 JSONObject로 변환
                    JSONObject jsonObject = new JSONObject(response);

                    // 도시 키값 받기
                    String city = jsonObject.getString("name");
                    cityView.setText(city);

                    // 날씨 키값 받기
                    JSONArray weatherJson = jsonObject.getJSONArray("weather");
                    JSONObject weatherObj = weatherJson.getJSONObject(0);
                    String weather = weatherObj.getString("description");
                    weatherView.setText(weather);

                    // 기온 키값 받기
                    JSONObject tempK = new JSONObject(jsonObject.getString("main"));
                    double tempDo = (Math.round((tempK.getDouble("temp") - 273.15) * 100) / 100.0);
                    tempView.setText(tempDo + "°C");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley Error", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        request.setShouldCache(false);
        requestQueue.add(request);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                CurrentCall();
            } else {
                Log.d("Permission Denied", "Location permission denied");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 액티비티가 활성화되었을 때, 데이터 업데이트 시작
        handler.postDelayed(updateDataRunnable, UPDATE_INTERVAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 액티비티가 일시 중지되었을 때, 데이터 업데이트 중
    }
}