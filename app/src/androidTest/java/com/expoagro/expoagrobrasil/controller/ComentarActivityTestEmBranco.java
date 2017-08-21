package com.expoagro.expoagrobrasil.controller;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.expoagro.expoagrobrasil.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.scrollTo;
import static com.expoagro.expoagrobrasil.controller.TesteUtils.clicaEm;
import static com.expoagro.expoagrobrasil.controller.TesteUtils.fazerLogin;
import static com.expoagro.expoagrobrasil.controller.TesteUtils.preencheCampo;
import static com.expoagro.expoagrobrasil.controller.TesteUtils.selecionaItem;
import static com.expoagro.expoagrobrasil.controller.TesteUtils.vejaTexto;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ComentarActivityTestEmBranco {

    @Rule
    public ActivityTestRule<MenuProdutoActivity> mActivityTestRule = new ActivityTestRule<>(MenuProdutoActivity.class);

    @Test
    public void inicialArrobaActivityTest() {
        fazerLogin();
        selecionaItem(R.id.recyclerview,0);
        clicaEm(R.id.textoComentarios,"VER COMENTÁRIOS",scrollTo());
        clicaEm(R.id.btnComentar,"Adicionar Comentário");
        preencheCampo(R.id.campoComentario,"");
        clicaEm(R.id.btnCadastrar);
        vejaTexto("Seu comentário está em branco.");
    }
}
