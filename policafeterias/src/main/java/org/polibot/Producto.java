package org.polibot;

public class Producto {

    private String id;
    private String nombre;
    private float precio;
    private int en_stock;
    private int t_preparacion;
    private String categoria;


    public Producto(String id, String nombre, float precio, int en_stock, int t_preparacion, String categoria) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.en_stock = en_stock;
        this.t_preparacion = t_preparacion;
        this.categoria = categoria;
    }

    public Producto() {
    }

    // Getters y setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public int getEn_stock() {
        return en_stock;
    }

    public void setEn_stock(int en_stock) {
        this.en_stock = en_stock;
    }

    public int getT_preparacion() {
        return t_preparacion;
    }

    public void setT_preparacion(int t_preparacion) {
        this.t_preparacion = t_preparacion;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
