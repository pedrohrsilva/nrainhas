/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufop.ico.nrainhas.model;

import java.util.Random;

/**
 *
 * @author silvap
 */
public class Solucao implements Comparable<Solucao> {

    private static int tamanho;
    private int[] colunas;
    private int avaliacao;
    private static int[] origem;

    public int[] geraAleatorio(int tam) {
        if (origem == null || origem.length != tam) {
            origem = new int[tam];
            for (int i = 0; i < tam; i++) {
                origem[i] = i;
            }
        }
        Random rand = new Random();
        for (int i = 0; i < tam; i++) {
            int pos1 = rand.nextInt(tam);
            int pos2 = rand.nextInt(tam);
            int aux = origem[pos1];
            origem[pos1] = origem[pos2];
            origem[pos2] = aux;
        }

        return origem;
    }

    public Solucao(int tam, int[] col) throws Exception {

        tamanho = tam;
        if (col.length == tam) {
            colunas = col;
            avaliacao = avaliar();
        } else {
            throw new Exception("Solucao Invalida! Tamanho nao bate.\n");
        }
    }

    public Solucao(int tam) {
        tamanho = tam;
        Random rand = new Random();
        colunas = geraAleatorio(tam);
        avaliacao = avaliar();
    }
    
    public Solucao(int[] col) throws Exception
    {
        if (col.length == tamanho) {
            colunas = col;
            avaliacao = avaliar();
        } else {
            throw new Exception("Solucao Invalida! Tamanho nao bate.\n");
        }
    }

    public int[] getColunas() {
        return colunas.clone();
    }

    

    
    public Solucao cruzar(Solucao outra) {
        return cruzar(outra, tamanho/2);
        
    }
    
    
    public Solucao cruzar(Solucao outra, int tam) {
        
        int[] nova = cruzarVetor(outra, tam);
        try {
            return new Solucao(tamanho, nova);
        } catch (Exception ex) {
            /**
             * TODO
             */
            return null;
        }
    }
    
    public int[] cruzarVetor(Solucao outra)
    {
        return cruzarVetor(outra, tamanho/2);
    }
    
    public int[] cruzarVetor(Solucao outra, int tam)
    {
        int[] nova = outra.getColunas();
        System.arraycopy(colunas, 0, nova, 0, tam);
        return nova;
    }
    
    public static int[] mutarVetor(int[] vetor)
    {
        Random rand =  new Random();
        int coluna1 = rand.nextInt(tamanho);
        int coluna2 = rand.nextInt(tamanho);
        int aux = vetor[coluna1];
        vetor[coluna1] = vetor[coluna2];
        vetor[coluna2] = aux;
        return vetor;
    }

    /*  public int[] cruzar(Solucao outra, int tam)
     {
     int[] nova = outra.getColunas();
     System.arraycopy(colunas, 0, nova, 0, tam);
     return nova;
     }*/
    /*
    public void mutar() {

        Random rand = new Random();
        int coluna = rand.nextInt(tamanho);
        int valor = rand.nextInt(tamanho);
        int anterior = colunas[coluna];
        int ataques_antigos = 0;
        int ataques_novos = 0;
        for (int i = 0; i < tamanho; i++) {
            if (i != coluna) {
                if (atacar(coluna, anterior, i, colunas[i])) {
                    ataques_antigos++;
                }
                if (atacar(coluna, valor, i, colunas[i])) {
                    ataques_novos++;
                }
            }
        }
        colunas[coluna] = valor;
        avaliacao = avaliacao + ataques_novos - ataques_antigos;

    }*/

    
     public void mutar()
     {
        Random rand =  new Random();
        int coluna = rand.nextInt(tamanho);
        int valor = rand.nextInt(tamanho);
        int aux = colunas[coluna];
        colunas[coluna] = colunas[valor];
        colunas[valor] = aux;
        avaliacao = avaliar();
        
     }
     
     
    private int avaliar() {
        int repeticoes = 0;
        for (int j = 0; j < tamanho-1 ; j++) {
            for (int i = j+1; i < tamanho; i++) {
          
                    repeticoes += atacar(j, colunas[j], i, colunas[i]);
                
            }
        }
        return repeticoes;
    }

    /*
    private boolean atacar(int x1, int y1, int x2, int y2) {
        if (y1 == y2) {
            return true;
        }
        if (x1 == x2) {
            return true;
        }
        int direcaoX, direcaoY;
        if (x1 > x2) {
            direcaoX = -1;
        } else {
            direcaoX = 1;
        }
        if (y1 > y2) {
            direcaoY = -1;
        } else {
            direcaoY = 1;
        }

        do {
            x1 = x1 + direcaoX;
            y1 = y1 + direcaoY;
            if (x1 == x2 && y1 == y2) {
                return true;
            }
            if (direcaoX == -1 && x1 <= x2) {
                return false;
            }
            if (direcaoY == -1 && y1 <= y2) {
                return false;
            }
            if (direcaoX == 1 && x1 >= x2) {
                return false;
            }
            if (direcaoY == 1 && y1 >= y2) {
                return false;
            }
        } while ((x1 >= 0 && x1 < tamanho) && (y1 >= 0 && y1 < tamanho));

        return false;
    }*/
    
    public int atacar(int x1, int y1, int x2, int y2) 
    {
        if (y1 == y2) {
            return 2;
        }
        if (x1 == x2) {
            return 2;
        }
        if(x1-y1 == x2 - y2 || x1+y1 == x2+y2)
        {
           return 1;
        }
        return 0;
        
    }
    
    
    

    public String toString() {
        String retorno = "[";
        for (int linha : colunas) {
            retorno += " " + linha;
        }
        retorno += " ]";
        return retorno;
    }

    public int getAvaliacao() {
        return avaliacao;
    }

    public Solucao combate(Solucao o) {
        if (this.getAvaliacao() < o.getAvaliacao()) {
            return this;
        } else {
            return o;
        }
    }

    @Override
    public int compareTo(Solucao o) {
        return this.getAvaliacao() - o.getAvaliacao();
    }

    public int penalizacao() {
        int repetidos = 0;
        for (int i = 0; i < colunas.length - 1; i++) {
            for (int j = i + 1; j < colunas.length; j++) {
                if (colunas[i] == colunas[j]) {
                    repetidos++;
                    break;
                }
            }
        }
        return repetidos;
    }

}
