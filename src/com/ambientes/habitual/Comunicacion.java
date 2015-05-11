package com.ambientes.habitual;

import java.net.*;
import java.io.*;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * la tarea comunicacion sera el hilo correspondiente a ejecutar ciertas tareas como recorrer seekbars, crear el hilo comunicacion
 * indicar y registrar enteros de luz humedad y temperatura
 * @author Cuervo
 *
 */
public class Comunicacion extends AsyncTask<Void, Integer,Boolean> {

	private Socket socket;
	private DataInputStream input;
	private DataOutputStream output;
	private boolean conectado = false;
	private Activity acti;
	private ProgressBar progressBar;

	private hiloMensajes hiloMsg = null;
	private TextView indiTemp = null;
	private static Comunicacion instancia;
	private Object objeto;
	
	private Handler handler = new Handler();
	
	

	
	private Comunicacion(){
	
	
	}
	
	/**
	 * setea la actividad principal para poder llamarla
	 * @param act
	 */
	public void setActivity(Activity act){
		acti = act;	
		if(!conectado){
			progressBar = (ProgressBar)act.findViewById(R.id.progressBar1);	
		}
		
	}
	
	/**
	 * setea el objeto o clase que esta ejecutandose
	 * @param o
	 */
	public void setObjeto(Object o){
		objeto = o;
	}
	
	
	
	/**
	 * crea la instancia unica de comunicacion
	 * @return
	 */
	public static Comunicacion getInstancia(){
		if (instancia == null){ 
			instancia = new Comunicacion(); 
			} 
		return instancia;
	}
	
	/**
	 * inicia y registra la ip local
	 */
	public void conectarse(){
		try{
			InetAddress ip = InetAddress.getLocalHost();
			String[] tokens = ip.toString().split("/");
			//String[] dirs = tokens[1].split(".");
			//String ipHost = ""+dirs[0]+"."+dirs[1]+"."+dirs[2]+".";
			System.out.println("");
			
			
			System.out.println("intentando conexion con server");
			socket = new Socket("192.168.137.1",6780);
			input = new DataInputStream(socket.getInputStream());
			output = new DataOutputStream(socket.getOutputStream());
			conectado = true;
			
			System.out.println("conectado: "+conectado);
			
		}catch(IOException e){
			System.out.println("error en conexion");
		}
	}
	
	
	/**
	 * actualiza los datos en la actividad ejecutada
	 */
	public void actualizar(){
		if(acti != null){
			if(objeto instanceof Principal){
				((Principal) objeto).actualizar(hiloMsg.getTemperatura(),hiloMsg.getIntLuz(),hiloMsg.getHumedad());	
			}
			if(objeto instanceof ModoManual){
				((ModoManual) objeto).actualizar(hiloMsg.getTemperatura(),hiloMsg.getIntLuz(),hiloMsg.getHumedad());	
			}
			
		}
	}
	
	
	public boolean getListo(){
		return conectado;
	}
	
	
	/**
	 * rebisa el estado para decidir en cual pantalla entrar manual o automatico
	 */
	public void estado(){
		
		try{
			if(input.readBoolean()){
				Intent intent = new Intent(acti,Principal.class);
				acti.finish();
				acti.startActivity(intent);
			}else{
				Intent intent = new Intent(acti,ModoManual.class);
				acti.finish();
				acti.startActivity(intent);
			}
		}catch(IOException e){
			
		}
		
	}
	
	/**
	 * envia el estado al servidor
	 * @param est
	 */
	public void estadoServ(boolean est){
		try{
			output.writeUTF("modo");
			output.writeBoolean(est);
		}catch(IOException e){
			
		}
	}
	
	/**
	 * pide la configuracion guardada del server
	 */
	public void pedirConfig(){
		try{
			output.writeUTF("mandar config");
			
			
		}catch(IOException e){
			
		}
	}
	
	/**
	 * envia los datos registrados por la actividad correspondiente
	 * @param t temperatura
	 * @param h humedad
	 * @param l luz
	 */
	public void enviarDatos(int t,int h,int l){
		try{
		output.writeUTF("nueva config");
		output.writeInt(t);
		output.writeInt(h);
		output.writeInt(l);
		}catch(IOException e){
			
		}
	}
	
	/**
	 * tarea del asynctask
	 */
	@Override
	protected Boolean doInBackground(Void... arg0) {
		// TODO Auto-generated method stub
		System.out.println("hilo: conectando...");
		
			if(!conectado){
				for(int i = 1 ; i <= 10;i++ ){
					
					publishProgress(i*10);
					if(i == 10){
						conectarse();
						}
					if(conectado || isCancelled()){
						estado();
						
						break;
					}
					
				}	
			}else{
				if(hiloMsg == null){
				hiloMsg = new hiloMensajes(socket,input,output);
				hiloMsg.setActivity(acti);
				}else{
					
				}
			}
			
			return true;
		
	
	}
	protected void onProgressUpdate(Integer... values) {
		if(!conectado){
	        int progreso = values[0].intValue();
	        System.out.println("hilo: progreso...:"+ progreso);
	        progressBar.setProgress(progreso);	
		}
    }
	protected void onPreExecute() {
		if(!conectado){
			progressBar.setMax(100);
			progressBar.setProgress(0);	
		}
		
    }
	 protected void onPostExecute(Boolean result) {
	        if(result)
	            System.out.println("final de conexion");
	    }
	 protected void onCancelled() {
		 System.out.println("cancelada....");
	    }
	 
	 
	 /**
	  * envia la orden de regar o no regar
	  * @param reg
	  */
	 public void enviarRegar(boolean reg){
		 try{
			 output.writeUTF("regar");
			 if(reg){
				 output.writeUTF("si");
			 }else{
				 output.writeUTF("no");
			 }
		 }catch(IOException e){
			 
		 }
	 }
	
	 public int getLuz(){
		 return hiloMsg.getLuz();
	 }
	 public int getHum(){
		 return hiloMsg.getHum();
	 }
	 public int getTemp(){
		 return hiloMsg.getTemp();
	 }
	
}
