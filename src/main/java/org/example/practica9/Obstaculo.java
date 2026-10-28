package org.example.practica9;

public class Obstaculo implements Destruible {

    private final String nombre;
    private final int danio;
    private final int posicionX;
    private final int posicionY;
    private boolean destruido;

    public Obstaculo(String nombre, int danio, int x, int y) {
        this.nombre = nombre;
        this.danio = danio;
        this.posicionX = x;
        this.posicionY = y;
    }

    public String getNombre() { return nombre; }
    public int getDanio() { return danio; }
    public int getPosicionX() { return posicionX; }
    public int getPosicionY() { return posicionY; }
    public boolean isDestruido() { return destruido; }

    @Override
    public void destruye() {
        destruido = true;
    }
}