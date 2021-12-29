package com.example.covid_19tracker;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class YourLocationFragment extends Fragment implements LocationListener {
    String stateName;
    TextView deathCasesText, recoveredCasesText, totalCasesText, stateNameAppend;
    LocationManager locationManager;

    private String url = "https://api.rootnet.in/covid19-in/stats/latest";

    private RequestQueue requestQueue;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_your_location, container, false);

        grantPermission();

        //
        deathCasesText = view.findViewById(R.id.deathCasesText);
        recoveredCasesText = view.findViewById(R.id.recoveredCasesText);
        totalCasesText = view.findViewById(R.id.totalCasesText);
        stateNameAppend = view.findViewById(R.id.stateNameAppend);


        requestQueue = Volley.newRequestQueue(getContext());

        checkLocationIsEnabledOrNot();
        getLocation();







        return view;
    }

    private void getLocation(){
        try {
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 5,(LocationListener) this);
        }catch (SecurityException e){
            e.printStackTrace();
        }
    }

    private void checkLocationIsEnabledOrNot() {

        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        boolean gpsEnabled = false;
        boolean networkEnabled = false;

        try {
            gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            networkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }catch (Exception e){
            e.printStackTrace();
        }

        if(!gpsEnabled && !networkEnabled){
            new AlertDialog.Builder(getContext())
                    .setTitle("Enable GPS Service")
                    .setCancelable(false)
                    .setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
//                            intent for enabling location service
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    }).setNegativeButton("Cancel", null)
                    .show();
        }




    }

    private void grantPermission() {
        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100 );
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

        Geocoder geocoder = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            stateName = addresses.get(0).getAdminArea();
//            Log.d("stateMy", stateName);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("response", "success");

                            try {
                                JSONObject data = response.getJSONObject("data");

                                JSONArray regional = data.getJSONArray("regional");

                                for(int i = 0; i < regional.length(); i++){
                                    JSONObject stateData = regional.getJSONObject(i);

                                    String stateDataName = stateData.getString("loc");
                                    if(stateDataName.equals(stateName)){
                                        Log.d("StateName", stateDataName);

                                        totalCasesText.setText(String.valueOf(stateData.getInt("totalConfirmed")));
                                        deathCasesText.setText(String.valueOf(stateData.getInt("deaths")));
                                        recoveredCasesText.setText(String.valueOf(stateData.getInt("discharged")));
                                        stateNameAppend.append(stateName);





                                        break;
                                    }
                                    else{
                                        Log.d("StateName", "Not found: ");
                                    }

                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Log.d("responseError", "onErrorResponse: ");

                }
            });

            requestQueue.add(request);





        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }
}
