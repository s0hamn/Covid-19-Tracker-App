package com.example.covid_19tracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class WorldFragment extends Fragment {

    TextView activeCasesText, deathCasesText, recoveredCasesText, totalCasesText, todayActiveCasesText,todayRecoveredCasesText,todayDeathCasesText;
    int activeCases, deathCases, recoveredCases, totalCases, todayActiveCases, todayRecoveredCases, todayDeathCases;

    private String url = "https://disease.sh/v3/covid-19/all";

    CardView totalCasesCardView;

    private RequestQueue requestQueue;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_world, container, false);

        activeCasesText = view.findViewById(R.id.activeCasesText);
        deathCasesText = view.findViewById(R.id.deathCasesText);
        recoveredCasesText = view.findViewById(R.id.recoveredCasesText);
        totalCasesText = view.findViewById(R.id.totalCasesText);

        todayActiveCasesText = view.findViewById(R.id.todayActiveCasesText);
        todayRecoveredCasesText = view.findViewById(R.id.todayRecoveredCasesText);
        todayDeathCasesText = view.findViewById(R.id.todayDeathCasesText);

        totalCasesCardView = view.findViewById(R.id.totalCasesCardView);

        totalCasesCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), DetailedData.class));
            }
        });

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
                            todayActiveCases = response.getInt("todayCases");
                            todayRecoveredCases = response.getInt("todayRecovered");
                            todayDeathCases = response.getInt("todayDeaths");

                            activeCasesText.setText(String.valueOf(activeCases));
                            recoveredCasesText.setText(String.valueOf(recoveredCases));
                            deathCasesText.setText(String.valueOf(deathCases));
                            totalCasesText.setText(String.valueOf(totalCases));
                            todayActiveCasesText.append(String.valueOf(todayActiveCases));
                            todayDeathCasesText.append(String.valueOf(todayDeathCases));
                            todayRecoveredCasesText.append(String.valueOf(todayRecoveredCases));




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
