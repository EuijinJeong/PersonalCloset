package kr.ac.skuniv.practive;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * 메인화면의 정보를 나타내는 것을 담당하는 클래스
 */

public class MainActivity extends AppCompatActivity {
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    // 사용자의 주소 관련한 변수 선언
    private GpsTracker gpsTracker; // 현재 위치를 가져와 주소로 변환하는 처리기능을 하는 객체 불러옴
    double latitude, longitude;
    private String address; // 사용자의 주소를 저장하는 변수

    // viewpager2 관련한 객체 선언
    private ViewPager2 mpager;
    private MainPagerAdapter mainPagerAdaapter;

    // OpenWeatherMap의 주소와 api 키 값 변수에 저장
//    private static final String url = "https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&apikey={apikey}";
    private static final String apikey = "56c3420cfd9a05f05c2235f752efb84d"; // 추후 환경변수로 수정해야 함

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 현재 시간 정보를 가져오는 코드
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        /**
         * 가로 슬라이드 뷰 Fragment list 작성
         */

        // fragment1의 순서는 현재 시간의 정보를 나타내므로 항상 1번째 순서 고정
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(Fragment1.newInstance(0));
        // 나머지 fragment 순서 설정
        if (hourOfDay >= 5 && hourOfDay < 12) {
            // 오전 시간에 해당하는 fragment 순서
            fragments.add(Fragment4.newInstance(3));
            fragments.add(Fragment2.newInstance(1));
            fragments.add(Fragment3.newInstance(2));
        } else if (hourOfDay >= 12 && hourOfDay < 18) {
            // 낮 시간에 해당하는 fragment 순서
            fragments.add(Fragment2.newInstance(1));
            fragments.add(Fragment3.newInstance(2));
            fragments.add(Fragment4.newInstance(3));
        } else {
            // 저녁 시간에 해당하는 fragment 순서
            fragments.add(Fragment2.newInstance(1));
            fragments.add(Fragment3.newInstance(2));
            fragments.add(Fragment4.newInstance(3));
        }

        // ViewPager2 load
        mpager = (ViewPager2) findViewById(R.id.viewpager);
        mainPagerAdaapter = new MainPagerAdapter(this, fragments); // MainPagerAdapter 객체 생성
        mpager.setAdapter(mainPagerAdaapter);


        // 사용자 권한 확인
        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting();
        } else {
            checkRunTimePermission();
        }

        gpsTracker = new GpsTracker(MainActivity.this);
        if (gpsTracker != null) {
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();
            Log.d(TAG, "latitude: " + latitude);
            Log.d(TAG, "longitude: " + longitude); // 여기까지는 잘 실행
        } else {
            // gpsTracker가 null인 경우 처리할 로직 작성
            Log.d(TAG, "gpsTracker == null!");
        }

        // 주소에서 도시명만 추출
        if (address != null) {
            address = getCurrentAddress(latitude, longitude);
            String[] addressParts = address.split(",");
            String city = addressParts[1].trim();
            Log.d(TAG, "city name: " + city);
        } else {
            Log.d(TAG, "address is null"); // 이 부분이 계속 실행
        }

//        tv_address.setText(city); // 사용자의 위치를 표시한다.

        // 현재 시간 가져오기
//        tv_time = findViewById(R.id.userTime);
        SimpleDateFormat sdf = new SimpleDateFormat("a h:mm", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
//        tv_time.setText("지금, "+currentDateandTime);

        // 사용자의 날씨를 받아오는 메소드 호출
        getWeather();

    }

    public void getWeather() {
        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&apikey=" + apikey + "&units=metric";

        /**
         * volley 라이브러리를 통해 json형태의 데이터를 자바로 변환합니다.
         */
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        // 날씨 정보 처리
                        JSONObject mainObject = response.getJSONObject("main");
                        double temperature = mainObject.getDouble("temp");
                        int temp = (int) temperature; // 화면에 명료한 표시를 위해 정보를 정수 형태로 가공함.
                        double feelsLike = mainObject.getDouble("feels_like");
                        int feelslike = (int) feelsLike;
//                        double temp_min = mainObject.getDouble("temp_min");
//                        double temp_max = mainObject.getDouble("temp_max");
                        int humidity = mainObject.getInt("humidity");
                        int pressure = mainObject.getInt("pressure");

                        // 처리한 날씨 정보 출력
                        Log.d(TAG, "Temperature: " + temperature + "°C");
                        Log.d(TAG, "Feels Like: " + feelslike + "°C");
                        Log.d(TAG, "Humidity: " + humidity + "%");
                        Log.d(TAG, "Pressure: " + pressure + "hPa");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            error.printStackTrace();
        });

        // RequestQueue에 API 호출 요청 추가
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    /**
     * ActivityCompat.requestPermissions를 사용한 퍼미션 요청의 결과를 리턴받는 메소드입니다.
     */

    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        super.onRequestPermissionsResult(permsRequestCode, permissions, grandResults);
        if (permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면

            boolean check_result = true;


            // 모든 퍼미션을 허용했는지 체크합니다.

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }


            if (check_result) {

                //위치 값을 가져올 수 있음
                ;
            } else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {

                    Toast.makeText(MainActivity.this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
                    finish();


                } else {

                    Toast.makeText(MainActivity.this, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();

                }
            }

        }
    }

    void checkRunTimePermission() {

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


            // 3.  위치 값을 가져올 수 있음


        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(MainActivity.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);


            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }

    }


    public String getCurrentAddress(double latitude, double longitude) {

        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    7);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";

        }


        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";

        }

        Address address = addresses.get(0);
        return address.getAddressLine(0).toString() + "\n";

    }


    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {

                        Log.d("@@@", "onActivityResult : GPS 활성화 되있음");
                        checkRunTimePermission();
                        return;
                    }
                }

                break;
        }
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

}
