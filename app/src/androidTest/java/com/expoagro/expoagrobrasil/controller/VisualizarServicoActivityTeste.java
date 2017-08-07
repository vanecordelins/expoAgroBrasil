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
public class VisualizarServicoActivityTeste {

    @Rule
    public ActivityTestRule<MenuProdutoActivity> mActivityTestRule = new ActivityTestRule<>(MenuProdutoActivity.class);

    @Test
    public void visualizarServico() {
        espera();
        fazerLogin();
        espera();
        clicaEm(R.id.rdoBtnServico2,"Serviços");
        espera();
        selecionaItem(R.id.recyclerview2,0);
        espera();
        vejaItem(R.id.observacaoServico);
        vejaItem(R.id.descricaoServico);
    }

}
