package org.polibot;

import org.polibot.bot.Meserito;
import org.polibot.sql.MySQLConnection;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.sql.SQLException;

public class App {
    public static void main(String[] args) {
        try {
            TelegramBotsApi polinesio = new TelegramBotsApi(DefaultBotSession.class);
            polinesio.registerBot(new Meserito());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        try (var connection = MySQLConnection.connect()) {
            System.out.println("Conexion exitosa a la base de datos.");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        // Mantener el programa en ejecución
        System.out.println("El bot está en ejecución. Presiona Ctrl+C para detenerlo.");
        while (true) {
            try {
                Thread.sleep(1000); // Mantén el programa despierto
            } catch (InterruptedException e) {
                System.err.println("El programa ha sido interrumpido.");
                break;
            }
        }
    }
}
