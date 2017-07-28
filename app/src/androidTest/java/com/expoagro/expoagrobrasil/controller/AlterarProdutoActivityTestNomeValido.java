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
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class AlterarProdutoActivityTestNomeValido {

    @Rule
    public ActivityTestRule<MenuActivity> mActivityTestRule = new ActivityTestRule<>(MenuActivity.class);

    @Test
    public void alterarProdutoActivityTestNomeValido() throws InterruptedException {

        TesteUtils.fazerLogin();

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        withParent(withId(R.id.toolbar)),
                        isDisplayed()));
        appCompatImageButton2.perform(click());

        ViewInteraction appCompatCheckedTextView2 = onView(
                allOf(withId(R.id.design_menu_item_text), withText("Meus anúncios"), isDisplayed()));
        appCompatCheckedTextView2.perform(click());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.recyclerview2),
                        withParent(allOf(withId(R.id.activity_meusanuncios),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(2, click()));

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.alterarProduto), withText("Alterar"), isDisplayed()));
        appCompatButton3.perform(click());


        ViewInteraction appCompatAutoCompleteTextView = onView(
                allOf(withId(R.id.campoNomeProduto), isDisplayed()));
        appCompatAutoCompleteTextView.perform(replaceText("nome alterado"), closeSoftKeyboard());

        Thread.sleep(3000);
        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.btnCadastrar), withText("Alterar"), isDisplayed()));
        appCompatButton4.perform(  click());

        ViewInteraction appCompatButton5 = onView(
                allOf(withId(android.R.id.button1), withText("Sim")));
        appCompatButton5.perform(scrollTo(), click());

        ViewInteraction result = onView(withText("Produto atualizado com sucesso.")).inRoot(new TesteUtils.ToastMatcher())
                .check(matches(withText("Produto atualizado com sucesso.")));



        Assert.assertNotNull(result);
    }

}
