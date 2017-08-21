package com.expoagro.expoagrobrasil.controller;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.expoagro.expoagrobrasil.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.scrollTo;
import static com.expoagro.expoagrobrasil.controller.TesteUtils.abreMenu;
import static com.expoagro.expoagrobrasil.controller.TesteUtils.clicaEm;
import static com.expoagro.expoagrobrasil.controller.TesteUtils.fazerLogin;
import static com.expoagro.expoagrobrasil.controller.TesteUtils.preencheCampo;
import static com.expoagro.expoagrobrasil.controller.TesteUtils.selecionaItem;
import static com.expoagro.expoagrobrasil.controller.TesteUtils.vejaErro;

@RunWith(AndroidJUnit4.class)
public class AlterarProdutoActivityTestNomeVazio {

    @Rule
    public ActivityTestRule<MenuProdutoActivity> mActivityTestRule = new ActivityTestRule<>(MenuProdutoActivity.class);

    @Test
    public void alterarProdutoActivityTestNomeValido() throws InterruptedException {
        fazerLogin();
        abreMenu(R.id.design_menu_item_text,"Meus anúncios");
        selecionaItem(R.id.recyclerview2,0);
        clicaEm(R.id.alterarProduto,"Alterar");
        preencheCampo(R.id.campoNomeProduto,"");
        clicaEm(R.id.btnAlterar,"Confirmar",scrollTo());
        vejaErro(R.id.campoNomeProduto,"Campo obrigatório");
    }

}
