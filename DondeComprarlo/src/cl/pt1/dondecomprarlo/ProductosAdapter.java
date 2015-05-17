package cl.pt1.dondecomprarlo;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductosAdapter extends BaseAdapter {
	protected Activity activity;
	  protected ArrayList<Productos> items;
	              
	  public ProductosAdapter(Activity activity, ArrayList<Productos> items) {
	    this.activity = activity;
	    this.items = items;
	  }
	  
	  @Override
	  public int getCount() {
	    return items.size();
	  }
	  
	  @Override
	  public Object getItem(int position) {
	    return items.get(position);
	  }
	  
	  @Override
	  public long getItemId(int position) {
	    return items.get(position).getId();
	  }
	  
	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    View vi=convertView;
	          
	    if(convertView == null) {
	      LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	      vi = inflater.inflate(R.layout.list_item, null);
	    }
	          
	    Productos productos = items.get(position);
	          
	    ImageView image = (ImageView) vi.findViewById(R.id.imagen1);
	    image.setImageBitmap(productos.getFoto());
	          
	    TextView nombre = (TextView) vi.findViewById(R.id.nombre);
	    nombre.setText("Modelo: "+productos.getNombre());
	    
	    TextView precio = (TextView) vi.findViewById(R.id.precio);
	    precio.setText("Precio: $"+productos.getPrecio());
	    
	    TextView id = (TextView) vi.findViewById(R.id.id1);
	    id.setText(Integer.toString(productos.getId()));
	 
	    return vi;
	  }
}
