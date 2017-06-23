package com.expoagro.expoagrobrasil.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Fabricio on 6/22/2017.
 */
@IgnoreExtraProperties
public class Usuario {

        private String id;
        private String nome;
        private String email;
        private String telefone;
        private String senha;

        public Usuario(String nome, String email, String telefone, String senha) {
            this.nome=nome;
            this.email=email;
            this.telefone=telefone;
            this.senha=senha;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public void setSenha(String senha) {
            this.senha = senha;
        }

        public void setTelefone(String telefone) {
            this.telefone = telefone;
        }

        public  String getId() {
            return id;
        }

        public String getEmail() {
            return email;
        }

        public String getNome() {
            return nome;
        }

        public String getSenha() {
            return senha;
        }

        public String getTelefone() {
            return telefone;
        }

    }
