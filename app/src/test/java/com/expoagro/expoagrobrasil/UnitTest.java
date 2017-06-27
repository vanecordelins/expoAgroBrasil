package com.expoagro.expoagrobrasil;

import com.expoagro.expoagrobrasil.controller.LoginActivity;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UnitTest {
    
    @Test
    public void emailValido() throws Exception {
        LoginActivity loginActivityEmail = new LoginActivity();
        boolean email = loginActivityEmail.isEmailValid("nome@123.com");
        assertEquals(true, email);
    }

    @Test
    public void senhaValida() throws Exception {
        LoginActivity loginActivitySenha = new LoginActivity();
        boolean senha = loginActivitySenha.isPasswordValid("123456");
        assertEquals(true,senha);
    }

}