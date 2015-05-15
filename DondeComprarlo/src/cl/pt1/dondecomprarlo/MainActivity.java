package cl.pt1.dondecomprarlo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class MainActivity extends Activity {

	EditText txtbuscar;
	private static final String TAG_BUSCAR = "buscar";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
	 public void ListadoProductos(View v){
		 Intent i=new Intent(MainActivity.this, ListaProductos.class);
			startActivity(i);
	 }
	 public void BusquedaCategoria(View v){
		 Intent i=new Intent(MainActivity.this, BusquedaCategoria.class);
			startActivity(i);
	 }
	 public void BusquedaProducto(View v){
		 txtbuscar = (EditText) findViewById(R.id.TextoBuscar);
         String buscar;
         buscar = txtbuscar.getText().toString();
       
      // Starting new intent
 		Intent in = new Intent(getApplicationContext(),
 				BusquedaProducto.class);
 		// sending pid to next activity
 		in.putExtra(TAG_BUSCAR, buscar);
 		
 		// starting new activity and expecting some response back
 		startActivityForResult(in, 100);
         
	 }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
