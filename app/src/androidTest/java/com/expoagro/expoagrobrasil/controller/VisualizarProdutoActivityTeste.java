package com.expoagro.expoagrobrasil.controller;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;

import com.expoagro.expoagrobrasil.R;
import com.google.firebase.auth.FirebaseAuth;

import junit.framework.Assert;

import org.junit.Rule;

import cucumber.api.CucumberOptions;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;


@CucumberOptions(features = "features")
public class VisualizarProdutoActivityTeste {

    @Rule
    public ActivityTestRule<MenuProdutoActivity> mActivityTestRule = new ActivityTestRule<>(MenuProdutoActivity.class);

    @Given("^I have a LoginActivity")
    static void fazerLogin(){
        if(FirebaseAuth.getInstance().getCurrentUser()==null){

            ViewInteraction appCompatImageButton2 = onView(
                    allOf(withContentDescription("Open navigation drawer"),
                            withParent(withId(R.id.toolbar)),
                            isDisplayed()));
            appCompatImageButton2.perform(click());

            ViewInteraction appCompatTextView = onView(
                    allOf(withId(R.id.menu_email), withText("Fazer Login"), isDisplayed()));
            appCompatTextView.perform(click());

            ViewInteraction appCompatEditText = onView(
                    allOf(withId(R.id.campoEmail), isDisplayed()));
            appCompatEditText.perform(replaceText("dinego.nos@gmail.com"), closeSoftKeyboard());

            ViewInteraction appCompatEditText2 = onView(
                    allOf(withId(R.id.campoSenha), isDisplayed()));
            appCompatEditText2.perform(replaceText("123456"), closeSoftKeyboard());

            ViewInteraction appCompatButton = onView(
                    allOf(withId(R.id.btnEntrar), withText("Entrar"),
                            withParent(withId(R.id.email_login_form)),
                            isDisplayed()));
            appCompatButton.perform(click());
        }
    }
    @When("^I input click (\\S+)$")
    static void clicaEm(int botao, String texto){
        ViewInteraction appCompatButton = onView(
                allOf(withId(botao), withText(texto), isDisplayed()));
        appCompatButton.perform(click());
    }
    @When("^I select \"(.*?)\"$")
    static void selecionaItem(int recycle, int item){
        ViewInteraction recyclerView = onView(
                allOf(withId(recycle),isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(item, click()));
    }
    @Then("^I should see item (\\S+)$")
    static void vejaItem(int item) {
        ViewInteraction result = onView(withId(item));
        result.check(matches(isDisplayed()));

        Assert.assertNotNull(result);
    }



}
