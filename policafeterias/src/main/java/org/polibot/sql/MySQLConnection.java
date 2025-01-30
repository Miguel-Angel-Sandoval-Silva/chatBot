package org.polibot.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection {
    public static Connection connect() throws SQLException {

        try {
            // registrar el JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // obtener credenciales
            var jdbcUrl = ConfigBD.getDbUrl();
            var user = ConfigBD.getDbUsername();
            var password = ConfigBD.getDbPassword();

            // conectar
            return DriverManager.getConnection(jdbcUrl, user, password);

        } catch (SQLException | ClassNotFoundException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
}
