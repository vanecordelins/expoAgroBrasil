package com.expoagro.expoagrobrasil.model;

/**
 * Created by Fabricio on 7/16/2017.
 */

public class Servico extends Anuncio {

    private String frequencia;

    public Servico(String id, String nome, String observacao, String descricao, String data, String hora, float valor, String frequencia) {
        super(id, nome, observacao, descricao, data, hora, valor);
        this.frequencia = frequencia;
    }

    public String getFrequencia() {
        return frequencia;
    }

    public void setFrequencia(String frequencia) {
        this.frequencia = frequencia;
    }
}
