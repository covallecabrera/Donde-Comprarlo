package cl.pt1.dondecomprarlo;

import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class Productos {
	protected int id;
	protected String nombre;
	protected String precio;
	protected String data;
	protected Bitmap foto;

	public Productos(int id, String nombre, String precio) {
		this.id = id;
		this.nombre = nombre;
		this.precio = precio;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getPrecio() {
		return precio;
	}

	public void setPrecio(String precio) {
		this.precio = precio;
	}


	public String getData() {
		return data;
	}

	public void setData(String... urls) {
		String urldisplay = urls[0]; 
		try {   
			InputStream in = new java.net.URL(urldisplay).openStream(); 		
			this.foto = BitmapFactory.decodeStream(in);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public Bitmap getFoto() {
		return foto;
	}
}
