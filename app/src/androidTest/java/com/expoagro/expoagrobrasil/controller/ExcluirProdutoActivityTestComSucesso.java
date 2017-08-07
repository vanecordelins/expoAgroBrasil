package com.expoagro.expoagrobrasil.controller;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.expoagro.expoagrobrasil.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.expoagro.expoagrobrasil.controller.TesteUtils.abreMenu;
import static com.expoagro.expoagrobrasil.controller.TesteUtils.clicaEm;
import static com.expoagro.expoagrobrasil.controller.TesteUtils.espera;
import static com.expoagro.expoagrobrasil.controller.TesteUtils.fazerLogin;
import static com.expoagro.expoagrobrasil.controller.TesteUtils.selecionaItem;
import static com.expoagro.expoagrobrasil.controller.TesteUtils.verificaTexto;

@RunWith(AndroidJUnit4.class)
public class ExcluirProdutoActivityTestComSucesso {

    @Rule
    public ActivityTestRule<MenuProdutoActivity> mActivityTestRule = new ActivityTestRule<>(MenuProdutoActivity.class);

    @Test
    public void excluirProdutoActivityTestComSucesso() {
        espera();
        fazerLogin();
        espera();
        abreMenu(R.id.design_menu_item_text,"Meus anúncios");
       espera();
        selecionaItem(R.id.recyclerview2,1);
        espera();
        clicaEm(R.id.excluirProduto,"Excluir");
        espera();
        clicaEm(android.R.id.button1,"Sim");
        verificaTexto("Produto deletado com sucesso.");


    }

}
