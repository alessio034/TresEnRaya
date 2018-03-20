package com.pmdm.TresEnRaya;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class Resultados extends ListActivity {


    AdaptadorBD db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultados);

        db = new AdaptadorBD(this);
        db.open();

        Dialog dialog = null;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

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
                            case 0:
                                setListAdapter(new MiAdaptador(Resultados.this,db.ObtenerPuntuacion(1)));break;
                            case 1:
                                setListAdapter(new MiAdaptador(Resultados.this,db.ObtenerPuntuacion(2)));break;
                            case 2:
                                setListAdapter(new MiAdaptador(Resultados.this,db.ObtenerPuntuacion(3)));break;
                        }
                    }
                });
        dialog = builder.create();
        dialog.show();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_resultados, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
