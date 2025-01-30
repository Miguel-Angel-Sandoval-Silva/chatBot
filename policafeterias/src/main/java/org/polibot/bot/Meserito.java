package org.polibot.bot;

import org.polibot.Pedido;
import org.polibot.Producto;
import org.polibot.sql.Consultas;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.HashMap;
import java.util.Map;

public class Meserito extends TelegramLongPollingBot {
    private String infoPedido;
    //1586280541L
    //Jared Admin 5730808185L
    private long adminID = 6809111636L; // Reemplaza con el ID real del administrador
    private boolean pedidoGenerado = false;

    private Consultas consultar = new Consultas();
    private Map<Long, EstadoUsuario> estadosUsuarios = new HashMap<>();

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private void responder(long chatId, String text) {
        EnviarRes responder = new EnviarRes(chatId, text);
        try {
            execute(responder.getMessage());
            System.out.println("Mensaje enviado al chatId " + chatId + ": " + text);
        } catch (TelegramApiException e) {
            System.err.println("Error al enviar mensaje al chatId " + chatId);
            e.printStackTrace();
        }
    }

    @Override
    public String getBotToken() {
        return "7129730060:AAGPnXIjRv4jhdh2NGe24hnOi9bFrjFCTKk"; // Reemplaza con el token real de tu bot
    }

    @Override
    public String getBotUsername() {
        return "polimeseritoBot"; // Reemplaza con el nombre de tu bot
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {

            Long chatId = update.getMessage().getChatId();
            String message = update.getMessage().getText();
            Long userId = update.getMessage().getFrom().getId();

            // Mensajes de depuración
            System.out.println("Mensaje recibido de " + userId + ": " + message);

            // Obtener o crear el estado del usuario
            EstadoUsuario estado = estadosUsuarios.get(userId);
            if (estado == null) {
                estado = new EstadoUsuario();
                estadosUsuarios.put(userId, estado);
                System.out.println("Creado nuevo estado para el usuario " + userId);
            } else {
                System.out.println("Estado existente para el usuario " + userId);
            }

            if (esAdmin(userId)) {
                estado.esAdmin = true;
                procesarComandosAdmin(message, chatId, estado);
            } else {
                if (estado.comprando) {
                    System.out.println("Estoy en comprando");
                    opcionesCarrito(message, chatId, estado);
                } else if (estado.comprado) {
                    System.out.println("Estoy en comprado");
                    pedidoRealizado(message, chatId, estado);
                } else if (estado.haySubtipo) {
                    System.out.println("Estoy en subtipo");
                    procesarSubtipo(message, chatId, estado);
                } else {
                    System.out.println("Estoy en pedir comida");
                    comandosIniciales(message, chatId, estado);
                }
            }
        }
    }

    // ADMIN
    private boolean esAdmin(Long userId) {
        return userId.equals(adminID);
    }

    private void procesarComandosAdmin(String message, Long chatId, EstadoUsuario estado) {
        if (message.equals("/start")) {
            mostrarMenuAdmin(chatId, estado);
        } else if (estado.adminOperation == null) {
            if (message.equals("/addCafeteria")) {
                estado.adminOperation = "addCafeteria";
                solicitarNombreCafeteria(chatId, estado);
            } else if (message.equals("/modifyCafeteria")) {
                estado.adminOperation = "modifyCafeteria";
                mostrarListaCafeterias(chatId, estado);
            } else if (message.equals("/deleteCafeteria")) {
                estado.adminOperation = "deleteCafeteria";
                mostrarListaCafeterias(chatId, estado);
            } else {
                responder(chatId, "Por favor seleccione una opción válida.");
                mostrarMenuAdmin(chatId, estado);
            }
        } else {
            switch (estado.adminOperation) {
                case "addCafeteria":
                    procesarAgregarCafeteria(message, chatId, estado);
                    break;
                case "modifyCafeteria":
                    procesarModificarCafeteria(message, chatId, estado);
                    break;
                case "deleteCafeteria":
                    procesarEliminarCafeteria(message, chatId, estado);
                    break;
                case "addProduct":
                    procesarAgregarProducto(message, chatId, estado);
                    break;
                case "modifyProduct":
                    procesarModificarProducto(message, chatId, estado);
                    break;
                case "deleteProduct":
                    procesarEliminarProducto(message, chatId, estado);
                    break;
                default:
                    responder(chatId, "Operación no válida.");
                    estado.adminOperation = null;
                    estado.adminStep = 0;
                    mostrarMenuAdmin(chatId, estado);
                    break;
            }
        }
    }

    private void mostrarMenuAdmin(Long chatId, EstadoUsuario estado) {
        String menu = "Bienvenido administrador, por favor seleccione una opción:\n" +
                "/addCafeteria - Agregar cafetería\n" +
                "/modifyCafeteria - Modificar cafetería\n" +
                "/deleteCafeteria - Eliminar cafetería";
        responder(chatId, menu);
        estado.adminOperation = null;
        estado.adminStep = 0;
    }

    private void solicitarNombreCafeteria(Long chatId, EstadoUsuario estado) {
        responder(chatId, "Por favor, ingrese el nombre de la cafetería que desea agregar:");
        estado.adminStep = 1;
    }

    private void procesarAgregarCafeteria(String message, Long chatId, EstadoUsuario estado) {
        if (estado.adminStep == 1) {
            String nombreCafeteria = message.replaceAll("\\s+", "_").toLowerCase();
            boolean success = consultar.crearCafeteria(nombreCafeteria);
            if (success) {
                responder(chatId, "La cafetería '" + nombreCafeteria + "' ha sido creada exitosamente.");
            } else {
                responder(chatId, "Hubo un error al crear la cafetería '" + nombreCafeteria + "'.");
            }
            estado.adminOperation = null;
            estado.adminStep = 0;
            mostrarMenuAdmin(chatId, estado);
        }
    }

    private void mostrarListaCafeterias(Long chatId, EstadoUsuario estado) {
        List<String> cafeterias = consultar.obtenerCafeterias();
        if (cafeterias.isEmpty()) {
            responder(chatId, "No hay cafeterías registradas.");
            estado.adminOperation = null;
            estado.adminStep = 0;
            mostrarMenuAdmin(chatId, estado);
        } else {
            StringBuilder sb = new StringBuilder("Cafeterías existentes:\n");
            for (String cafeteria : cafeterias) {
                sb.append("/").append(cafeteria).append("\n");
            }
            responder(chatId, sb.toString());
            responder(chatId, "Por favor, seleccione una cafetería:");
            estado.adminStep = 1;
        }
    }

    private void procesarEliminarCafeteria(String message, Long chatId, EstadoUsuario estado) {
        if (estado.adminStep == 1) {
            String cafeteriaSeleccionada = message.startsWith("/") ? message.substring(1) : message;
            boolean success = consultar.eliminarCafeteria(cafeteriaSeleccionada);
            if (success) {
                responder(chatId, "La cafetería '" + cafeteriaSeleccionada + "' ha sido eliminada exitosamente.");
            } else {
                responder(chatId, "Hubo un error al eliminar la cafetería '" + cafeteriaSeleccionada + "'.");
            }
            estado.adminOperation = null;
            estado.adminStep = 0;
            mostrarMenuAdmin(chatId, estado);
        }
    }

    private void procesarModificarCafeteria(String message, Long chatId, EstadoUsuario estado) {
        if (estado.adminStep == 1) {
            estado.selectedCafeteria = message.startsWith("/") ? message.substring(1) : message;
            estado.adminStep = 2;
            mostrarCategorias(chatId, estado);
        } else if (estado.adminStep == 2) {
            estado.selectedCategory = message.startsWith("/") ? message.substring(1) : message;
            estado.adminStep = 3;
            mostrarAccionesCategoria(chatId, estado);
        } else if (estado.adminStep == 3) {
            if (message.equals("/addProduct")) {
                estado.adminOperation = "addProduct";
                estado.adminStep = 4;
                solicitarDetallesProducto(chatId, estado);
            } else if (message.equals("/modifyProduct")) {
                estado.adminOperation = "modifyProduct";
                estado.adminStep = 4;
                mostrarProductosEnCategoria(chatId, estado);
            } else if (message.equals("/deleteProduct")) {
                estado.adminOperation = "deleteProduct";
                estado.adminStep = 4;
                mostrarProductosEnCategoria(chatId, estado);
            } else {
                responder(chatId, "Por favor, seleccione una acción válida.");
            }
        }
    }

    private void mostrarCategorias(Long chatId, EstadoUsuario estado) {
        String categorias = "Escoja una categoría:\n" +
                "/galletas\n" +
                "/comida\n" +
                "/sabritas\n" +
                "/bebidas\n" +
                "/dulces";
        responder(chatId, categorias);
    }

    private void mostrarAccionesCategoria(Long chatId, EstadoUsuario estado) {
        String acciones = "Seleccione una acción:\n" +
                "/addProduct - Agregar producto\n" +
                "/modifyProduct - Modificar producto\n" +
                "/deleteProduct - Eliminar producto";
        responder(chatId, acciones);
    }

    private void solicitarDetallesProducto(Long chatId, EstadoUsuario estado) {
        responder(chatId, "Ingrese los detalles del producto separados por comas en el siguiente orden:\n" +
                "Nombre, Precio, Stock, Tiempo de preparación en minutos");
    }

    private void procesarAgregarProducto(String message, Long chatId, EstadoUsuario estado) {
        if (estado.adminStep == 4) {
            String[] detalles = message.split(",");
            if (detalles.length == 4) {
                String nombreProducto = detalles[0].trim();
                float precio = Float.parseFloat(detalles[1].trim());
                int enStock = Integer.parseInt(detalles[2].trim());
                int tPreparacion = Integer.parseInt(detalles[3].trim());
                String idProducto = consultar.generarIdProducto();

                Producto producto = new Producto(idProducto, nombreProducto, precio, enStock, tPreparacion, estado.selectedCategory);

                boolean success = consultar.agregarProducto(estado.selectedCafeteria, producto);
                if (success) {
                    responder(chatId, "Producto agregado exitosamente.");
                } else {
                    responder(chatId, "Error al agregar el producto.");
                }
            } else {
                responder(chatId, "Formato incorrecto. Por favor, intente nuevamente.");
                return; // Salimos para que el usuario vuelva a intentar
            }
            estado.adminOperation = "modifyCafeteria";
            estado.adminStep = 2; // Volvemos a las acciones de categoría
            mostrarAccionesCategoria(chatId, estado);
        }
    }

    private void mostrarProductosEnCategoria(Long chatId, EstadoUsuario estado) {
        List<Producto> productos = consultar.obtenerProductosPorCategoria(estado.selectedCafeteria, estado.selectedCategory);
        if (productos.isEmpty()) {
            responder(chatId, "No hay productos en esta categoría.");
            estado.adminOperation = "modifyCafeteria";
            estado.adminStep = 2;
            mostrarAccionesCategoria(chatId, estado);
        } else {
            StringBuilder sb = new StringBuilder("Productos en la categoría '" + estado.selectedCategory + "':\n");
            for (Producto producto : productos) {
                sb.append("/").append(producto.getId()).append(" - ").append(producto.getNombre()).append("\n");
            }
            responder(chatId, sb.toString());
            responder(chatId, "Seleccione un producto:");
            estado.adminStep = 5; // Avanzamos al siguiente paso
        }
    }

    private void procesarModificarProducto(String message, Long chatId, EstadoUsuario estado) {
        if (estado.adminStep == 5) {
            estado.selectedProductId = message.startsWith("/") ? message.substring(1) : message;
            Producto producto = consultar.tomarProducto(estado.selectedProductId, estado.selectedCafeteria);
            if (producto != null) {
                estado.productoEnEdicion = producto;
                responder(chatId, "Ingrese los nuevos detalles del producto separados por comas en el siguiente orden:\n" +
                        "Nombre, Precio, Stock, Tiempo de preparación en minutos");
                estado.adminStep = 6;
            } else {
                responder(chatId, "Producto no encontrado. Intente nuevamente.");
            }
        } else if (estado.adminStep == 6) {
            String[] detalles = message.split(",");
            if (detalles.length == 4) {
                estado.productoEnEdicion.setNombre(detalles[0].trim());
                estado.productoEnEdicion.setPrecio(Float.parseFloat(detalles[1].trim()));
                estado.productoEnEdicion.setEn_stock(Integer.parseInt(detalles[2].trim()));
                estado.productoEnEdicion.setT_preparacion(Integer.parseInt(detalles[3].trim()));

                boolean success = consultar.modificarProducto(estado.selectedCafeteria, estado.productoEnEdicion);
                if (success) {
                    responder(chatId, "Producto modificado exitosamente.");
                } else {
                    responder(chatId, "Error al modificar el producto.");
                }
            } else {
                responder(chatId, "Formato incorrecto. Por favor, intente nuevamente.");
                return; // Salimos para que el usuario vuelva a intentar
            }
            estado.adminOperation = "modifyCafeteria";
            estado.adminStep = 2;
            mostrarAccionesCategoria(chatId, estado);
        }
    }

    private void procesarEliminarProducto(String message, Long chatId, EstadoUsuario estado) {
        if (estado.adminStep == 5) {
            String idProducto = message.startsWith("/") ? message.substring(1) : message;
            boolean success = consultar.eliminarProducto(estado.selectedCafeteria, idProducto);
            if (success) {
                responder(chatId, "Producto eliminado exitosamente.");
            } else {
                responder(chatId, "Error al eliminar el producto.");
            }
            estado.adminOperation = "modifyCafeteria";
            estado.adminStep = 2;
            mostrarAccionesCategoria(chatId, estado);
        }
    }

    // PDFs
    private void recibirPedido(String nombreArchivo) {
        System.out.println("Enviando PDF al administrador: " + nombreArchivo);
        File pdf = new File(nombreArchivo);
        mensajePDF(adminID, pdf);
        //pedidoGenerado = true;
    }

    private void crearPDF(Pedido pedido, String cafeteria) {
        PDF pdf = new PDF();
        String nombreArchivo = "pedido_" + pedido.getNum_pedido() + ".pdf";
        pdf.crearPDF(pedido, nombreArchivo, cafeteria);
        //recibirPedido(nombreArchivo);
    }

    private void mensajePDF(Long chatId, File file) {
        System.out.println("Enviando PDF al chatId: " + chatId);
        InputFile inputFile = new InputFile(file);
        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(chatId.toString());
        sendDocument.setDocument(inputFile);
        sendDocument.setCaption("Ha recibido un nuevo pedido.");
        try {
            execute(sendDocument);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private String obtenerInfo(Pedido pedido) {
        return consultar.infoDoc(pedido, "pedidos_" + pedido.getNombre_user()); // Usar el nombre de usuario del pedido
    }

    // COMANDOS
    private void mensajeIncial(Long chatId, EstadoUsuario estado) {
        System.out.println("Enviando mensaje inicial al chatId: " + chatId);
        responder(chatId, "Hola, bienvenido\n¿En qué cafetería desea comprar?\n" + listarCafeterias());
        estado.entrao = true;
    }

    private String listarCafeterias() {
        List<String> cafeterias = consultar.obtenerCafeterias();
        StringBuilder sb = new StringBuilder();
        for (String cafeteria : cafeterias) {
            sb.append("/").append(cafeteria).append("\n");
        }
        return sb.toString();
    }

    private void comandosIniciales(String msj, Long chatId, EstadoUsuario estado) {
        System.out.println("Entrando en comandosIniciales con mensaje: " + msj);

        if (msj.equals("/start")) {
            System.out.println("Procesando comando /start");
            estado.entrao = true;
            estado.estaEnMenu = false;
            estado.comprando = false;
            mensajeIncial(chatId, estado);
        } else if (msj.startsWith("/") && !estado.estaEnMenu) {
            String cafeteriaSeleccionada = msj.substring(1);
            List<String> cafeterias = consultar.obtenerCafeterias();
            if (cafeterias.contains(cafeteriaSeleccionada)) {
                estado.estaEnMenu = true;
                estado.cafeteria = cafeteriaSeleccionada;
                System.out.println("Seleccionada cafetería: " + estado.cafeteria);
                menuCafeteria(chatId, estado.cafeteria, estado);
            } else {
                responder(chatId, "Cafetería no encontrada. Por favor, seleccione una cafetería válida.");
            }
        } else {
            escogerCategoria(chatId, msj, estado);
        }
    }

    private void menuCafeteria(Long chatId, String cafe, EstadoUsuario estado) {
        responder(chatId, "Escoja una categoría:\n/galletas\n/comida\n/sabritas\n/bebidas\n/dulces");
    }

    private void escogerCategoria(Long chatId, String msj, EstadoUsuario estado) {
        System.out.println("escogerCategoria llamado con msj: " + msj);
        if (msj.equals("/galletas")) {
            System.out.println("Categoría galletas seleccionada");
            mostrarMensajeAdecuado(chatId, estado.estaEnMenu, "galletas", estado);
        } else if (msj.equals("/comida")) {
            System.out.println("Categoría comida seleccionada");
            mostrarMensajeAdecuado(chatId, estado.estaEnMenu, "comida", estado);
        } else if (msj.equals("/sabritas")) {
            System.out.println("Categoría sabritas seleccionada");
            mostrarMensajeAdecuado(chatId, estado.estaEnMenu, "sabritas", estado);
        } else if (msj.equals("/bebidas")) {
            System.out.println("Categoría bebidas seleccionada");
            mostrarMensajeAdecuado(chatId, estado.estaEnMenu, "bebidas", estado);
        } else if (msj.equals("/dulces")) {
            System.out.println("Categoría dulces seleccionada");
            mostrarMensajeAdecuado(chatId, estado.estaEnMenu, "dulces", estado);
        } else if (msj.equals("/cancelarCompra")) {
            estado.entrao = false;
            estado.estaEnMenu = false;
            estado.comprando = false;
            mensajeIncial(chatId, estado);
        } else if (msj.equals("/cambiarCategoria")) {
            estado.entrao = false;
            estado.estaEnMenu = false;
            estado.comprando = false;
            mensajeIncial(chatId, estado);
        } else {
            respuestaInvalida(chatId, estado);
        }
    }

    private void menuCategoria(Long chatId, String categoria, EstadoUsuario estado) {

        estado.comprando = true;
        List<Producto> pproductos = consultar.productos(estado.cafeteria);

        StringBuilder response = new StringBuilder(categoria + ":\n\n");

        for (Producto producto : pproductos) {
            if (producto.getCategoria().equalsIgnoreCase(categoria)) {
                response.append("| /")
                        .append(producto.getId())
                        .append(". ")
                        .append(producto.getNombre())
                        .append(" - $")
                        .append(producto.getPrecio())
                        .append(" -- stock: ")
                        .append(producto.getEn_stock())
                        .append("\n");
            }
        }
        response.append("\n/cambiarCategoria\n\n" +
                "Si se equivoca siempre puede pulsar /cancelarCompra\npulse /finalizarCompra cuando lo desee");

        responder(chatId, response.toString());
    }

    // PROCESAR PEDIDO
    private void confirmarPedido(Long chatId, EstadoUsuario estado) {
        float total = 0;
        estado.tiempo = 0;
        StringBuilder response = new StringBuilder();

        response.append("Confirmar pedido:\n\n");

        for (Producto item : estado.carrito) {
            response.append(item.getNombre()).append(" - $").append(item.getPrecio()).append("\n");
            total += item.getPrecio();
            estado.tiempo += item.getT_preparacion();
        }

        // Verificar si el total es 0.0
        if (total == 0.0) {
            responder(chatId, "Tu carrito está vacío. Por favor, agrega productos antes de finalizar la compra.");
            return;
        }

        response.append("\nTotal a pagar: $").append(total).append("\n\n/cancelarCompra\n").append("/confirmarCompra");

        responder(chatId, response.toString());
    }


    private void pedidoRealizado(String msj, Long chatId, EstadoUsuario estado) {
        if (estado.pedidoProcesado) {
            System.out.println("El pedido ya ha sido procesado para este usuario.");
            return;
        }

        System.out.println("Entrando a pedidoRealizado para el chatId: " + chatId + ", nombre de usuario: " + msj);
        if (removerProductos(chatId, estado)) {
            Pedido pedido = consultar.enviarPedido(estado.carrito, "pedidos_" + estado.cafeteria, msj, estado.tiempo);
            String yaquedo = "Estimado " + msj + "\nSu pedido ha sido procesado con éxito. El número de su orden es: " + pedido.getNum_pedido() + "." +
                    "\nPodrá pasar a recogerlo en aproximadamente " + estado.tiempo + " minutos.";
            responder(chatId, yaquedo);
            pedidoGenerado = true;
            crearPDF(pedido, estado.cafeteria);
            String nombreArchivo = "pedido_" + pedido.getNum_pedido() + ".pdf";
            recibirPedido(nombreArchivo);

            if (estado.tiempo > 0) {
                try {
                    scheduler.schedule(() -> {
                        try {
                            System.out.println("Enviando mensaje: Pedido listo para el usuario: " + msj);
                            String mensaje = "Estimado " + msj + ", su pedido está listo. Puede pasar a recogerlo.";
                            responder(chatId, mensaje);
                        } catch (Exception e) {
                            System.err.println("Error al enviar el mensaje programado: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }, estado.tiempo, TimeUnit.MINUTES);
                } catch (Exception e) {
                    System.err.println("Error al programar el mensaje: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                String mensaje = "Estimado " + msj + ", su pedido está listo. Puede pasar a recogerlo.";
                responder(chatId, mensaje);
            }

            resetearVar(estado);
        } else {
            String yaquedo = "Puedes generar un nuevo pedido con /start";
            responder(chatId, yaquedo);
            resetearVar(estado);
        }
    }

    private boolean removerProductos(Long chatId, EstadoUsuario estado) {

        for (Producto p : estado.carrito) {
            if (consultar.getStock(p, estado.cafeteria) <= 0) {
                responder(chatId, "Lo sentimos, no tenemos suficiente\n" + p.getNombre()
                        + "\n\nen existencia: " + p.getEn_stock());
                consultar.devolverProducto(p, estado.cafeteria);
                estado.carrito.remove(p);
                return false;
            } else {
                consultar.updateStock(p, estado.cafeteria);
            }
        }

        return true;
    }

    // OPCIONES CARRITO DE COMPRAS
    private void agregarAlCarrito(String id, Long chatId, EstadoUsuario estado) {

        Producto item = consultar.tomarProducto(id, estado.cafeteria);
        if (item == null) {
            responder(chatId, "Producto no encontrado.");
            return;
        }
        estado.comidaSubtipo = item;

        if (estado.comidaSubtipo.getCategoria().equalsIgnoreCase("comida")) {
            if (!ifsComida(estado.comidaSubtipo, chatId, estado)) {
                estado.haySubtipo = false;
                estado.carrito.add(item);
            } else {
                estado.haySubtipo = true;
            }
        } else {
            if (item.getEn_stock() <= 0) {
                responder(chatId, "Lo sentimos, nos quedamos sin " + item.getNombre()
                        + "\nescoge otro producto por favor");
            } else {
                estado.haySubtipo = false;
                estado.carrito.add(item);
            }
        }
    }

    private void opcionesCarrito(String msj, Long chatId, EstadoUsuario estado) {

        if (msj.equals("/cancelarCompra")) {
            estado.carrito.clear();
            estado.comprando = false;
        } else if (msj.equals("/finalizarCompra")) {
            mostrarCompras(estado);
            confirmarPedido(chatId, estado);
        } else if (msj.equals("/confirmarCompra")) {
            estado.comprando = false;
            estado.estaEnMenu = false;
            estado.comprado = true;
            responder(chatId, "A nombre de quién se hará el pedido?");
        } else if (msj.equals("/cambiarCategoria")) {
            estado.comprando = false;
            menuCafeteria(chatId, estado.cafeteria, estado);
        } else {
            String id = msj.replace("/", "");
            agregarAlCarrito(id, chatId, estado);
        }
    }

    private void mostrarCompras(EstadoUsuario estado) {
        System.out.println("Productos en el carrito:");
        for (Producto p : estado.carrito) {
            System.out.println("- " + p.getNombre() + " - $" + p.getPrecio());
        }
    }

    // HELPING METHODS
    private void mostrarMensajeAdecuado(Long chatId, boolean estaEnMenu, String categoria, EstadoUsuario estado) {
        if (estaEnMenu) {
            menuCategoria(chatId, categoria, estado);
        } else {
            responder(chatId, "Por favor, seleccione la cafetería antes de continuar.");
        }
    }

    private void respuestaInvalida(Long chatId, EstadoUsuario estado) {
        if (!estado.entrao) {
            responder(chatId, "Por favor, inicia la interacción con /start.");
        } else if (!estado.estaEnMenu) {
            responder(chatId, "Por favor, selecciona una cafetería para continuar.");
        } else if (!estado.comprando) {
            responder(chatId, "Por favor, selecciona una categoría para ver los productos.");
        } else {
            responder(chatId, "Esa no es una opción válida. Intenta nuevamente.");
        }
    }

    private boolean ifsComida(Producto p, Long chatId, EstadoUsuario estado) {
        estado.comprando = false;
        boolean flag = true;
        if (p.getNombre().startsWith("Boneless")) {
            responder(chatId, "Escoja una salsa: /bbq /habanero /buffalo /sinSalsa");
        } else if (p.getNombre().startsWith("Flauta")) {
            responder(chatId, "Escoja un tipo: /picadillo /salsaVerde /deshebrada /huevoConPapas");
        } else if (p.getNombre().startsWith("Gordita") || p.getNombre().startsWith("Sope")) {
            responder(chatId, "Escoja un tipo: /picadillo /salsaVerde /deshebrada /huevoConPapas");
        } else if (p.getNombre().startsWith("Torta")) {
            responder(chatId, "Escoja un tipo: /jamon /salsaVerde /deshebrada");
        } else if (p.getNombre().startsWith("Sushi")) {
            responder(chatId, "Escoja un tipo: /salmon /californiaRoll /res");
        } else {
            estado.comprando = true;
            flag = false;
        }

        return flag;
    }

    private void procesarSubtipo(String message, Long chatId, EstadoUsuario estado) {
        String subtipo = message.replace("/", "");
        estado.comidaSubtipo.setNombre(estado.comidaSubtipo.getNombre() + " [" + subtipo + "]");
        estado.carrito.add(estado.comidaSubtipo);
        estado.haySubtipo = false;
        estado.comprando = true;
        responder(chatId, "¡Ok! Puede seguir comprando más alimentos.");
    }

    private void resetearVar(EstadoUsuario estado) {
        estado.comprado = false;
        estado.estaEnMenu = false;
        estado.carrito.clear();
        estado.tiempo = 0;
        estado.pedidoProcesado = false;
    }
}
