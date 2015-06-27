package cl.pt1.dondecomprarlo;
import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapaTienda extends Activity {
	String id_tienda,id_tienda_sucursal,latitud,longitud, direccion_sucursal,nombre_tienda;
	private static final String TAG_ID_TIENDA = "id_tienda";
	private static final String TAG_ID_TIENDA_SUCURSAL = "id_tienda_sucursal";
	private static final String TAG_LATITUD = "latitud";
	private static final String TAG_LONGITUD = "longitud";
	private static final String TAG_DIRECCION_SUCURSAL = "direccion_sucursal";
	private static final String TAG_NOMBRE_TIENDA = "nombre_tienda";

	private GoogleMap gMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapa);

		// tomando datos de intent
		Intent i = getIntent();


		// sacando el id del producto del intent
		id_tienda = i.getStringExtra(TAG_ID_TIENDA);
		id_tienda_sucursal=i.getStringExtra(TAG_ID_TIENDA_SUCURSAL);
		latitud=i.getStringExtra(TAG_LATITUD);
		longitud=i.getStringExtra(TAG_LONGITUD);
		direccion_sucursal=i.getStringExtra(TAG_DIRECCION_SUCURSAL);
		nombre_tienda = i.getStringExtra(TAG_NOMBRE_TIENDA);
		
		Double lat = Double.parseDouble(latitud);
		Double lon = Double.parseDouble(longitud);

		System.out.println("Id tienda "+id_tienda);
		System.out.println("id sucursal "+id_tienda_sucursal);
		System.out.println("direccion "+direccion_sucursal);
		System.out.println("nombre tieda "+nombre_tienda);


		
		MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
		gMap = mapFragment.getMap();
		gMap.setMyLocationEnabled(true);

		//Asigno punto inicial para mostrar.
		CameraUpdate cam = CameraUpdateFactory.newLatLng(new LatLng(-33, -71.5167));
		gMap.moveCamera(cam);

		// Asigno un nivel de zoom
		CameraUpdate ZoomCam = CameraUpdateFactory.zoomTo(10);
		gMap.animateCamera(ZoomCam);

		gMap.addMarker(new MarkerOptions().position(new LatLng(lat,lon))
				.title(nombre_tienda).snippet(direccion_sucursal));


	}


}