package org.example.practica9;

public class Utileria implements ElementoDinamico {

    private final String nombre;
    private final String descripcion;
    private int posicionX;
    private int posicionY;
    private boolean usada;

    public Utileria(String nombre, String descripcion, int x, int y) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        posicionX = x;
        posicionY = y;
    }

    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public int getPosicionX() { return posicionX; }
    public int getPosicionY() { return posicionY; }
    public boolean isUsada() { return usada; }

    public void usar() {
        usada = true;
    }

    @Override
    public void mover(String direccion, int distancia) {
        switch (direccion.toLowerCase()) {
            case "este":
                posicionX += distancia;
                break;
            case "oeste":
                posicionX -= distancia;
                break;
            case "norte":
                posicionY -= distancia;
                break;
            case "sur":
                posicionY += distancia;
                break;
        }
    }
}