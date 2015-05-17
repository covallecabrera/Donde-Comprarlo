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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;


public class ResultadosCategoria extends Activity {

	String buscar,buscar1,producto,product,img1;
	private static final String TAG_BUSCAR = "buscar";
	private static final String TAG_ID_MARCA = "id_marca";
	// Progress Dialog
	private ProgressDialog pDialog;

	// Crear objeto JSON Parser 
	JSONParser jParser = new JSONParser();
	ArrayList<Productos> productosDisponibles;
	ListView lvProductos;


	private static String url_all_productos = "http://192.168.0.5/donde_comprarlo/mostrar_productos_marca.php";

	// JSON Nodos
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_productos = "productos";
	private static final String TAG_ID = "id_productos";
	private static final String TAG_NOMBRE = "nombre_producto";
	private static final String TAG_PRECIO = "precio_producto";
	private static final String TAG_IMAGEN = "imagen_producto1";

	// productos JSONArray
	JSONArray productosjson = null;

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
		lvProductos = (ListView) findViewById(R.id.list);
		productosDisponibles = new ArrayList<Productos>();

		// Llamando Clase LoadProductos
		new LoadProductos().execute();

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
			// Creamos el objeto CityAdapter y lo asignamos al ListView 
			ProductosAdapter cityAdapter = new ProductosAdapter(ResultadosCategoria.this, productosDisponibles);
			lvProductos.setAdapter(cityAdapter);	


		}

	}

}
