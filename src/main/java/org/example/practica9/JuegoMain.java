package org.example.practica9;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JuegoMain extends Application {

    private static final int ANCHO = 900;
    private static final int ALTO = 500;
    private static final int SUELO = 430;

    private final Pane escenario = new Pane();
    private final List<Circle> monedas = new ArrayList<>();
    private final List<Rectangle> balas = new ArrayList<>();

    private Personaje jugador;
    private Enemigo enemigo;
    private Utileria plataformaMovil;
    private Nivel nivel;
    private Arma arma;

    private Rectangle figuraJugador;
    private Rectangle figuraEnemigo;
    private Rectangle figuraMovil;

    private Text marcador;
    private Text mensaje;

    private boolean izquierda;
    private boolean derecha;
    private boolean mirandoDerecha = true;
    private boolean terminado;

    private int puntos = 0;
    private int reaparicionX = 50;
    private int reaparicionY = 390;
    private int inmunidad = 0;

    private long ultimoFrame = 0;
    private long ultimoDisparo = 0;

    private String direccionPlataforma = "este";

    @Override
    public void start(Stage ventana) {

        escenario.setPrefSize(ANCHO, ALTO);
        escenario.setStyle("-fx-background-color: #a9e1ff;");

        Inventario inventario = new Inventario(20);
        nivel = new Nivel("Pradera", 1, "Facil", inventario);

        jugador = new Personaje("Heroe", 3, 50, 390);
        jugador.setEnSuelo(true);
        nivel.agregarElementoDinamico(jugador);

        arma = new Arma("Lanzador", 1, 450);
        inventario.agregarItem(arma);

        enemigo = new Enemigo(
                "Monstruo", 2, 620, 390, 550, 760
        );
        nivel.agregarElementoDinamico(enemigo);

        plataformaMovil = new Utileria(
                "Plataforma movil",
                "Va de un lado a otro",
                310, 305
        );
        nivel.agregarElementoDinamico(plataformaMovil);

        nivel.agregarObstaculo(
                new Obstaculo("Bloque 1", 0, 175, 355)
        );

        nivel.agregarObstaculo(
                new Obstaculo("Bloque 2", 0, 470, 325)
        );

        CheckPoint checkpoint = new CheckPoint(
                "Bandera", 505, 380
        );
        nivel.agregarCheckPoint(checkpoint);

        Rectangle suelo = new Rectangle(
                0, SUELO, ANCHO, ALTO - SUELO
        );
        suelo.setFill(Color.FORESTGREEN);
        escenario.getChildren().add(suelo);

        for (Obstaculo o : nivel.getObstaculos()) {
            Rectangle bloque = new Rectangle(
                    o.getPosicionX(),
                    o.getPosicionY(),
                    105, 20
            );
            bloque.setFill(Color.SADDLEBROWN);
            escenario.getChildren().add(bloque);
        }

        figuraMovil = new Rectangle(
                plataformaMovil.getPosicionX(),
                plataformaMovil.getPosicionY(),
                95, 15
        );
        figuraMovil.setFill(Color.DARKORANGE);
        escenario.getChildren().add(figuraMovil);

        Rectangle bandera = new Rectangle(
                checkpoint.getPosicionX(),
                checkpoint.getPosicionY() - 45,
                8, 95
        );
        bandera.setFill(Color.DARKBLUE);
        escenario.getChildren().add(bandera);

        Rectangle tela = new Rectangle(
                checkpoint.getPosicionX() + 8,
                checkpoint.getPosicionY() - 45,
                30, 22
        );
        tela.setFill(Color.GOLD);
        escenario.getChildren().add(tela);

        Rectangle meta = new Rectangle(855, 335, 12, 95);
        meta.setFill(Color.DARKGREEN);
        escenario.getChildren().add(meta);

        Text metaTexto = new Text(830, 325, "META");
        escenario.getChildren().add(metaTexto);

        int[][] posicionesMonedas = {
                {205, 325},
                {350, 265},
                {500, 295},
                {780, 390}
        };

        for (int[] pos : posicionesMonedas) {
            Circle moneda = new Circle(
                    pos[0], pos[1], 11, Color.GOLD
            );
            monedas.add(moneda);
            escenario.getChildren().add(moneda);
        }

        figuraJugador = new Rectangle(
                jugador.getPosicionX(),
                jugador.getPosicionY(),
                jugador.getAncho(),
                jugador.getAlto()
        );
        figuraJugador.setFill(Color.RED);
        escenario.getChildren().add(figuraJugador);

        figuraEnemigo = new Rectangle(
                enemigo.getPosicionX(),
                enemigo.getPosicionY(),
                30, 40
        );
        figuraEnemigo.setFill(Color.PURPLE);
        escenario.getChildren().add(figuraEnemigo);

        marcador = new Text(15, 25, "");
        escenario.getChildren().add(marcador);

        Text controles = new Text(
                15, 45,
                "Flechas: mover | ESPACIO: saltar | F: disparar | R: reiniciar"
        );
        escenario.getChildren().add(controles);

        Scene escena = new Scene(escenario, ANCHO, ALTO);

        escena.setOnKeyPressed(event -> {
            KeyCode tecla = event.getCode();

            if (tecla == KeyCode.LEFT) {
                izquierda = true;
            }

            if (tecla == KeyCode.RIGHT) {
                derecha = true;
            }

            if (tecla == KeyCode.SPACE && !terminado) {
                jugador.saltar();
            }

            if (tecla == KeyCode.F && !terminado) {
                disparar(System.nanoTime());
            }

            if (tecla == KeyCode.R) {
                reiniciar();
            }
        });

        escena.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.LEFT) {
                izquierda = false;
            }

            if (event.getCode() == KeyCode.RIGHT) {
                derecha = false;
            }
        });

        ventana.setTitle("Practica 9 - Juego de plataformas");
        ventana.setScene(escena);
        ventana.show();

        escenario.requestFocus();

        new AnimationTimer() {
            @Override
            public void handle(long ahora) {

                if (ultimoFrame == 0) {
                    ultimoFrame = ahora;
                    return;
                }

                if (ahora - ultimoFrame < 16_000_000L) {
                    return;
                }

                ultimoFrame = ahora;

                if (!terminado) {
                    actualizar();
                }

                dibujar();
            }
        }.start();
    }

    private void disparar(long ahora) {

        if (ahora - ultimoDisparo < 250_000_000L) {
            return;
        }

        ultimoDisparo = ahora;

        Rectangle bala = new Rectangle(
                jugador.getPosicionX() + (mirandoDerecha ? 30 : -10),
                jugador.getPosicionY() + 16,
                10, 6
        );

        bala.setFill(Color.ORANGE);
        bala.setUserData(mirandoDerecha ? 1 : -1);

        balas.add(bala);
        escenario.getChildren().add(bala);
    }

    private boolean solapa(
            double ax, double ay, double aw, double ah,
            double bx, double by, double bw, double bh) {

        return ax < bx + bw
                && ax + aw > bx
                && ay < by + bh
                && ay + ah > by;
    }

    private void actualizar() {

        if (inmunidad > 0) {
            inmunidad--;
        }

        int x = jugador.getPosicionX();
        int yAnterior = jugador.getPosicionY();

        if (izquierda) {
            x -= 4;
            mirandoDerecha = false;
        }

        if (derecha) {
            x += 4;
            mirandoDerecha = true;
        }

        x = Math.max(
                0,
                Math.min(ANCHO - jugador.getAncho(), x)
        );

        jugador.setPosicion(x, yAnterior);
        jugador.aplicarGravedad();

        int y = jugador.getPosicionY();
        int aterrizaje = SUELO;

        for (Obstaculo o : nivel.getObstaculos()) {

            if (!o.isDestruido()
                    && x + 30 > o.getPosicionX()
                    && x < o.getPosicionX() + 105
                    && yAnterior + 40 <= o.getPosicionY()
                    && y + 40 >= o.getPosicionY()) {

                aterrizaje = Math.min(
                        aterrizaje, o.getPosicionY()
                );
            }
        }

        int mx = plataformaMovil.getPosicionX();

        if (mx >= 390) {
            direccionPlataforma = "oeste";
        }

        if (mx <= 280) {
            direccionPlataforma = "este";
        }

        plataformaMovil.mover(direccionPlataforma, 2);

        int movX = plataformaMovil.getPosicionX();
        int movY = plataformaMovil.getPosicionY();

        if (x + 30 > movX
                && x < movX + 95
                && yAnterior + 40 <= movY
                && y + 40 >= movY) {

            aterrizaje = Math.min(aterrizaje, movY);
        }

        if (y + 40 >= aterrizaje
                && yAnterior + 40 <= aterrizaje) {

            jugador.setPosicion(x, aterrizaje - 40);
            jugador.setEnSuelo(true);

        } else if (y + 40 >= SUELO) {

            jugador.setPosicion(x, SUELO - 40);
            jugador.setEnSuelo(true);

        } else {
            jugador.setEnSuelo(false);
        }

        enemigo.patrullar();

        if (enemigo.isEstaVivo()
                && inmunidad == 0
                && solapa(
                jugador.getPosicionX(),
                jugador.getPosicionY(),
                30, 40,
                enemigo.getPosicionX(),
                enemigo.getPosicionY(),
                30, 40)) {

            jugador.recibirDanio(1);
            inmunidad = 65;

            if (!jugador.isEstaVivo()) {
                terminado = true;
                mostrarMensaje("GAME OVER - Presiona R");
            } else {
                jugador.setPosicion(
                        reaparicionX, reaparicionY
                );
            }
        }

        Iterator<Rectangle> it = balas.iterator();

        while (it.hasNext()) {

            Rectangle bala = it.next();
            int sentido = (int) bala.getUserData();

            bala.setX(bala.getX() + sentido * 9);

            boolean impacto = enemigo.isEstaVivo()
                    && solapa(
                    bala.getX(), bala.getY(), 10, 6,
                    enemigo.getPosicionX(),
                    enemigo.getPosicionY(),
                    30, 40);

            if (impacto) {
                enemigo.recibirDanio(arma.getDanio());
            }

            if (impacto
                    || bala.getX() < 0
                    || bala.getX() > ANCHO) {

                escenario.getChildren().remove(bala);
                it.remove();
            }
        }

        Iterator<Circle> monedasIt = monedas.iterator();

        while (monedasIt.hasNext()) {

            Circle moneda = monedasIt.next();

            if (solapa(
                    jugador.getPosicionX(),
                    jugador.getPosicionY(),
                    30, 40,
                    moneda.getCenterX() - 11,
                    moneda.getCenterY() - 11,
                    22, 22)) {

                Recompensa premio = new Recompensa(
                        "Moneda", 10, "Puntos"
                );

                if (nivel.getInventario().agregarItem(premio)) {
                    puntos += premio.getValor();

                    escenario.getChildren().remove(moneda);
                    monedasIt.remove();
                }
            }
        }

        for (CheckPoint cp : nivel.getCheckPoints()) {

            if (!cp.isActivado()
                    && solapa(
                    jugador.getPosicionX(),
                    jugador.getPosicionY(),
                    30, 40,
                    cp.getPosicionX(),
                    cp.getPosicionY() - 45,
                    38, 95)) {

                cp.activar();

                reaparicionX = cp.getPosicionX() + 35;
                reaparicionY = SUELO - 40;

                plataformaMovil.usar();
            }
        }

        if (jugador.getPosicionX() + 30 >= 855) {
            terminado = true;
            mostrarMensaje("¡GANASTE! - Presiona R");
        }
    }

    private void mostrarMensaje(String texto) {

        if (mensaje != null) {
            escenario.getChildren().remove(mensaje);
        }

        mensaje = new Text(280, 200, texto);
        mensaje.setStyle(
                "-fx-font-size: 25px; -fx-font-weight: bold;"
        );

        escenario.getChildren().add(mensaje);
    }

    private void dibujar() {

        figuraJugador.setX(jugador.getPosicionX());
        figuraJugador.setY(jugador.getPosicionY());

        figuraJugador.setOpacity(
                inmunidad > 0 && inmunidad % 10 < 5
                        ? 0.35 : 1
        );

        figuraEnemigo.setX(enemigo.getPosicionX());
        figuraEnemigo.setY(enemigo.getPosicionY());

        figuraEnemigo.setVisible(enemigo.isEstaVivo());

        figuraMovil.setX(plataformaMovil.getPosicionX());
        figuraMovil.setY(plataformaMovil.getPosicionY());

        marcador.setText(
                "Vida: " + jugador.getVida()
                        + "   Puntos: " + puntos
                        + "   Arma: " + arma.getNombre()
                        + "   Enemigo: "
                        + (enemigo.isEstaVivo() ? "vivo" : "derrotado")
        );
    }

    private void reiniciar() {

        Stage ventana = (Stage) escenario.getScene().getWindow();

        ventana.close();
        new JuegoMain().start(new Stage());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
