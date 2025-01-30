package org.polibot;

import java.util.List;

public class Pedido {

    private int num_pedido;
    private String nombre_user;
    private StringBuilder orden;
    private float total;
    private int tiempo_estimado;
    private List<Producto> productos; // Agrega este campo

    public Pedido(int numPedido, String nombreUser, StringBuilder orden, float total, int tiempoEstimado, List<Producto> productos) {
        this.num_pedido = numPedido;
        this.nombre_user = nombreUser;
        this.orden = orden;
        this.total = total;
        this.tiempo_estimado = tiempoEstimado;
        this.productos = productos;
    }

    // Getters y Setters
    public int getNum_pedido() {
        return num_pedido;
    }

    public String getNombre_user() {
        return nombre_user;
    }

    public StringBuilder getOrden() {
        return orden;
    }

    public float getTotal() {
        return total;
    }

    public int getTiempo_estimado() {
        return tiempo_estimado;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }
}
