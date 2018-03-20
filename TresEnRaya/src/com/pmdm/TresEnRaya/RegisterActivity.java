package com.pmdm.TresEnRaya;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class RegisterActivity extends Activity {
    AdaptadorBD db;
    EditText usuario;
    EditText pass1, pass2;
    Button btnReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        usuario = (EditText)findViewById(R.id.txtUsuario);
        pass1 = (EditText)findViewById(R.id.txtContra1);
        pass2 =(EditText)findViewById(R.id.txtContra2);
        btnReg =(Button)findViewById(R.id.btnRegistrar);
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = new AdaptadorBD(RegisterActivity.this);
                db.open();
                if (usuario.getText().length() > 0 && pass1.getText().length() > 0 && pass2.getText().length() > 0 ){
                    if (pass1.getText().toString().equals(pass2.getText().toString())){
                        db.RegistrarUsuarios(usuario.getText().toString(),pass1.getText().toString());
                        Toast.makeText(RegisterActivity.this,"Registrado correctamente",Toast.LENGTH_LONG).show();
                        finish();
                    }else{
                        Toast.makeText(RegisterActivity.this,"Las contraseñas deben coincidir",Toast.LENGTH_LONG).show();
                    }

                }else{
                    Toast.makeText(RegisterActivity.this,"Debes rellenar los campos",Toast.LENGTH_LONG).show();
                }

            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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
