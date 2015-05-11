package com.ambientes.habitual;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
/**
 * la actividad configuraciones sera el panel de control de la configracion presente en el servidor
 * @author Cuervo
 *
 */
public class Configuraciones extends Activity {

	private Comunicacion com;
	private TextView porcentLuz;
	private TextView porcentHumedad;
	private TextView temperatura;
	private SeekBar seekLuz;
	private int tempEstandar = 25;
	private SeekBar seekHumedad;
	private Handler handler = new Handler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_configuraciones);
		com = Comunicacion.getInstancia();
		
		porcentLuz = (TextView) findViewById(R.id.configPorcentLuz);
		seekLuz = (SeekBar) findViewById(R.id.datoIntensidad);
		seekLuz.setProgress(com.getLuz());
		porcentLuz.setText(""+seekLuz.getProgress()+"");
		
		porcentHumedad = (TextView) findViewById(R.id.configPorcentHumedad);
		seekHumedad = (SeekBar) findViewById(R.id.datoHumedad);
		seekHumedad.setProgress(com.getHum());
		porcentHumedad.setText(""+seekHumedad.getProgress()+"");
		
		tempEstandar = com.getTemp();
		temperatura = (TextView) findViewById(R.id.temperaturaConfig);
		temperatura.setText(""+tempEstandar+"");
		
		seekLuz.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {       

		    @Override       
		    public void onStopTrackingTouch(SeekBar seekBar) {      
		        // TODO Auto-generated method stub      
		    }       

		    @Override       
		    public void onStartTrackingTouch(SeekBar seekBar) {     
		        // TODO Auto-generated method stub      
		    }       

		    @Override       
		    public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {     
		        // TODO Auto-generated method stub      

		       porcentLuz.setText(""+seekLuz.getProgress()+"");
		        //Toast.makeText(getApplicationContext(), String.valueOf(progress),Toast.LENGTH_LONG).show();

		    }       
		});
		
		seekHumedad.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {       

		    @Override       
		    public void onStopTrackingTouch(SeekBar seekBar) {      
		        // TODO Auto-generated method stub      
		    }       

		    @Override       
		    public void onStartTrackingTouch(SeekBar seekBar) {     
		        // TODO Auto-generated method stub      
		    }       

		    @Override       
		    public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {     
		        // TODO Auto-generated method stub      

		       porcentHumedad.setText(""+seekHumedad.getProgress()+"");
		        //Toast.makeText(getApplicationContext(), String.valueOf(progress),Toast.LENGTH_LONG).show();

		    }       
		});
		
	}
	
	
	/**
	 * sube el entero de temperatura
	 * @param v
	 */
	public void subeTemp(View v){
		
		handler.post(new Runnable(){
			public void run(){
				tempEstandar += 1;
				temperatura.setText(""+tempEstandar+"");
			}
		});
		
	}
	
	
	/**
	 * envia los datos para guardarlos y cambia de actividad
	 * @param v
	 */
	public void guardarDatos(View v){
		System.out.println("enviando nuevos datos al servidor");
		com.enviarDatos(tempEstandar,seekHumedad.getProgress(),seekLuz.getProgress());
		System.out.println("datos nuevos enviados y guardados.");
		Intent intent = new Intent(this,Principal.class);
		finish();
		startActivity(intent);
		Toast.makeText(this, "Datos guardados", Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * baja el entero de la temperatura
	 * @param v
	 */
	public void bajaTemp(View v){
		
		handler.post(new Runnable(){
			public void run(){
				tempEstandar -= 1;
				temperatura.setText(""+tempEstandar+"");
			}
		});
		
	}
	
		
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.configuraciones, menu);
		return true;
	}

}
