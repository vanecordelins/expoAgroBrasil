package com.expoagro.expoagrobrasil.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Fabricio on 8/20/2017.
 */
@IgnoreExtraProperties
public class Comentario {

    private String id;
    private String comentario;
    private String data;
    private String idAnuncio;
    private String nomeUsuario;

    public Comentario() {}

    public Comentario(String id, String comentario, String data, String idAnuncio, String nomeUsuario) {
        this.id = id;
        this.comentario = comentario;
        this.data = data;
        this.idAnuncio = idAnuncio;
        this.nomeUsuario = nomeUsuario;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getIdProduto() {
        return idAnuncio;
    }

    public void setIdAnuncio(String idAnuncio) {
        this.idAnuncio = idAnuncio;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }
}
