/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufop.ico.nrainhas.gui;

import br.ufop.ico.nrainhas.model.Solucao;

/**
 *
 * @author silvap
 */
public interface GUI {

    public void desenhaSolucao(Solucao sol);

    public void setMelhor(int melhor);

    public void setPior(int pior);

    public void setMedia(double media);

    public void setGeracao(int geracao);

    public void setSemMelhora(int sm);

    public void finaliza();

    public void setPopulacao(int pop);
}
