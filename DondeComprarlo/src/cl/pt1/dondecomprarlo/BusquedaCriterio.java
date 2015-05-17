package cl.pt1.dondecomprarlo;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;


public class BusquedaCriterio extends Activity{

	//	Button resultadosBusqueda;
	String buscar,id_marca;
	EditText nom_producto,pre_producto;
	private static final String TAG_BUSCAR = "buscar";
	private static final String TAG_NOM_PRODUCTO = "nom_producto";
	private static final String TAG_PRE_PRODUCTO = "pre_producto";
	// Progress Dialog
	private ProgressDialog pDialog;

	// Creating JSON Parser object
	JSONParser jParser = new JSONParser();

	private static String url_all_categoria = "http://192.168.0.5/donde_comprarlo/busqueda_categoria.php";
	private static String url_all_marcas = "http://192.168.0.5/donde_comprarlo/mostrar_marcas_categoria.php";

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



	String categorias, categorias_id,marcas,marca_id;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.busqueda_criterio);

		// Loading categorias in Background Thread
		new LoadAllCategorias().execute();
		///////////

	}

	public void ResultadoBusqueda(View v){


		buscar = categorias_id; 
		id_marca = marca_id;
		nom_producto = (EditText) findViewById(R.id.nombre_producto);
		pre_producto = (EditText) findViewById(R.id.precio_producto);
		String nomb_producto, prec_producto;
		nomb_producto = nom_producto.getText().toString();
		prec_producto = pre_producto.getText().toString();
		// Starting new intent
		Intent in = new Intent(getApplicationContext(),
				ResultadosCriterio.class);
		// sending pid to next activity
		in.putExtra(TAG_BUSCAR, buscar);
		in.putExtra(TAG_ID_MARCA, id_marca);
		in.putExtra(TAG_NOM_PRODUCTO,nomb_producto);
		in.putExtra(TAG_PRE_PRODUCTO,prec_producto);

		// starting new activity and expecting some response back
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
			System.out.println("2");
			super.onPreExecute();
			pDialog = new ProgressDialog(BusquedaCriterio.this);
			pDialog.setMessage("Cargando Categorias. Por favor, espere...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();

		}



		/**
		 * getting All categorias from url
		 * */
		protected String doInBackground(String... args) {
			System.out.println("Class doInBc");
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

			spinnerCategorias = (Spinner) BusquedaCriterio.this.findViewById(R.id.spinner_categorias);
			ArrayAdapter<String> adaptador = new ArrayAdapter(BusquedaCriterio.this, android.R.layout.simple_spinner_item, categoriaList);
			adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinnerCategorias.setAdapter(adaptador);
			spinnerCategorias.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					categorias = arg0.getItemAtPosition(arg2).toString();
					categorias_id = idcategoriaList.get(arg2).toString();

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

		List<String> marcaList = new ArrayList<String>();
		List<String> idmarcaList = new ArrayList<String>();

		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(BusquedaCriterio.this);
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
					marcaList.add("Marcas");
					idmarcaList.add("0");
					// looping through All empleados
					for (int i = 0; i < marca.length(); i++) {
						JSONObject c = marca.getJSONObject(i);


						// Storing each json item in variable
						String id_marca = c.getString(TAG_ID_MARCA);
						String nombre_marca = c.getString(TAG_NOMBRE_MARCA);



						idmarcaList.add(id_marca);
						marcaList.add(nombre_marca);


					}
				} else {
					// no empleados found
					// Launch Add New Empleado Activity
					/*Intent i = new Intent(getApplicationContext(),
									NewEmpladoActivity.class);
							// Closing all previous activities
							i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(i);*/

					System.out.println("No se han encontrado marcas");
				}



			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}
		protected void onPostExecute(String file_url) {
//			dismiss the dialog after getting all categorias
			pDialog.dismiss();
			Spinner spinnerMarcas;

			spinnerMarcas = (Spinner) findViewById(R.id.spinner_marcas);

			spinnerMarcas = (Spinner) BusquedaCriterio.this.findViewById(R.id.spinner_marcas);
			ArrayAdapter<String> adaptador = new ArrayAdapter(BusquedaCriterio.this, android.R.layout.simple_spinner_item, marcaList);
			adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinnerMarcas.setAdapter(adaptador);
			spinnerMarcas.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					marcas = arg0.getItemAtPosition(arg2).toString();
					marca_id = idmarcaList.get(arg2).toString();

				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub

				}


			});

		}

	}

}