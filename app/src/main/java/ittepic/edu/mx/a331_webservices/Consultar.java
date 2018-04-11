package ittepic.edu.mx.a331_webservices;

/**
 * Created by irvin on 03/04/2018.
 */

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Consultar extends AppCompatActivity {
    EditText id, nombre, domicilio;
    Button consulta, eliminar, ok,insertar, consultaNombre, actualizar;
    String opcion ="";

    String IP = "http://192.168.0.13/datos1";
    String GET_TODO = IP + "/obtener_alumnos.php";
    String GET_BY_ID = IP + "/obtener_alumno_por_id.php";
    String INSERT = IP + "/insertar_alumno.php";
    String DELETE = IP + "/borrar_alumno.php";
    String UPDATE = IP + "/actualizar_alumno.php";


    ServicioWeb2 cone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar);

        id=(EditText)findViewById(R.id.editTextId_c);
        nombre=(EditText)findViewById(R.id.editTextNom_c);
        domicilio=(EditText)findViewById(R.id.editTextDir_c);

        consulta=(Button)findViewById(R.id.btnConsulta_c);
        insertar=(Button)findViewById(R.id.btnInsertar_c);
        eliminar=(Button)findViewById(R.id.btnElim_c);
        ok=(Button)findViewById(R.id.btnBack_c);
        consultaNombre=(Button)findViewById(R.id.btnConsultNombre_c);
        actualizar=(Button)findViewById(R.id.btnActualizar_c);

        insertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cone = new ServicioWeb2();
                cone.execute(INSERT,"insertar",nombre.getText().toString(),domicilio.getText().toString());

            }
        });

        consulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idalumno = id.getText().toString();
                cone = new ServicioWeb2();
                cone.execute(GET_BY_ID,"consulta_id",idalumno);
            }
        });
        consultaNombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cone = new ServicioWeb2();
                cone.execute(GET_TODO, "todo");

            }
        });
        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cone = new ServicioWeb2();
                cone.execute(UPDATE,"actualizar",id.getText().toString(),nombre.getText().toString(),domicilio.getText().toString());

            }
        });

        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cone = new ServicioWeb2();
                cone.execute(DELETE,"borrar",id.getText().toString());
                id.setText("");
                nombre.setText("");
                domicilio.setText("");
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back=new Intent(Consultar.this,MainActivity.class);
                startActivity(back);
            }
        });



    }
    public class ServicioWeb2 extends AsyncTask<String, Void, String> {

        private String error="";
        // private ProgressDialog dialog = new ProgressDialog(MainActivity.this);
        @Override
        protected String doInBackground(String... params) {
            URL url;
            String cadena = params[0];
            String res ="";
            String resp;


            switch(params[1]) {

                case "todo":

                    try {
                        url = new URL(params[0]);
                        try {
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            int respuesta = connection.getResponseCode();
                            if (respuesta == HttpURLConnection.HTTP_OK) {
                                InputStream flujo = new BufferedInputStream(connection.getInputStream());
                                BufferedReader lector = new BufferedReader(new InputStreamReader(flujo));
                                resp = lector.readLine();
                                flujo.close();
                                connection.disconnect();
                                opcion = "todo";
                                return resp;

                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }

                    break;



                case "consulta_id":
                    try {
                        url = new URL(params[0]+"?idalumno="+params[2]);
                        try {
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            int respuesta = connection.getResponseCode();
                            if (respuesta == HttpURLConnection.HTTP_OK) {
                                InputStream flujo = new BufferedInputStream(connection.getInputStream());
                                BufferedReader lector = new BufferedReader(new InputStreamReader(flujo));
                                resp = lector.readLine();
                                flujo.close();
                                connection.disconnect();
                                opcion = "consulta_id";
                                return resp;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }


                    break;

                case "insertar":
                    try {
                        HttpURLConnection url1;
                        url = new URL(cadena);
                        url1 = (HttpURLConnection) url.openConnection();
                        url1.setDoInput(true);
                        url1.setDoOutput(true);
                        url1.setUseCaches(false);
                        url1.setRequestProperty("Content-Type", "application/json");
                        url1.setRequestProperty("Accept", "application/json");
                        url1.connect();
                        JSONObject jsonParam = new JSONObject();
                        jsonParam.put("nombre", params[2]);
                        jsonParam.put("direccion", params[3]);
                        OutputStream os = url1.getOutputStream();
                        BufferedWriter writer = new BufferedWriter(
                                new OutputStreamWriter(os, "UTF-8"));
                        writer.write(jsonParam.toString());
                        writer.flush();
                        writer.close();

                        int respuesta = url1.getResponseCode();
                        StringBuilder result = new StringBuilder();
                        if (respuesta == HttpURLConnection.HTTP_OK) {
                            String line;
                            BufferedReader br=new BufferedReader(new InputStreamReader(url1.getInputStream()));
                            while ((line=br.readLine()) != null) {
                                result.append(line);
                            }
                            JSONObject respuestaJSON = new JSONObject(result.toString());
                            String resultJSON = respuestaJSON.getString("estado");

                            if (resultJSON == "1") {
                                res = "Insertado correctamente";
                                Toast.makeText(getApplicationContext(), res, Toast.LENGTH_SHORT).show();
                            } else if (resultJSON == "2") {
                                res = "Error";
                            }
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return res;

                case "actualizar":
                    try {
                        HttpURLConnection url1;
                        url = new URL(cadena);
                        url1 = (HttpURLConnection) url.openConnection();
                        url1.setDoInput(true);
                        url1.setDoOutput(true);
                        url1.setUseCaches(false);
                        url1.setRequestProperty("Content-Type", "application/json");
                        url1.setRequestProperty("Accept", "application/json");
                        url1.connect();
                        JSONObject jsonParam = new JSONObject();
                        jsonParam.put("idalumno",params[2]);
                        jsonParam.put("nombre", params[3]);
                        jsonParam.put("direccion", params[4]);
                        OutputStream os = url1.getOutputStream();
                        BufferedWriter writer = new BufferedWriter(
                                new OutputStreamWriter(os, "UTF-8"));
                        writer.write(jsonParam.toString());
                        writer.flush();
                        writer.close();

                        int respuesta = url1.getResponseCode();
                        StringBuilder result = new StringBuilder();
                        if (respuesta == HttpURLConnection.HTTP_OK) {
                            String line;
                            BufferedReader br=new BufferedReader(new InputStreamReader(url1.getInputStream()));
                            while ((line=br.readLine()) != null) {
                                result.append(line);
                            }

                            JSONObject respuestaJSON = new JSONObject(result.toString());
                            String resultJSON = respuestaJSON.getString("estado");

                            if (resultJSON == "1") {
                                res = "Actualizado correctamente";

                            } else if (resultJSON == "2") {
                                res = "Error";
                            }
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return res;

                case "borrar":
                    try {
                        HttpURLConnection url1;
                        url = new URL(cadena);
                        url1 = (HttpURLConnection) url.openConnection();
                        url1.setDoInput(true);
                        url1.setDoOutput(true);
                        url1.setUseCaches(false);
                        url1.setRequestProperty("Content-Type", "application/json");
                        url1.setRequestProperty("Accept", "application/json");
                        url1.connect();
                        JSONObject jsonParam = new JSONObject();
                        jsonParam.put("idalumno", params[2]);
                        OutputStream os = url1.getOutputStream();
                        BufferedWriter writer = new BufferedWriter(
                                new OutputStreamWriter(os, "UTF-8"));
                        writer.write(jsonParam.toString());
                        writer.flush();
                        writer.close();
                        int respuesta = url1.getResponseCode();
                        StringBuilder result = new StringBuilder();
                        if (respuesta == HttpURLConnection.HTTP_OK) {
                            String line;
                            BufferedReader br=new BufferedReader(new InputStreamReader(url1.getInputStream()));
                            while ((line=br.readLine()) != null) {
                                result.append(line);
                            }
                            JSONObject respuestaJSON = new JSONObject(result.toString());
                            String resultJSON = respuestaJSON.getString("estado");
                            if (resultJSON == "1") {
                                res = "Borrado correctamente";
                            } else if (resultJSON == "2") {
                                res = "No hay alumnos";
                            }

                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return res;




            }
            return null;


        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String s) {

            String[] nombres;
            String[] direccion;
            switch(opcion) {
                case "todo":

                    try {
                        JSONObject json = new JSONObject(s);
                        JSONArray alumnos = json.getJSONArray("alumnos");
                        nombres = new String[alumnos.length()];
                        for (int i = 0; i < alumnos.length(); i++) {
                            JSONObject nombre = alumnos.getJSONObject(i);
                            nombres[i] = nombre.getString("nombre");

                        }
                        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
                case "consulta_id":
                    String Nombre="";
                    String Direccion="";
                    // String IDA="";
                    try {
                        JSONObject parentObject = new JSONObject(s);
                        JSONObject userDetails = parentObject.getJSONObject(s);
                        //IDA=userDetails.getString("idalumno");
                        Nombre = userDetails.getString("nombre");
                        Direccion = userDetails.getString("direccion");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //id.setText("");
                    nombre.setText("NOM: "+Nombre);
                    domicilio.setText(Direccion);
                    Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                    break;
                case "actualizar":
                    String ID="";
                    String Nombre1="";
                    String Direccion1="";

                    try {
                        JSONObject parentObject = new JSONObject(s); JSONObject userDetails = parentObject.getJSONObject("alumnos");
                        Nombre = userDetails.getString("nombre");
                        Direccion = userDetails.getString("direccion");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    id.setText("");
                    nombre.setText(Nombre1);
                    domicilio.setText(Direccion1);
                    Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                    break;

                case "insertar":

                    break;

            }



            super.onPostExecute(s);

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }



    }
}

