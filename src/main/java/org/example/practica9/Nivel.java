package org.example.practica9;

import java.util.ArrayList;
import java.util.List;

public class Nivel {

    private final String nombre;
    private final String dificultad;
    private final int numero;

    private final List<Obstaculo> obstaculos = new ArrayList<>();
    private final List<CheckPoint> checkPoints = new ArrayList<>();
    private final List<ElementoDinamico> elementosDinamicos =
            new ArrayList<>();

    private final Inventario inventario;

    public Nivel(String nombre, int numero, String dificultad,
                 Inventario inventario) {

        this.nombre = nombre;
        this.numero = numero;
        this.dificultad = dificultad;
        this.inventario = inventario;
    }

    public String getNombre() {
        return nombre;
    }

    public int getNumero() {
        return numero;
    }

    public String getDificultad() {
        return dificultad;
    }

    public Inventario getInventario() {
        return inventario;
    }

    public List<Obstaculo> getObstaculos() {
        return obstaculos;
    }

    public List<CheckPoint> getCheckPoints() {
        return checkPoints;
    }

    public List<ElementoDinamico> getElementosDinamicos() {
        return elementosDinamicos;
    }

    public void agregarObstaculo(Obstaculo o) {
        obstaculos.add(o);
    }

    public void agregarCheckPoint(CheckPoint c) {
        checkPoints.add(c);
    }

    public void agregarElementoDinamico(ElementoDinamico e) {
        elementosDinamicos.add(e);
    }

    public void moverElementosDinamicos(String direccion, int distancia) {
        for (ElementoDinamico e : elementosDinamicos) {
            e.mover(direccion, distancia);
        }
    }

    public void mostrarEstado() {
        System.out.println(nombre + " - " + numero + " - " + dificultad);
        System.out.println("Obstáculos: " + obstaculos.size());
    }
}