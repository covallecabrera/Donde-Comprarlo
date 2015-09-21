package cl.pt1.dondecomprarlo;

import cl.pt1.dondecomprarlo.R;
import cl.pt1.dondecomprarlo.modelo.producto;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
import android.support.v4.app.Fragment;
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
import android.widget.Toast;

public class InformacionProductoFragment extends Fragment{

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
	
	View rootView;

	// productos JSONArray
	JSONArray productos = null;
	JSONArray imagenes = null;
	JSONArray tiendas = null;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.informacion_producto_fragment, container, false);
		buscar = producto.productos;
		
		new LoadProductos().execute();
		ImageView img1 = (ImageView) rootView.findViewById(R.id.imageView2);
		img1.setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
		    	cargarImagen1();
		    }
		});
		ImageView img2 = (ImageView) rootView.findViewById(R.id.imageView3);
		img2.setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
		    	cargarImagen2();
		    }
		});
		ImageView img3 = (ImageView) rootView.findViewById(R.id.imageView4);
		img3.setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
		    	cargarImagen3();
		    }
		});
		ImageView img4 = (ImageView) rootView.findViewById(R.id.imageView5);
		img4.setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
		    	cargarImagen4();
		    }
		});
		ImageView img5 = (ImageView) rootView.findViewById(R.id.imageView6);
		img5.setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
		    	cargarImagen5();
		    }
		});
		ImageView img6 = (ImageView) rootView.findViewById(R.id.imageView7);
		img6.setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
		    	cargarImagen6();
		    }
		});
		ImageView img = (ImageView) rootView.findViewById(R.id.imageView1);
		img.setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
		    	cargarImagen();
		    }
		});
		
		// Cargar productos en Background Thread
		return rootView;
	}


	public void cargarImagen(){
//		 Starting new intent
		Intent in = new Intent(getActivity(),
				ImagenCompleta.class);
		// sending pid to next activity
		in.putExtra(TAG_IMG, img);
		in.putExtra(TAG_BUSCAR, buscar); //id_producto
		startActivityForResult(in, 100);
		//finish();
	}
	// Poniendo imagenes en ImageView tras hacerle click a las pequeñas.
	
	
	
	public void cargarImagen1(){
		img =img1;
		new DownloadImageTask((ImageView) rootView.findViewById(R.id.imageView1)) .execute(img);
	}
	public void cargarImagen2(){
		img=img2;
		new DownloadImageTask((ImageView) rootView.findViewById(R.id.imageView1)) .execute(img);
	}
	public void cargarImagen3(){
		img=img3;
		new DownloadImageTask((ImageView) rootView.findViewById(R.id.imageView1)) .execute(img);
	}
	public void cargarImagen4(){
		img=img4;
		new DownloadImageTask((ImageView) rootView.findViewById(R.id.imageView1)) .execute(img);
	}
	public void cargarImagen5(){
		img=img5;
		new DownloadImageTask((ImageView) rootView.findViewById(R.id.imageView1)) .execute(img);
	}
	public void cargarImagen6(){
		img=img6;
		new DownloadImageTask((ImageView) rootView.findViewById(R.id.imageView1)) .execute(img);
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
			pDialog = new ProgressDialog(getActivity());
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
			TextView nombre1 = (TextView) rootView.findViewById(R.id.nombre);
			TextView descripcion1 = (TextView) rootView.findViewById(R.id.descripcion);
			TextView precio1 = (TextView) rootView.findViewById(R.id.precio);
			TextView categoria1 = (TextView) rootView.findViewById(R.id.categoria);
			TextView marca1 = (TextView) rootView.findViewById(R.id.marca);

			// Seteando valores en los TextView
			nombre1.setText("Modelo: "+nombre);
			descripcion1.setText("Descripción: "+descripcion);
			precio1.setText("Precio: $"+precio);
			categoria1.setText("Categoría: "+categoria);
			marca1.setText("Marca: "+marca);

			// LLamando a la clase LoadImagenes, para cargar las imagenes...
			new LoadImagenes().execute(); 
		}


	}	

	class LoadImagenes extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(getActivity());
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

			new DownloadImageTask((ImageView) rootView.findViewById(R.id.imageView1)) .execute(imag[0]);
			img=imag[0];
			int i = 0;
			while (i<imagenes.length()){
				img1=imag[i];
				new DownloadImageTask((ImageView) rootView.findViewById(R.id.imageView2)) .execute(img1);			
				i++;
				while (i<imagenes.length()){
					img2=imag[i];
					new DownloadImageTask((ImageView) rootView.findViewById(R.id.imageView3)) .execute(img2);					
					i++;
					while (i<imagenes.length()){
						img3=imag[i];
						new DownloadImageTask((ImageView) rootView.findViewById(R.id.imageView4)) .execute(img3);						
						i++;
						while (i<imagenes.length()){
							img4=imag[i];
							new DownloadImageTask((ImageView) rootView.findViewById(R.id.imageView5)) .execute(img4);							
							i++;
							while (i<imagenes.length()){
								img5=imag[i];
								new DownloadImageTask((ImageView) rootView.findViewById(R.id.imageView6)) .execute(img5);								
								i++;
								while (i<imagenes.length()){
									img6=imag[i];
									new DownloadImageTask((ImageView) rootView.findViewById(R.id.imageView7)) .execute(img6);					
									i++;

								}

							}

						}

					}

				}

			}

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
