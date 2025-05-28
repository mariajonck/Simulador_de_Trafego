/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.udesc.trafegoveiculos;


import java.util.concurrent.Semaphore;

public class ExclusaoSemaforo implements MecanismoExclusao {
    private final Semaphore mutex = new Semaphore(1);

    @Override
    public boolean tentarEntrar(Celula[] caminho) {
       try {
            
            mutex.acquire();

            for (Celula c : caminho) {
                if (c.isOcupada()) {
                    mutex.release();
                    return false; 
                }
            }
            for (Celula c : caminho) {
                c.reservar();
            }
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
        
    }

    @Override
    public void liberar(Celula[] caminho) {
           
            for (Celula c : caminho) {
                c.liberar();
            }
            mutex.release();
        }
    }

