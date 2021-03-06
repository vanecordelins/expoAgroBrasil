package com.expoagro.expoagrobrasil.controller;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.expoagro.expoagrobrasil.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static com.expoagro.expoagrobrasil.R.id.btnFavoritarProduto;
import static com.expoagro.expoagrobrasil.controller.TesteUtils.clicaEm;
import static com.expoagro.expoagrobrasil.controller.TesteUtils.fazerLogin;
import static com.expoagro.expoagrobrasil.controller.TesteUtils.selecionaItem;
import static com.expoagro.expoagrobrasil.controller.TesteUtils.verificaBotaoAtivo;

@RunWith(AndroidJUnit4.class)
public class FavoritarActivityTest {

    @Rule
    public ActivityTestRule<MenuProdutoActivity> mActivityTestRule = new ActivityTestRule<>(MenuProdutoActivity.class);

    @Test
    public void favoritarActivityTest() {
        fazerLogin();
        selecionaItem(R.id.recyclerview,0);
        clicaEm(btnFavoritarProduto);
        verificaBotaoAtivo(btnFavoritarProduto,isEnabled());
    }
}
