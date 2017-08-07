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
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class CadastroServicoActivityTestNomeVazio {

    @Rule
    public ActivityTestRule<MenuProdutoActivity> mActivityTestRule = new ActivityTestRule<>(MenuProdutoActivity.class);

    @Test
    public void cadastroServicoActivityTest() {

        TesteUtils.fazerLogin();

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        withParent(withId(R.id.toolbar)),
                        isDisplayed()));
        appCompatImageButton2.perform(click());

        ViewInteraction appCompatCheckedTextView4 = onView(
                allOf(withId(R.id.design_menu_item_text), withText("Novo anúncio"), isDisplayed()));
        appCompatCheckedTextView4.perform(click());

        ViewInteraction appCompatRadioButton2 = onView(
                allOf(withId(R.id.rdoBtnServico), withText("Serviço"),
                        withParent(withId(R.id.tipoAnuncio)),
                        isDisplayed()));
        appCompatRadioButton2.perform(click());

        TesteUtils.preencheCampo(R.id.campoNomeServico,"");
        TesteUtils.preencheCampo(R.id.campoValor,"R$1,000");
        TesteUtils.preencheCampo(R.id.campoDescricao,"serviços do campo");
        TesteUtils.preencheCampo(R.id.campoObservacao,"aceito dinheiro em forma de pagamento");

        ViewInteraction result = TesteUtils.matcherResult(R.id.campoNomeServico,"");

        Assert.assertNotNull(result);

    }

}
