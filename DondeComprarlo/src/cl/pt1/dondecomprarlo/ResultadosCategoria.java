package cl.pt1.dondecomprarlo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;


public class ResultadosCategoria extends ListActivity {

	String buscar,buscar1,producto;
	private static final String TAG_BUSCAR = "buscar";
	private static final String TAG_ID_MARCA = "id_marca";
	// Progress Dialog
	private ProgressDialog pDialog;

	// Crear objeto JSON Parser 
	JSONParser jParser = new JSONParser();

	ArrayList<HashMap<String, String>> productosList;

	private static String url_all_productos = "http://192.168.0.5/donde_comprarlo/mostrar_productos_marca.php";
	
	// JSON Nodos
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_productos = "productos";
	private static final String TAG_ID = "id_productos";
	private static final String TAG_NOMBRE = "nombre_producto";
	private static final String TAG_DESCRIPCION = "descripcion_producto";
	private static final String TAG_PRECIO = "precio_producto";
	private static final String TAG_IMAGEN = "imagen_producto1";

	// productos JSONArray
	JSONArray productos = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.todos_productos);
		// Tomando detalles del Intent
		Intent i = getIntent();


		// Recibiendo los datos del Intent
		buscar = i.getStringExtra(TAG_BUSCAR);
		buscar1 = i.getStringExtra(TAG_ID_MARCA);


		// Hashmap para el ListView
		productosList = new ArrayList<HashMap<String, String>>();

		// Llamando Clase LoadProductos
		new LoadProductos().execute();
		
		// Get listview
		ListView lv = getListView();

		// on seleting single Empleado
		// launching Edit Empleado Screen
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// getting values from selected ListItem
				producto = ((TextView) view.findViewById(R.id.id1)).getText()
						.toString();
	

				// Starting new intent
				Intent in = new Intent(getApplicationContext(),
						InformacionProductos.class);
				// sending pid to next activity
				in.putExtra(TAG_BUSCAR, producto);
				
				// starting new activity and expecting some response back
				startActivityForResult(in, 100);
			}
		});
		
	}

	class LoadProductos extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ResultadosCategoria.this);
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
			params.add(new BasicNameValuePair("buscar", buscar));
			params.add(new BasicNameValuePair("buscar1",buscar1));
			// getting JSON string from URL

			JSONObject json = jParser.makeHttpRequest(url_all_productos, "GET", params);

			// Check your log cat for JSON reponse
			Log.d("All productos: ", json.toString());

			try{

				// Checking for SUCCESS TAG
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					// Se encontraron productos
					// Tomar el Array de productos
					productos = json.getJSONArray(TAG_productos);
					// looping through All productos
					for (int i = 0; i < productos.length(); i++) {
						JSONObject c = productos.getJSONObject(i);

						// Guardando cada item del Json en una variable
					
						String id = c.getString(TAG_ID);
						String nombre = c.getString(TAG_NOMBRE);
						String descripcion = c.getString(TAG_DESCRIPCION);
						String precio = c.getString(TAG_PRECIO);
						String imagen = c.getString(TAG_IMAGEN);


						// Creando un nuevo HashMap
						HashMap<String, String> map = new HashMap<String, String>();

						// Agregando cada nodo con su variable al HashMap
						map.put(TAG_ID, id);
						map.put(TAG_NOMBRE, nombre);
						map.put(TAG_DESCRIPCION, descripcion);
						map.put(TAG_PRECIO, precio);
						map.put(TAG_IMAGEN,imagen);


						// Agregando el HashMap al Arreglo
						productosList.add(map);
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
							ResultadosCategoria.this, productosList,
							R.layout.list_item_busqueda, new String[] { TAG_ID,
									TAG_NOMBRE,TAG_PRECIO,TAG_IMAGEN },
									new int[] { R.id.id1, R.id.nombre,R.id.precio,R.id.imagen1 });
					// updating listview
					setListAdapter(adapter);
				}
			});

		}

	}

}
