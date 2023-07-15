package com.app.laperla;

import android.os.Parcel;
import android.os.Parcelable;


public class Producto implements Parcelable {
    private int id;
    private String nombre;
    private String imagen;
    private String detalle;
    private double precio;

    private int cantidad;

    public Producto(int id, String nombre, String imagen, String detalle, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.imagen = imagen;
        this.detalle = detalle;
        this.precio = precio;
        this.cantidad = 1;
    }

    protected Producto(Parcel in) {
        id = in.readInt();
        nombre = in.readString();
        imagen = in.readString();
        detalle = in.readString();
        precio = in.readDouble();
    }

    public static final Creator<Producto> CREATOR = new Creator<Producto>() {
        @Override
        public Producto createFromParcel(Parcel in) {
            return new Producto(in);
        }

        @Override
        public Producto[] newArray(int size) {
            return new Producto[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void incrementarCantidad() {
        cantidad++;
    }

    public void decrementarCantidad() {
        if (cantidad > 1) {
            cantidad--;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nombre);
        dest.writeString(imagen);
        dest.writeString(detalle);
        dest.writeDouble(precio);
    }
}
