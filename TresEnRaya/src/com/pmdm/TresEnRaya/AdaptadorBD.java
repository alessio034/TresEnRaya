package com.pmdm.TresEnRaya;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AdaptadorBD {

	public static final String KEY_ROWID_USUARIO = "_id";
	public static final String KEY_NOMBRE_USUARIO = "nombre";
	public static final String KEY_CLAVE_USUARIO = "clave";
	public static final String KEY_ROWID_PUNTUACION = "_id";
	public static final String KEY_USUARIO_PUNTUACION = "usuario";
    public static final String KEY_PUNTUACION_PUNTUACION = "puntuacion";
    public static final String KEY_DIFICULTAD_PUNTUACION = "dificultad";
	
	private static final String TAG = "AdaptadorBD";
	
	private static final String DATABASE_NAME = "dbtresraya";
	private static final String DATABASE_TABLE_USUARIO = "usuarios";
    private static final String DATABASE_TABLE_PUNTUACION = "puntuaciones";
	private static final int DATABASE_VERSION = 1;
	
	private static final String DATABASE_CREATE_PUNTUACION =
	"create table "+DATABASE_TABLE_PUNTUACION+
	"("+KEY_ROWID_PUNTUACION+" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
	+KEY_USUARIO_PUNTUACION+" text not null, "
	+KEY_PUNTUACION_PUNTUACION+" INTEGER not null, "
	+KEY_DIFICULTAD_PUNTUACION+" text not null);";

    private static final String DATABASE_CREATE_USUARIOS =
            "create table "+DATABASE_TABLE_USUARIO+
            "("+KEY_ROWID_USUARIO+" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
            +KEY_NOMBRE_USUARIO+" text not null, "
            +KEY_CLAVE_USUARIO+" text not null);";
	
	
	private final Context context;	
	private BaseDatosHelper BDHelper;
	private SQLiteDatabase bsSql;
	private String[] todasColumnas_USUARIOS =new String[] {KEY_ROWID_USUARIO,KEY_NOMBRE_USUARIO,KEY_CLAVE_USUARIO};
    private String[] todasColumnas_PUNTUACIONES =new String[] {KEY_ROWID_PUNTUACION,KEY_USUARIO_PUNTUACION,KEY_PUNTUACION_PUNTUACION,KEY_DIFICULTAD_PUNTUACION};

	
	//---constructor--- 
	public AdaptadorBD(Context ctx) {
		this.context = ctx;
		BDHelper = new BaseDatosHelper(context);
	}
	
	//--- abre una conexi�n a la BD para lectura/escritura
	public AdaptadorBD open() throws SQLException{
		bsSql = BDHelper.getWritableDatabase();
		return this;
	}
		
	//---cierra la base de datos---
	public void close(){
		BDHelper.close();
	}

    public Boolean Loguear(String usu, String pass){

        Boolean resultado = false;

        Cursor mCursor = bsSql.query(true, DATABASE_TABLE_USUARIO, todasColumnas_USUARIOS, KEY_NOMBRE_USUARIO +"='"+ usu+"' AND "+KEY_CLAVE_USUARIO+" = '"+pass+"'",null,null,null,null,null);

        if(mCursor.getCount()==1){
            resultado = true;
        }

        return resultado;
    }

    public int ObtenerIdUsuario(String usuario){
        Cursor mCursor = bsSql.rawQuery("Select _id From usuarios WHERE nombre LIKE '"+usuario+"'",null);

        mCursor.moveToFirst();
        return mCursor.getInt(0);
    }

    public String ObtenerNombreUsuario(int usuario){
        Cursor mCursor = bsSql.rawQuery("Select nombre From usuarios WHERE _id = '"+usuario+"'",null);

        mCursor.moveToFirst();
        return mCursor.getString(0);
    }

    public Cursor ObtenerPuntuacion(int dificultad){
        return bsSql.rawQuery("Select usuarios.nombre, (SELECT COUNT(1) from puntuaciones WHERE usuario = usuarios.nombre AND puntuacion = 2 AND dificultad LIKE '"+dificultad+"') FROM usuarios ORDER BY 2 DESC",null);
    }


    public void RegistrarUsuarios(String usu, String pass){
        ContentValues valores = new ContentValues();

        valores.put(KEY_NOMBRE_USUARIO,usu);
        valores.put(KEY_CLAVE_USUARIO,pass);
        bsSql.insert(DATABASE_TABLE_USUARIO,null,valores);
    }

    //Metodo para guardar la puntuacion en la base de datos
    public void GuardaPuntuacion(String usuario, int puntuacion, int nivel ){
        ContentValues values = new ContentValues();
        values.put(KEY_USUARIO_PUNTUACION, usuario);
        values.put(KEY_PUNTUACION_PUNTUACION,puntuacion);
        values.put(KEY_DIFICULTAD_PUNTUACION,nivel);
        bsSql.insert(DATABASE_TABLE_PUNTUACION,null,values);
    }


    public void InsertarDemo(){
        ContentValues valores = new ContentValues();

        valores.put(KEY_NOMBRE_USUARIO,"Demo");
        valores.put(KEY_CLAVE_USUARIO,"demo");
        bsSql.insert(DATABASE_TABLE_USUARIO,null,valores);
    }

		
//**** CLASE PRIVADA subclase SQLiteOpenHelper***/	
	
	//clase para crear la base de datos SQLite 
	private static class BaseDatosHelper extends SQLiteOpenHelper{
		BaseDatosHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		@Override
		public void onCreate(SQLiteDatabase db)	{
			try{
			//ejecuta la sentencia SQL de creaci�n de la BD	
			db.execSQL(DATABASE_CREATE_PUNTUACION);
            db.execSQL(DATABASE_CREATE_USUARIOS);

                ContentValues valores = new ContentValues();

                valores.put(KEY_NOMBRE_USUARIO,"Demo");
                valores.put(KEY_CLAVE_USUARIO,"demo");
                db.insert(DATABASE_TABLE_USUARIO,null,valores);


			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion,int newVersion){
				Log.w(TAG, "Actualizando base de datos de la versi�n " + oldVersion
				+ " a "
				+ newVersion + ", borraremos todos los datos");
				//elimina tabla de la BD
				db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_PUNTUACION);
                db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_USUARIO);
				//crea la nueva BD
				onCreate(db);
		}
	}

	public void BorrarBD() {
		// TODO Auto-generated method stub
		context.deleteDatabase(DATABASE_NAME);
	}

	
}
