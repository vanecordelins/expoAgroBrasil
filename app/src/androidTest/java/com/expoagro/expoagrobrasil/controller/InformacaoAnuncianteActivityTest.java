package com.expoagro.expoagrobrasil.controller;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.expoagro.expoagrobrasil.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.expoagro.expoagrobrasil.controller.TesteUtils.clicaEm;
import static com.expoagro.expoagrobrasil.controller.TesteUtils.selecionaItem;
import static com.expoagro.expoagrobrasil.controller.TesteUtils.vejaItem;

@RunWith(AndroidJUnit4.class)
public class InformacaoAnuncianteActivityTest {

    @Rule
    public ActivityTestRule<MenuProdutoActivity> mActivityTestRule = new ActivityTestRule<>(MenuProdutoActivity.class);

    @Test
    public void informacaoAnuncianteActivityTest() {
        selecionaItem(R.id.recyclerview,0);
        clicaEm(R.id.vendedorProduto,"Vendedor: Vanessa");
        vejaItem(R.id.editText,"INFORMAÇÕES DO ANUNCIANTE");
        vejaItem(R.id.nomeEditText,"Vanessa");
        vejaItem(R.id.telefoneEditText,"(87) 99999-9999");
        vejaItem(R.id.emailEditText,"vanecordelins@gmail.com");
        vejaItem(R.id.cidadeEditText,"Alagoinha");
    }
}
