package cl.pt1.dondecomprarlo;

import java.io.InputStream;
import java.lang.reflect.Array;
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
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;

public class InformacionProductos extends ListActivity{


	String buscar,nombre,descripcion,precio,marca,categoria,id,ima,img,img1,img2,img3,img4,img5,img6;
	String id_tienda,nombre_tienda,id_tienda_sucursal,latitud,longitud,direccion_sucursal;
	String[]imag = new String[6];
	private static final String TAG_BUSCAR = "buscar";
	// Progress Dialog
	private ProgressDialog pDialog;

	// Creating JSON Parser object
	JSONParser jParser = new JSONParser();

	ArrayList<HashMap<String, String>> tiendaList;

	private static String url_all_productos = servidor.ip() + servidor.ruta() +"informacion_producto.php";
	private static String url_all_imagenes = servidor.ip() + servidor.ruta() +"todas_imagenes.php";
	private static String url_all_tiendas = servidor.ip() + servidor.ruta() +"tiendas_producto.php";
	
	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_productos = "productos";
	private static final String TAG_imagenes = "imagenes";
	private static final String TAG_tienda = "tiendas";
	private static final String TAG_ID = "id_productos";
	private static final String TAG_NOMBRE = "nombre_producto";
	private static final String TAG_DESCRIPCION = "descripcion_producto";
	private static final String TAG_PRECIO = "precio_producto";
	private static final String TAG_IMAGEN = "url_imagen";
	private static final String TAG_CATEGORIA = "nombre_sub_categoria";
	private static final String TAG_MARCA = "nombre_marca";
	private static final String TAG_IMG = "img";
	private static final String TAG_ID_TIENDA = "id_tienda";
	private static final String TAG_NOMBRE_TIENDA = "nombre_tienda";
	private static final String TAG_ID_TIENDA_SUCURSAL = "id_tienda_sucursal";
	private static final String TAG_LATITUD = "latitud";
	private static final String TAG_LONGITUD = "longitud";
	private static final String TAG_DIRECCION_SUCURSAL = "direccion_sucursal";




	
	// productos JSONArray
	JSONArray productos = null;
	JSONArray imagenes = null;
	JSONArray tiendas = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.informacion_producto);
		// tomando datos de intent
		Intent i = getIntent();


		// sacando el id del producto del intent
		buscar = i.getStringExtra(TAG_BUSCAR);
		// Hashmap for ListView
		tiendaList = new ArrayList<HashMap<String, String>>();

		// Cargar productos en Background Thread
		new LoadProductos().execute();
		
		// Get listview
				ListView lv = getListView();

				// Al seleccionar una tienda

				lv.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// getting values from selected ListItem
						String id_tienda = ((TextView) view.findViewById(R.id.id1)).getText()
								.toString();
						String id_tienda_sucursal = ((TextView) view.findViewById(R.id.id_tienda_sucursal)).getText()
								.toString();
						String latitud = ((TextView) view.findViewById(R.id.latitud)).getText()
								.toString();
						String longitud = ((TextView) view.findViewById(R.id.longitud)).getText()
								.toString();
						String direccion_sucursal = ((TextView) view.findViewById(R.id.direccion_sucursal)).getText()
								.toString();
						String nombre_tienda = ((TextView) view.findViewById(R.id.nombre)).getText()
								.toString();

						// Nuevo Intent
						Intent in = new Intent(getApplicationContext(),
								MapaTienda.class);
						// Enviando datos en intent
						in.putExtra(TAG_ID_TIENDA, id_tienda); //TAG_BUSCAR= id_categoria
						in.putExtra(TAG_ID_TIENDA_SUCURSAL, id_tienda_sucursal);
						in.putExtra(TAG_LATITUD, latitud);
						in.putExtra(TAG_LONGITUD, longitud);
						in.putExtra(TAG_DIRECCION_SUCURSAL, direccion_sucursal);
						in.putExtra(TAG_NOMBRE_TIENDA, nombre_tienda);
						// empezando actividad.
						startActivityForResult(in, 100);
					}
				});
		
		

	}
	public void cargarImagen(View v){
		// Starting new intent
		Intent in = new Intent(getApplicationContext(),
				ImagenCompleta.class);
		// sending pid to next activity
		in.putExtra(TAG_IMG, img);
		in.putExtra(TAG_BUSCAR, buscar); //id_producto
		startActivityForResult(in, 100);
		//finish();
	}
	// Poniendo imagenes en ImageView tras hacerle click a las pequeñas.
	public void cargarImagen1(View v){
		img =img1;
		new DownloadImageTask((ImageView) findViewById(R.id.imageView1)) .execute(img);
	}
	public void cargarImagen2(View v){
		img=img2;
		new DownloadImageTask((ImageView) findViewById(R.id.imageView1)) .execute(img);
	}
	public void cargarImagen3(View v){
		img=img3;
		new DownloadImageTask((ImageView) findViewById(R.id.imageView1)) .execute(img);
	}
	public void cargarImagen4(View v){
		img=img4;
		new DownloadImageTask((ImageView) findViewById(R.id.imageView1)) .execute(img);
	}
	public void cargarImagen5(View v){
		img=img5;
		new DownloadImageTask((ImageView) findViewById(R.id.imageView1)) .execute(img);
	}
	public void cargarImagen6(View v){
		img=img6;
		new DownloadImageTask((ImageView) findViewById(R.id.imageView1)) .execute(img);
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
					// Getting Array of productos
					productos = json.getJSONArray(TAG_productos);

					// looping through All productos

					for (int i = 0; i < productos.length(); i++) {
						JSONObject c = productos.getJSONObject(i);

						// Storing each json item in variable
						id = c.getString(TAG_ID);
						nombre = c.getString(TAG_NOMBRE);
						descripcion = c.getString(TAG_DESCRIPCION);
						precio = c.getString(TAG_PRECIO);
						categoria = c.getString(TAG_CATEGORIA);
						marca = c.getString(TAG_MARCA);
					}

				} else {
					// no productos found

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

			// Seteando valores en los TextView
			nombre1.setText("Modelo: "+nombre);
			descripcion1.setText("Descripción: "+descripcion);
			precio1.setText("Precio: $"+precio);
			categoria1.setText("Categoría: "+categoria);
			marca1.setText("Marca: "+marca);

			// LLamando a la clase LoadImagenes, para cargar las imagenes...
			new LoadTiendas().execute();
		}




	}	

	class LoadImagenes extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(InformacionProductos.this);
			pDialog.setMessage("Cargando Imagenes del Producto. Por favor, espere...");
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
			JSONObject jsonImagen = jParser.makeHttpRequest(url_all_imagenes, "GET", params);

			// Check your log cat for JSON reponse
			Log.d("Todas imagenes del producto", jsonImagen.toString());
			try{
				// Checking for SUCCESS TAG
				int successIMG = jsonImagen.getInt(TAG_SUCCESS);

				if (successIMG == 1) {

					// Getting Array of productos
					imagenes = jsonImagen.getJSONArray(TAG_imagenes);
					// looping through All productos

					for (int i = 0; i < imagenes.length(); i++) {
						JSONObject c = imagenes.getJSONObject(i);

						// Storing each json item in variable
						//						id = c.getString(TAG_ID);
						ima = c.getString(TAG_IMAGEN);
						imag[i]=ima;

					}

				} else {
					// no imagenes found

					System.out.println("No se han encontrado imagenes para producto");
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
			// Mostrando Imagen en ImageView

			new DownloadImageTask((ImageView) findViewById(R.id.imageView1)) .execute(imag[0]);
			img=imag[0];
			int i = 0;
			while (i<imagenes.length()){
				img1=imag[i];
				new DownloadImageTask((ImageView) findViewById(R.id.imageView2)) .execute(img1);			
				i++;
				while (i<imagenes.length()){
					img2=imag[i];
					new DownloadImageTask((ImageView) findViewById(R.id.imageView3)) .execute(img2);					
					i++;
					while (i<imagenes.length()){
						img3=imag[i];
						new DownloadImageTask((ImageView) findViewById(R.id.imageView4)) .execute(img3);						
						i++;
						while (i<imagenes.length()){
							img4=imag[i];
							new DownloadImageTask((ImageView) findViewById(R.id.imageView5)) .execute(img4);							
							i++;
							while (i<imagenes.length()){
								img5=imag[i];
								new DownloadImageTask((ImageView) findViewById(R.id.imageView6)) .execute(img5);								
								i++;
								while (i<imagenes.length()){
									img6=imag[i];
									new DownloadImageTask((ImageView) findViewById(R.id.imageView7)) .execute(img6);					
									i++;

								}

							}

						}

					}

				}

			}

		}	

	}

	class LoadTiendas extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			System.out.println("Entre al onpreexecute de cargar tiendas");
			super.onPreExecute();
			pDialog = new ProgressDialog(InformacionProductos.this);
			pDialog.setMessage("Cargando Tiendas que poseen el producto. Por favor, espere...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();

		}



		/**
		 * getting All productos from url
		 * */
		protected String doInBackground(String... args) {
			System.out.println("Entre al doingbackround de cargar tiendas");

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("buscar", buscar));

			// getting JSON string from URL
			JSONObject jsonTienda = jParser.makeHttpRequest(url_all_tiendas, "GET", params);

			// Check your log cat for JSON reponse
			Log.d("Todas tiendas que poseen el producto", jsonTienda.toString());
			try{
				// Checking for SUCCESS TAG
				int success = jsonTienda.getInt(TAG_SUCCESS);

				if (success == 1) {

					// Getting Array of productos
					tiendas = jsonTienda.getJSONArray(TAG_tienda);
					// looping through All productos

					for (int i = 0; i < tiendas.length(); i++) {
						JSONObject c = tiendas.getJSONObject(i);

						// Storing each json item in variable
					
						id_tienda=c.getString(TAG_ID_TIENDA);
						nombre_tienda=c.getString(TAG_NOMBRE_TIENDA);
						precio="Precio: $"+c.getString(TAG_PRECIO);
						id_tienda_sucursal = c.getString(TAG_ID_TIENDA_SUCURSAL);
						latitud = c.getString(TAG_LATITUD);
						longitud = c.getString(TAG_LONGITUD);
						direccion_sucursal = c.getString(TAG_DIRECCION_SUCURSAL);

						
						// creating new HashMap
						HashMap<String, String> map = new HashMap<String, String>();

						// adding each child node to HashMap key => value
						map.put(TAG_ID_TIENDA, id_tienda);
						map.put(TAG_NOMBRE_TIENDA, nombre_tienda);
						map.put(TAG_PRECIO, precio);
						map.put(TAG_ID_TIENDA_SUCURSAL, id_tienda_sucursal);
						map.put(TAG_LATITUD,latitud);
						map.put(TAG_LONGITUD,longitud);
						map.put(TAG_DIRECCION_SUCURSAL,direccion_sucursal);
						
						// adding HashList to ArrayList
						tiendaList.add(map);

					}

				} else {
					// no imagenes found

					System.out.println("No se han encontrado tiendas para producto");
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
			System.out.println("Entre al onPOSTexecute de cargar tiendas");

			// dismiss the dialog after getting all productos
			pDialog.dismiss();
		
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					/**
					 * Updating parsed JSON data into ListView
					 * */
					ListAdapter adapter = new SimpleAdapter(
							InformacionProductos.this, tiendaList,
							R.layout.list_item_tienda, new String[] { TAG_ID_TIENDA,
									TAG_NOMBRE_TIENDA,TAG_PRECIO , TAG_ID_TIENDA_SUCURSAL,TAG_LATITUD,TAG_LONGITUD,TAG_DIRECCION_SUCURSAL},
							new int[] { R.id.id1, R.id.nombre,R.id.precio ,R.id.id_tienda_sucursal , R.id.latitud,R.id.longitud, R.id.direccion_sucursal});
					// updating listview
					setListAdapter(adapter);
				}
			});
			new LoadImagenes().execute();



		}	

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





