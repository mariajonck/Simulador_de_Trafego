/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.udesc.trafegoveiculos;

import javax.swing.*;
import java.awt.*;

public class PainelMalha extends JPanel {
    private final MalhaViaria malha;
    private static final int TAM = 25;
    
    public PainelMalha(MalhaViaria malha) {
        this.malha = malha;
        setPreferredSize(new Dimension(malha.getColunas() * TAM, malha.getLinhas() * TAM));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int i = 0; i < malha.getLinhas(); i++) {
            for (int j = 0; j < malha.getColunas(); j++) {
                Celula cel = malha.getCelula(i, j);
                switch (cel.getTipo()) {
                    case 0: g.setColor(Color.LIGHT_GRAY); break;
                    default: g.setColor(Color.WHITE); break;
                }

                g.fillRect(j * TAM, i * TAM, TAM, TAM);

                if (cel.isOcupada()) {
                    g.setColor(Color.RED);
                    g.fillOval(j * TAM + 5, i * TAM + 5, TAM - 10, TAM - 10);
                    g.setColor(Color.BLACK);
                    g.setFont(new Font("Arial", Font.PLAIN, 10));
                    g.drawString(String.valueOf(i+ "," +j), j * TAM + 7, i * TAM + 15);
                }

                g.setColor(Color.BLACK);
                g.drawRect(j * TAM, i * TAM, TAM, TAM);
            }
        }
    }
}
