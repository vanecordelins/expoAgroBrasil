package com.expoagro.expoagrobrasil.controller;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;

import com.expoagro.expoagrobrasil.R;

import junit.framework.Assert;

import org.junit.Rule;
import org.junit.Test;

import cucumber.api.CucumberOptions;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.expoagro.expoagrobrasil.controller.TesteUtils.fazerLogin;
import static org.hamcrest.Matchers.allOf;

@CucumberOptions(format = "pretty",features = "features")
public class ExcluirProdutoActivityTestCucumber extends Instrumentation {

    @Rule
    public ActivityTestRule<MenuProdutoActivity> mActivityTestRule = new ActivityTestRule<>(MenuProdutoActivity.class);


    @Test
    public void excluirProdutoActivityTestComSucesso() {

        fazerLogin();
     //   @Given("^Estou em Meus Anuncios")
        abreMenu(R.id.design_menu_item_text,"Meus anúncios");

     //   @When("^Seleciono o primeiro item")
        view();
    /*    ViewInteraction recyclerView = onView(allOf(withId(R.id.recyclerview2),
                        withParent(allOf(withId(R.id.activity_meusanuncios),
                        withParent(withId(android.R.id.content)))), isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(0, click())); */

     //   @And("^Aperto o botão Excluir")
        clicaEm_Excluir(R.id.excluirProduto,"Excluir");

     //   @And("^Aperto o botão Sim")
        clicaEm_Sim(android.R.id.button1,"Sim");

     //   @Then("^Verifico se Produto foi Deletado")
        verificaTexto("Produto deletado com sucesso.");


    }

    @Given("^Estou em Meus Anuncios")
    static void abreMenu(int menu, String texto){
        ViewInteraction appCompatImageButton2 = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        withParent(withId(R.id.toolbar)),
                        isDisplayed()));
        appCompatImageButton2.perform(click());

        ViewInteraction appCompatCheckedTextView2 = onView(
                allOf(withId(menu), withText(texto), isDisplayed()));
        appCompatCheckedTextView2.perform(click());
    }

    @When("^Seleciono o primeiro item")
    static void view() {
        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.recyclerview2),
                        withParent(allOf(withId(R.id.activity_meusanuncios),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(0, click()));
    }

    @And("^Aperto o botão Excluir")
    static void clicaEm_Excluir(int botao, String texto){
        ViewInteraction appCompatButton = onView(
                allOf(withId(botao), withText(texto), isDisplayed()));
        appCompatButton.perform(click());
    }

    @And("^Aperto o botão Sim")
    static void clicaEm_Sim(int botao, String texto){
        ViewInteraction appCompatButton = onView(
                allOf(withId(botao), withText(texto), isDisplayed()));
        appCompatButton.perform(click());
    }

    @Then("^Verifico se Produto foi Deletado")
    static void verificaTexto(String texto){
        ViewInteraction result = onView(withText(texto)).inRoot(new TesteUtils.ToastMatcher())
                .check(matches(withText(texto)));

        Assert.assertNotNull(result);
    }
}
