package cl.pt1.dondecomprarlo;

import java.io.InputStream;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;


public class ImagenCompleta extends Activity{
	String img,id;
	private ProgressDialog pDialog;

	private static final String TAG_IMG = "img";
	private static final String TAG_BUSCAR = "buscar";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.imagen_completa);

		Intent i = getIntent();


		// getting Empleado id (pid) from intent
		img = i.getStringExtra(TAG_IMG);
		id = i.getStringExtra(TAG_BUSCAR);
		new cargarImagen().execute();

	}
	public void VolverInformacion(View v){
		// Starting new intent
		System.out.println("Volver");
		Intent in = new Intent(getApplicationContext(),
				InformacionProductos.class);
		// sending pid to next activity
		in.putExtra(TAG_BUSCAR, id);
		startActivityForResult(in, 100);
		finish();
	}
	


	/**
	 * Background Async Task to Load all Productos by making HTTP Request
	 * */
	class cargarImagen extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ImagenCompleta.this);
			pDialog.setMessage("Cargando Imagen de Producto. Por favor, espere...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();

		}



		/**
		 * getting All productos from url
		 * */
		protected String doInBackground(String... args) {



				new DownloadImageTask((ImageView) findViewById(R.id.imagen_completa)) .execute(img);

			return null; 

		}



		/**
		 * After completing background task Dismiss the progress dialog
		 * **/


		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all productos
			pDialog.dismiss();

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
