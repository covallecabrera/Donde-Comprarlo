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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;


public class RegistroUsuario extends Activity {
	private static String url_all_productos = servidor.ip() + servidor.ruta() +"registro_usuario.php";
	EditText nombre,correo;
	String nombreUsuario, correoUsuario;
	int success;
	JSONParser jParser = new JSONParser();
	private ProgressDialog pDialog;
	private static final String TAG_SUCCESS = "success";
	JSONArray productosjson = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registro_usuario);
		
		 Button btnSend = (Button) findViewById(R.id.registro);
	        btnSend.setOnClickListener(new OnClickListener() {      
	        	@Override
				public void onClick(View v) {
	        		nombre = (EditText) findViewById(R.id.nombre);
	        		correo = (EditText) findViewById(R.id.correo);

	        		nombreUsuario = nombre.getText().toString();
	        		correoUsuario = correo.getText().toString();
	        		
	        		new LoadProductos().execute();
	        
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
			pDialog = new ProgressDialog(RegistroUsuario.this);
			pDialog.setMessage("Registrando Usuario. Por favor, espere...");
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
			params.add(new BasicNameValuePair("nombre", nombreUsuario));
			params.add(new BasicNameValuePair("correo",correoUsuario));

			// getting JSON string from URL

			JSONObject json = jParser.makeHttpRequest(url_all_productos, "GET", params);

			// Check your log cat for JSON reponse
			Log.d("All productos: ", json.toString());

			try{
				// Checking for SUCCESS TAG
				success = json.getInt(TAG_SUCCESS);
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null; 

		}




		protected void onPostExecute(String file_url) {
			
			pDialog.dismiss();
			if (success == 1) {
				finish();
				success = 0 ;
			} else {

			}

						
		}

	}



}	
            

