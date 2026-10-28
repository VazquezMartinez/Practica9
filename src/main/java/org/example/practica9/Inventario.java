package org.example.practica9;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class Inventario {

    private final int capacidadMaxima;
    private final List<Inventariable> items = new ArrayList<>();

    public Inventario(int capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
    }

    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public List<Inventariable> getItems() {
        return Collections.unmodifiableList(items);
    }

    public boolean agregarItem(Inventariable item) {
        if (items.size() >= capacidadMaxima) {
            return false;
        }

        items.add(item);
        item.registrar();
        return true;
    }

    public boolean eliminarItem(Inventariable item) {
        if (!items.remove(item)) {
            return false;
        }

        item.borrar();
        return true;
    }

    public void listarItems() {
        items.forEach(System.out::println);
    }
}
