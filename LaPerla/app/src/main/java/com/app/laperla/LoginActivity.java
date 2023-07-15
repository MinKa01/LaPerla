package com.app.laperla;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.laperla.admin.MainAdminActivity;
import com.app.laperla.cliente.MainClienteActivity;

public class LoginActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private EditText etCorreo;
    private EditText etClave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        // Crear instancia de DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Obtener referencias a las vistas
        etCorreo = findViewById(R.id.etCorreo);
        etClave = findViewById(R.id.etClave);

        // Agregar eventos
        setupEventListeners();
    }

    private void setupEventListeners() {
        // Obtener referencias a las vistas
        Button btnLogin = findViewById(R.id.btnLogin);
        TextView tvCrearCuenta = findViewById(R.id.tvCrearCuenta);

        btnLogin.setOnClickListener(v -> {
            String correo = etCorreo.getText().toString();
            String clave = etClave.getText().toString();

            // Validar algún campo está vacío
            if (correo.isEmpty() || clave.isEmpty()) {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validar el correo electrónico
            if (!validarCorreo(correo)) {
                Toast.makeText(this, "El correo electrónico no es válido", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validar la contraseña
            if (!validarContrasena(clave)) {
                Toast.makeText(this, "La contraseña no es válida", Toast.LENGTH_SHORT).show();
                return;
            }

            iniciarSesion(correo, clave);
        });

        tvCrearCuenta.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });


    }

    private void iniciarSesion(String correo, String clave) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT idUsuario, rolUsu FROM usuario WHERE correoUsu = ? AND claveUsu = ?";
        String[] selectionArgs = {correo, clave};
        Cursor cursor = db.rawQuery(sql, selectionArgs);

        if (cursor.moveToFirst()) {
            // Las credenciales son correctas
            int idIndex = cursor.getColumnIndex("idUsuario");
            int rolIndex = cursor.getColumnIndex("rolUsu");

            int id = cursor.getInt(idIndex);
            String rol = cursor.getString(rolIndex);

            // Guardar el ID del usuario en SharedPreferences
            SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("usuario_id", id);
            editor.apply();

            Intent intent;
            if (rol.equals("admin")) {
                // Derivar a MainAdminActivity
                intent = new Intent(this, MainAdminActivity.class);
            } else {
                // Derivar a MainClienteActivity
                intent = new Intent(this, MainClienteActivity.class);
            }
            startActivity(intent);

            // Finalizar la actividad actual
            finish();

        } else {
            // Las credenciales son incorrectas
            Toast.makeText(this, "Usuario no existe", Toast.LENGTH_SHORT).show();
        }

        cursor.close();
    }


    private boolean validarCorreo(String correo) {
        // verifica que tenga al menos 6 caracteres antes del símbolo “@” y que el dominio sea “gmail.com” o “hotmail.com”.
        if (correo == null || correo.isEmpty()) {
            return false;
        }

        String regex = "^[\\w-]{6,}@(gmail|hotmail)\\.com$";
        return correo.matches(regex);
    }

    private boolean validarContrasena(String clave) {
        // verifica que tenga al menos 8 caracteres y que contenga al menos una letra mayúscula, una letra minúscula y un número.
        if (clave == null || clave.isEmpty()) {
            return false;
        }

        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$";
        return clave.matches(regex);
    }

}