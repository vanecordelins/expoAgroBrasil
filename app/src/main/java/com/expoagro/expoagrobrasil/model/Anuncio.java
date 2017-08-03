package com.expoagro.expoagrobrasil.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Fabricio on 7/16/2017.
 */

@IgnoreExtraProperties
public class Anuncio {

    private String id;
    private String nome;
    private String observacao;
    private String descricao;
    private String data;
    private String hora;
    private String valor;
    private String cidade;
    private String idUsuario;

    public Anuncio() { }

    public Anuncio(String nome, String observacao, String descricao, String data, String hora, String valor, String cidade, String idUsuario) {

        this.nome = nome;
        this.observacao = observacao;
        this.descricao = descricao;
        this.data = data;
        this.hora = hora;
        this.valor = valor;
        this.cidade = cidade;
        this.idUsuario = idUsuario;
    }

    public Anuncio(String nome, String observacao, String descricao, String valor, String idUsuario) {
        this.nome = nome;
        this.observacao = observacao;
        this.descricao = descricao;
        this.valor = valor;
        this.idUsuario = idUsuario;
    }


    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
