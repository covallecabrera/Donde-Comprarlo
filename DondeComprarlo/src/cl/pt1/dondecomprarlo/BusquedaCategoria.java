package cl.pt1.dondecomprarlo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import cl.pt1.dondecomprarlo.servidor;


import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class BusquedaCategoria extends ListActivity{

	EditText txtbuscar;

	String buscar,id_marca;
	private static final String TAG_BUSCAR = "buscar";
	// Progress Dialog
	private ProgressDialog pDialog;

	// Creating JSON Parser object
	JSONParser jParser = new JSONParser();

	
	private static String url_all_categoria = servidor.ip() + servidor.ruta() + "busqueda_categoria.php";
	private static String url_all_marcas = servidor.ip() + servidor.ruta() + "mostrar_marcas_categoria.php";
	
	
	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_categoria = "categoria";
	private static final String TAG_ID_CATEGORIA = "id_categoria";
	private static final String TAG_NOMBRE_CATEGORIA = "nombre_categoria";
	private static final String TAG_marca = "marca";
	private static final String TAG_ID_MARCA = "id_marca";
	private static final String TAG_NOMBRE_MARCA = "nombre_marca";
	// categorias JSONArray
	JSONArray categoria = null;
	JSONArray marca = null;

	ArrayList<HashMap<String, String>> marcaList;

	String categorias, categorias_id;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.busqueda_categoria);
		marcaList = new ArrayList<HashMap<String, String>>();
		// Loading categorias in Background Thread
		new LoadAllCategorias().execute();
		///////////
		// Get listview
		ListView lv = getListView();

		// Al seleccionar una categoria

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// getting values from selected ListItem
				id_marca = ((TextView) view.findViewById(R.id.id1)).getText()
						.toString();
				buscar = categorias_id; 

				// Nuevo Intent
				Intent in = new Intent(getApplicationContext(),
						ResultadosCategoria.class);
				// Enviando datos en intent
				in.putExtra(TAG_BUSCAR, buscar); //TAG_BUSCAR= id_categoria
				in.putExtra(TAG_ID_MARCA, id_marca);
				
				// empezando actividad.
				startActivityForResult(in, 100);
			}
		});
		

	}

	 public void BusquedaProducto(View v){
		 txtbuscar = (EditText) findViewById(R.id.TextoBuscar);
         String buscar;
         buscar = txtbuscar.getText().toString();
       System.out.println("aprete click en busqueda bategoria");
      // Nuevo Intent
 		Intent in = new Intent(getApplicationContext(),
 				BusquedaProducto.class);
 		// Enviando datos en intent
 		in.putExtra(TAG_BUSCAR, buscar);
 		
 		// empezando actividad
 		startActivityForResult(in, 100);
         
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
			super.onPreExecute();
			pDialog = new ProgressDialog(BusquedaCategoria.this);
			pDialog.setMessage("Cargando Categorias. Por favor, espere...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();

		}



		/**
		 * getting All categorias from url
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
						String id = c.getString(TAG_ID_CATEGORIA);
						String nombre = c.getString(TAG_NOMBRE_CATEGORIA);

						// Agregando datos a las listas
						categoriaList.add("  ·  "+nombre);
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
			spinnerCategorias.setAdapter(adaptador);
			spinnerCategorias.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					categorias = arg0.getItemAtPosition(arg2).toString();
					categorias_id = idcategoriaList.get(arg2).toString();
		
					marcaList = new ArrayList<HashMap<String, String>>();

					buscar = categorias_id;
					new CargarMarcasCategoria().execute();
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub

				}


			});

		}


	}
	
	class CargarMarcasCategoria extends AsyncTask<String, String, String> {

		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(BusquedaCategoria.this);
			pDialog.setMessage("Cargando Marcas. Por favor, espere...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();

		}
		
		@Override
		protected String doInBackground(String... params) {
			// Building Parameters
			List<NameValuePair> params2 = new ArrayList<NameValuePair>();
			params2.add(new BasicNameValuePair("buscar", buscar));

			// getting JSON string from URL
			JSONObject json = jParser.makeHttpRequest(url_all_marcas, "GET", params2);

			// Check your log cat for JSON reponse
			Log.d("All productos: ", json.toString());

			try{

				// Checking for SUCCESS TAG
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					// productos found
					// Getting Array of empleados

	
					marca = json.getJSONArray(TAG_marca);
					// looping through All empleados
					for (int i = 0; i < marca.length(); i++) {
						JSONObject c = marca.getJSONObject(i);


						// Storing each json item in variable
						String id_marca = c.getString(TAG_ID_MARCA);
						String nombre_marca = c.getString(TAG_NOMBRE_MARCA);


						// creating new HashMap
						HashMap<String, String> map = new HashMap<String, String>();

						// adding each child node to HashMap key => value
						map.put(TAG_ID_MARCA, id_marca);
						map.put(TAG_NOMBRE_MARCA, nombre_marca);



						// adding HashList to ArrayList
						marcaList.add(map);
					}
				} else {



					System.out.println("No se han encontrado marcas");
				}



			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all marcas
			pDialog.dismiss();
			System.out.println("onPostEx");
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					/**
					 * Updating parsed JSON data into ListView
					 * */
					
					ListAdapter adapter = new SimpleAdapter(
							BusquedaCategoria.this, marcaList,
							R.layout.list_item_busqueda, new String[] { TAG_ID_MARCA,
									TAG_NOMBRE_MARCA},
									new int[] { R.id.id1, R.id.nombre});
					// actualzizar listview
					setListAdapter(adapter);
				}
			});

		}
		
	}
	
}