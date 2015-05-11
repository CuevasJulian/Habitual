package com.ambientes.habitual;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import android.app.Activity;
import android.widget.TextView;
/**
 *  esta clase es un hilo independiente dedicado a recibir mensajes
 * @author Cuervo
 *
 */
public class hiloMensajes extends Thread {
	private Socket socket;
	private DataInputStream input;
	private DataOutputStream output;
	private int temperatura = 0;
	private int intLuz = 0;
	private int humedad = 0;
	private Comunicacion com;
	private Activity acti;
	
	
	private int luz;
	private int temp;
	private int hum;
	
	
	/**
	 * el constructor recibe socket y los canales para la comunicacion
	 * @param socket
	 * @param dai
	 * @param dao
	 */
	public hiloMensajes(Socket socket,DataInputStream dai, DataOutputStream dao){
		this.socket = socket;
		input = dai;
		output = dao;
		com = Comunicacion.getInstancia();
		start();
	}
	
	
	/**
	 * setea la actividad actual para poder usar sus metodos
	 * @param a
	 */
	public void setActivity(Activity a){
		acti = a;
	}
	
	
	/**
	 * mantiene viva la recepcion de mensajes
	 */
	public void run(){
		while(true){
			
			recibirMensajes();
			try{
				sleep(10);
			}catch(InterruptedException e){
				
			}
		}
	}
	
	
	/**
	 * analiza los mensajes entrantes
	 * @param msg
	 */
	public void analizarMSG(String msg){
		
		try{
			System.out.println("mensaje recibido");
			if(msg.equalsIgnoreCase("datos")){
			
				temperatura = input.readInt();
				intLuz = input.readInt();
				humedad = input.readInt();
				System.out.println("nueva temperatura: "+temperatura);
				System.out.println("nueva luz: "+intLuz);
				System.out.println("nueva humedad: "+humedad);
				com.actualizar();
			}
			if(msg.equalsIgnoreCase("va config")){
				
				luz = input.readInt();
				hum = input.readInt();
				temp = input.readInt();
				System.out.println("datos recibidos | luz: "+luz+" humedad: "+hum+" temp: "+temp);
			}
		}catch(IOException e){
		}
	}
	
	
	/**
	 * recibe los mensajes del servidor
	 */
	public void recibirMensajes(){
		try{
			System.out.println("esperando mensaje de servidor");
			String msg = input.readUTF();
			analizarMSG(msg);
		}catch(IOException e){
			
		}	
	}
	public int getTemperatura(){
		return temperatura;
	}
	public int getIntLuz(){
		return intLuz;
	}
	public int getHumedad(){
		return humedad;
	}
	public int getLuz(){
		return luz;
	}
	public int getHum(){
		return hum;
	}
	public int getTemp(){
		return temp;
	}
}
