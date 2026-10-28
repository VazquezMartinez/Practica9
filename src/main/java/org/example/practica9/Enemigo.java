package org.example.practica9;

public class Enemigo extends Personaje {

    private final int minimoX;
    private final int maximoX;
    private boolean haciaDerecha = true;

    public Enemigo(String nombre, int vida, int x, int y,
                   int minimoX, int maximoX) {

        super(nombre, vida, x, y);
        this.minimoX = minimoX;
        this.maximoX = maximoX;
    }

    public void patrullar() {
        if (!isEstaVivo()) return;

        if (getPosicionX() >= maximoX) {
            haciaDerecha = false;
        }

        if (getPosicionX() <= minimoX) {
            haciaDerecha = true;
        }

        mover(haciaDerecha ? "este" : "oeste", 2);
    }
}