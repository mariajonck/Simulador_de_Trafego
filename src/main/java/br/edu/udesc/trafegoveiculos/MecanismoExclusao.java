/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.udesc.trafegoveiculos;

public interface MecanismoExclusao {
    boolean tentarEntrar(Celula[] caminho);
    void liberar(Celula[] caminho);
}
