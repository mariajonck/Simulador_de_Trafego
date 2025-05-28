/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.udesc.trafegoveiculos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class SimuladorGUI extends JFrame {
    private MalhaViaria malha;
    private GerenciadorVeiculos gerenciador;
    private PainelMalha painelMalha;
    private JTextField txtMaxVeiculos;
    private JTextField txtIntervalo;
    private JComboBox<String> comboExclusao;

    private Timer timerRepaint;

    public SimuladorGUI() {
        setTitle("Simulador de Tráfego");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        carregarMalha();
        construirPainelControle();

        painelMalha = new PainelMalha(malha);
        add(painelMalha, BorderLayout.CENTER);

        timerRepaint = new Timer(100, e -> painelMalha.repaint());
        timerRepaint.start();

        setSize(800, 800);
        setVisible(true);
    }

    private void carregarMalha() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Selecione o arquivo da malha");

        int resultado = chooser.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            try {
                malha = new MalhaViaria();
                malha.carregarArquivo(chooser.getSelectedFile().getAbsolutePath());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar malha: " + e.getMessage());
                System.exit(1);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Nenhum arquivo selecionado. Encerrando.");
            System.exit(0);
        }
    }

    private void construirPainelControle() {
        JPanel painel = new JPanel(new FlowLayout());

        txtMaxVeiculos = new JTextField("10", 5);
        txtIntervalo = new JTextField("1000", 5);
        comboExclusao = new JComboBox<>(new String[]{"Monitor", "Semáforo"});

        JButton btnIniciar = new JButton("Iniciar Simulação");
        JButton btnParar = new JButton("Parar Inserção");
        JButton btnEncerrar = new JButton("Encerrar Simulação");

        painel.add(new JLabel("Máx Veículos:"));
        painel.add(txtMaxVeiculos);
        painel.add(new JLabel("Intervalo (ms):"));
        painel.add(txtIntervalo);
        painel.add(new JLabel("Exclusão:"));
        painel.add(comboExclusao);
        painel.add(btnIniciar);
        painel.add(btnParar);
        painel.add(btnEncerrar);

        btnIniciar.addActionListener(this::iniciarSimulacao);
        btnParar.addActionListener(e -> gerenciador.pararInsercao());
        btnEncerrar.addActionListener(e -> gerenciador.encerrarSimulacao());

        add(painel, BorderLayout.NORTH);
    }

    private void iniciarSimulacao(ActionEvent e) {
        int max = Integer.parseInt(txtMaxVeiculos.getText());
        int intervalo = Integer.parseInt(txtIntervalo.getText());
        MecanismoExclusao exclusao = comboExclusao.getSelectedItem().equals("Monitor")
                ? new ExclusaoMonitor()
                : new ExclusaoSemaforo();

        gerenciador = new GerenciadorVeiculos(malha, exclusao, max, intervalo);
        gerenciador.iniciar();

        if (!timerRepaint.isRunning()) {
            timerRepaint.start();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SimuladorGUI::new);
    }
}
