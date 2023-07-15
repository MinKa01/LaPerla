package com.app.laperla.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.laperla.DatabaseHelper;
import com.app.laperla.LoginActivity;
import com.app.laperla.R;
import com.app.laperla.cliente.EditProfileClienteFragment;

public class MainAdminActivity extends AppCompatActivity implements EditProfileClienteFragment.OnNombreActualizadoListener{

    private DatabaseHelper dbHelper;
    private TextView etNombre;

    private int selectedTab = 1;

    // Declarar las vistas como variables de instancia
    private LinearLayout opHome;
    private LinearLayout opUsers;
    private LinearLayout opFoods;
    private LinearLayout opSettings;
    private ImageView ivHome;
    private ImageView ivUsers;
    private ImageView ivFoods;
    private ImageView ivSettings;
    private TextView tvHome;
    private TextView tvUsers;
    private TextView tvFoods;
    private TextView tvSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);

        // Crear instancia de DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Obtener referencias a las vistas
        etNombre = findViewById(R.id.tvSaludoAdmin);


        // Inicializar las vistas
        opHome = findViewById(R.id.optionHome);
        opUsers = findViewById(R.id.optionUsers);
        opFoods = findViewById(R.id.optionFoods);
        opSettings = findViewById(R.id.optionSettings);
        ivHome = findViewById(R.id.ivOptionHome);
        ivUsers = findViewById(R.id.ivOptionUsers);
        ivFoods = findViewById(R.id.ivOptionFoods);
        ivSettings = findViewById(R.id.ivOptionSettings);
        tvHome = findViewById(R.id.tvOptionHome);
        tvUsers = findViewById(R.id.tvOptionUsers);
        tvFoods = findViewById(R.id.tvOptionFoods);
        tvSettings = findViewById(R.id.tvOptionSettings);

        // Cargar el fragmento por defecto
        selectTab(1);

        // Configurar los eventos de los botones
        setupEventListeners();

        // Recuperar el ID del usuario desde SharedPreferences
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        int usuarioId = prefs.getInt("usuario_id", -1);

        // Obtener el nombre del usuario desde la base de datos
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT nombreUsu FROM usuario WHERE idUsuario = ?";
        String[] selectionArgs = {String.valueOf(usuarioId)};
        Cursor cursor = db.rawQuery(sql, selectionArgs);

        if (cursor.moveToFirst()) {
            // El usuario existe
            int nombreIndex = cursor.getColumnIndex("nombreUsu");
            String nombre = cursor.getString(nombreIndex);
            etNombre.setText(getString(R.string.welcome_message, nombre));
        } else {
            // El usuario no existe
            Toast.makeText(this, "Error al obtener el nombre del usuario", Toast.LENGTH_SHORT).show();
        }
        cursor.close();


    }

    @Override
    public void onNombreActualizado(String nuevoNombre) {
        etNombre.setText(getString(R.string.welcome_message, nuevoNombre));
    }

    private void setupEventListeners() {

        opHome.setOnClickListener(v -> {
            if (selectedTab != 1) {
                selectTab(1);
                LinearLayout header = findViewById(R.id.header);
                header.setVisibility(View.VISIBLE);
            }
        });

        opUsers.setOnClickListener(v -> {
            if (selectedTab != 2) {
                selectTab(2);
                LinearLayout header = findViewById(R.id.header);
                header.setVisibility(View.GONE);
            }
        });

        opFoods.setOnClickListener(v -> {
            if (selectedTab != 3) {
                selectTab(3);
                LinearLayout header = findViewById(R.id.header);
                header.setVisibility(View.GONE);
            }
        });

        opSettings.setOnClickListener(v -> {
            if (selectedTab != 4) {
                selectTab(4);
                LinearLayout header = findViewById(R.id.header);
                header.setVisibility(View.GONE);
            }
        });

    }

    private void selectTab(int tab) {
        // Cambiar el fragmento
        switch (tab) {
            case 1:
                getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.fragmentContainer,HomeAdminFragment.class, null).commit();
                break;
            case 2:
                getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.fragmentContainer,UsersAdminFragment.class, null).commit();
                break;
            case 3:
                getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.fragmentContainer,FoodAdminFragment.class, null).commit();
                break;
            case 4:
                getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.fragmentContainer,SettingsAdminFragment.class, null).commit();
                break;
        }

        // Actualizar la interfaz de usuario
        ivHome.setImageResource(R.drawable.ic_home);
        ivUsers.setImageResource(R.drawable.ic_user);
        ivFoods.setImageResource(R.drawable.ic_food_menu);
        ivSettings.setImageResource(R.drawable.ic_setting);

        tvHome.setVisibility(View.GONE);
        tvUsers.setVisibility(View.GONE);
        tvFoods.setVisibility(View.GONE);
        tvSettings.setVisibility(View.GONE);

        opHome.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        opUsers.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        //opFoods.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        opFoods.setBackgroundColor(ContextCompat.getColor(MainAdminActivity.this, android.R.color.transparent));
        opSettings.setBackgroundColor(ContextCompat.getColor(MainAdminActivity.this, android.R.color.transparent));

        switch (tab) {
            case 1:
                ivHome.setImageResource(R.drawable.ic_home_selected);
                tvHome.setVisibility(View.VISIBLE);
                opHome.setBackgroundResource(R.drawable.round_back_option_100);

                // Animación
                ScaleAnimation scaleAnimation = new ScaleAnimation(0.9f, 1.0f, 1f, 1f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                scaleAnimation.setDuration(100);
                scaleAnimation.setFillAfter(true);
                opHome.startAnimation(scaleAnimation);

                break;
            case 2:
                ivUsers.setImageResource(R.drawable.ic_user_selected);
                tvUsers.setVisibility(View.VISIBLE);
                opUsers.setBackgroundResource(R.drawable.round_back_option_100);

                animateView(opUsers);
                break;
            case 3:
                ivFoods.setImageResource(R.drawable.ic_food_menu_selected);
                tvFoods.setVisibility(View.VISIBLE);
                opFoods.setBackgroundResource(R.drawable.round_back_option_100);
                animateView(opFoods);
                break;
            case 4:
                ivSettings.setImageResource(R.drawable.ic_setting_selected);
                tvSettings.setVisibility(View.VISIBLE);
                opSettings.setBackgroundResource(R.drawable.round_back_option_100);
                animateView(opSettings);
                break;
        }

        selectedTab = tab;
    }

    private void animateView(View view) {
        // Animación
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.9f, 1.0f, 1f, 1f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        scaleAnimation.setDuration(100);
        scaleAnimation.setFillAfter(true);
        view.startAnimation(scaleAnimation);
    }

    public void hideOptionsBottomBar() {
        LinearLayout optionsBottomBar = findViewById(R.id.optionsBottomBar);
        optionsBottomBar.setVisibility(View.GONE);
    }

    public void showOptionsBottomBar() {
        LinearLayout optionsBottomBar = findViewById(R.id.optionsBottomBar);
        optionsBottomBar.setVisibility(View.VISIBLE);
    }

    private void cerrarSesion() {
        // Eliminar el ID del usuario de SharedPreferences
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("usuario_id");
        editor.apply();

        // Derivar a la actividad de inicio de sesión
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

        // Finalizar la actividad actual
        finish();
    }

}