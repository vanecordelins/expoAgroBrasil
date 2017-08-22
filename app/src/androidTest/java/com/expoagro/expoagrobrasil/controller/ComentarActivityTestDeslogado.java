package com.expoagro.expoagrobrasil.controller;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.expoagro.expoagrobrasil.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.scrollTo;
import static com.expoagro.expoagrobrasil.controller.TesteUtils.clicaEm;
import static com.expoagro.expoagrobrasil.controller.TesteUtils.fazerLogout;
import static com.expoagro.expoagrobrasil.controller.TesteUtils.naoVejaItem;
import static com.expoagro.expoagrobrasil.controller.TesteUtils.selecionaItem;

@RunWith(AndroidJUnit4.class)
public class ComentarActivityTestDeslogado {

    @Rule
    public ActivityTestRule<MenuProdutoActivity> mActivityTestRule = new ActivityTestRule<>(MenuProdutoActivity.class);

    @Test
    public void inicialArrobaActivityTest() {
        fazerLogout();
        selecionaItem(R.id.recyclerview,0);
        clicaEm(R.id.textoComentarios,"VER COMENTÁRIOS",scrollTo());
        clicaEm(R.id.btnComentar);
        naoVejaItem(R.id.btnComentar);

    }
}
