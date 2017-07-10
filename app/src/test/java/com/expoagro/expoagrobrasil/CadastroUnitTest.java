package com.expoagro.expoagrobrasil;

import android.content.Intent;
import android.widget.Toast;

import com.expoagro.expoagrobrasil.controller.AlterarUsuarioActivity;
import com.expoagro.expoagrobrasil.controller.AnunciosActivity;
import com.expoagro.expoagrobrasil.controller.CadastroUsuarioActivity;
import com.expoagro.expoagrobrasil.dao.UserDAO;
import com.expoagro.expoagrobrasil.model.Usuario;
import com.expoagro.expoagrobrasil.util.Regex;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.junit.Test;
import static org.junit.Assert.*;


public class CadastroUnitTest {

    @Test
    public void telefoneValido() throws Exception {
        boolean result = Regex.isTelephoneValid("(87) 96547-9654");
        assertEquals(true, result);
    }

    @Test
    public void telefoneInvalido() throws Exception {
        boolean result = Regex.isTelephoneValid("87 96547-9654");
        assertEquals(false, result);
    }

    @Test
    public void nomeValido() throws Exception {
        boolean result = Regex.isNameValid("Vanessa");
        assertEquals(true, result);
    }

    @Test
    public void nomeInvalido() throws Exception {
        boolean result = Regex.isNameValid("123");
        assertEquals(false, result);
    }



}
