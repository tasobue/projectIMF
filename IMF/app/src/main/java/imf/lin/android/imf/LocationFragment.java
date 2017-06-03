package imf.lin.android.imf;

import android.support.v4.app.Fragment;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by stmac0001 on 2017/05/21.
 */

public class LocationFragment extends Fragment implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    public static final String TAG = LocationFragment.class.getSimpleName();
    private IntentFilter mIntentFilter;
    private static final String KEY_WORD = "keyword";


    //位置情報取得　
    private boolean mResolvingError = false;
    private FusedLocationProviderApi fusedLocationProviderApi;
    private LocationRequest locationRequest;
    private android.location.Location location;
    private long lastLocationTime = 0;
    private GoogleApiClient mGoogleApiClient;
    private String textLog = "start \n";
    private double dblLat ;
    private double dblLong;

    public double getdblLat(){
        return dblLat;
    }

    public  double getdblLong(){
        return dblLong;
    }

    public void setdblLat(double _lat){
        dblLat = _lat;
    }

    public void setdblLong(double _long){
        dblLong = _long;
    }

    static public LocationFragment newInstance() {
        return new LocationFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        //位置情報取得
        // LocationRequest を生成して精度、インターバルを設定

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(16);

        fusedLocationProviderApi = LocationServices.FusedLocationApi;

        mGoogleApiClient = new GoogleApiClient
                .Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        // 測位開始
        startFusedLocation();


        // 測位終了
        //stopFusedLocation();
    }

    private void startFusedLocation(){
        Log.d("LocationActivity", "onStart");

        // Connect the client.
        if (!mResolvingError) {
            // Connect the client.
            mGoogleApiClient.connect();
        } else {
            Log.d("MyApp Location","connect Failed");
        }

    }

    private void stopFusedLocation(){
        // Disconnecting the client invalidates it.
        mGoogleApiClient.disconnect();
        Log.d("MyApp Location","disconnect");
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
        stopFusedLocation();
    }


    @Override
    public void onConnected(Bundle bundle) {
        Log.d("LocationActivity", "onConnected");

        if(Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission( android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && getActivity().checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        android.location.Location currentLocation = fusedLocationProviderApi.getLastLocation(mGoogleApiClient);

        if (currentLocation != null && currentLocation.getTime() > 20000) {
            location = currentLocation;

            textLog += "---------- onConnected \n";
            textLog += "Latitude=" + String.valueOf(location.getLatitude()) + "\n";
            textLog += "Longitude=" + String.valueOf(location.getLongitude()) + "\n";
            textLog += "Accuracy=" + String.valueOf(location.getAccuracy()) + "\n";
            textLog += "Altitude=" + String.valueOf(location.getAltitude()) + "\n";
            textLog += "Time=" + String.valueOf(location.getTime()) + "\n";
            textLog += "Speed=" + String.valueOf(location.getSpeed()) + "\n";
            textLog += "Bearing=" + String.valueOf(location.getBearing()) + "\n";

            Log.d("debug", textLog);

            dblLat = location.getLatitude();
            dblLong = location.getLongitude();

        } else {
            // バックグラウンドから戻ってしまうと例外が発生する場合がある
            try {
                //
                fusedLocationProviderApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
                // Schedule a Thread to unregister location listeners
                Executors.newScheduledThreadPool(1).schedule(new Runnable() {
                    @Override
                    public void run() {
                        fusedLocationProviderApi.removeLocationUpdates(mGoogleApiClient, LocationFragment.this);
                    }
                }, 60000, TimeUnit.MILLISECONDS);

                textLog += "onConnected(), requestLocationUpdates \n";

            } catch (Exception e) {
                e.printStackTrace();
                Toast toast = Toast.makeText(getActivity(), "例外が発生、位置情報のPermissionを許可していますか？", Toast.LENGTH_SHORT);
                toast.show();

                //MainActivityに戻す
                getActivity().finish();
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocationTime = location.getTime() - lastLocationTime;

        textLog += "---------- onLocationChanged \n";
        textLog += "Latitude=" + String.valueOf(location.getLatitude()) + "\n";
        textLog += "Longitude=" + String.valueOf(location.getLongitude()) + "\n";
        textLog += "Accuracy=" + String.valueOf(location.getAccuracy()) + "\n";
        textLog += "Altitude=" + String.valueOf(location.getAltitude()) + "\n";
        textLog += "Time=" + String.valueOf(location.getTime()) + "\n";
        textLog += "Speed=" + String.valueOf(location.getSpeed()) + "\n";
        textLog += "Bearing=" + String.valueOf(location.getBearing()) + "\n";
        textLog += "time= " + String.valueOf(lastLocationTime) + " msec \n";

        dblLat = location.getLatitude();
        dblLong = location.getLongitude();
    }

    @Override
    public void onConnectionSuspended(int i) {
        textLog += "onConnectionSuspended() \n";
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        textLog += "onConnectionFailed()n";

        if (mResolvingError) {
            // Already attempting to resolve an error.
            Log.d("", "Already attempting to resolve an error");

            return;
        } else if (connectionResult.hasResolution()) {

        } else {
            mResolvingError = true;
        }
    }
}