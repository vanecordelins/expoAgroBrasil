package com.expoagro.expoagrobrasil.controller;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.expoagro.expoagrobrasil.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.expoagro.expoagrobrasil.controller.TesteUtils.clicaEm;
import static com.expoagro.expoagrobrasil.controller.TesteUtils.espera;
import static com.expoagro.expoagrobrasil.controller.TesteUtils.fazerLogin;
import static com.expoagro.expoagrobrasil.controller.TesteUtils.selecionaItem;
import static com.expoagro.expoagrobrasil.controller.TesteUtils.vejaItem;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class CompartilharProdutoActivityTeste {

    @Rule
    public ActivityTestRule<MenuProdutoActivity> mActivityTestRule = new ActivityTestRule<>(MenuProdutoActivity.class);

    @Test
    public void visualizarProduto() {

        espera();
        fazerLogin();

        espera();
        clicaEm(R.id.rdoBtnProduto2,"Produtos");
        espera();
        selecionaItem(R.id.recyclerview,0);
        espera();
        vejaItem(R.id.observacaoProduto);
        espera();
        vejaItem(R.id.descricaoProduto);

    }

}
