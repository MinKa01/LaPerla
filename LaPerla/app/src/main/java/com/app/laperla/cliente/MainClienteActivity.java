package com.app.laperla.cliente;

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
import com.app.laperla.admin.FoodAdminFragment;
import com.app.laperla.admin.HomeAdminFragment;
import com.app.laperla.admin.MainAdminActivity;
import com.app.laperla.admin.SettingsAdminFragment;
import com.app.laperla.admin.UsersAdminFragment;

public class MainClienteActivity extends AppCompatActivity implements EditProfileClienteFragment.OnNombreActualizadoListener{

    private DatabaseHelper dbHelper;
    private TextView etNombre;

    private int selectedTab = 1;

    // Declarar las vistas como variables de instancia
    private LinearLayout opHome;
    private LinearLayout opOrders;
    private LinearLayout opSettings;
    private ImageView ivHome;
    private ImageView ivOrders;
    private ImageView ivSettings;
    private TextView tvHome;
    private TextView tvOrders;
    private TextView tvSettings;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_cliente);

        // Crear instancia de DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Obtener referencias a las vistas
        etNombre = findViewById(R.id.tvSaludoCliente);

        // Inicializar las vistas
        opHome = findViewById(R.id.optionHome);
        opOrders = findViewById(R.id.optionOrders);
        opSettings = findViewById(R.id.optionSettings);
        ivHome = findViewById(R.id.ivOptionHome);
        ivOrders = findViewById(R.id.ivOptionOrders);
        ivSettings = findViewById(R.id.ivOptionSettings);
        tvHome = findViewById(R.id.tvOptionHome);
        tvOrders = findViewById(R.id.tvOptionOrders);
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

        opOrders.setOnClickListener(v -> {
            if (selectedTab != 2) {
                selectTab(2);
                LinearLayout header = findViewById(R.id.header);
                header.setVisibility(View.GONE);
            }
        });

        opSettings.setOnClickListener(v -> {
            if (selectedTab != 3) {
                selectTab(3);
                LinearLayout header = findViewById(R.id.header);
                header.setVisibility(View.GONE);
            }
        });

    }

    private void selectTab(int tab) {
        // Cambiar el fragmento
        switch (tab) {
            case 1:
                getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.fragmentContainerCliente, HomeClienteFragment.class, null).commit();
                break;
            case 2:
                getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.fragmentContainerCliente, OrdersClienteFragment.class, null).commit();
                break;
            case 3:
                getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.fragmentContainerCliente, SettingsClienteFragment.class, null).commit();
                break;
        }

        // Actualizar la interfaz de usuario
        ivHome.setImageResource(R.drawable.ic_home);
        ivOrders.setImageResource(R.drawable.ic_orders);
        ivSettings.setImageResource(R.drawable.ic_setting);

        tvHome.setVisibility(View.GONE);
        tvOrders.setVisibility(View.GONE);
        tvSettings.setVisibility(View.GONE);

        opHome.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        opOrders.setBackgroundColor(ContextCompat.getColor(MainClienteActivity.this, android.R.color.transparent));
        opSettings.setBackgroundColor(ContextCompat.getColor(MainClienteActivity.this, android.R.color.transparent));

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
                ivOrders.setImageResource(R.drawable.ic_orders_selected);
                tvOrders.setVisibility(View.VISIBLE);
                opOrders.setBackgroundResource(R.drawable.round_back_option_100);

                animateView(opOrders);
                break;
            case 3:
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

    public void hideHeader(){
        LinearLayout header = findViewById(R.id.header);
        header.setVisibility(View.GONE);
    }

    public void showHeader(){
        LinearLayout header = findViewById(R.id.header);
        header.setVisibility(View.VISIBLE);
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