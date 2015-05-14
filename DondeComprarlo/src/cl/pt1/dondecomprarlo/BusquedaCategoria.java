package cl.pt1.dondecomprarlo;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.ImageView;

public class BusquedaCategoria extends ListActivity{

	// Progress Dialog
	private ProgressDialog pDialog;

	// Creating JSON Parser object
	JSONParser jParser = new JSONParser();

	ArrayList<HashMap<String, String>> categoriaList;

	private static String url_all_categoria = "http://192.168.0.5/donde_comprarlo/busqueda_categoria.php";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_categoria = "categoria";
	private static final String TAG_ID = "id_productos";
	private static final String TAG_NOMBRE = "nombre_producto";


	// productos JSONArray
	JSONArray productos = null;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.spinner);
		// Hashmap for ListView
		categoriaList = new ArrayList<HashMap<String, String>>();

		// Loading empleados in Background Thread
		new LoadAllCategorias().execute();

		// Get listview
		ListView lv = getListView();

		// on seleting single Empleado
		// launching Edit Empleado Screen
		//lv.setOnItemClickListener(new OnItemClickListener() {
	}

	/**
	 * Background Async Task to Load all Productos by making HTTP Request
	 * */
	class LoadAllCategorias extends AsyncTask<String, String, String> {

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
					productos = json.getJSONArray(TAG_categoria);
					System.out.println("Se encontraron categorias");
					// looping through All empleados
					for (int i = 0; i < productos.length(); i++) {
						JSONObject c = productos.getJSONObject(i);

						// Storing each json item in variable
						String id = c.getString(TAG_ID);
						String nombre = c.getString(TAG_NOMBRE);



						// creating new HashMap
						HashMap<String, String> map = new HashMap<String, String>();

						// adding each child node to HashMap key => value
						map.put(TAG_ID, id);
						map.put(TAG_NOMBRE, nombre);



						// adding HashList to ArrayList
						categoriaList.add(map);
					}
				} else {
					// no empleados found
					// Launch Add New Empleado Activity
					/*Intent i = new Intent(getApplicationContext(),
									NewEmpladoActivity.class);
							// Closing all previous activities
							i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(i);*/

					System.out.println("No se han encontrado productos");
				}



			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null; 

		}



		/**
		 * After completing background task Dismiss the progress dialog
		 * **/


		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all productos
			pDialog.dismiss();
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					/**
					 * Updating parsed JSON data into ListView
					 * */

					//ImageView image = (ImageView) findViewById(R.id.imagen1);
					//new LoadProfileImage(image).execute(TAG_IMAGEN);
					ListAdapter adapter = new SimpleAdapter(
							BusquedaCategoria.this, categoriaList,
							R.layout.list_item, new String[] { TAG_ID,
									TAG_NOMBRE },
									new int[] { R.id.id1, R.id.nombre,R.id.descripcion,R.id.precio,R.id.imagen1 });
					// updating listview
					setListAdapter(adapter);
				}
			});

		}
	}
}