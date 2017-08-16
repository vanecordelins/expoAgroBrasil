package com.expoagro.expoagrobrasil.controller;

import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.expoagro.expoagrobrasil.R;

import junit.framework.Assert;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.expoagro.expoagrobrasil.controller.TesteUtils.clicaEm;
import static com.expoagro.expoagrobrasil.controller.TesteUtils.espera;
import static com.expoagro.expoagrobrasil.controller.TesteUtils.fazerLogin;
import static com.expoagro.expoagrobrasil.controller.TesteUtils.vejaItem;


@LargeTest
@RunWith(AndroidJUnit4.class)
public class PesquisarPalavraProdutoActivityTeste {

    @Rule
    public ActivityTestRule<MenuProdutoActivity> mActivityTestRule = new ActivityTestRule<>(MenuProdutoActivity.class);

    @Test
    public void pesquisarCategoriaProdutoActivityTeste() {
        espera();
        fazerLogin();

     //   filtro();

        clicaEm(R.id.animal,"Animal");
        vejaItem(R.id.recyclerview);
     //   filtro();
        clicaEm(R.id.ferramenta,"Ferramenta");
        vejaItem(R.id.recyclerview);
     //   filtro();
        clicaEm(R.id.produto,"Todos os Produtos");
        vejaItem(R.id.recyclerview);
     //   filtro();
        clicaEm(R.id.propriedade,"Propriedade");
        vejaItem(R.id.recyclerview);
     //   filtro();
        clicaEm(R.id.maquinarios,"Maquinários");
        vejaItem(R.id.recyclerview);
     //   filtro();
        clicaEm(R.id.outros,"Outros");

        ViewInteraction result = onView(withId(R.id.recyclerview));
        result.check(matches(isDisplayed()));
        Assert.assertNotNull(result);
     //   vejaItem(R.id.recyclerview);
    }

}
