package org.example.practica9;

public class Recompensa implements Inventariable {

    private final String nombre;
    private final int valor;
    private final String tipo;

    public Recompensa(String nombre, int valor, String tipo) {
        this.nombre = nombre;
        this.valor = valor;
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public int getValor() {
        return valor;
    }

    public String getTipo() {
        return tipo;
    }

    @Override
    public void registrar() {
        System.out.println("Recogido: " + nombre);
    }

    @Override
    public void borrar() {
        System.out.println("Retirado: " + nombre);
    }

    @Override
    public String toString() {
        return nombre + " (" + valor + " puntos)";
    }
}