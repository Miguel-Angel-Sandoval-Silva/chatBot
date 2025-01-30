package org.polibot.bot;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.*;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.polibot.Pedido;
import org.polibot.Producto;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PDF {

    public void crearPDF(Pedido pedido, String nombreArchivo, String cafeteria) {
        try {
            // Crear el documento y la página
            PDDocument documento = new PDDocument();
            PDPage pagina = new PDPage(PDRectangle.LETTER);
            documento.addPage(pagina);

            // Configurar las fuentes
            PDFont fontTitulo = PDType1Font.HELVETICA_BOLD;
            PDFont fontSubtitulo = PDType1Font.HELVETICA_OBLIQUE;
            PDFont fontContenido = PDType1Font.HELVETICA;
            PDFont fontPie = PDType1Font.HELVETICA_OBLIQUE;

            // Iniciar el flujo de contenido
            PDPageContentStream contenido = new PDPageContentStream(documento, pagina);

            // Agregar el logotipo
            String rutaLogo = "/Users/miguelangelsandovalsilva/Downloads/policafes/policafes/policafeterias/src/main/java/org/polibot/bot/logo.png"; // Asegúrate de tener el logo en esta ruta
            PDImageXObject logo = PDImageXObject.createFromFile(rutaLogo, documento);
            float scale = 0.2f; // Escala del logo
            contenido.drawImage(logo, 50, 700, logo.getWidth() * scale, logo.getHeight() * scale);

            // Agregar el título
            contenido.beginText();
            contenido.setFont(fontTitulo, 24);
            contenido.newLineAtOffset(200, 750);
            contenido.showText("Ticket de Pedido");
            contenido.endText();

            // Agregar fecha y hora
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            String fechaHora = LocalDateTime.now().format(formatter);

            contenido.beginText();
            contenido.setFont(fontSubtitulo, 12);
            contenido.newLineAtOffset(400, 770);
            contenido.showText("Fecha: " + fechaHora);
            contenido.endText();

            // Línea separadora
            contenido.moveTo(50, 690);
            contenido.lineTo(550, 690);
            contenido.stroke();

            // Agregar información del pedido
            contenido.beginText();
            contenido.setFont(fontContenido, 12);
            contenido.newLineAtOffset(50, 670);
            contenido.showText("Cafetería: " + cafeteria);
            contenido.newLineAtOffset(0, -15);
            contenido.showText("Cliente: " + pedido.getNombre_user());
            contenido.newLineAtOffset(0, -15);
            contenido.showText("Número de Pedido: " + pedido.getNum_pedido());
            contenido.newLineAtOffset(0, -15);
            contenido.showText("Tiempo Estimado: " + pedido.getTiempo_estimado() + " minutos");
            contenido.endText();

            // Agregar tabla de productos
            float yPosition = 570;
            agregarTabla(contenido, pedido.getProductos(), yPosition);

            // Agregar total a pagar
            contenido.beginText();
            contenido.setFont(fontTitulo, 14);
            contenido.newLineAtOffset(400, yPosition - (pedido.getProductos().size() * 20) - 30);
            contenido.showText("Total: $" + pedido.getTotal());
            contenido.endText();

            // Agregar pie de página
            contenido.beginText();
            contenido.setFont(fontPie, 10);
            contenido.newLineAtOffset(50, 50);
            contenido.showText("Gracias por su preferencia. Si tiene alguna pregunta, contáctenos al 123-456-7890.");
            contenido.endText();

            // Cerrar el contenido y guardar el documento
            contenido.close();
            documento.save(new File(nombreArchivo));
            documento.close();

            System.out.println("PDF creado correctamente: " + nombreArchivo);
        } catch (IOException e) {
            System.err.println("Error al crear el PDF: " + e.getMessage());
        }
    }

    private void agregarTabla(PDPageContentStream contenido, List<Producto> productos, float yPosition) throws IOException {
        String[] cabecera = {"Producto", "Cantidad", "Precio"};
        String[][] datos = obtenerDatosTabla(productos);

        float margin = 50;
        float yStart = yPosition;
        float yPositionCursor = yStart;
        float rowHeight = 20;
        float tableWidth = 500;
        float cellMargin = 5;
        int cols = cabecera.length;
        float colWidth = tableWidth / (float) cols;

        // Dibujar la cabecera de la tabla
        contenido.setFont(PDType1Font.HELVETICA_BOLD, 12);

        // Fondo gris para la cabecera
        contenido.setNonStrokingColor(200);
        contenido.addRect(margin, yPositionCursor - rowHeight, tableWidth, rowHeight);
        contenido.fill();
        contenido.setNonStrokingColor(0); // Restablecer color

        yPositionCursor -= rowHeight;
        for (int i = 0; i < cabecera.length; i++) {
            String text = cabecera[i];
            float textX = margin + cellMargin + i * colWidth;
            float textY = yPositionCursor + rowHeight / 2 - 6;

            contenido.beginText();
            contenido.newLineAtOffset(textX, textY);
            contenido.showText(text);
            contenido.endText();
        }

        // Dibujar las filas de datos
        contenido.setFont(PDType1Font.HELVETICA, 12);
        for (String[] fila : datos) {
            yPositionCursor -= rowHeight;
            for (int i = 0; i < fila.length; i++) {
                String text = fila[i];
                float textX = margin + cellMargin + i * colWidth;
                float textY = yPositionCursor + rowHeight / 2 - 6;

                contenido.beginText();
                contenido.newLineAtOffset(textX, textY);
                contenido.showText(text);
                contenido.endText();
            }
        }

        // Dibujar bordes de la tabla
        contenido.setStrokingColor(0); // Negro
        float tableTopY = yStart;
        float tableBottomY = yPositionCursor;

        // Líneas horizontales
        for (int i = 0; i <= datos.length + 1; i++) {
            float y = yStart - i * rowHeight;
            contenido.moveTo(margin, y);
            contenido.lineTo(margin + tableWidth, y);
        }

        // Líneas verticales
        for (int i = 0; i <= cols; i++) {
            float x = margin + i * colWidth;
            contenido.moveTo(x, tableTopY);
            contenido.lineTo(x, tableBottomY);
        }
        contenido.stroke();
    }

    private String[][] obtenerDatosTabla(List<Producto> productos) {
        String[][] datos = new String[productos.size()][3];

        for (int i = 0; i < productos.size(); i++) {
            Producto producto = productos.get(i);
            String nombreProducto = producto.getNombre();
            String cantidad = "1"; // Puedes ajustar esto si manejas cantidades
            String precio = "$" + producto.getPrecio();

            datos[i][0] = nombreProducto;
            datos[i][1] = cantidad;
            datos[i][2] = precio;
        }

        return datos;
    }
}
