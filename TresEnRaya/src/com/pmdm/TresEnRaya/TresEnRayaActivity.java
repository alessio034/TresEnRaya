package com.pmdm.TresEnRaya;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

public class TresEnRayaActivity extends Activity {
	private Button mBotonesTablero[];
	TresRaya mGame;
	TextView mensajeJuego;
	TextView MarcadorAndroid, MarcadorUsuario;
	TextView nivel;

	AdaptadorBD db;
    RegisterActivity rG;

	SoundPool soundPool;
	int iDRisas, iDVictoria, iDEmpate; 
	
	
	static final int DIALOG_DIFICULTAD_ID = 0; 
	static final int DIALOG_SALIR_ID = 1;
	static final int DIALOG_ABOUT_ID = 2;
    static final int DIALOG_PUNTUACION=3;

    static String Usuario;
    static int Id_Usuario;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //quita la barra con el titulo
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //no deja que se vea la barra del reloj y demas
        Log.d("TresEnRaya","Metodo onCreate");
        
        setContentView(R.layout.main);
        Bundle bundle = getIntent().getExtras();
        Id_Usuario = bundle.getInt("IDUSUARIO");

        db = new AdaptadorBD(this);
        db.open();
        Usuario = db.ObtenerNombreUsuario(Id_Usuario);
        TextView user = (TextView)findViewById(R.id.Usuario);

        user.setText(Usuario);

        
        mBotonesTablero = new Button[TresRaya.BOARD_SIZE];
        
        mBotonesTablero[0] = (Button) findViewById(R.id.button1);
        mBotonesTablero[1] = (Button) findViewById(R.id.button2);
        mBotonesTablero[2] = (Button) findViewById(R.id.button3);
        mBotonesTablero[3] = (Button) findViewById(R.id.button4);
        mBotonesTablero[4] = (Button) findViewById(R.id.button5);
        mBotonesTablero[5] = (Button) findViewById(R.id.button6);
        mBotonesTablero[6] = (Button) findViewById(R.id.button7);
        mBotonesTablero[7] = (Button) findViewById(R.id.button8);
        mBotonesTablero[8] = (Button) findViewById(R.id.button9);

        mensajeJuego = (TextView) findViewById(R.id.MensajeJuego);
        MarcadorAndroid = (TextView) findViewById(R.id.MarcadorAndroid);
        MarcadorUsuario = (TextView) findViewById(R.id.MarcadorUsuario);
        nivel = (TextView) findViewById(R.id.TextModo);
        
        
        setVolumeControlStream(AudioManager.STREAM_MUSIC); 
        soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
        
        AssetManager assetManager = getAssets();
        try {
			AssetFileDescriptor risas = assetManager.openFd("Laugh.ogg");
			AssetFileDescriptor victoria = assetManager.openFd("Victory.ogg");
			AssetFileDescriptor empate = assetManager.openFd("Oops.ogg");
			
			 iDRisas = soundPool.load(risas, 1);
			 iDVictoria = soundPool.load(victoria, 1);
			 iDEmpate = soundPool.load(empate, 1);
			 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        mGame = new TresRaya();

        	  nivel.setText(R.string.nivelFacil); //Empiezo con f�cil
              comenzarNuevoJuego();
      
    }
    
  
 
/******************
 *     
 */
    private void comenzarNuevoJuego() {
    	for (int i = 0; i < mBotonesTablero.length; i++) { 
    		mBotonesTablero[i].setText(""); 
    		mBotonesTablero[i].setEnabled(true); 
    		mBotonesTablero[i].setOnClickListener(new ButtonClickListener(i));
    	}
    	mensajeJuego.setText("");	
    	mGame.nuevoJuego();
    	
    }
 /********************
  * 
  * @param Marcador
  */
  private void victoriaMarcador(TextView Marcador){
	 String marcador= Marcador.getText().toString();
	 int marcadorInt=Integer.parseInt(marcador)+1;
	 
	 Marcador.setText(marcadorInt+"");
  }
  
  
  /****************
   *   
   * @param jugador
   * @param posicion
   */
    private void setMovimiento(char jugador, int posicion) {
    		mGame.setMovimiento(jugador, posicion); 
    		mBotonesTablero[posicion].setEnabled(false); 
    		mBotonesTablero[posicion].setText(String.valueOf(jugador)); 
    		
    		if (jugador == TresRaya.JUGADOR)
    			mBotonesTablero[posicion].setTextColor(Color.rgb(0, 200, 0));
    		else
    	    	mBotonesTablero[posicion].setTextColor(Color.rgb(200, 0, 0));
    	
    }
    
    
    public boolean onCreateOptionsMenu(Menu menu) { 
    	super.onCreateOptionsMenu(menu);
    	MenuInflater inflater = getMenuInflater(); 
    	inflater.inflate(R.layout.menu, menu); 
    	return true;
    }
    
    
    public boolean onOptionsItemSelected(MenuItem item) { 
    	
    	switch (item.getItemId()) { 
    	case R.id.nuevo_juego:
    		comenzarNuevoJuego(); 
    		return true;
    	case R.id.ai_dificultad:
    	    showDialog(DIALOG_DIFICULTAD_ID); 
    	    return true;
        case R.id.Puntuaciones:
            Intent in = new Intent(TresEnRayaActivity.this,Resultados.class);
            startActivity(in);
            return true;
    	case R.id.salir: 
    		showDialog(DIALOG_SALIR_ID);
    	    return true; 
    	case R.id.about: 
    		showDialog(DIALOG_ABOUT_ID);
    	    return true;

    	}
    
    	return false;
    }
    
    protected Dialog onCreateDialog(int id) { 
    	Dialog dialog = null;
    
    	AlertDialog.Builder builder = new AlertDialog.Builder(this); 
    	switch(id) {
    		case DIALOG_DIFICULTAD_ID:
    				builder.setTitle(R.string.dificultad_menu);
    					final CharSequence[] niveles = {
    							getResources().getString(R.string.dificultad_facil), 
    							getResources().getString(R.string.dificultad_medio), 
    							getResources().getString(R.string.dificultad_experto)};
    					
		    		int seleccionado=0;
		    		
		    		builder.setSingleChoiceItems(niveles, seleccionado, 
		    				new DialogInterface.OnClickListener() {
		    				public void onClick(DialogInterface dialog, int item) { 
		    						dialog.dismiss();	// Cierra dialogo y establece el juego
		    				 switch (item){
		    				 	case 0: mGame.setNivelFacil(); 
		    				 			nivel.setText(R.string.nivelFacil);break;
		    				 	case 1: mGame.setNivelMedio(); 
		    				 			nivel.setText(R.string.nivelMedio);break;
		    				 	case 2: mGame.setNivelExperto(); 
		    				 			nivel.setText(R.string.nivelExperto);break;
		    				 }
		    				}
		    		});
		    		dialog = builder.create();
    		break;
    		
    		case DIALOG_SALIR_ID: 
    			builder.setMessage(R.string.abandonarPregunta) 
    				.setCancelable(false)
    				.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() { 
    					public void onClick(DialogInterface dialog, int id) {
    							TresEnRayaActivity.this.finish();//Finaliza la  Actividad
    						}
    					})
    					.setNegativeButton(R.string.no, null);
    			dialog = builder.create();
    			break;
    			
    		case DIALOG_ABOUT_ID:
    			Context context = getApplicationContext(); 
    			LayoutInflater inflater = (LayoutInflater) 
    					context.getSystemService(LAYOUT_INFLATER_SERVICE); 
    			View layout = inflater.inflate(R.layout.about, null); 
    			builder.setView(layout); 
    			builder.setPositiveButton("OK", null); 
    			dialog = builder.create();
    			break;

            case DIALOG_PUNTUACION:

                break;

    	}

    	
    	return dialog;
    }
 /*****************
  *    
  */
    protected void onStart (){
    	super.onStart();
    	Log.d("TresEnRaya","M�todo onStart");
    }
 /*****************
  * 
  */
       protected void onStop (){
    	super.onStop();
      	Log.d("TresEnRaya","M�todo onStop");
    }
/*****************
 * 
 */
    protected void onPause (){
    	super.onPause();
     	Log.d("TresEnRaya","M�todo onPause");
    }

    /*****************
     *     
     */
    protected void onResume (){
    	super.onResume();
     	Log.d("TresEnRaya","M�todo onResume");
    }

    /*****************
     *     
     */
    protected void onRestart (){
    	super.onRestart();
    	Log.d("TresEnRaya","M�todo onRestart");
    }

    /*****************
     *     
     */
    protected void onDestroy (){
    	super.onDestroy();
    	Log.d("TresEnRaya","M�todo onDestroy");
    }
/*******************
 *     
 * @author monte
 *
 */
    private class ButtonClickListener implements View.OnClickListener { 
    	
    	int location;
    	public ButtonClickListener(int location) { 
    		this.location = location;
    	}
    	
    	public void onClick(View view) { 
    	
    		if ((mBotonesTablero[location].isEnabled()) & !mGame.esJuegoFinalizado() ) {
    			setMovimiento(TresRaya.JUGADOR, location);
    		
    			int ganador = mGame.buscarGanador(); 
    			if (ganador == 0) {
    				mensajeJuego.setText("Turno de Android."); 
    				int movimiento = mGame.getMovimientoAndroid(); 
    				setMovimiento(TresRaya.ANDROID, movimiento); 
    				ganador = mGame.buscarGanador();
    			}
    		
    			if (ganador!=0) mGame.JuegoFinalizado();
    				
    			switch (ganador){
    				case 0:  mensajeJuego.setText(R.string.mensajeTurno);break;
    				
    				case 1:  mensajeJuego.setText(R.string.mensajeEmpate); 
    							soundPool.play(iDEmpate, 1, 1, 0, 0, 1);
    							break;
    				case 2:  mensajeJuego.setText(R.string.mensajeGanaUsuario); 
    							victoriaMarcador (MarcadorUsuario);
    							soundPool.play(iDVictoria, 1, 1, 0, 0, 1);
    							break;
    				case 3:  mensajeJuego.setText(R.string.mensajeGanaAndroid); 
    							victoriaMarcador (MarcadorAndroid);
    							soundPool.play(iDRisas, 1, 1, 0, 0, 1);
    							break;
    			}

                if(ganador!=0)
                    db.GuardaPuntuacion(Usuario,ganador,mGame.ObtenerDificultad());

    			
    		}
    }
 }
}