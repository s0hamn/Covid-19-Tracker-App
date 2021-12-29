package com.example.covid_19tracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class IndiaFragment extends Fragment {

    TextView activeCasesText, deathCasesText, recoveredCasesText, totalCasesText;
    int activeCases, deathCases, recoveredCases, totalCases;


    private RequestQueue requestQueue;

    private String url = "https://disease.sh/v3/covid-19/countries/india";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_india, container, false);

        activeCasesText = view.findViewById(R.id.activeCasesText);
        deathCasesText = view.findViewById(R.id.deathCasesText);
        recoveredCasesText = view.findViewById(R.id.recoveredCasesText);
        totalCasesText = view.findViewById(R.id.totalCasesText);

        requestQueue = Volley.newRequestQueue(getContext());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            activeCases = response.getInt("active");
                            recoveredCases = response.getInt("recovered");
                            deathCases = response.getInt("deaths");
                            totalCases = response.getInt("cases");

                            activeCasesText.setText(String.valueOf(activeCases));
                            recoveredCasesText.setText(String.valueOf(recoveredCases));
                            deathCasesText.setText(String.valueOf(deathCases));
                            totalCasesText.setText(String.valueOf(totalCases));




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(request);

        return view;


    }
}



