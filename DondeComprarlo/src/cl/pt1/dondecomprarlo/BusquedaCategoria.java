package cl.pt1.dondecomprarlo;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

public class BusquedaCategoria extends Activity{

	// Progress Dialog
	private ProgressDialog pDialog;

	// Creating JSON Parser object
	JSONParser jParser = new JSONParser();

	private static String url_all_categoria = "http://192.168.0.5/donde_comprarlo/busqueda_categoria.php";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_categoria = "categoria";
	private static final String TAG_ID = "id_categoria";
	private static final String TAG_NOMBRE = "nombre_categoria";


	// categorias JSONArray
	JSONArray categoria = null;

	String categorias, categorias_id;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.busqueda_categoria);

		// Loading categorias in Background Thread
		new LoadAllCategorias().execute();

	}

	/**
	 * Background Async Task to Load all Productos by making HTTP Request
	 * */
	class LoadAllCategorias extends AsyncTask<String, String, String> {


		List<String> categoriaList = new ArrayList<String>();
		List<String> idcategoriaList = new ArrayList<String>();

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			System.out.println("2");
			super.onPreExecute();
			pDialog = new ProgressDialog(BusquedaCategoria.this);
			pDialog.setMessage("Cargando Productos. Por favor, espere...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();

		}



		/**
		 * getting All productos from url
		 * */
		protected String doInBackground(String... args) {

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			// getting JSON string from URL
			JSONObject json = jParser.makeHttpRequest(url_all_categoria, "GET", params);

			// Check your log cat for JSON reponse
			Log.d("All categoria: ", json.toString());

			try{

				// Checking for SUCCESS TAG
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					// productos found
					// Getting Array of empleados
					categoria = json.getJSONArray(TAG_categoria);
					System.out.println("Se encontraron categorias");
					// looping through All empleados
					categoriaList.add("Categorías");
					idcategoriaList.add("0");
					for (int i = 0; i < categoria.length(); i++) {
						JSONObject c = categoria.getJSONObject(i);

						// Storing each json item in variable
						String id = c.getString(TAG_ID);
						String nombre = c.getString(TAG_NOMBRE);

						// Agregando datos a las listas
						categoriaList.add(nombre);
						idcategoriaList.add(id);


					}
				} else {
					// no categorias found

					System.out.println("No se han encontrado categorias");
				}



			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null; 

		}



		/**
		 * After completing background task Dismiss the progress dialog
		 * **/


		protected void onPostExecute(String file_url){
			//dismiss the dialog after getting all categorias
			pDialog.dismiss();
			Spinner spinnerCategorias;

			spinnerCategorias = (Spinner) findViewById(R.id.spinner_categorias);

			spinnerCategorias = (Spinner) BusquedaCategoria.this.findViewById(R.id.spinner_categorias);
			ArrayAdapter<String> adaptador = new ArrayAdapter(BusquedaCategoria.this, android.R.layout.simple_spinner_item, categoriaList);
			adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinnerCategorias.setAdapter(adaptador);///////////////
			spinnerCategorias.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					Toast.makeText(arg0.getContext(), "Seleccionado: " + arg0.getItemAtPosition(arg2).toString(), Toast.LENGTH_SHORT).show();
					categorias = arg0.getItemAtPosition(arg2).toString();
					//Toast.makeText(arg0.getContext(), "Seleccionado id: " + categorias_id.get(arg2), Toast.LENGTH_SHORT).show();
					categorias_id = idcategoriaList.get(arg2).toString();
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub

				}


			});

		}


	}
}