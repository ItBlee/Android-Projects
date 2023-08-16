package com.example.json2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import static com.example.json2.JsonReader.readJsonFromUrl;

public class MainActivity extends AppCompatActivity {
    ArrayList<Country> countryList;

    Button btnSubmit;
    ListView list;

    ProgressDialog progressDialog;
    String url = "http://api.geonames.org/countryInfoJSON?username=chcicken";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        countryList = new ArrayList<>();

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        list = (ListView) findViewById(R.id.list);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetServerData().execute();
            }
        });
    }

    class GetServerData extends AsyncTask {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Fetching country data");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        protected Object doInBackground(Object[] objects) {
            try {
                JSONObject json = readJsonFromUrl(url);
                JSONArray recs = json.getJSONArray("geonames");
                for (int i = 0; i < recs.length(); i++) {
                    JSONObject obj = recs.getJSONObject(i);
                    Country country  = new Country(obj.optString("continent"),
                            obj.optString("countryName"),
                            obj.optString("capital"),
                            obj.optString("countryCode"),
                            obj.optLong("areaInSqKm"),
                            obj.optLong("population"));
                    countryList.add(country);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            // Dismiss the progress dialog
            if (progressDialog.isShowing())
                progressDialog.dismiss();

            list.setAdapter(new CustomCountryList(MainActivity.this, countryList));

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                    intent.putExtra("CountryCode", countryList.get(position).getCountryCode());
                    intent.putExtra("Detail", countryList.get(position).toString());
                    startActivity(intent);
                }
            });
        }
    }
}