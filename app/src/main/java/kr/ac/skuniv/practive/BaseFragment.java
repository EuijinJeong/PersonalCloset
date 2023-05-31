//package kr.ac.skuniv.practive;
//
//import static android.content.ContentValues.TAG;
//
//import kr.ac.skuniv.practive.BuildConfig;
//
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.android.volley.toolbox.Volley;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
///**
// * 이 클래스는 Main 화면을 구성하는 fragment들의 공통적으로 구현되어야 하는 기능들을 작성한 클래스입니다.
// *
// */
//
//public class BaseFragment extends Fragment {
//    protected GpsTracker gpsTracker;
//    protected TextView tv_temperature;
//    protected TextView tv_feelsLike;
//
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        // 위치 정보 가져오기
//        gpsTracker = new GpsTracker(requireContext());
//        double latitude = gpsTracker.getLatitude();
//        double longitude = gpsTracker.getLongitude();
//
//        // 날씨 정보 가져오기
//        String apikey = "56c3420cfd9a05f05c2235f752efb84d";
//        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&apikey=" + apikey + "&units=metric";
//
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
//                response -> {
//                    try {
//                        // 날씨 정보 처리
//                        JSONObject mainObject = response.getJSONObject("main");
//                        double temperature = mainObject.getDouble("temp");
//                        int temp = (int) temperature; // 화면에 명료한 표시를 위해 정보를 정수 형태로 가공함.
//                        double feelsLike = mainObject.getDouble("feels_like");
//                        int feelslike = (int) feelsLike;
//                        int humidity = mainObject.getInt("humidity");
//                        int pressure = mainObject.getInt("pressure");
//
//                        // 처리한 날씨 정보 출력
//                        Log.d(TAG, "Temperature: " + temperature + "°C");
//                        Log.d(TAG, "Feels Like: " + feelsLike + "°C");
//                        Log.d(TAG, "Humidity: " + humidity + "%");
//                        Log.d(TAG, "Pressure: " + pressure + "hPa");
//
//                        // 각 Fragment에서 화면에 표시하기 위해 변수에 저장
//                        tv_temperature = view.findViewById(R.id.temperature);
//                        tv_temperature.setText(temp +"°C");
//                        tv_feelsLike = view.findViewById(R.id.feelsLike);
//                        tv_feelsLike.setText("체감 "+feelslike+"°C");
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }, error -> {
//            error.printStackTrace();
//        });
//
//        // RequestQueue에 API 호출 요청 추가
//        RequestQueue queue = Volley.newRequestQueue(requireContext());
//        queue.add(request);
//    }
//}
