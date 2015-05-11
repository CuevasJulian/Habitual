package com.ambientes.habitual;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
/**
 * la clase modo manual muestra los datos que llegan del servidor y adiciona
 * un modo de ejecutar el regar y no regar.
 * @author Cuervo
 *
 */
public class ModoManual extends Activity {

	private TextView temperatura;
	private TextView porcentLuz;
	private TextView porcentHumedad;
	private Comunicacion com;
	private int grados;
	private int intLuz;
	private int nivHumedad;
	private Handler handlerManual = new Handler();
	private ProgressBar luz;
	private ProgressBar nivelHumedad;
	private ImageButton desactivarMan;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modo_manual);
		com = Comunicacion.getInstancia();
		com.setObjeto(this);
		com.setActivity(this);
		temperatura = (TextView) findViewById(R.id.tempManual);
		temperatura.setText("--");
		porcentLuz = (TextView) findViewById(R.id.porcentajeLuzManual);
		porcentLuz.setText("--");
		porcentHumedad = (TextView) findViewById(R.id.porcentajeHumedadManual);
		porcentHumedad.setText("--");
		luz = (ProgressBar) findViewById(R.id.IntensidadLuzManual);
		luz.setMax(100);
		luz.setProgress(0);
		nivelHumedad = (ProgressBar) findViewById(R.id.nivelHumedadManual);
		nivelHumedad.setMax(100);
		nivelHumedad.setProgress(0);
		desactivarMan = (ImageButton) findViewById(R.id.desactivarManual);
		
		com.doInBackground(null);
	}
	
	/**
	 * desactiva el modo manual iniciando el modo automatico.
	 */
	public void desactiManual(){
		System.out.println("activando modo automatico");
		Intent intent = new Intent(this,Principal.class);
		finish();
		startActivity(intent);
		
	}
	
	/**
	 * actualiza los datos que vienen de server
	 * @param i dato de server
	 * @param o dato de server
	 * @param u dato de server
	 */
	public void actualizar(int i,int o,int u){
		//temperatura.setText(""+i+"");
		grados = i;
		intLuz = o;
		nivHumedad = u;
		System.out.println("actividad cambiando temperatura: "+grados);
		System.out.println("actividad cambiando intensidad: "+intLuz);
		System.out.println("actividad cambiando humedad: "+nivHumedad);
		handlerManual.post(new Runnable(){
			public void run(){
				temperatura.setText(""+grados+"");
				luz.setProgress(intLuz);
				nivelHumedad.setProgress(nivHumedad);
				porcentHumedad.setText(""+nivHumedad+"");
				porcentLuz.setText(""+intLuz+"");
			}
		});
		
		//temperatura.setText(""+grados+"");
	}

	protected void onStart(){
		super.onStart();
		System.out.println("onstart....");
		//temperatura.setText(""+grados+"");
		
	}
	protected void onPause(){
		super.onPause();
		System.out.println("onpause....");
		
			
		
	}
	protected void onResume(){
		super.onResume();
		System.out.println("onresume....");
	}
	protected void onStop(){
		super.onStop();
		System.out.println("onstop....");
		
	}
	
	/**
	 * inicia el alertBox de connfirmacion
	 * @param v
	 */
	public void onClicK_AlertBox(View v){
        confirmacion("Activando modo automatico.\n¿Esta seguro?:");
    }
	
	
	/**
	 * confirma el cambio de modo
	 * @param cadena
	 */
	public void confirmacion(String cadena){
	    //se prepara la alerta creando nueva instancia
	        AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
	    //seleccionamos la cadena a mostrar
	        alertbox.setMessage(cadena);
	        //elegimos un positivo SI y creamos un Listener
	        alertbox.setPositiveButton("Activar", new DialogInterface.OnClickListener() {
	            //Funcion llamada cuando se pulsa el boton Si
	            public void onClick(DialogInterface arg0, int arg1) {
	                mensaje("Activando modo automatico");
	                com.estadoServ(true);
	                desactiManual();
	                
	            }
	        });
	 
	        //elegimos un positivo NO y creamos un Listener
	        alertbox.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
	            //Funcion llamada cuando se pulsa el boton No
	            public void onClick(DialogInterface arg0, int arg1) {
	                mensaje("Cancelado");
	            }
	        });
	 
	        //mostramos el alertbox
	        alertbox.show();
	    }
	
	/**
	 * muestra el mensaje por un toast
	 * @param cadena
	 */
	public void mensaje(String cadena){
	    Toast.makeText(this, cadena, Toast.LENGTH_SHORT).show();
	    }
	
	/**
	 * envia la orden de regar
	 * @param v
	 */
	public void enviarRegar(View v){
		com.enviarRegar(true);
		Toast.makeText(this, "Regando", Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * envia la orden de no regar
	 * @param v
	 */
	public void enviarNoRegar(View v){
		com.enviarRegar(false);
		Toast.makeText(this, "Riego detenido", Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.modo_manual, menu);
		
		return true;
	}

}
