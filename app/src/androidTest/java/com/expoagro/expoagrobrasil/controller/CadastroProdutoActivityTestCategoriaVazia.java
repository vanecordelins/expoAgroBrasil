package com.expoagro.expoagrobrasil.controller;

import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.expoagro.expoagrobrasil.R;
import com.google.firebase.auth.FirebaseAuth;

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
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class CadastroProdutoActivityTestCategoriaVazia {

    @Rule
    public ActivityTestRule<MenuActivity> mActivityTestRule = new ActivityTestRule<>(MenuActivity.class);

    @Test
    public void cadastroProdutoActivityTest() throws InterruptedException {

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

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        withParent(withId(R.id.toolbar)),
                        isDisplayed()));
        appCompatImageButton2.perform(click());

        ViewInteraction appCompatCheckedTextView = onView(
                allOf(withId(R.id.design_menu_item_text), withText("Novo anúncio"), isDisplayed()));
        appCompatCheckedTextView.perform(click());

        ViewInteraction appCompatAutoCompleteTextView = onView(
                allOf(withId(R.id.campoNomeProduto), isDisplayed()));
        appCompatAutoCompleteTextView.perform(replaceText("Papa Capim"), closeSoftKeyboard());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.campoValor), isDisplayed()));
        appCompatEditText5.perform(replaceText("$600.00"), closeSoftKeyboard());


        ViewInteraction appCompatEditText11 = onView(
                allOf(withId(R.id.campoDescricao), isDisplayed()));
        appCompatEditText11.perform(replaceText("Papa Capim dos Sonhos"), closeSoftKeyboard());

        ViewInteraction appCompatEditText12 = onView(
                allOf(withId(R.id.campoObservacao), isDisplayed()));
        appCompatEditText12.perform(replaceText("Não Deixa Passar Vergonha"), closeSoftKeyboard());
        Thread.sleep(3000);
        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.btnCadastrar),isDisplayed()));
        appCompatButton2.perform(click());
        Thread.sleep(3000);
        ViewInteraction appCompatButton3 = onView(
                allOf(withId(android.R.id.button1), withText("Sim")));
        appCompatButton3.perform(scrollTo(), click());



        ViewInteraction result = onView(withText(R.string.error_categoria_nao_selecionada)).inRoot(new TesteUtils.ToastMatcher())
                .check(matches(withText("Selecione a categoria do produto")));



        Assert.assertNotNull(result);

    }

}