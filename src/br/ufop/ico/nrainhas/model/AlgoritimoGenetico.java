/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufop.ico.nrainhas.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import br.ufop.ico.nrainhas.gui.GUI;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author silvap
 */
public class AlgoritimoGenetico implements Runnable {

    private final int tamanho_populacao;
    private final int tamanho_tabuleiro;
    private final double probabilidade_crossover;
    private final double probabilidade_mutacao;
    private final int max_geracoes;
    private final int max_sem_melhora;
    private ArrayList<Solucao> populacao;
    private int avaliacao;
    private Solucao melhor;
    private double porcentagem_roleta = 0.3;
    private double porcentagem_aleatoria = 0.3;
    private double porcentagem_elitismo = 0.2;
    private double porcentagem_torneio = 0.2;
    private boolean parar;
    private GUI gui;
    private int i_sem_melhora;
    private int geracao;

    public AlgoritimoGenetico(int tamanho_tabuleiro, int tamanho_populacao, double probabilidade_crossover, double probabilidade_mutacao, int max_geracoes, int max_sem_melhora) {
        this.tamanho_populacao = tamanho_populacao;
        this.tamanho_tabuleiro = tamanho_tabuleiro;
        this.probabilidade_crossover = probabilidade_crossover;
        this.probabilidade_mutacao = probabilidade_mutacao;
        this.max_geracoes = max_geracoes;
        this.max_sem_melhora = max_sem_melhora;
        avaliacao = 0;
    }

    public AlgoritimoGenetico(int tabuleiro, int populacao, double crossover, double mutacao, int max_geracoes) {
        this.tamanho_populacao = populacao;
        this.tamanho_tabuleiro = tabuleiro;
        this.probabilidade_crossover = crossover;
        this.probabilidade_mutacao = mutacao;
        this.max_geracoes = max_geracoes;
        this.max_sem_melhora = Integer.MAX_VALUE;
        avaliacao = 0;
    }

    public void geraSolucaoInicial() {
        //melhor = null;
        populacao = new ArrayList<>();
        int menor = Integer.MAX_VALUE;
        for (int i = 0; i < tamanho_populacao; i++) {
            Solucao aleatoria = new Solucao(tamanho_tabuleiro);
            avaliacao += aleatoria.getAvaliacao();
            if (aleatoria.getAvaliacao() < menor) {
                menor = aleatoria.getAvaliacao();
                melhor = aleatoria;
            }
            populacao.add(aleatoria);
        }
    }

    public void geraCrossovers() throws Exception {
        ArrayList<Solucao> filhos = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < tamanho_populacao; i++) {
            double chance = rand.nextDouble();
            if (chance < probabilidade_crossover) {
                int ipai = rand.nextInt(populacao.size());
                int imae;
                do {
                    imae = rand.nextInt(populacao.size());
                } while (imae == ipai);
                Solucao pai = populacao.get(ipai);
                Solucao mae = populacao.get(imae);

                               
                Solucao filho = cruzar(pai,mae);
                avaliacao += filho.getAvaliacao();
                filhos.add(filho);

                filho = cruzar(mae, pai);
                avaliacao += filho.getAvaliacao();
                filhos.add(filho);
            }
        }
        populacao.addAll(filhos);
        Collections.sort(populacao);
    }
    
    public Solucao cruzar(Solucao sol1, Solucao sol2) throws Exception
    {
        Random rand = new Random();
        int[] vet_filho = sol1.cruzarVetor(sol2);
                double chance = rand.nextDouble();
                if (chance < probabilidade_mutacao) {
                    vet_filho = Solucao.mutarVetor(vet_filho);
                }
                
                Solucao filho = new Solucao(vet_filho);
                return filho;
    }

    
    
    public void seleciona() {
        int tam_elite = (int) (tamanho_populacao * porcentagem_elitismo);
        int tam_aleatorios = (int) (tamanho_populacao * porcentagem_aleatoria);
        int tam_roleta = (int) (tamanho_populacao * porcentagem_roleta);
        int tam_torneio = (int) (tamanho_populacao * porcentagem_torneio);

        ArrayList<Solucao> elites = selecionaElite(tam_elite);
        ArrayList<Solucao> aleatorios = selecionaAleatorios(tam_aleatorios);
        ArrayList<Solucao> roleta = selecionaRoleta(tam_roleta);
        ArrayList<Solucao> torneio = selecionaTorneio(tam_torneio);
        populacao.clear();
        populacao.addAll(roleta);
        populacao.addAll(elites);
        populacao.addAll(aleatorios);
        populacao.addAll(torneio);
        avaliacao = 0;
        for (Solucao sol : populacao) {
            avaliacao += sol.getAvaliacao();
        }
        Collections.sort(populacao);
    }

    //public void geraNovaPopulacao()
    public Solucao getMelhor() {
        return melhor;
    }

    public int getAvaliacao() {
        return avaliacao;
    }

    /*public static void main(String[] args)
     {
     AlgoritimoGenetico ag = new AlgoritimoGenetico(50, 100, 0.9, 0.1, Integer.MAX_VALUE, Integer.MAX_VALUE);
     ag.run();
     //ag.geraSolucaoInicial();
     System.out.println("Melhor: " + ag.melhor + " Valor: "+ ag.melhor.getAvaliacao());
      
     // double fitness = ();
     System.out.println("Avaliacao: "+ ag.getAvaliacao() +" Media: "+ ag.getAvaliacao()/100);
        
     }*/
    @Override
    public void run() {
        parar = false;
        i_sem_melhora = 0;
        geracao = 0;
        this.geraSolucaoInicial();
        while (!parar && i_sem_melhora < max_sem_melhora && geracao < max_geracoes && melhor.getAvaliacao() != 0) {

            //System.out.println("Tamanho da Populacao: "+populacao.size());
            //double media = this.getAvaliacao() / populacao.size();
            //System.out.println("Media: "+ media);
            geracao++;
            i_sem_melhora++;
            try {
                geraCrossovers();
                //System.out.println("Melhor da vez: "+ populacao.get(0) + " Valor: "+ populacao.get(0).getAvaliacao());
            } catch (Exception ex) {
                Logger.getLogger(AlgoritimoGenetico.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (populacao.get(0).getAvaliacao() < melhor.getAvaliacao()) {
                //System.out.println("--- Geracao  "+geracao +" --- ");
                i_sem_melhora = 0;
                melhor = populacao.get(0);
                //System.out.println("Melhor: " + melhor + " Valor: "+ melhor.getAvaliacao());

            }
            seleciona();
            atualiza();
        }
       
        if (gui != null) {
             atualiza();
            gui.desenhaSolucao(melhor);
            gui.finaliza();
        }

    }

    private ArrayList<Solucao> selecionaElite(int tam_elite) {
        ArrayList<Solucao> solucoes = new ArrayList<>();
        for (int i = 0; i < tam_elite; i++) {
            solucoes.add(populacao.get(i));
        }
        return solucoes;
    }

    private ArrayList<Solucao> selecionaAleatorios(int tam_aleatorios) {
        ArrayList<Solucao> solucoes = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < tam_aleatorios; i++) {
            int aleatorio = rand.nextInt(populacao.size());
            solucoes.add(populacao.get(aleatorio));
        }
        return solucoes;
    }

    private ArrayList<Solucao> selecionaRoleta(int tam_roleta) {
        ArrayList<Solucao> solucoes = new ArrayList<>();
        Random rand = new Random();
        int pior = populacao.get(populacao.size() - 1).getAvaliacao();
        int erros = 0;
        for (int i = 0; i < tam_roleta; i++) {
            int aleatorio = rand.nextInt(populacao.size());
            Solucao solucao = populacao.get(aleatorio);
            int limite = rand.nextInt((int) pior / 2 + 1);
            if (solucao.getAvaliacao() <= limite) {
                solucoes.add(populacao.get(aleatorio));
            } else {
                erros++;
                i--;
            }
            if (erros > tam_roleta) {
                pior++;
                erros = 0;
            }
        }
        return solucoes;
    }

    private ArrayList<Solucao> selecionaTorneio(int tam_torneio) {
        ArrayList<Solucao> solucoes = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < tam_torneio; i++) {
            int aleatorio1 = rand.nextInt(populacao.size());
            int aleatorio2 = rand.nextInt(populacao.size());
            Solucao sol1 = populacao.get(aleatorio1);
            Solucao sol2 = populacao.get(aleatorio2);
            solucoes.add(sol1.combate(sol2));
        }
        return solucoes;
    }

    public double getPorcentagem_roleta() {
        return porcentagem_roleta;
    }

    public void setPorcentagem_roleta(double porcentagem_roleta) {
        this.porcentagem_roleta = porcentagem_roleta;
    }

    public double getPorcentagem_aleatoria() {
        return porcentagem_aleatoria;
    }

    public void setPorcentagem_aleatoria(double porcentagem_aleatoria) {
        this.porcentagem_aleatoria = porcentagem_aleatoria;
    }

    public double getPorcentagem_elitismo() {
        return porcentagem_elitismo;
    }

    public void setPorcentagem_elitismo(double porcentagem_elitismo) {
        this.porcentagem_elitismo = porcentagem_elitismo;
    }

    public double getPorcentagem_torneio() {
        return porcentagem_torneio;
    }

    public void setPorcentagem_torneio(double porcentagem_torneio) {
        this.porcentagem_torneio = porcentagem_torneio;
    }

    public boolean isParar() {
        return parar;
    }

    public void parar() {
        this.parar = true;
    }

    public int getAvaliacaoMelhor() {
        return melhor.getAvaliacao();
    }

    public int getAvaliacaoPior() {
        Solucao pior = populacao.get(populacao.size() - 1);
        return pior.getAvaliacao();
    }

    public double getMedia() {
        return (double) (this.getAvaliacao() / populacao.size());
    }

    public int getTamanhoPopulacao() {
        return populacao.size();
    }

    public int getI_sem_melhora() {
        return i_sem_melhora;
    }

    public int getGeracao() {
        return geracao;
    }

    public void atualiza() {
        if (gui != null) {
            gui.setMedia(this.getMedia());
            gui.setMelhor(this.getAvaliacaoMelhor());
            gui.setPior(this.getAvaliacaoPior());
            gui.setGeracao(this.getGeracao());
            gui.setSemMelhora(this.getI_sem_melhora());
            gui.setPopulacao(this.getTamanhoPopulacao());
        }
    }

    public void setGUI(GUI gui) {
        this.gui = gui;
    }

}
