package com.expoagro.expoagrobrasil;

import com.expoagro.expoagrobrasil.util.Regex;

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
        boolean email = Regex.isEmailValid("nome@123.com");
        assertEquals(true, email);
    }

    @Test
    public void senhaValida() throws Exception {
        boolean senha = Regex.isPasswordValid("123456");
        assertEquals(true,senha);
    }

}