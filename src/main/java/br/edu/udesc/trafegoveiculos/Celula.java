/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.udesc.trafegoveiculos;

public class Celula {
    private final int linha;
    private final int coluna;
    private final int tipo;

    private boolean ocupada = false;
    private boolean reservada = false;

    private final Object lock = new Object();

    public Celula(int linha, int coluna, int tipo) {
        this.linha = linha;
        this.coluna = coluna;
        this.tipo = tipo;
    }

    public void ocupar() {
        synchronized (lock) {
            ocupada = true;
            reservada = false;
            System.out.println("Célula ocupada: (" + linha + "," + coluna + ")");
        }
    }

    public void reservar() {
        synchronized (lock) {
            reservada = true;
            System.out.println("Célula reservada: (" + linha + "," + coluna + ")");
        }
    }

    public void setReservada(boolean reservada) {
        synchronized (lock) {
            this.reservada = reservada;
            System.out.println("SetReservada(" + reservada + ") em: (" + linha + "," + coluna + ")");
        }
    }

    public void liberar() {
        synchronized (lock) {
            if (ocupada || reservada) {
                ocupada = false;
                reservada = false;
                System.out.println("Célula liberada: (" + linha + "," + coluna + ")");
            }
        }
    }

   public boolean isOcupada() {
        synchronized (lock) {
            return ocupada;
        }
    }

    public boolean isReservada() {
        synchronized (lock) {
            return reservada;
        }
    }


    public int getTipo() {
        return tipo;
    }

    public int getLinha() {
        return linha;
    }

    public int getColuna() {
        return coluna;
    }
}
