package com.pmdm.TresEnRaya;

import android.app.Activity;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MiAdaptador extends BaseAdapter{

    private final Activity actividad;
    private final Cursor Datos;

    // *******************************************************************

    // constructor del adaptador personalizado
    public MiAdaptador(Activity actividad, Cursor Datos) {

        super();
        this.actividad = actividad;
        this.Datos = Datos;
    }

    // M�todo para construir un nuevo objeto View con el Layout correspondiente
    // a la posici�n pasada en position y devolverlo.
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // objeto inflater para rellenar la View a partir de un recurso xml
        LayoutInflater inflater = actividad.getLayoutInflater();
        // rellena o infla la vista a partir del layout elemento_lista.xml
        // dentro de la misma View padre (null, true)
        View view = inflater.inflate(R.layout.elemento_lista, null, true);
        // referencia al primer TextView del Layout
        TextView textView = (TextView) view.findViewById(R.id.titulo);
        // referencia al segundo TextView del Layout
        TextView subT = (TextView) view.findViewById(R.id.subtitulo);
        // asigna texto
        Datos.moveToPosition(position);
        textView.setText(Datos.getString(0));
        subT.setText(Datos.getString(1));
        // ***************************************************************
        return view;
    }

    // Devuelve el n�mero de elementos de la lista.
    @Override
    public int getCount() {
        return Datos.getCount();
    }

    // Devuelve el elemento en una determinada posici�n de la lista
    @Override
    public Object getItem(int arg0) {
        return Datos.moveToPosition(arg0);
    }

    // Devuelve el identificador de fila de una determinada posici�n de la lista
    @Override
    public long getItemId(int position) {
        return position;
    }
}