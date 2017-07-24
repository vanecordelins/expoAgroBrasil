package com.expoagro.expoagrobrasil.model;

import java.util.List;


/**
 * Created by Fabricio on 7/16/2017.
 */

public class Produto extends Anuncio {

    private String categoria;
    private List<String> fotos;

    public Produto(String nome, String observacao, String descricao, String valor, String idUsuario, String categoria, List<String> fotos) {
        super(nome, observacao, descricao, valor, idUsuario);
        this.categoria = categoria;
        this.fotos = fotos;
    }

    public Produto() { }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public List<String> getFoto() {
        return fotos;
    }

    public void setFoto(List<String> fotos) {
        this.fotos = fotos;

    }
}
