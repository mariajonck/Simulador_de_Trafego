    /*
     * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
     * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
     */
    package br.edu.udesc.trafegoveiculos;

    import java.util.*;
    import java.util.concurrent.CopyOnWriteArrayList;

    public class GerenciadorVeiculos {
        private final MalhaViaria malha;
        private final MecanismoExclusao exclusao;
        private final int maxVeiculos;
        private final int intervaloInsercaoMs;

        private final List<Veiculo> veiculosAtivos = new CopyOnWriteArrayList<>();
        private final List<Celula> entradas;

        private boolean inserindo = true;
        private final Random random = new Random();

        public GerenciadorVeiculos(MalhaViaria malha, MecanismoExclusao exclusao, int maxVeiculos, int intervaloInsercaoMs) {
            this.malha = malha;
            this.exclusao = exclusao;
            this.maxVeiculos = maxVeiculos;
            this.intervaloInsercaoMs = intervaloInsercaoMs;
            this.entradas = localizarEntradas();
        }

        public void iniciar() {
            Thread t = new Thread(() -> {
                while (inserindo) {
                    removerFinalizados();

                    while (veiculosAtivos.size() < maxVeiculos && inserindo) {
                        Celula entrada = escolherEntradaDisponivel();
                        if (entrada != null) {
                            int velocidade = 200 + random.nextInt(500);
                            Veiculo v = new Veiculo(malha, entrada, velocidade, exclusao);
                            veiculosAtivos.add(v);
                            v.start();
                        }
                    }

                    try {
                        Thread.sleep(intervaloInsercaoMs);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            });
            t.start();
        }
        
        public void pararInsercao() {
            inserindo = false;
        }

        public void encerrarSimulacao() {
            pararInsercao();
            for (Veiculo v : veiculosAtivos) {
                v.interrupt();
            }
            veiculosAtivos.clear();
        }

        private void removerFinalizados() {
            veiculosAtivos.removeIf(v -> !v.isAlive());
        }

        private Celula escolherEntradaDisponivel() {
            Collections.shuffle(entradas);
            for (Celula entrada : entradas) {
                if (!entrada.isOcupada()) return entrada;
            }
            return null;
        }

        private List<Celula> localizarEntradas() {
            List<Celula> lista = new ArrayList<>();
            int linhas = malha.getLinhas();
            int colunas = malha.getColunas();

            for (int c = 0; c < colunas; c++) {
                Celula sup = malha.getCelula(0, c);
                Celula inf = malha.getCelula(linhas - 1, c);
                if (sup != null && sup.getTipo() >= 1 && sup.getTipo() <= 4) lista.add(sup);
                if (inf != null && inf.getTipo() >= 1 && inf.getTipo() <= 4) lista.add(inf);
            }

            for (int l = 0; l < linhas; l++) {
                Celula esq = malha.getCelula(l, 0);
                Celula dir = malha.getCelula(l, colunas - 1);
                if (esq != null && esq.getTipo() >= 1 && esq.getTipo() <= 4) lista.add(esq);
                if (dir != null && dir.getTipo() >= 1 && dir.getTipo() <= 4) lista.add(dir);
            }

            return lista;
        }
    }
