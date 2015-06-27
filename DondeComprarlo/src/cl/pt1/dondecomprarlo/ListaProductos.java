package cl.pt1.dondecomprarlo;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;


public class ListaProductos extends Activity{


	String producto,img1,product;
	// Progress Dialog
	private ProgressDialog pDialog;

	// Creating JSON Parser object
	JSONParser jParser = new JSONParser();

	//	ArrayList<HashMap<String, String>> productosList;
	ArrayList<Productos> productosDisponibles;
	ListView lvProductos;
	private static String url_all_productos = servidor.ip() + servidor.ruta() +"productos.php";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_productos = "productos";
	private static final String TAG_ID = "id_productos";
	private static final String TAG_NOMBRE = "nombre_producto";
	private static final String TAG_PRECIO = "precio_producto";
	private static final String TAG_IMAGEN = "url_imagen";
	private static final String TAG_BUSCAR = "buscar";
	// productos JSONArray
	JSONArray productosjson = null;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.todos_productos);
		// Hashmap for ListView
		lvProductos = (ListView) findViewById(R.id.list);
		productosDisponibles = new ArrayList<Productos>();

		// Loading empleados in Background Thread
		new LoadAllProductos().execute();

		lvProductos.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				product = ((TextView) view.findViewById(R.id.id1)).getText()
						.toString();


				// Starting new intent
				Intent in = new Intent(getApplicationContext(),
						InformacionProductos.class);
				// sending pid to next activity
				in.putExtra(TAG_BUSCAR, product);

				// starting new activity and expecting some response back
				startActivityForResult(in, 100);

			}
		});

	}

	/**
	 * Background Async Task to Load all Productos by making HTTP Request
	 * */
	class LoadAllProductos extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			System.out.println("2");
			super.onPreExecute();
			pDialog = new ProgressDialog(ListaProductos.this);
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
			JSONObject json = jParser.makeHttpRequest(url_all_productos, "GET", params);

			// Check your log cat for JSON reponse
			Log.d("All productos: ", json.toString());

			try{

				// Checking for SUCCESS TAG
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					// productos found
					// Getting Array of empleados
					productosjson = json.getJSONArray(TAG_productos);
					// looping through All empleados
					for (int i = 0; i < productosjson.length(); i++) {
						JSONObject producto = productosjson.getJSONObject(i);

						Productos c = new Productos(producto.getInt(TAG_ID), producto.getString(TAG_NOMBRE),
								producto.getString(TAG_PRECIO));
						img1=producto.getString(TAG_IMAGEN);
						// Creamos el objeto City

						c.setData(producto.getString(TAG_IMAGEN));


						// Almacenamos el objeto en el array que hemos creado anteriormente
						productosDisponibles.add(c);
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

			// Creamos el objeto productAdapter y lo asignamos al ListView 
			ProductosAdapter productAdapter = new ProductosAdapter(ListaProductos.this, productosDisponibles);
			lvProductos.setAdapter(productAdapter);	


		}




	}	

}

