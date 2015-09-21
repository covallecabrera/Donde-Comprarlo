package cl.pt1.dondecomprarlo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import cl.pt1.dondecomprarlo.R;
import cl.pt1.dondecomprarlo.modelo.producto;
import android.support.v4.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class TiendasProductoFragment extends ListFragment {
	String buscar,nombre,descripcion,precio,marca,categoria,id,ima,img,img1,img2,img3,img4,img5,img6;
	String id_tienda,nombre_tienda,id_tienda_sucursal,latitud,longitud,direccion_sucursal;
	String[]imag = new String[6];
	private static final String TAG_BUSCAR = "buscar";
	// Progress Dialog
	private ProgressDialog pDialog;
	ListView lv;
	// Creating JSON Parser object
	JSONParser jParser = new JSONParser();

	ArrayList<HashMap<String, String>> tiendaList;

	private static String url_all_tiendas = servidor.ip() + servidor.ruta() +"tiendas_producto.php";
	
	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_tienda = "tiendas";
	private static final String TAG_PRECIO = "precio_producto";
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

		rootView = inflater.inflate(R.layout.tiendas_producto_fragment, container, false);
		buscar = producto.productos;	
		
		tiendaList = new ArrayList<HashMap<String, String>>();
		
		new LoadTiendas().execute();
	
		return rootView;
	}
		
	@Override
    public void onListItemClick(ListView l, View rootView, int position, long id) {
        // TODO Auto-generated method stub
		// getting values from selected ListItem
		String id_tienda = ((TextView) rootView.findViewById(R.id.id1)).getText()
				.toString();
		String id_tienda_sucursal = ((TextView) rootView.findViewById(R.id.id_tienda_sucursal)).getText()
				.toString();
		String latitud = ((TextView) rootView.findViewById(R.id.latitud)).getText()
				.toString();
		String longitud = ((TextView) rootView.findViewById(R.id.longitud)).getText()
				.toString();
		String direccion_sucursal = ((TextView) rootView.findViewById(R.id.direccion_sucursal)).getText()
				.toString();
		String nombre_tienda = ((TextView) rootView.findViewById(R.id.nombre)).getText()
				.toString();
		
		System.out.println("Pesca que hice click en el listview de tiendas...:"+direccion_sucursal);
		// Nuevo Intent
		Intent in = new Intent(getActivity().getApplicationContext(),
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
        super.onListItemClick(l, rootView, position, id);
    }
	
	
	class LoadTiendas extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			System.out.println("Entre al onpreexecute de cargar tiendas");
			super.onPreExecute();
			pDialog = new ProgressDialog(getActivity());
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

						System.out.println(id_tienda);
						System.out.println(nombre_tienda);
						System.out.println(precio);
						System.out.println(id_tienda_sucursal);
						System.out.println(latitud);
						System.out.println(longitud);
						System.out.println(direccion_sucursal);
						
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
			getActivity().runOnUiThread(new Runnable() {
				public void run() {
					/**
					 * Updating parsed JSON data into ListView
					 * */
					ListAdapter adapter = new SimpleAdapter(
							getActivity(), tiendaList,
							R.layout.list_item_tienda, new String[] { TAG_ID_TIENDA,
									TAG_NOMBRE_TIENDA,TAG_PRECIO , TAG_ID_TIENDA_SUCURSAL,TAG_LATITUD,TAG_LONGITUD,TAG_DIRECCION_SUCURSAL},
							new int[] { R.id.id1, R.id.nombre,R.id.precio ,R.id.id_tienda_sucursal , R.id.latitud,R.id.longitud, R.id.direccion_sucursal});
					// updating listview
					setListAdapter(adapter);
				}
			});
			

		}	

	}


}
