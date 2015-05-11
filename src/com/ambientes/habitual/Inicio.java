package com.ambientes.habitual;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

/**
 * clase inicio en la cual se conecta la aplicacion, busqueda e inicio de la comunicacion.
 * @author Cuervo
 *
 */
public class Inicio extends Activity {
	
	
	protected Comunicacion com;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("onCreate....");
		setContentView(R.layout.activity_inicio);
		
		
		com = Comunicacion.getInstancia();
		com.setActivity(this);
		com.execute();	
	}	
	public void onStart(){
		super.onStart();
		System.out.println("onstart....");
		
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
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.inicio, menu);
		return true;
	}
	
	

}


