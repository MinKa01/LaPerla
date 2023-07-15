package com.app.laperla;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "laperla.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //Creación de las tablas
        String sqlUsuario = "CREATE TABLE usuario (" +
                "idUsuario INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombreUsu TEXT," +
                "apellidoUsu TEXT," +
                "telefonoUsu TEXT," +
                "direccionUsu TEXT," +
                "correoUsu TEXT," +
                "claveUsu TEXT," +
                "rolUsu TEXT" +
                ");";
        db.execSQL(sqlUsuario);

        String sqlCompra = "CREATE TABLE compra (" +
                "idCompra INTEGER PRIMARY KEY AUTOINCREMENT," +
                "idUsuario INTEGER," +
                "fechaCom TEXT," +
                "totalCom REAL," +
                "FOREIGN KEY (idUsuario) REFERENCES usuario (idUsuario)" +
                ");";
        db.execSQL(sqlCompra);

        String sqlDetalleCompra = "CREATE TABLE detallecompra (" +
                "idDetalle INTEGER PRIMARY KEY AUTOINCREMENT," +
                "idProducto INTEGER," +
                "idCompra INTEGER," +
                "cantidad INTEGER," +
                "FOREIGN KEY (idProducto) REFERENCES producto (idProducto)," +
                "FOREIGN KEY (idCompra) REFERENCES compra (idCompra)" +
                ");";
        db.execSQL(sqlDetalleCompra);

        String sqlProducto = "CREATE TABLE producto (" +
                "idProducto INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombreProd TEXT," +
                "imagenProd TEXT," +
                "detalleProd TEXT," +
                "precioProd REAL" +
                ");";
        db.execSQL(sqlProducto);

        // Insertar un usuario administrador
        String correoAdmin = "admin1@gmail.com";
        String claveAdmin = "Admin1234";
        String sqlInsertAdmin = "INSERT INTO Usuario (nombreUsu, apellidoUsu, telefonoUsu, direccionUsu, correoUsu, claveUsu, rolUsu)" +
                " VALUES ('Admin', 'Admin', '1234567890', 'Calle Falsa 123', '" + correoAdmin + "', '" + claveAdmin + "', 'admin')";
        db.execSQL(sqlInsertAdmin);

        // Insertar un usuario cliente
        String correoClient = "cliente@gmail.com";
        String claveClient = "Cliente1234";
        String sqlInsertClient = "INSERT INTO Usuario (nombreUsu, apellidoUsu, telefonoUsu, direccionUsu, correoUsu, claveUsu, rolUsu)" +
                " VALUES ('Julio', 'Perez Gutierrez', '999999998', 'Calle Verdadera 123', '" + correoClient + "', '" + claveClient + "', 'cliente')";
        db.execSQL(sqlInsertClient);

        // Insertar platos
        String sqlInsertPlato1 = "INSERT INTO producto (nombreProd, imagenProd, detalleProd, precioProd)" +
                " VALUES ('Plato 1', 'plato', 'Descripción del plato 1', 10.50)";
        db.execSQL(sqlInsertPlato1);

        String sqlInsertPlato2 = "INSERT INTO producto (nombreProd, imagenProd, detalleProd, precioProd)" +
                " VALUES ('Plato 2', 'plato', 'Descripción del plato 2', 15.00)";
        db.execSQL(sqlInsertPlato2);

        String sqlInsertPlato3 = "INSERT INTO producto (nombreProd, imagenProd, detalleProd, precioProd)" +
                " VALUES ('Plato 3', 'plato', 'Descripción del plato 3', 22.50)";
        db.execSQL(sqlInsertPlato3);

        String sqlInsertPlato4 = "INSERT INTO producto (nombreProd, imagenProd, detalleProd, precioProd)" +
                " VALUES ('Plato 4', 'plato', 'Descripción del plato 4', 24.30)";
        db.execSQL(sqlInsertPlato4);

        String sqlInsertPlato5 = "INSERT INTO producto (nombreProd, imagenProd, detalleProd, precioProd)" +
                " VALUES ('Plato 5', 'plato', 'Descripción del plato 5', 13.50)";
        db.execSQL(sqlInsertPlato5);

        String sqlInsertPlato6 = "INSERT INTO producto (nombreProd, imagenProd, detalleProd, precioProd)" +
                " VALUES ('Plato 6', 'plato', 'Descripción del plato 6', 27.00)";
        db.execSQL(sqlInsertPlato6);

        // Insertar una compra del usuario cliente(defecto)
        String sqlInsertCompra1 = "INSERT INTO compra (idUsuario, fechaCom, totalCom) " +
                " VALUES ('2','2023-07-06',19.00)";
        db.execSQL(sqlInsertCompra1);
        String sqlInsertDetalleCompra1 = "INSERT INTO detallecompra (idProducto, idCompra, cantidad) " +
                "VALUES ('1','1',1)";
        db.execSQL(sqlInsertDetalleCompra1);

        String sqlInsertCompra2 = "INSERT INTO compra (idUsuario, fechaCom, totalCom) " +
                " VALUES ('2','2023-07-12',52.00)";
        db.execSQL(sqlInsertCompra2);
        String sqlInsertDetalleCompra2 = "INSERT INTO detallecompra (idProducto, idCompra, cantidad) " +
                "VALUES ('6','2',2)";
        db.execSQL(sqlInsertDetalleCompra2);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void registrarUsuario(String nombre, String apellido, String telefono, String direccion, String correo, String clave, String rol) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombreUsu", nombre);
        values.put("apellidoUsu", apellido);
        values.put("telefonoUsu", telefono);
        values.put("direccionUsu", direccion);
        values.put("correoUsu", correo);
        values.put("claveUsu", clave);
        values.put("rolUsu", rol);
        db.insert("Usuario", null, values);
    }

    public void actualizarUsuario(Usuario usuario) {
        // Obtener una referencia a la base de datos en modo escritura
        SQLiteDatabase db = getWritableDatabase();

        // Crear un objeto ContentValues con los valores a actualizar
        ContentValues values = new ContentValues();
        values.put("nombreUsu", usuario.getNombre());
        values.put("apellidoUsu", usuario.getApellido());
        values.put("telefonoUsu", usuario.getTelefono());
        values.put("direccionUsu", usuario.getDireccion());
        values.put("correoUsu", usuario.getCorreo());
        values.put("claveUsu", usuario.getClave());
        values.put("rolUsu", usuario.getRol());

        // Definir la cláusula WHERE para especificar qué registro actualizar
        String whereClause = "idUsuario = ?";
        String[] whereArgs = {String.valueOf(usuario.getId())};

        // Ejecutar la sentencia UPDATE
        db.update("usuario", values, whereClause, whereArgs);

        // Cerrar la base de datos
        db.close();
    }


    public void eliminarUsuario(int idUsuario) {
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = "idUsuario = ?";
        String[] whereArgs = {String.valueOf(idUsuario)};
        db.delete("usuario", whereClause, whereArgs);
    }

}
