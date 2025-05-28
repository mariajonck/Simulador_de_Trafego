/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.udesc.trafegoveiculos;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Veiculo extends Thread {
    private static int contadorId = 0;
    private final int id;
    private final int velocidade;
    private Celula atual;
    private final MecanismoExclusao exclusao;
    private final MalhaViaria malha;
    private final Random random = new Random();
    private boolean ativo = true;

    public Veiculo(MalhaViaria malha, Celula entrada, int velocidade, MecanismoExclusao exclusao) {
        this.id = ++contadorId;
        this.malha = malha;
        this.atual = entrada;
        this.velocidade = velocidade;
        this.exclusao = exclusao;
    }

    @Override
    public void run() {
        try {
            synchronized (atual) {
                if (!atual.isOcupada()) {
                    atual.ocupar();
                    atual.setReservada(false);
                    System.out.println("[V" + id + "] Ocupou inicial: (" + atual.getLinha() + "," + atual.getColuna() + ")");
                } else {
                    encerrar();
                    return;
                }
            }

            while (ativo) {
                Celula[] rotaParaPercorrer = calcularProximoCaminho();

                if (rotaParaPercorrer == null || rotaParaPercorrer.length == 0) {
                    System.out.println("[V" + id + "] Caminho nulo ou vazio. Encerrando.");
                    encerrar();
                    return;
                }

                boolean isCruzamento = rotaParaPercorrer.length > 1;

                if (isCruzamento) {
                    if (exclusao.tentarEntrar(rotaParaPercorrer)) {
                        for (Celula c : rotaParaPercorrer) {
                            c.reservar();
                        }
                        for (Celula proxima : rotaParaPercorrer) {
                            boolean avancou = false;

                            while (!avancou) {
                                synchronized (proxima) {
                                    if (!proxima.isOcupada()) {
                                        proxima.ocupar();
                                        proxima.setReservada(false);

                                        synchronized (atual) {
                                            if (atual != null && atual != proxima) {
                                                atual.liberar();
                                            }
                                            atual = proxima;
                                        }

                                        System.out.println("[V" + id + "] Avançou para: (" + atual.getLinha() + "," + atual.getColuna() + ")");
                                        Thread.sleep(velocidade);
                                        avancou = true;

                                        if (ehSaida(atual)) {
                                            encerrar();
                                        }
                                    }
                                }

                                if (!avancou) {
                                    Thread.sleep(velocidade / 4);
                                }
                            }
                        }
                        exclusao.liberar(rotaParaPercorrer);

                        if (ehSaida(atual)) {
                            encerrar();
                        }
                    } else {
                        Thread.sleep(velocidade / 2);
                    }
                } else {
                    Celula proximaCelula = rotaParaPercorrer[0];
                    boolean avancou = false;

                    proximaCelula.reservar();

                    while (!avancou) {
                        synchronized (proximaCelula) {
                            if (!proximaCelula.isOcupada()) {
                                proximaCelula.ocupar();
                                proximaCelula.setReservada(false);

                                synchronized (atual) {
                                    atual.liberar();
                                    atual = proximaCelula;
                                }

                                System.out.println("[V" + id + "] Movendo para: (" + atual.getLinha() + "," + atual.getColuna() + ")");
                                Thread.sleep(velocidade);
                                avancou = true;

                                if (ehSaida(atual)) {
                                    encerrar();
                                }
                            }
                        }

                        if (!avancou) {
                            Thread.sleep(velocidade / 4);
                        }
                    }
                }
            }
        } catch (InterruptedException e) {
            encerrar();
            Thread.currentThread().interrupt();
        }
    }

    private Celula[] calcularProximoCaminho() {
    int tipo = atual.getTipo();
    int l = atual.getLinha();
    int c = atual.getColuna();

    System.out.println("[V" + id + "] Tipo atual: " + tipo + " em (" + l + "," + c + ")");

    if (tipo >= 9 && tipo <= 12) {
        List<Celula> rotaCruzamento = new ArrayList<>();

        switch (tipo) {
            case 9 -> {
                Celula cima1 = malha.getCelula(l - 1, c);
                Celula cima2 = malha.getCelula(l - 2, c);
                boolean cimaValida = cima1 != null && cima2 != null && cima1.getTipo() != 0 && cima2.getTipo() != 0;

                Celula dir1 = malha.getCelula(l, c + 1);
                Celula dir2 = malha.getCelula(l, c + 2);
                boolean dirValida = dir1 != null && dir2 != null && dir1.getTipo() != 0 && dir2.getTipo() != 0;

                if (cimaValida && (random.nextBoolean() || !dirValida)) {
                    cima1.reservar(); cima2.reservar();
                    rotaCruzamento.add(cima1); rotaCruzamento.add(cima2);
                } else if (dirValida) {
                    dir1.reservar(); dir2.reservar();
                    rotaCruzamento.add(dir1); rotaCruzamento.add(dir2);
                }
            }

            case 10 -> {
                Celula cima1 = malha.getCelula(l - 1, c);
                Celula cima2 = malha.getCelula(l - 2, c);
                boolean cimaValida = cima1 != null && cima2 != null && cima1.getTipo() != 0 && cima2.getTipo() != 0;

                Celula esq1 = malha.getCelula(l, c - 1);
                Celula esq2 = malha.getCelula(l, c - 2);
                boolean esqValida = esq1 != null && esq2 != null && esq1.getTipo() != 0 && esq2.getTipo() != 0;

                if (cimaValida && (random.nextBoolean() || !esqValida)) {
                    cima1.reservar(); cima2.reservar();
                    rotaCruzamento.add(cima1); rotaCruzamento.add(cima2);
                } else if (esqValida) {
                    esq1.reservar(); esq2.reservar();
                    rotaCruzamento.add(esq1); rotaCruzamento.add(esq2);
                }
            }

            case 11 -> {
                Celula bai1 = malha.getCelula(l + 1, c);
                Celula bai2 = malha.getCelula(l + 2, c);
                boolean baiValida = bai1 != null && bai2 != null && bai1.getTipo() != 0 && bai2.getTipo() != 0;

                Celula dir1 = malha.getCelula(l, c + 1);
                Celula dir2 = malha.getCelula(l, c + 2);
                boolean dirValida = dir1 != null && dir2 != null && dir1.getTipo() != 0 && dir2.getTipo() != 0;

                if (baiValida && (random.nextBoolean() || !dirValida)) {
                    bai1.reservar(); bai2.reservar();
                    rotaCruzamento.add(bai1); rotaCruzamento.add(bai2);
                } else if (dirValida) {
                    dir1.reservar(); dir2.reservar();
                    rotaCruzamento.add(dir1); rotaCruzamento.add(dir2);
                }
            }

            case 12 -> {
                Celula bai1 = malha.getCelula(l + 1, c);
                Celula bai2 = malha.getCelula(l + 2, c);
                boolean baiValida = bai1 != null && bai2 != null && bai1.getTipo() != 0 && bai2.getTipo() != 0;

                Celula esq1 = malha.getCelula(l, c - 1);
                Celula esq2 = malha.getCelula(l, c - 2);
                boolean esqValida = esq1 != null && esq2 != null && esq1.getTipo() != 0 && esq2.getTipo() != 0;

                if (baiValida && (random.nextBoolean() || !esqValida)) {
                    bai1.reservar(); bai2.reservar();
                    rotaCruzamento.add(bai1); rotaCruzamento.add(bai2);
                } else if (esqValida) {
                    esq1.reservar(); esq2.reservar();
                    rotaCruzamento.add(esq1); rotaCruzamento.add(esq2);
                }
            }
        }

        return rotaCruzamento.isEmpty() ? null : rotaCruzamento.toArray(new Celula[0]);
    } else {
        Celula next = proximaCelulaUnitaria(tipo, l, c);
        if (next != null && next.getTipo() != 0) {
            next.reservar();
            System.out.println("[V" + id + "] Próxima célula simples: (" + next.getLinha() + "," + next.getColuna() + ")");
            return new Celula[]{next};
        }
        return null;
    }
}

    private void logCaminho(int id, Celula c1, Celula c2) {
        if (c1 != null && c2 != null) {
            System.out.println("[V" + id + "] Caminho planejado: (" + c1.getLinha() + "," + c1.getColuna() + ") -> (" + c2.getLinha() + "," + c2.getColuna() + ")");
        } else {
            System.out.println("[V" + id + "] Caminho inválido detectado no cruzamento");
        }
    }

    private Celula proximaCelulaUnitaria(int tipo, int l, int c) {
        return switch (tipo) {
            case 1 -> malha.getCelula(l - 1, c);
            case 2 -> malha.getCelula(l, c + 1);
            case 3 -> malha.getCelula(l + 1, c);
            case 4 -> malha.getCelula(l, c - 1);
            case 5 -> malha.getCelula(l - 1, c);
            case 6 -> malha.getCelula(l, c + 1);
            case 7 -> malha.getCelula(l + 1, c);
            case 8 -> malha.getCelula(l, c - 1);
            default -> null;
        };
    }

    private boolean ehSaida(Celula celula) {
        int l = celula.getLinha();
        int c = celula.getColuna();
        int tipo = celula.getTipo();
        return (l == 0 || l == malha.getLinhas() - 1 || c == 0 || c == malha.getColunas() - 1) && tipo >= 1 && tipo <= 4;
    }

    private void encerrar() {
        if (atual != null) {
            atual.liberar();
        }
        ativo = false;
        System.out.println("[V" + id + "] Finalizado em: (" + atual.getLinha() + "," + atual.getColuna() + ")");
    }
}
