package org.polibot.bot;

import org.polibot.Producto;
import java.util.ArrayList;
import java.util.List;

public class EstadoUsuario {
    public boolean pedidoProcesado = false;
    public boolean entrao = false;
    public boolean estaEnMenu = false;
    public boolean comprando = false;
    public boolean comprado = false;
    public boolean haySubtipo = false;
    public List<Producto> carrito = new ArrayList<>();
    public int tiempo;
    public String cafeteria;
    public Producto comidaSubtipo;
    public boolean esAdmin = false;
    public String adminOperation = null;
    public String selectedCafeteria = null;
    public String selectedCategory = null;
    public String selectedProductId = null;
    public Producto productoEnEdicion = null;
    public int adminStep = 0; // Para controlar los pasos en cada operación

    // Constructor vacío
    public EstadoUsuario() {
    }
}
