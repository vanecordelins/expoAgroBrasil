package com.expoagro.expoagrobrasil.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joao on 21/07/17.
 */

public class Lista {

    private String nome,data,valor,categoria,frequencia;
    private List<String> foto;
    public Lista() {
    }
    public Lista(String nome, String data,String valor, String categoria,String frequencia, List<String> foto) {
        this.nome = nome;
        this.data = data;
        this.valor = valor;
        this.categoria = categoria;
        this.frequencia = frequencia;
        this.foto =foto;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }
    public List<String> getFoto() {return foto;}
    public void setFoto(List<String> foto) {
        this.foto = foto;
    }

    public String getValor() {
        return valor;
    }
    public void setValor(String valor) {
        this.valor = valor;
    }
    public String getCategoria() {
        return categoria;
    }
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    public String getFrequencia() {
        return frequencia;
    }
    public void setFrequencia(String frequencia) {
        this.frequencia= frequencia;
    }



}
