package cl.pt1.dondecomprarlo;

import java.util.ArrayList;
import java.util.List;
import android.support.v4.app.DialogFragment;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;




import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;


public class ResultadosCategoria extends Activity {

	String buscar,buscar1,producto,product,img1,texto;
	private static final String TAG_BUSCAR = "buscar";
	private static final String TAG_ID_MARCA = "id_marca";
	private static final String TAG_TEXTO = "texto";
	// Progress Dialog
	private ProgressDialog pDialog;

	// Crear objeto JSON Parser 
	JSONParser jParser = new JSONParser();
	ArrayList<Productos> productosDisponibles;
	ListView lvProductos;


	private static String url_all_productos = servidor.ip() + servidor.ruta() +"mostrar_productos_marca.php";

	// JSON Nodos
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_productos = "productos";
	private static final String TAG_ID = "id_productos";
	private static final String TAG_NOMBRE = "nombre_producto";
	private static final String TAG_PRECIO = "precio_producto";
	private static final String TAG_IMAGEN = "url_imagen";

	// productos JSONArray
	JSONArray productosjson = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.todos_productos);
		// Tomando detalles del Intent
		Intent i = getIntent();


		// Recibiendo los datos del Intent
		buscar = i.getStringExtra(TAG_BUSCAR); //id_categoria
		buscar1 = i.getStringExtra(TAG_ID_MARCA);
		texto = i.getStringExtra(TAG_TEXTO); // Recibiendo el texto a ingresar en la busqueda para productos
		System.out.println("asdasdasdasd"+texto);
		// Hashmap para el ListView
		lvProductos = (ListView) findViewById(R.id.list);
		productosDisponibles = new ArrayList<Productos>();

		// Llamando Clase LoadProductos
		new LoadProductos().execute();

		// Programar Click en ListView.
		lvProductos.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//Tomando datos del TextView en donde se encuentra el id del producto, para mandarlo a InformaciónProducto
				product = ((TextView) view.findViewById(R.id.id1)).getText()
						.toString();


				// Empezando el nuevo Intent
				Intent in = new Intent(getApplicationContext(),
						InformacionProductos.class);
				// mandando an intent el TAG_BUSCAR, que el entra el id del producto al que se le hizo click.
				in.putExtra(TAG_BUSCAR, product);
				
				// Se empieza la nueva actividad, esperando una respuesta de vuelta...
				startActivityForResult(in, 100);

//				FragmentManager fragmentManager = getSupportFragmentManager();
//	            FragmentTransaction fragmentTransaction = fragmentManager
//	                    .beginTransaction();
//	            InformacionProductoFragment fragmentS1 = new InformacionProductoFragment();
//	            fragmentTransaction.replace(R.id.pager, fragmentS1);
//	            fragmentTransaction.commit();

//				InformacionProductoFragment fragmentS1 = new InformacionProductoFragment();
//				getFragmentManager().beginTransaction().replace(R.id.pager, fragmentS1).commit();

//				Bundle bundle=new Bundle();
//				bundle.putString(TAG_BUSCAR, product);
//				  //set Fragmentclass Arguments
//				InformacionProductoFragment fragobj=new InformacionProductoFragment();
//				fragobj.setArguments(bundle);

				
				
				
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
			params.add(new BasicNameValuePair("texto",texto));
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
						Productos c = new Productos(1,"No hay productos",
								" No Disponible");
						productosDisponibles.add(c);

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
			ProductosAdapter productAdapter = new ProductosAdapter(ResultadosCategoria.this, productosDisponibles);
			lvProductos.setAdapter(productAdapter);	


		}

	}

}
