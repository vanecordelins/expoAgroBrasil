package com.expoagro.expoagrobrasil;


import com.expoagro.expoagrobrasil.util.Regex;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


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
