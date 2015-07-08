/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufop.ico.nrainhas.gui;

import br.ufop.ico.nrainhas.model.Solucao;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.*;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author silvap
 */
public class Tabuleiro extends Canvas {

    private int[] solucao;
    private int largura;
    private int altura;
    private int tamanho;
    private static Image rainha;

    public Tabuleiro(Solucao solucao, int largura, int altura) {
        rainha = null;
        this.solucao = solucao.getColunas();
        this.largura = largura;
        this.altura = altura;
        tamanho = this.solucao.length;
    }

    public Tabuleiro(int tamanho, int largura, int altura) {
        rainha = null;
        this.largura = largura;
        this.altura = altura;
        this.tamanho = tamanho;
        solucao = null;
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        //g2D.setBackground(Color.WHITE);
        double width = largura / tamanho;
        double height = altura / tamanho;
        int par = 0;
        for (int i = 0; i < tamanho; i++) {
            for (int j = 0; j < tamanho; j++) {
                if (par % 2 == 0) {
                    g2D.setColor(Color.black);
                } else {
                    g2D.setColor(Color.white);
                }
                drawRect(g2D, i * width, j * height, width, height);
                par = (par + 1) % 2;
                if (solucao != null && solucao[i] == j) {
                    drawRainha(g2D, i * width, j * height);
                }
            }
            par++;
        }

    }

    public void drawRect(Graphics2D g2D, double x1, double y1, double x2, double y2) {
        Rectangle2D.Double quadrado = new Rectangle2D.Double(x1, y1, x2, y2);
        g2D.fill(quadrado);
    }

    private void drawRainha(Graphics2D g2D, double x, double y) {
        if (this.getRainha() != null) {
            g2D.drawImage(this.getRainha(), (int) x, (int) y, this);
        }
    }

    public Image getRainha() {
        if (rainha == null) {
            try {
                rainha = ImageIO.read(new File("imagem/rainha.jpg"));
                rainha = rainha.getScaledInstance(largura / tamanho, altura / tamanho, Image.SCALE_DEFAULT);
            } catch (IOException ex) {
                Logger.getLogger(Tabuleiro.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return rainha;
    }

}
