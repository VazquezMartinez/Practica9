package org.example.practica9;

public class Personaje implements Destruible, ElementoDinamico {

    private final String nombre;
    private int vida, posicionX, posicionY;
    private boolean estaVivo = true, enSuelo;
    private double velocidadY;
    private final int ancho = 30, alto = 40;

    public Personaje(String nombre, int vida, int x, int y) {
        this.nombre = nombre;
        this.vida = vida;
        posicionX = x;
        posicionY = y;
        estaVivo = vida > 0;
    }

    public String getNombre() { return nombre; }
    public int getVida() { return vida; }
    public int getPosicionX() { return posicionX; }
    public int getPosicionY() { return posicionY; }
    public int getAncho() { return ancho; }
    public int getAlto() { return alto; }
    public boolean isEnSuelo() { return enSuelo; }
    public boolean isEstaVivo() { return estaVivo; }

    public void setEnSuelo(boolean valor) {
        enSuelo = valor;
        if (valor) velocidadY = 0;
    }

    public void setPosicion(int x, int y) {
        posicionX = x;
        posicionY = y;
    }

    public void recibirDanio(int danio) {
        if (!estaVivo) return;

        vida = Math.max(0, vida - Math.max(0, danio));

        if (vida == 0) {
            destruye();
        }
    }

    public void saltar() {
        if (enSuelo && estaVivo) {
            velocidadY = -12;
            enSuelo = false;
        }
    }

    public void aplicarGravedad() {
        if (!estaVivo) return;

        velocidadY = Math.min(10, velocidadY + 0.5);
        posicionY += (int) Math.round(velocidadY);
    }

    @Override
    public void mover(String direccion, int distancia) {
        if (!estaVivo) return;

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

    @Override
    public void destruye() {
        estaVivo = false;
        System.out.println(nombre + " ha muerto");
    }
}