package ittepic.edu.mx.webservice_2;

import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Equipos_NFL extends AppCompatActivity {
    Button btn ;
    TextView res,nombre,peso,altura,diametro,costo,pais;
    Spinner spnNave;
    WServices http;

    String json_string;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipos__nfl);
        spnNave=(Spinner)findViewById(R.id.spnNave);
        res=(TextView) findViewById(R.id.txtDesc);

        ArrayAdapter spinner_adapter=ArrayAdapter.createFromResource(this,R.array.naves,android.R.layout.simple_spinner_item);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnNave.setAdapter(spinner_adapter);

        spnNave.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                http=new WServices();
                res.setText("");
                switch (position){
                    case 0:
                        res.setText("");
                        //mensaje("posicion",""+position);
                        http.execute("https://api.spacexdata.com/v1/vehicles/falcon1","1");
                        break;
                    case 1:
                        res.setText("");
                        //mensaje("posicion",""+position);
                        http.execute("https://api.spacexdata.com/v1/vehicles/falcon9","1");
                        break;
                    case 2:
                        res.setText("");
                        //mensaje("posicion",""+position);
                        http.execute("https://api.spacexdata.com/v1/vehicles/falconheavy","1");
                        break;
                    case 3:
                        res.setText("");
                        //mensaje("posicion",""+position);
                        http.execute("https://api.spacexdata.com/v1/vehicles/dragon","1");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void mensaje(String t, String s) {
        AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        alerta.setTitle(t).setMessage(s).show();
    }


    public class WServices extends AsyncTask<String, Void, String> {
        URL url;

        @Override
        protected String doInBackground(String... params) {
            String cadena = "";
            if (params[1] == "1") {
                try {
                    url = new URL(params[0]);
                    HttpURLConnection connection = null; // Abrir conexion
                    connection = (HttpURLConnection) url.openConnection();
                    int respuesta = 0;
                    respuesta = connection.getResponseCode();
                    InputStream inputStream = null;
                    inputStream = connection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder stringBuilder = new StringBuilder();

                    if (respuesta == HttpURLConnection.HTTP_OK) {
                        while ((json_string = bufferedReader.readLine()) != null) {
                            stringBuilder.append(json_string + "\n");
                        }
                        bufferedReader.close();
                        inputStream.close();
                        connection.disconnect();
                        String temporal = stringBuilder.toString();
                        JSONObject jsonObj = new JSONObject(temporal);
                        JSONObject heigh=jsonObj.getJSONObject("height");
                        //JSONObject diameter=jsonObj.getJSONObject("diameter");
                        JSONObject mass=jsonObj.getJSONObject("mass");
                        cadena+="Cohete espacial "+jsonObj.getString("name")+
                                "\n\nAltura: "+heigh.optInt("meters")+
                                "\n\nPeso: "+mass.getString("kg")+"kg"+
                                "\n\nDescripción: "+jsonObj.getString("description")
                                +"\n\nPaís de localización: "+jsonObj.getString("country")
                                +"\n\nCosto por lanzamiento: $"+jsonObj.getString("cost_per_launch");

                        /*
                        JSONArray clima = jsonObj.getJSONArray("weather");
                        JSONObject uno = clima.getJSONObject(0);
                        JSONObject main = jsonObj.getJSONObject("main");
                        JSONObject wind = jsonObj.getJSONObject("wind");
                        JSONObject clouds = jsonObj.getJSONObject("clouds");
                        JSONObject sys = jsonObj.getJSONObject("sys");

                        cadena += "CLIMA" + "\n" + "ID: " + uno.getString("id") + " MAIN: " + uno.getString("main") + " \nDESCRIPCION: " +
                                uno.getString("description") +
                                "\n" + " BASE: " + jsonObj.getString("base") +
                                "\n" + "MAIN" +
                                "\n" + "TEMPERATURA: " + main.getString("temp") + " \nPRESIÓN: " + main.getString("pressure") + " \nHUMEDAD: " +
                                main.getString("humidity") + " \nTEMP_MIN: " + main.getString("temp_min") + " \nTEMP_MAX: " + main.getString("temp_max") +
                                /*"\n" + "VISIBILITY: " + jsonObj.getString("visibility") +
                                "\n" + "WIND" +
                                "\n" + "SPEED: " + wind.optInt("speed") + " DEG: " + wind.getString("deg") +
                                "\n" + "CLOUDS" +
                                "\n" + "ALL: " + clouds.optInt("all") +
                                "\n" + "DT: " + jsonObj.getString("dt") +
                                "\n" + "SYS" +
                                "\n" + " ID: " + sys.optInt("id") + " MESSAGE: " + sys.optInt("message") +
                                "\n COUNTRY: " + sys.getString("country") + " SUNRISE: " + sys.optInt("sunrise") + " SUNSET: " + sys.getString("sunset") +
                                "\n" + "ID: " + jsonObj.getString("id") +
                                "\n" + "NAME: " + jsonObj.getString("name") +
                                "\n" + "COD: " + jsonObj.getString("cod");*/

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return cadena;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            res.setText(s);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}
