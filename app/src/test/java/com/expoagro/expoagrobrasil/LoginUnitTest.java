package com.expoagro.expoagrobrasil;

import com.expoagro.expoagrobrasil.util.Regex;
import org.junit.Test;
import static org.junit.Assert.*;


public class LoginUnitTest {
    @Test
    public void emailValido() throws Exception {
        boolean email = Regex.isEmailValid("nome@123.com");
        assertEquals(true, email);
    }

    @Test
    public void emailInValido() throws Exception {
        boolean email = Regex.isEmailValid("nome123.com");
        assertEquals(false, email);
    }

    @Test
    public void senhaValida() throws Exception {
        boolean senha = Regex.isPasswordValid("123456");
        assertEquals(true,senha);
    }

    @Test
    public void senhaInValida() throws Exception {
        boolean senha = Regex.isPasswordValid("123");
        assertEquals(false,senha);
    }

}