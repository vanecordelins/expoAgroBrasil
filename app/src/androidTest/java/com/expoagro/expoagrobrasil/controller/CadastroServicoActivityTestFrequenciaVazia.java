package com.expoagro.expoagrobrasil.controller;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.expoagro.expoagrobrasil.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.scrollTo;
import static com.expoagro.expoagrobrasil.controller.TesteUtils.abreMenu;
import static com.expoagro.expoagrobrasil.controller.TesteUtils.clicaEm;
import static com.expoagro.expoagrobrasil.controller.TesteUtils.fazerLogin;
import static com.expoagro.expoagrobrasil.controller.TesteUtils.preencheCampo;
import static com.expoagro.expoagrobrasil.controller.TesteUtils.vejaTexto;

@RunWith(AndroidJUnit4.class)
public class CadastroServicoActivityTestFrequenciaVazia {

    @Rule
    public ActivityTestRule<MenuProdutoActivity> mActivityTestRule = new ActivityTestRule<>(MenuProdutoActivity.class);

    @Test
    public void cadastroServicoActivityTest() {
        fazerLogin();
        abreMenu(R.id.design_menu_item_text,"Novo anúncio");
        clicaEm(R.id.rdoBtnServico2,"Serviço");
        preencheCampo(R.id.campoNomeServico,"Serviço Valido");
        preencheCampo(R.id.campoValor,"$600.00");
        preencheCampo(R.id.campoDescricao,"Criar Novo Serviço");
        preencheCampo(R.id.campoObservacao,"Novo Serviço será Criado");
        clicaEm(R.id.btnCadastrar,"Cadastrar",scrollTo());
        vejaTexto("Selecione a frequência do serviço");
    }

}
