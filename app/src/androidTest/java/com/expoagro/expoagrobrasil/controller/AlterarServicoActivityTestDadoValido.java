package com.expoagro.expoagrobrasil.controller;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.expoagro.expoagrobrasil.R;

import junit.framework.Assert;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.expoagro.expoagrobrasil.controller.TesteUtils.abreMenu;
import static com.expoagro.expoagrobrasil.controller.TesteUtils.clicaEm;
import static com.expoagro.expoagrobrasil.controller.TesteUtils.espera;
import static com.expoagro.expoagrobrasil.controller.TesteUtils.fazerLogin;
import static com.expoagro.expoagrobrasil.controller.TesteUtils.preencheCampo;
import static com.expoagro.expoagrobrasil.controller.TesteUtils.selecionaItem;
import static com.expoagro.expoagrobrasil.controller.TesteUtils.vejaTexto;

@RunWith(AndroidJUnit4.class)
public class AlterarServicoActivityTestDadoValido {

    @Rule
    public ActivityTestRule<MenuProdutoActivity> mActivityTestRule = new ActivityTestRule<>(MenuProdutoActivity.class);

    @Test
    public void alterarServicoActivityTestNomeValido() throws InterruptedException {
        fazerLogin();
        abreMenu(R.id.design_menu_item_text,"Meus anúncios");
        clicaEm(R.id.rdoBtnServico3,"Serviços");
        selecionaItem(R.id.recyclerview4,0);
        clicaEm(R.id.alterarServico,"Alterar");
        preencheCampo(R.id.campoNomeServico,"Serviço Valido");
        clicaEm(R.id.btnCadastrar,"ALTERAR",scrollTo());
        clicaEm(android.R.id.button1,"Sim",scrollTo());
        vejaTexto("Serviço atualizado com sucesso.");
    }

}
