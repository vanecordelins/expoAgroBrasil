package com.expoagro.expoagrobrasil.controller;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.expoagro.expoagrobrasil.R;
import com.google.firebase.auth.FirebaseAuth;

import junit.framework.Assert;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.expoagro.expoagrobrasil.controller.TesteUtils.abreMenu;
import static com.expoagro.expoagrobrasil.controller.TesteUtils.clicaEm;
import static com.expoagro.expoagrobrasil.controller.TesteUtils.fazerLogin;
import static com.expoagro.expoagrobrasil.controller.TesteUtils.preencheCampo;
import static com.expoagro.expoagrobrasil.controller.TesteUtils.vejaErro;
import static com.expoagro.expoagrobrasil.controller.TesteUtils.vejaTexto;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class CadastroServicoActivityTestNomeVazio {

    @Rule
    public ActivityTestRule<MenuProdutoActivity> mActivityTestRule = new ActivityTestRule<>(MenuProdutoActivity.class);

    @Test
    public void cadastroServicoActivityTest() {

        fazerLogin();
        abreMenu(R.id.design_menu_item_text,"Novo anúncio");
        clicaEm(R.id.rdoBtnServico,"Serviço");
        preencheCampo(R.id.campoNomeServico,"");
        preencheCampo(R.id.campoValor,"$600.00");
        preencheCampo(R.id.campoDescricao,"Criar Novo Serviço");
        preencheCampo(R.id.campoObservacao,"Novo Serviço será Criado");
        clicaEm(R.id.btnCadastrar,"Cadastrar",scrollTo());
        vejaErro(R.id.campoNomeServico,"Campo obrigatório");

    }

}
