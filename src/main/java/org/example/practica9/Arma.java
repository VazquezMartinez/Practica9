package org.example.practica9;

public class Arma implements Inventariable {

    private final String nombre;
    private final int danio;
    private final double alcance;

    public Arma(String nombre, int danio, double alcance) {
        this.nombre = nombre;
        this.danio = danio;
        this.alcance = alcance;
    }

    public String getNombre() {
        return nombre;
    }

    public int getDanio() {
        return danio;
    }

    public double getAlcance() {
        return alcance;
    }

    @Override
    public void registrar() {
        System.out.println("Arma equipada: " + nombre);
    }

    @Override
    public void borrar() {
        System.out.println("Arma retirada: " + nombre);
    }

    @Override
    public String toString() {
        return "Arma: " + nombre;
    }
}