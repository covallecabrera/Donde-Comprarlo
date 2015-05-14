package cl.pt1.dondecomprarlo;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;
import android.widget.Toast;

public class spinner extends Activity {
	

	private static final String TAG_SUCCESS = "success";

	JSONParser jParser = new JSONParser();

	
	int success = 0;
	JSONArray datosJson = null;

	Button actualizarTareas, verTareaEnMapa;
	TextView descripcionTarea;
	TextView nombreTarea;
	EditText comentarioTarea;
	
	
	String estado_tarea, nombre_tarea, descripcion_tarea, estado_tarea_id, comentario_tarea;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.spinner); 
		
		new EstadoTarea().execute();

		
	}//onCreate
	
			/** PREGUNTAR POR ESTADO TAREAS*/
			//--------------------------------------------------------
			class EstadoTarea extends AsyncTask<String, String, String>{
				int idEstado_tarea  ;
				String nombreEstado_tarea ;
				String descripcionEstado_tarea ;
				
				//estados string
				List<String> lista2 = new ArrayList<String>();
				//id estados
				List<String> idEstado = new ArrayList<String>();
				
				@Override
				protected void onPreExecute(){
					super.onPreExecute();
					/*
							pDialog = new ProgressDialog(CircularProgressBarSample.this);
							pDialog.setMessage("cargando direccion");
							pDialog.setIndeterminate(false);
							pDialog.setCancelable(false);
							pDialog.show();
					 */
				}
				@Override
				protected String doInBackground(String... params) {
					//building parameters	
					List<NameValuePair> params2 = new ArrayList<NameValuePair>();
					params2.add(new BasicNameValuePair("", null));
					//getting JSON string from URL
					JSONObject json = jParser.makeHttpRequest("tu url con lso datos","GET", params2);
					//checj your log cat for JSON response
					Log.d("todas los datos: ", json.toString());
					try{
						//checking for success tag
						success = json.getInt(TAG_SUCCESS);
						if(success == 1){
							//empleados found
							datosJson = json.getJSONArray("estadoTarea");
							//System.out.println(datosJson);
							//looping through all empleados
							lista2.add("categorias");
							for(int i=0; i<datosJson.length();i++){
								JSONObject c = datosJson.getJSONObject(i);	
								//storig each json item in variable
								idEstado_tarea  = c.getInt("idEstado_tarea");
								idEstado.add(Integer.toString(idEstado_tarea));
								
								nombreEstado_tarea = c.getString("nombreEstado_tarea");
								lista2.add(nombreEstado_tarea);
								
								descripcionEstado_tarea = c.getString("descripcionEstado_tarea");
							}
						}
					}catch(JSONException e){
						e.printStackTrace();
					}
					return null;
				}//doInBackground

				protected void onPostExecute(String file_url){
					//dismiss the dialog after getting all empleados
					//pDialog.dismiss();
					if (success==1){
						
						
						//Toast.makeText(getApplicationContext(), "id usuario: "+UserData.usuario_idlUsuario, Toast.LENGTH_LONG).show();
						//Toast.makeText(getApplicationContext(), "id rol: "+UserData.rol_idRol, Toast.LENGTH_LONG).show();	
					
						estadoTarea(lista2,idEstado);
					}

				}
			}
			/** PREGUNTAR POR ESTADO TAREAS**/
			/** RELLENA EL SPINNER DE ESTADO DE TAREAS */
			public void estadoTarea(List<String> lista2, final List<String> listaId){
				//SPINNER
				boolean agregar = true;
				Spinner spinner2;

				String L;
				ListActivity me;
				spinner2 = (Spinner) findViewById(R.id.spinner1);
				
				spinner2 = (Spinner) this.findViewById(R.id.spinner1);
				ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lista2);
				adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spinner2.setAdapter(adaptador);///////////////
				spinner2.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
						//Toast.makeText(arg0.getContext(), "Seleccionado: " + arg0.getItemAtPosition(arg2).toString(), Toast.LENGTH_SHORT).show();
						estado_tarea = arg0.getItemAtPosition(arg2).toString();
						//Toast.makeText(arg0.getContext(), "Seleccionado id: " + listaId.get(arg2), Toast.LENGTH_SHORT).show();
						estado_tarea_id = listaId.get(arg2).toString();
					}///////////////
					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
					}
				});
			}
			//SPINNER


}//class ActualizarTarea