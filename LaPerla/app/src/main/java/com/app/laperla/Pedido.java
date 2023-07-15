package com.app.laperla;

public class Pedido {
    private String fecha;
    private int cantidadProductos;
    private double total;

    public Pedido(String fecha, int cantidadProductos, double total) {
        this.fecha = fecha;
        this.cantidadProductos = cantidadProductos;
        this.total = total;
    }

    public String getFecha() {
        return fecha;
    }

    public int getCantidadProductos() {
        return cantidadProductos;
    }

    public double getTotal() {
        return total;
    }
}

