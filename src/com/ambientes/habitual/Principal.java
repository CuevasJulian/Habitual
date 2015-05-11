package com.ambientes.habitual;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * clase principal, modo automatico de la aplicacion
 * tiene como objetivo mostrar solo el estado automatico y los datos
 * provenientes del servidor
 * @author Cuervo
 *
 */
public class Principal extends Activity {

	private TextView temperatura; 
	private TextView porcentLuz;
	private TextView porcentHumedad;
	private Comunicacion com;
	private int grados;
	private int intLuz;
	private int nivHumedad;
	private Handler handler = new Handler();
	private ProgressBar luz;
	private ProgressBar nivelHumedad;
	private ImageButton activarMan;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_principal);
		com = Comunicacion.getInstancia();
		com.setObjeto(this);
		com.setActivity(this);
		porcentLuz = (TextView) findViewById(R.id.porcentajeLuz);
		porcentLuz.setText("--");
		porcentHumedad = (TextView) findViewById(R.id.porcentajeHumedad);
		porcentHumedad.setText("--");
		temperatura = (TextView) findViewById(R.id.temp);
		temperatura.setText("--");
		luz = (ProgressBar) findViewById(R.id.IntensidadLuz);
		luz.setMax(100);
		luz.setProgress(0);
		nivelHumedad = (ProgressBar) findViewById(R.id.nivelHumedad);
		nivelHumedad.setMax(100);
		nivelHumedad.setProgress(0);
		activarMan = (ImageButton) findViewById(R.id.activarManual);
		com.pedirConfig();
		
		
		com.doInBackground(null);
		
	}
	
	/**
	 * metodo para activar la actividad manual 
	 */
	public void actiManual(){
		System.out.println("activando modo manual");
		Intent intent = new Intent(this,ModoManual.class);
		finish();
		startActivity(intent);
		
	}
	
	/**
	 * actualiza los textview correspondientes a los datos recibidos
	 * @param i dato proveniente del servidor
	 * @param o dato proveniente del servidor
	 * @param u dato proveniente del servidor
	 */
	public void actualizar(int i,int o,int u){
		//temperatura.setText(""+i+"");
		grados = i;
		intLuz = o;
		nivHumedad = u;
		System.out.println("actividad cambiando temperatura: "+grados);
		System.out.println("actividad cambiando intensidad: "+intLuz);
		System.out.println("actividad cambiando humedad: "+nivHumedad);
		handler.post(new Runnable(){
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
	 * inicia el alert box de confirmacion
	 * @param v
	 */
	public void onClicK_AlertBox(View v){
        confirmacion("Activando modo manual.\n¿Esta seguro?:");
    }
	
	/**
	 * inicia la actividad configuracion 
	 * @param v
	 */
	public void irConfig(View v){
		Intent intent = new Intent(this,Configuraciones.class);
		
		startActivity(intent);
        Toast.makeText(this, "Configuraciones", Toast.LENGTH_SHORT).show();
    }
	
	/**
	 * confirma el cambio de modo o lo  niega
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
	                mensaje("Activando modo manual");
	                com.estadoServ(false);
	                actiManual();
	                
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
	 * manda el toast del mensaje cadena
	 * @param cadena
	 */
	public void mensaje(String cadena){
	    Toast.makeText(this, cadena, Toast.LENGTH_SHORT).show();
	    }
	


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.principal, menu);
		return true;
	}

}
