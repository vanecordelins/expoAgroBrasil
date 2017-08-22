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
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.expoagro.expoagrobrasil.controller.TesteUtils.abreMenu;
import static com.expoagro.expoagrobrasil.controller.TesteUtils.clicaEm;
import static com.expoagro.expoagrobrasil.controller.TesteUtils.espera;
import static com.expoagro.expoagrobrasil.controller.TesteUtils.fazerLogin;
import static com.expoagro.expoagrobrasil.controller.TesteUtils.selecionaItem;
@RunWith(AndroidJUnit4.class)
public class ExcluirProdutoActivityTest {

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

        ViewInteraction result = onView(withText("Produto deletado com sucesso.")).inRoot(new TesteUtils.ToastMatcher())
                .check(matches(withText("Produto deletado com sucesso.")));

        Assert.assertNotNull(result);
      //  verificaTexto("Produto deletado com sucesso.");

    }

}
