package com.pmdm.TresEnRaya;

import java.util.Random;

public class TresRaya {

	private char mBoard[] = {'1','2','3','4','5','6','7','8','9'};
	public static final int BOARD_SIZE = 9;
	
	public static final char JUGADOR = 'X';
	public static final char ANDROID = 'O';
	public static final char LIBRE = ' ';

    AdaptadorBD db;
    RegisterActivity rg;
    TresEnRayaActivity tresRaya;
	
	private Random mRand; 
	boolean finalizado=false;
	private enum NivelDificultad {Facil,Medio,Experto};
	private NivelDificultad nivelD=NivelDificultad.Facil;
	
	public TresRaya() {
		
		// Seed the random number generator
		mRand = new Random(); 
		limpiaTablero();

	}

    public int ObtenerDificultad(){

        int num = 0;
        if (nivelD == NivelDificultad.Facil)
            num = 1;
        else if (nivelD == NivelDificultad.Medio)
            num = 2;
        else if (nivelD == NivelDificultad.Experto)
            num = 3;
        return num;
    }

	public char [] getTablero(){
		return mBoard;
	}

	public void getTablero(char [] Tablero){
		mBoard=Tablero;
	}
/*******************
 *  	
 * @return 0 si no ocurre nada (ni ganador ni empate)
 * 		   1 es un empate
 * 		   2 Gana X
 * 		   3 Gana O 
 */
	public int buscarGanador() {

		// Buscamos horizontales
		for (int i = 0; i <= 6; i += 3)	{
			if (mBoard[i] == JUGADOR && 
				mBoard[i+1] == JUGADOR &&
				mBoard[i+2]== JUGADOR)
				return 2;
			if (mBoard[i] == ANDROID && 
				mBoard[i+1]== ANDROID && 
				mBoard[i+2] == ANDROID)
				return 3;
		}
	
		// Buscamos Verticales
		for (int i = 0; i <= 2; i++) {
			if (mBoard[i] == JUGADOR && 
				mBoard[i+3] == JUGADOR && 
				mBoard[i+6]== JUGADOR)
				return 2;
			if (mBoard[i] == ANDROID && 
				mBoard[i+3] == ANDROID && 
				mBoard[i+6]== ANDROID)
				return 3;
		}
	
		// Buscamos diagonales
		if ((mBoard[0] == JUGADOR &&
			 mBoard[4] == JUGADOR && 
			 mBoard[8] == JUGADOR) ||
			(mBoard[2] == JUGADOR && 
			 mBoard[4] == JUGADOR &&
			 mBoard[6] == JUGADOR))
			return 2;
		if ((mBoard[0] == ANDROID &&
			 mBoard[4] == ANDROID && 
			 mBoard[8] == ANDROID) ||
			(mBoard[2] == ANDROID && 
			 mBoard[4] == ANDROID &&
			 mBoard[6] == ANDROID))
			return 3;
	
		// Buscamos casillas libres
		for (int i = 0; i < BOARD_SIZE; i++) {
			if (mBoard[i] != JUGADOR && mBoard[i] != ANDROID)
				return 0;
		}
	
		// Si hemos llegado aqu� es que no hay casillas libre, es empate.
		return 1;
	}
/***************
 * 	Devuelve movimiento Aleatorio
 * @return
 */
	protected int getMovimientoAleatorio(){
	 int move=-1;
	 
		do
		{
			move = mRand.nextInt(BOARD_SIZE);
		} while (mBoard[move] == JUGADOR || mBoard[move] == ANDROID);
		
		return move;
	}
/****************
 * 	Busca Movimiento Ganador
 * @return
 */
	protected int getMovimientoGanador(){
		 int move=-1;
		 
			for (int i = 0; i < BOARD_SIZE; i++) {
				if (mBoard[i] != JUGADOR && mBoard[i] != ANDROID) {
					char curr = mBoard[i];
					mBoard[i] = ANDROID;
			
					if (buscarGanador() == 3) {
						System.out.println("Computer is moving to " + (i + 1));
						return i;
					}
					else mBoard[i] = curr;
				}
			}
			
			return move;
		}
/**************
 * Busca un Movimiento que Bloque al adversario para que no gane	
 * @return
 */
	
	protected int getMovimientoBloqueo (){
		int move=-1;
		
		for (int i = 0; i < BOARD_SIZE; i++) {
			if (mBoard[i] != JUGADOR && mBoard[i] != ANDROID) {
				char curr = mBoard[i];   
				mBoard[i] = JUGADOR;
				if (buscarGanador() == 2) {
					mBoard[i] = ANDROID;
					System.out.println("Computer is moving to " + (i + 1));
					return i;
				}
				else
					mBoard[i] = curr;
			}
		}
		return move;
	}
	
/************	
 * Hace uso de los tipos de movimientos dependiendo del nivel de dificultad
 * @return
 */
	
	public int getMovimientoAndroid() 
	{
		int movimiento=-1;

		switch (nivelD){
			case Facil: movimiento=getMovimientoAleatorio();	 break;
		
			case Medio: movimiento=this.getMovimientoGanador(); 
						if (movimiento==-1) movimiento=getMovimientoAleatorio();
					break;
					
			case Experto: 
				
				movimiento=this.getMovimientoGanador();
				if (movimiento==-1) movimiento=getMovimientoBloqueo();
				if (movimiento==-1) movimiento=getMovimientoAleatorio();
				break;
		}
		
		mBoard[movimiento] = ANDROID;
		return movimiento;
	}	
	
	
/**********
 * 
 */
	public void limpiaTablero(){
		   for (int i=0;i<BOARD_SIZE;i++){
			   mBoard[i]= LIBRE;
		   }
		   finalizado=false;
	}

/***************
 * 	Establece el movimiento del jugador en la representaci�n del  tablero 
 * @param jugador
 * @param posicion
 */
	public void setMovimiento(char jugador, int posicion){
			 mBoard[posicion] = jugador;
	}
	
/**********
 * 	
 */
public void setNivelFacil(){
	nivelD=NivelDificultad.Facil;
}

/**********
 * 	
 */
public void setNivelMedio(){
	nivelD=NivelDificultad.Medio;
}

/**********
 * 	
 */
public void setNivelExperto(){
	nivelD=NivelDificultad.Experto;
}
/*********
 * 	
 * @return
 */
	public boolean esJuegoFinalizado(){
		return finalizado;
	}
/*********	
 * 
 */
	public void JuegoFinalizado(){

        finalizado=true;
	}
	
	public void nuevoJuego(){
		limpiaTablero();
		finalizado=false;
	}
}