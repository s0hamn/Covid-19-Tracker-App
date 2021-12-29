package com.example.covid_19tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.animation.Animation;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailedData extends AppCompatActivity {



    LineChart lineChart;

    private String url = "https://api.covid19api.com/total/dayone/country/india";

    private RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_data);



        lineChart = findViewById(R.id.lineChart);
        LineDataSet lineDataSet1 = new LineDataSet(dataValues1(), "Data Set 1");
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet1);

        LineData data = new LineData(dataSets);
        lineChart.getAxisRight().setDrawGridLines(false);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getXAxis().setDrawAxisLine(false);
        lineChart.getXAxis().removeAllLimitLines();
        lineChart.getAxisRight().setDrawAxisLine(false);
        lineChart.getLegend().setEnabled(false);
        lineChart.getDescription().setEnabled(false);
        lineChart.getXAxis().setTextColor(getResources().getColor(R.color.white));
        lineChart.getAxisRight().setTextColor(getResources().getColor(R.color.white));
        lineChart.setData(data);
        lineChart.invalidate();

    }

    private ArrayList<Entry> dataValues1(){

        requestQueue = Volley.newRequestQueue(this);

        ArrayList<Entry> dataVals = new ArrayList<Entry>();

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++){

                            try {
                                JSONObject data = response.getJSONObject(i);
                                int y = data.getInt("Confirmed");

                                dataVals.add(new Entry(i, y));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }




                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(request);

        return dataVals;

    }
}