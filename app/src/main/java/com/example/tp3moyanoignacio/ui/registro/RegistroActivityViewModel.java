package com.example.tp3moyanoignacio.ui.registro;

import static android.app.Activity.RESULT_OK;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
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
            Usuario u = ApiClient.leer(context);
            avatar = u.getAvatar();
            mUsuario.setValue(u);

        }
    }

    public void recibirFoto(ActivityResult result){
        if(result.getResultCode() == RESULT_OK){
            Intent data = result.getData();
            Uri uri = data.getData();
            avatar = uri.toString();
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
                context.getContentResolver().takePersistableUriPermission (uri, Intent.FLAG_GRANT_READ_URI_PERMISSION|Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
            mAvatar.setValue(uri);
        }
    }

    public void setAvatar(String avatar){
        this.avatar = avatar;
    }
}