package cl.pt1.dondecomprarlo;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
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

public class InformacionProductos extends Activity{


	String buscar,nombre,descripcion,precio,marca,categoria,id,img1,img2,img3;
	private static final String TAG_BUSCAR = "buscar";
	// Progress Dialog
	private ProgressDialog pDialog;

	// Creating JSON Parser object
	JSONParser jParser = new JSONParser();

	ArrayList<HashMap<String, String>> productosList;

	private static String url_all_productos = "http://192.168.0.5/donde_comprarlo/informacion_producto.php";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_productos = "productos";
	private static final String TAG_ID = "id_productos";
	private static final String TAG_NOMBRE = "nombre_producto";
	private static final String TAG_DESCRIPCION = "descripcion_producto";
	private static final String TAG_PRECIO = "precio_producto";
	private static final String TAG_IMAGEN = "imagen_producto1";
	private static final String TAG_IMAGEN2 = "imagen_producto2";
	private static final String TAG_IMAGEN3 = "imagen_producto3";
	private static final String TAG_CATEGORIA = "nombre_categoria";
	private static final String TAG_MARCA = "nombre_marca";

	// productos JSONArray
	JSONArray productos = null;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.informacion_producto);
		// getting Empleado details from intent
		Intent i = getIntent();


		// getting Empleado id (pid) from intent
		buscar = i.getStringExtra(TAG_BUSCAR);
		// Hashmap for ListView
		productosList = new ArrayList<HashMap<String, String>>();

		// Loading empleados in Background Thread
		new LoadProductos().execute();

	}

	/**
	 * Background Async Task to Load all Productos by making HTTP Request
	 * */
	class LoadProductos extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(InformacionProductos.this);
			pDialog.setMessage("Cargando Informacion de Producto. Por favor, espere...");
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
					productos = json.getJSONArray(TAG_productos);
					// looping through All empleados

					for (int i = 0; i < productos.length(); i++) {
						JSONObject c = productos.getJSONObject(i);

						// Storing each json item in variable
						id = c.getString(TAG_ID);
						nombre = c.getString(TAG_NOMBRE);
						descripcion = c.getString(TAG_DESCRIPCION);
						precio = c.getString(TAG_PRECIO);
						categoria = c.getString(TAG_CATEGORIA);
						marca = c.getString(TAG_MARCA);
						img1 = c.getString(TAG_IMAGEN);
						img2 = c.getString(TAG_IMAGEN2);
						img3 = c.getString(TAG_IMAGEN3);
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
			// Inicializando los TextView con sus respectivos id.
			TextView nombre1 = (TextView) findViewById(R.id.nombre);
			TextView descripcion1 = (TextView) findViewById(R.id.descripcion);
			TextView precio1 = (TextView) findViewById(R.id.precio);
			TextView categoria1 = (TextView) findViewById(R.id.categoria);
			TextView marca1 = (TextView) findViewById(R.id.marca);
			// Mostrando Imagen en ImageView
			new DownloadImageTask((ImageView) findViewById(R.id.imageView1)) .execute(img1);
			new DownloadImageTask((ImageView) findViewById(R.id.imageView2)) .execute(img1);
			new DownloadImageTask((ImageView) findViewById(R.id.imageView3)) .execute(img2);
			new DownloadImageTask((ImageView) findViewById(R.id.imageView4)) .execute(img3);
			// Seteando valores en los TextView
			nombre1.setText(nombre);
			descripcion1.setText(descripcion);
			precio1.setText(precio);
			categoria1.setText(categoria);
			marca1.setText(marca);
		}


		private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> { 
			ImageView bmImage;
			public DownloadImageTask(ImageView bmImage) { 
				this.bmImage = bmImage; 
			} 
			protected Bitmap doInBackground(String... urls) { 
				String urldisplay = urls[0]; Bitmap mIcon11 = null; 
				try { 
					InputStream in = new java.net.URL(urldisplay).openStream(); 
					mIcon11 = BitmapFactory.decodeStream(in);
				} catch (Exception e) {
					Log.e("Error", e.getMessage());
					e.printStackTrace();
				}
				return mIcon11; 
			} 
			protected void onPostExecute(Bitmap result) { 
				bmImage.setImageBitmap(result); 
			}

		}

	}		

}

