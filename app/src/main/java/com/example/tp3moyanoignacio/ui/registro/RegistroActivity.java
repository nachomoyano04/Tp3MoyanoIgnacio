package com.example.tp3moyanoignacio.ui.registro;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.tp3moyanoignacio.R;
import com.example.tp3moyanoignacio.databinding.ActivityRegistroBinding;
import com.example.tp3moyanoignacio.model.Usuario;

public class RegistroActivity extends AppCompatActivity {

    private RegistroActivityViewModel mv;
    private ActivityRegistroBinding binding;
    private Intent intent;
    private ActivityResultLauncher<Intent> arl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mv = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(RegistroActivityViewModel.class);
        binding = ActivityRegistroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        abrirGaleria();
        mv.getMUsuario().observe(this, new Observer<Usuario>() {
            @Override
            public void onChanged(Usuario usuario) {
                binding.etDni.setText(usuario.getDNI());
                binding.etNombre.setText(usuario.getNombre());
                binding.etApellido.setText(usuario.getApellido());
                binding.etCorreo.setText(usuario.getCorreo());
                binding.etPassword.setText(usuario.getPassword());
                String avatarString = usuario.getAvatar();
                mv.setAvatar(avatarString);
                binding.ivAvatar.setImageURI(Uri.parse(avatarString));
            }
        });
        Intent i = getIntent();
        boolean booleano = i.getBooleanExtra("login", false);
        binding.btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dni = binding.etDni.getText().toString();
                String nombre = binding.etNombre.getText().toString();
                String apellido = binding.etApellido.getText().toString();
                String correo = binding.etCorreo.getText().toString();
                String password = binding.etPassword.getText().toString();
                Usuario u = new Usuario(dni, nombre, apellido, correo, password);
                mv.guardar(u);
            }
        });
        mv.getMAvatar().observe(this, new Observer<Uri>() {
            @Override
            public void onChanged(Uri uri) {
                binding.ivAvatar.setImageURI(uri);
            }
        });
        mv.leerDatos(booleano);

        //listener boton subir foto
        binding.btnSubirFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arl.launch(intent);
            }
        });
    }
    private void abrirGaleria() {
        intent = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        arl = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                mv.recibirFoto(result);
            }
        });
    }
}