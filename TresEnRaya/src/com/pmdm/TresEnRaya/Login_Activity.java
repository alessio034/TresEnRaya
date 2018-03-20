package com.pmdm.TresEnRaya;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class Login_Activity extends Activity {

    private TextView BtnRegistrar;
    private Button BtnLogin;
    private EditText usuario;
    private EditText password;
    AdaptadorBD db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        BtnLogin = (Button)findViewById(R.id.BtnLogin);

        usuario = (EditText)findViewById(R.id.usuarioText);
        password = (EditText)findViewById(R.id.passwordText);

        BtnRegistrar = (TextView)findViewById(R.id.BtnRegistrar);


        db = new AdaptadorBD(this);
        db.open();


        BtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(usuario.getText().length() >0 && password.getText().length()>0){
                    if (db.Loguear(usuario.getText().toString(),password.getText().toString())){
                        Intent in = new Intent(Login_Activity.this,TresEnRayaActivity.class);
                        in.putExtra("IDUSUARIO",db.ObtenerIdUsuario(usuario.getText().toString()));
                        startActivity(in);
                    } else {
                        Toast.makeText(Login_Activity.this,"Usuario o contraseña incorrecto", Toast.LENGTH_LONG).show();
                    }
                } else{
                    Toast.makeText(Login_Activity.this, "Rellene los campos correctamente.", Toast.LENGTH_LONG).show();
                }
            }
        });

        BtnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login_Activity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }
}
