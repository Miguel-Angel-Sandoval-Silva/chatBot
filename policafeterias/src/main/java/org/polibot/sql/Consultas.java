package org.polibot.sql;

import org.polibot.Pedido;
import org.polibot.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Consultas {

    public List<Producto> productos(String tabla) {
        List<Producto> productos = new ArrayList<>();

        try (var connection = MySQLConnection.connect()) {

            String selectQuery = "SELECT * FROM " + tabla;

            try (PreparedStatement statement = connection.prepareStatement(selectQuery)) {

                try (ResultSet resultSet = statement.executeQuery()) {

                    while (resultSet.next()) {

                        String id = resultSet.getString("id");
                        String nom = resultSet.getString("nombre");
                        float precio = resultSet.getFloat("precio");
                        int stock = resultSet.getInt("en_stock");
                        int prep = resultSet.getInt("t_preparacion");
                        String categoria = resultSet.getString("categoria");

                        Producto p = new Producto(id, nom, precio, stock, prep, categoria);
                        productos.add(p);
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return productos;
    }

    public String[] mostrarMenu(String categoria, List<Producto> productos) {
        List<String> lista = new ArrayList<>();

        for (Producto p : productos) {
            if (p.getCategoria().equalsIgnoreCase(categoria)) {
                lista.add("| /" + p.getId() + ". " + p.getNombre() + " - $" + p.getPrecio());
            }
        }

        return lista.toArray(new String[0]);
    }

    public Producto tomarProducto(String id, String cafeteria) {
        try (var connection = MySQLConnection.connect()) {
            String selectQuery = "SELECT * FROM " + cafeteria + " WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(selectQuery)) {
                statement.setString(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        String nombre = resultSet.getString("nombre");
                        float precio = resultSet.getFloat("precio");
                        int stock = resultSet.getInt("en_stock");
                        int tPreparacion = resultSet.getInt("t_preparacion");
                        String categoria = resultSet.getString("categoria");

                        return new Producto(id, nombre, precio, stock, tPreparacion, categoria);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al tomar el producto: " + e.getMessage());
        }
        return null;
    }


    public int getStock(Producto producto, String tabla) {

        int stock = 0;

        try (var connection = MySQLConnection.connect()) {

            String selectQuery = "SELECT * FROM " + tabla + " WHERE id = ?";

            try (PreparedStatement statement = connection.prepareStatement(selectQuery)) {

                statement.setString(1, producto.getId());

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        stock = resultSet.getInt("en_stock");
                        return stock;
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return stock;
    }

    public void updateStock(Producto producto, String tabla) {

        try (var connection = MySQLConnection.connect()) {

            String actualizarQuery = "UPDATE " + tabla + " SET en_stock = en_stock - 1 WHERE id = ?;";

            try (PreparedStatement statement = connection.prepareStatement(actualizarQuery)) {
                statement.setString(1, producto.getId());

                int productoAct = statement.executeUpdate();
                if (productoAct > 0) {
                    System.out.println("El stock del producto ha sido actualizado correctamente.");
                } else {
                    System.out.println("No se encontró ningún producto con el ID proporcionado.");
                }

            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

    }

    public void devolverProducto(Producto producto, String tabla) {

        try (var connection = MySQLConnection.connect()) {

            String actualizarQuery = "UPDATE " + tabla + " SET en_stock = en_stock + ? WHERE id = ?;";

            try (PreparedStatement statement = connection.prepareStatement(actualizarQuery)) {
                statement.setInt(1, producto.getEn_stock());
                statement.setString(2, producto.getId());

                int productoAct = statement.executeUpdate();
                if (productoAct > 0) {
                    System.out.println("El stock del producto ha sido actualizado correctamente.");
                } else {
                    System.out.println("No se encontró ningún producto con el ID proporcionado.");
                }

            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

    }

    public Pedido enviarPedido(List<Producto> carrito, String tabla, String nom_cliente, int tiempo) {

        StringBuilder pedidoTexto = new StringBuilder();
        for (Producto item : carrito) {
            pedidoTexto.append("- ").append(item.getNombre()).append("\n");
        }

        float total = 0;
        for (Producto item : carrito) {
            total += item.getPrecio();
        }

        try (var connection = MySQLConnection.connect()) {

            String insertarQuery = "INSERT INTO " + tabla + " (nom_cliente, pedido, total_pago, tiempo_estimado) " +
                    "VALUES (?, ?, ?, ?)";

            try (PreparedStatement insertStatement = connection.prepareStatement(insertarQuery, Statement.RETURN_GENERATED_KEYS)) {
                insertStatement.setString(1, nom_cliente);
                insertStatement.setString(2, pedidoTexto.toString());
                insertStatement.setFloat(3, total);
                insertStatement.setInt(4, tiempo);

                int filasInsertadas = insertStatement.executeUpdate();

                if (filasInsertadas > 0) {
                    ResultSet generatedKeys = insertStatement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        int idPedido = generatedKeys.getInt(1);
                        System.out.println("Pedido enviado correctamente a la tabla " + tabla + " con el ID: " + idPedido);
                        // Pasa la lista de productos al constructor
                        Pedido nwPedido = new Pedido(idPedido, nom_cliente, pedidoTexto, total, tiempo, carrito);
                        return nwPedido;
                    }
                }

                System.out.println("No se pudo enviar el pedido a la tabla " + tabla);
                return null;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }

    }


    public String infoDoc(Pedido pedido, String tabla) {

        StringBuilder informe = new StringBuilder();
        StringBuilder orden = new StringBuilder();

        try (var connection = MySQLConnection.connect()) {

            String selectQuery = "SELECT * FROM " + tabla + " WHERE num_pedido = ?";

            try (PreparedStatement statement = connection.prepareStatement(selectQuery)) {

                statement.setInt(1, pedido.getNum_pedido());

                try (ResultSet resultSet = statement.executeQuery()) {

                    System.out.println("Selecciono el pedido de: " + pedido.getNombre_user());
                    while (resultSet.next()) {

                        String id = resultSet.getString("num_pedido");
                        String user = resultSet.getString("nom_cliente");
                        orden.append(resultSet.getString("pedido"));
                        float total = resultSet.getFloat("total_pago");
                        int tiempo = resultSet.getInt("tiempo_estimado");

                        informe.append("Buenas!, tiene una orden pendiente a nombre de ").append(user)
                                .append("\nNo. de orden: ").append(id).append("\n\n")
                                .append("Tiempo estimado dado al cliente: ").append(tiempo).append(" minutos\n\n");
                        informe.append(orden);
                        informe.append("\nPrecio total: $").append(total);

                    }
                }
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return informe.toString();

    }

    public boolean crearCafeteria(String nombreCafeteria) {
        String tableName = nombreCafeteria;
        String pedidosTableName = "pedidos_" + nombreCafeteria;

        String createTableSQL = "CREATE TABLE " + tableName + " (" +
                "id VARCHAR(50) PRIMARY KEY," +
                "nombre VARCHAR(100) NOT NULL," +
                "precio DECIMAL(10, 2) NOT NULL," +
                "en_stock INT NOT NULL," +
                "t_preparacion INT NOT NULL," +
                "categoria VARCHAR(50) NOT NULL" +
                ");";

        String createPedidosTableSQL = "CREATE TABLE " + pedidosTableName + " (" +
                "num_pedido INT AUTO_INCREMENT PRIMARY KEY," +
                "nom_cliente VARCHAR(100) NOT NULL," +
                "pedido TEXT NOT NULL," +
                "total_pago DECIMAL(10, 2) NOT NULL," +
                "tiempo_estimado INT NOT NULL" +
                ");";

        try (var connection = MySQLConnection.connect();
             Statement statement = connection.createStatement()) {

            statement.executeUpdate(createTableSQL);
            statement.executeUpdate(createPedidosTableSQL);
            return true;

        } catch (SQLException e) {
            System.err.println("Error al crear las tablas: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarCafeteria(String nombreCafeteria) {
        String tableName = nombreCafeteria;
        String pedidosTableName = "pedidos_" + nombreCafeteria;

        String dropTableSQL = "DROP TABLE IF EXISTS " + tableName;
        String dropPedidosTableSQL = "DROP TABLE IF EXISTS " + pedidosTableName;

        try (var connection = MySQLConnection.connect();
             Statement statement = connection.createStatement()) {

            statement.executeUpdate(dropTableSQL);
            statement.executeUpdate(dropPedidosTableSQL);
            return true;

        } catch (SQLException e) {
            System.err.println("Error al eliminar las tablas: " + e.getMessage());
            return false;
        }
    }

    public List<String> obtenerCafeterias() {
        List<String> cafeterias = new ArrayList<>();
        String query = "SELECT table_name FROM information_schema.tables " +
                "WHERE table_schema = 'cafeterias_upv' AND table_name NOT LIKE 'pedidos_%'";

        try (var connection = MySQLConnection.connect();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                cafeterias.add(resultSet.getString("table_name"));
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener las cafeterías: " + e.getMessage());
        }

        return cafeterias;
    }

    public boolean agregarProducto(String cafeteria, Producto producto) {
        String insertSQL = "INSERT INTO " + cafeteria + " (id, nombre, precio, en_stock, t_preparacion, categoria) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (var connection = MySQLConnection.connect();
             PreparedStatement statement = connection.prepareStatement(insertSQL)) {

            statement.setString(1, producto.getId());
            statement.setString(2, producto.getNombre());
            statement.setFloat(3, producto.getPrecio());
            statement.setInt(4, producto.getEn_stock());
            statement.setInt(5, producto.getT_preparacion());
            statement.setString(6, producto.getCategoria());

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            System.err.println("Error al agregar producto: " + e.getMessage());
            return false;
        }
    }

    public boolean modificarProducto(String cafeteria, Producto producto) {
        try (var connection = MySQLConnection.connect()) {
            String updateQuery = "UPDATE " + cafeteria + " SET nombre = ?, precio = ?, en_stock = ?, t_preparacion = ? WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(updateQuery)) {
                statement.setString(1, producto.getNombre());
                statement.setFloat(2, producto.getPrecio());
                statement.setInt(3, producto.getEn_stock());
                statement.setInt(4, producto.getT_preparacion());
                statement.setString(5, producto.getId());
                int rowsAffected = statement.executeUpdate();
                return rowsAffected > 0; // True si se modificó al menos una fila
            }
        } catch (SQLException e) {
            System.err.println("Error al modificar el producto: " + e.getMessage());
        }
        return false;
    }


    public boolean eliminarProducto(String cafeteria, String idProducto) {
        try (var connection = MySQLConnection.connect()) {
            String deleteQuery = "DELETE FROM " + cafeteria + " WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(deleteQuery)) {
                statement.setString(1, idProducto);
                int rowsAffected = statement.executeUpdate();
                return rowsAffected > 0; // True si se eliminó al menos una fila
            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar el producto: " + e.getMessage());
        }
        return false;
    }


    public List<Producto> obtenerProductosPorCategoria(String cafeteria, String categoria) {
        List<Producto> productos = new ArrayList<>();
        String query = "SELECT * FROM " + cafeteria + " WHERE categoria = ?";

        try (var connection = MySQLConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, categoria);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Producto producto = new Producto(
                            resultSet.getString("id"),
                            resultSet.getString("nombre"),
                            resultSet.getFloat("precio"),
                            resultSet.getInt("en_stock"),
                            resultSet.getInt("t_preparacion"),
                            resultSet.getString("categoria")
                    );
                    productos.add(producto);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener productos: " + e.getMessage());
        }

        return productos;
    }

    // Método para generar un ID único para el producto
    public static String generarIdProducto() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder id = new StringBuilder();

        for (int i = 0; i < 8; i++) { // Cambia el número 8 para ajustar la longitud del ID
            int indice = random.nextInt(caracteres.length());
            id.append(caracteres.charAt(indice));
        }

        return id.toString();
    }

    // Puedes agregar otros métodos si es necesario...

}
