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
        vejaItem(R.id.recyclerview);
    }

}
