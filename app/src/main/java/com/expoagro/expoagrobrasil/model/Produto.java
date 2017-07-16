package com.expoagro.expoagrobrasil.model;

import android.graphics.Bitmap;


/**
 * Created by Fabricio on 7/16/2017.
 */

public class Produto extends Anuncio {

    private String categoria;
    private Bitmap foto;

    public Produto(String id, String nome, String observacao, String descricao, String data, String hora, float valor, String categoria, Bitmap foto) {
        super(id, nome, observacao, descricao, data, hora, valor);
        this.categoria = categoria;
        this.foto = foto;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Bitmap getFoto() {
        return foto;
    }

    public void setFoto(Bitmap foto) {
        this.foto = foto;
    }
}
