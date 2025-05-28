/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.udesc.trafegoveiculos;


public class ExclusaoMonitor implements MecanismoExclusao {

    private final Object lockGlobal = new Object();

    @Override
    public boolean tentarEntrar(Celula[] caminho) {
        synchronized (lockGlobal) {
            for (Celula c : caminho) {
                if (c.isOcupada()) {
                    return false;
                }
            }
            for (Celula c : caminho) {
                c.reservar();
            }
            return true;
        }
    }

    @Override
    public void liberar(Celula[] caminho) {
        synchronized (lockGlobal) {
            for (Celula c : caminho) {
                c.liberar();
            }
        }
    }
}
