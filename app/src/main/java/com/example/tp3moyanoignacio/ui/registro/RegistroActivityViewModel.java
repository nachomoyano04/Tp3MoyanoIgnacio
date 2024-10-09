package com.example.tp3moyanoignacio.ui.registro;

import static android.app.Activity.RESULT_OK;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tp3moyanoignacio.model.Usuario;
import com.example.tp3moyanoignacio.request.ApiClient;
import com.example.tp3moyanoignacio.ui.login.MainActivity;

public class RegistroActivityViewModel extends AndroidViewModel {
    private Context context;
    private MutableLiveData<Usuario> mUsuario;
    private MutableLiveData<Uri> mAvatar;
    private String avatar;

    public RegistroActivityViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public LiveData<Usuario> getMUsuario(){
        if(mUsuario == null){
            mUsuario = new MutableLiveData<>();
        }
        return mUsuario;
    }

    public LiveData<Uri> getMAvatar(){
        if(mAvatar == null){
            mAvatar = new MutableLiveData<>();
        }
        return mAvatar;
    }

    public void guardar(Usuario usuario){
        if(usuario.getNombre().isEmpty() || usuario.getApellido().isEmpty() || usuario.getDNI().isEmpty() || usuario.getCorreo().isEmpty() || usuario.getPassword().isEmpty()){
            Toast.makeText(context, "Debe completar todos los campos", Toast.LENGTH_SHORT).show();
        }else{
            usuario.setAvatar(avatar);
            if(ApiClient.Guardar(context, usuario)){
                Toast.makeText(context, "Guardado correctamente", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(context, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }else{
                Toast.makeText(context, "No se pudo registrar/editar al usuario", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void leerDatos(boolean booleano){
        if(booleano){
            mUsuario.setValue(ApiClient.leer(context));
        }
    }

    public void recibirFoto(ActivityResult result){
        if(result.getResultCode() == RESULT_OK){
            Intent data = result.getData();
            Uri uri = data.getData();
            avatar = uri.toString();
            mAvatar.setValue(uri);
        }
    }
}