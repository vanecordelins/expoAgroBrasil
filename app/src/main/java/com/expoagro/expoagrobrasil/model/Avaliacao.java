package com.expoagro.expoagrobrasil.model;

/**
 * Created by joao on 21/08/17.
 */

public class Avaliacao {

    private float avaliacao;
    private String idAnuciante;

    public Avaliacao(String idUsuario, float avaliacao, String idAnuciante){
        this.idAnuciante = idAnuciante;
        this.avaliacao = avaliacao;
    }
    public Avaliacao(){}

    public float getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(float avaliacao) {
        this.avaliacao = avaliacao;
    }

    public String getIdAnuciante() {
        return idAnuciante;
    }

    public void setIdAnuciante(String idAnuciante) {
        this.idAnuciante = idAnuciante;
    }


}
