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
import static com.expoagro.expoagrobrasil.controller.TesteUtils.vejaErro;

@RunWith(AndroidJUnit4.class)
public class CadastroProdutoActivityTestNomeEmBranco {

    @Rule
    public ActivityTestRule<MenuProdutoActivity> mActivityTestRule = new ActivityTestRule<>(MenuProdutoActivity.class);

    @Test
    public void cadastroProdutoActivityTest() throws InterruptedException {
        fazerLogin();
        abreMenu(R.id.design_menu_item_text,"Novo anúncio");
        preencheCampo(R.id.campoNomeProduto,"");
        preencheCampo(R.id.campoValor,"$600.00");
        preencheCampo(R.id.campoDescricao,"Papa Capim dos Sonhos");
        preencheCampo(R.id.campoObservacao,"Não Deixa Passar Vergonha");
        clicaEm(R.id.btnCadastrar,"Cadastrar",scrollTo());
        vejaErro(R.id.campoNomeProduto,"Campo obrigatório");


    }
}
