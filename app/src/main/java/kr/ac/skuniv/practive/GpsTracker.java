package kr.ac.skuniv.practive;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

import androidx.core.content.ContextCompat;

import android.util.Log;


/**
 * GpsTracker는 현재 위치를 가져와 주소로 변환하는 처리를 한다.
 * 네트워크와 GPS를 동시 사용하여 사용자의 주소 업데이트를 수행한다.
 */

public class GpsTracker extends Service implements LocationListener {

    private final Context mContext; // GpsTracker 클래스의 멤버 변수로서, 해당 클래스에서 사용되는 Context 객체를 저장하는 변수입니다.
    Location location;
    double latitude;
    double longitude;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    protected LocationManager locationManager;


    public GpsTracker(Context context) {
        this.mContext = context;
        getLocation(); // 위치정보를 가져오는 역할을 하는 함수 호출
    }


    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER); // 변수가 true일 경우, 위치정보 받아옴
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER); // 변수가 true일 경우, 위치정보 받아옴

            if (!isGPSEnabled && !isNetworkEnabled) { // 위치 정보를 받을 수 없는 경우를 처리 이 경우에는 null을 반환 (이 경우가 계속 실행되는 듯)

            } else {

                // 아래의 변수들을 사용하여 권한이 부여되었는지 확인
                int hasFineLocationPermission = ContextCompat.checkSelfPermission(mContext,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(mContext,
                        Manifest.permission.ACCESS_COARSE_LOCATION);


                if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                        hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) { // 위치 권한이 모두 승인되어 있는 경우, 아무 작업도 하지 않습니다.
                    ;
                } else
                    return null; // 권한이 없으면 null 반환함.


                if (isNetworkEnabled) { // 네트워크로부터 위치 정보를 가져옴

                    // requestLocationUpdates 함수는 LocationListener를 등록하여, 위치 정보가 업데이트 될 때마다 onLocationChanged() 콜백 함수를 호출하도록 요청한다.
                    // 이때, MIN_TIME_BW_UPDATES와 MIN_DISTANCE_CHANGE_FOR_UPDATES 값에 따라 위치 정보 업데이트 주기와 거리 필터링을 설정할 수 있다.
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null) {

                        // getLastKnownLocation 함수를 사용하여, 마지막으로 알려진 위치 정보를 가져와서 location 변수에 저장한다.
                        // 이때 LocationManager.NETWORK_PROVIDER를 사용한 이유는 GPS 신호가 잡히지 않는 실내에서도 위치 정보를 가져올 수 있기 때문이다.
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }


                if (isGPSEnabled) // GPS를 통해 위치정보를 가져옴
                {
                    if (location == null) {
                        // 위치 권한이 승인되어 있는 경우, requestLocationUpdates() 메서드를 사용하여 위치 업데이트를 요청합니다.
                        // requestLocationUpdates() 메서드는 위치 정보를 받을 수 있는 Provider, 최소 시간 간격, 최소 거리 변화, 위치 정보를 수신할 리스너를 매개 변수로 받습니다.
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) { // 위치 정보를 가져오는 도중 발생할 수 있는 예외처리
            Log.d("@@@", "" + e.toString());
        }

        return location;
    }

    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }

        return latitude;
    }

    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }

        return longitude;
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }


    public void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(GpsTracker.this);
        }
    }


}
