/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package br.edu.udesc.trafegoveiculos;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MalhaViaria {
    private int linhas;
    private int colunas;
    private Celula[][] grid;

    public void carregarArquivo(String caminho) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(caminho));

        this.linhas = Integer.parseInt(br.readLine().trim());
        this.colunas = Integer.parseInt(br.readLine().trim());

        grid = new Celula[linhas][colunas];

        for (int i = 0; i < linhas; i++) {
            String[] valores = br.readLine().trim().split("\\s+");
            for (int j = 0; j < colunas; j++) {
                int tipo = Integer.parseInt(valores[j]);
                grid[i][j] = new Celula(i, j, tipo);
            }
        }

        br.close();
    }

    public Celula getCelula(int linha, int coluna) {
        if (linha >= 0 && linha < linhas && coluna >= 0 && coluna < colunas) {
            return grid[linha][coluna];
        }
        return null;
    }

    public int getLinhas() {
        return linhas;
    }

    public int getColunas() {
        return colunas;
    }

    public Celula[][] getGrid() {
        return grid;
    }
}
