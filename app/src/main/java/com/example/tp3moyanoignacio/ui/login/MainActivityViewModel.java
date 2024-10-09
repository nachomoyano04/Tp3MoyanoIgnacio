package com.example.tp3moyanoignacio.ui.login;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.tp3moyanoignacio.model.Usuario;
import com.example.tp3moyanoignacio.request.ApiClient;
import com.example.tp3moyanoignacio.ui.registro.RegistroActivity;

public class MainActivityViewModel extends AndroidViewModel {
    private Context context;
    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public void login(String correo, String password){
        if(correo.isEmpty() || password.isEmpty()){
            Toast.makeText(context, "Debe completar todos los campos", Toast.LENGTH_SHORT).show();
        }else{
            Usuario usuario = ApiClient.login(context, correo, password);
            if(usuario == null){
                Toast.makeText(context, "Correo y/o contraseña incorrectos", Toast.LENGTH_SHORT).show();
            }else{
                Intent i = new Intent(context, RegistroActivity.class);
                i.putExtra("login", true);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        }
    }

    public void registrar(){
        Intent i = new Intent(context, RegistroActivity.class);
        i.putExtra("login", false);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
