package com.expoagro.expoagrobrasil.model;

/**
 * Created by Fabricio on 7/16/2017.
 */

public class Servico extends Anuncio {

    private String frequencia;

    public Servico() {}

    public Servico(String nome, String observacao, String descricao, String data, String hora, String cidade, String valor, String idUsuario, String frequencia) {
        super(nome, observacao, descricao, data, hora, valor, cidade, idUsuario);

        this.frequencia = frequencia;
    }

    public String getFrequencia() {
        return frequencia;
    }

    public void setFrequencia(String frequencia) {
        this.frequencia = frequencia;
    }
}
